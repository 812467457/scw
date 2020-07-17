package cn.yylm.scw.util;

import cn.yylm.scw.constant.CrowdConstant;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 通用工具方法类
 */
public class CrowdUtil {


    /**
     * 判断当前请求是否为Ajax请求
     *
     * @param request
     * @return
     */
    public static boolean judgeRequestType(HttpServletRequest request) {
        //获取请求消息头
        String accept = request.getHeader("Accept");
        String xRequest = request.getHeader("X-Requested-With");
        return (accept != null && accept.contains("application/json")) || (xRequest != null && xRequest.equals("XMLHttpRequest"));
    }

    /**
     * 对名文字符串进行md5加密
     *
     * @param source
     * @return
     */
    public static String md5(String source) {
        //判断source是否无效
        if (source == null || source.length() == 0) {
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        String algorithm = "md5";
        try {
            //获取MessageDigest
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            //把名文字符串转为字符数组
            byte[] bytes = source.getBytes();
            //加密
            byte[] digest = messageDigest.digest(bytes);
            //创建bigInteger
            int signum = 1;
            BigInteger bigInteger = new BigInteger(signum, digest);
            //按照16进制把数字转为字符串
            int radix = 16;
            return bigInteger.toString(radix).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用第三方短信接口发短信
     *
     * @param host     请求地址
     * @param path     发送短信功能的地址
     * @param phoneNum 手机号
     * @param appCode  第三方的appCode
     * @param sign     签名信息编号
     * @param skin     模板编号
     * @return 返回调用结果
     */
    public static ResultEntity<String> sendMessage(String host, String path, String phoneNum, String appCode, String sign, String skin) {
        //随机生成验证码
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int random = (int) (Math.random() * 10);
            builder.append(random);
        }
        String param = builder.toString();
        String urlSend = host + path + "?sign=" + sign + "&skin=" + skin + "&param=" + param + "&phone=" + phoneNum; // 【5】拼接请求链接
        try {
            URL url = new URL(urlSend);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Authorization", "APPCODE " + appCode);
            int httpCode = httpURLConnection.getResponseCode();
            if (httpCode == 200) {
                String json = read(httpURLConnection.getInputStream());
                System.out.println("正常请求计费(其他均不计费)");
                System.out.println("获取返回的json");
                System.out.print(json);
                return ResultEntity.successWithData(builder.toString());
            } else {
                Map<String, List<String>> map = httpURLConnection.getHeaderFields();
                String errorMessage = map.get("X-Ca-Error-Message").get(0);
                if (httpCode == 400 && errorMessage.equals("Invalid AppCode `not exists`")) {
                    System.out.println("AppCode错误 ");
                } else if (httpCode == 400 && errorMessage.equals("Invalid Url")) {
                    System.out.println("请求的 Method、Path 或者环境错误");
                } else if (httpCode == 400 && errorMessage.equals("Invalid Param Location")) {
                    System.out.println("参数错误");
                } else if (httpCode == 403 && errorMessage.equals("Unauthorized")) {
                    System.out.println("服务未被授权（或URL和Path不正确）");
                } else if (httpCode == 403 && errorMessage.equals("Quota Exhausted")) {
                    System.out.println("套餐包次数用完 ");
                } else {
                    System.out.println("参数名错误 或 其他错误");
                    System.out.println(errorMessage);
                }
                return ResultEntity.failed(errorMessage);
            }

        } catch (MalformedURLException e) {
            System.out.println("URL格式错误");
            return ResultEntity.failed(e.getMessage());
        } catch (UnknownHostException e) {
            System.out.println("URL地址错误");
            return ResultEntity.failed(e.getMessage());
        } catch (Exception e) {
            // 打开注释查看详细报错异常信息
            // e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /*
     * 读取返回结果
     */
    private static String read(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = new String(line.getBytes(), StandardCharsets.UTF_8);
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }


    /**
     * 专门负责上传文件到OSS服务器的工具方法
     *
     * @return 包含上传结果以及上传的文件在OSS上的访问路径
     * @paramendpoint OSS参数
     * @paramaccessKeyId OSS参数
     * @paramaccessKeySecret OSS参数
     * @paraminputStream 要上传的文件的输入流
     * @parambucketName OSS参数
     * @parambucketDomain OSS参数
     * @paramoriginalName 要上传的文件的原始文件名
     */
    public static ResultEntity<String> uploadFileToOss(
            String endpoint,
            String accessKeyId,
            String accessKeySecret,
            InputStream inputStream,
            String bucketName,
            String bucketDomain,
            String originalName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 生成上传文件的目录
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // 生成上传文件在OSS服务器上保存时的文件名
        // 原始文件名：beautfulgirl.jpg
        // 生成文件名：wer234234efwer235346457dfswet346235.jpg
        // 使用UUID生成文件主体名称
        String fileMainName = UUID.randomUUID().toString().replace("-", "");
        // 从原始文件名中获取文件扩展名
        String extensionName = originalName.substring(originalName.lastIndexOf("."));
        // 使用目录、文件主体名称、文件扩展名称拼接得到对象名称
        String objectName = folderName + "/" + fileMainName + extensionName;
        try {
            // 调用OSS客户端对象的方法上传文件并获取响应结果数据
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName,
                    inputStream);
            // 从响应结果中获取具体响应消息
            ResponseMessage responseMessage = putObjectResult.getResponse();
            // 根据响应状态码判断请求是否成功
            if (responseMessage == null) {
                // 拼接访问刚刚上传的文件的路径
                String ossFileAccessPath = bucketDomain + "/" + objectName;
                // 当前方法返回成功
                return ResultEntity.successWithData(ossFileAccessPath);
            } else {
                // 获取响应状态码
                int statusCode = responseMessage.getStatusCode();
                // 如果请求没有成功，获取错误消息
                String errorMessage = responseMessage.getErrorResponseAsString();
                // 当前方法返回失败
                return ResultEntity.failed("当前响应状态码=" + statusCode + " 错误消息 =" + errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 当前方法返回失败
            return ResultEntity.failed(e.getMessage());
        } finally {
            if (ossClient != null) {
                // 关闭OSSClient。
                ossClient.shutdown();
            }
        }
    }


}