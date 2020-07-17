package cn.yylm.scw.handler;

import cn.yylm.scw.api.MySQLRemoteService;
import cn.yylm.scw.config.OSSProperties;
import cn.yylm.scw.constant.CrowdConstant;
import cn.yylm.scw.entity.vo.*;
import cn.yylm.scw.util.CrowdUtil;
import cn.yylm.scw.util.ResultEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用来再project跳转页面
 */
@Controller
public class ProjectConsumerHandler {
    @Resource
    private OSSProperties ossProperties;

    @Resource
    private MySQLRemoteService mySQLRemoteService;

    /**
     * 保存项目的基本信息到redis
     *
     * @param projectVO         接收除了图片项目其他的基本信息
     * @param headerPicture     接收上传的头图
     * @param detailPictureList 接收上传的详情图
     * @param session           用来把数据存到session中
     * @return
     */
    @RequestMapping("/create/project/information")
    public String saveProjectBasicInfo(ProjectVO projectVO,
                                       MultipartFile headerPicture,
                                       List<MultipartFile> detailPictureList,
                                       HttpSession session,
                                       Model model) throws IOException {

        //上传头图
        boolean headerPictureIsEmpty = headerPicture.isEmpty();
        //判断是否有头图
        if (headerPictureIsEmpty) {
            //为空 返回
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }
        //不为空 上传到OSS
        ResultEntity<String> uploadHeaderPicResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                headerPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                Objects.requireNonNull(headerPicture.getOriginalFilename()));
        //判断是否上传成功
        String uploadHeaderPicResult = uploadHeaderPicResultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(uploadHeaderPicResult)) {
            //成功  获取图片返回路径
            String headerPicturePath = uploadHeaderPicResultEntity.getData();
            //存入projectVO对象
            projectVO.setHeaderPicturePath(headerPicturePath);
        } else {
            //失败
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
            return "project-launch";
        }


        //上传项目详情图
        //判断detailPictureList是否有效
        if (detailPictureList == null || detailPictureList.size() == 0) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_UPLOAD_FAILED);
            return "project-launch";
        }

        //创建一个集合保存详情图
        List<String> detailPicturePathList = new ArrayList<>();
        //遍历所有详情图
        for (MultipartFile detailPicture : detailPictureList) {
            //判断单个详情图是否为空
            if (detailPicture.isEmpty()) {
                //为空返回
                model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_UPLOAD_FAILED);
                return "project-launch";
            }

            //不为空，上传到OSS
            ResultEntity<String> uploadDetailPicResultEntity = CrowdUtil.uploadFileToOss(
                    ossProperties.getEndpoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    detailPicture.getInputStream(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    Objects.requireNonNull(detailPicture.getOriginalFilename()));

            String uploadDetailPicResult = uploadDetailPicResultEntity.getResult();

            //上传成功
            if (ResultEntity.SUCCESS.equals(uploadDetailPicResult)) {
                //获取每个详情图的访问路径并存入详情图集合
                String detailPicturePath = uploadDetailPicResultEntity.getData();
                detailPicturePathList.add(detailPicturePath);
            } else {
                //失败
                model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_UPLOAD_FAILED);
                return "project-launch";
            }
        }

        //把详情图集合存入projectVO
        projectVO.setDetailPicturePathList(detailPicturePathList);

        //把Vo对象存入session
        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT_, projectVO);
        //跳转回报信息页面
        return "redirect:http://localhost/project/return/info/page";
    }

    /**
     * 接收回报信息上传图片
     *
     * @return 图片上传后的路径
     */
    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public ResultEntity<String> uploadReturnPicture(@RequestParam("returnPicture") MultipartFile returnPicture) throws IOException {
        //执行文件上传
        ResultEntity<String> uploadReturnPicResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                returnPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                Objects.requireNonNull(returnPicture.getOriginalFilename()));
        //返回上传结果
        return uploadReturnPicResultEntity;
    }

    /**
     * 接收所有的回报信息
     *
     * @param returnVO
     * @param session
     * @return
     */
    @RequestMapping("/create/save/return.json")
    @ResponseBody
    public ResultEntity<String> saveReturn(ReturnVO returnVO, HttpSession session) {
        try {
            //从session中读取projectVO对象
            ProjectVO projectVo = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT_);
            //判断是否为null
            if (projectVo == null) {
                return ResultEntity.failed(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
            }
            //从projectVO获取回报信息的集合
            List<ReturnVO> returnVOList = projectVo.getReturnVOList();
            //判断集合是否有效
            if (returnVOList == null || returnVOList.size() == 0) {
                returnVOList = new ArrayList<>();
                projectVo.setReturnVOList(returnVOList);
            }
            //把表单数据存入returnVo
            returnVOList.add(returnVO);
            //从小把ProjectVo存入session
            session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT_, projectVo);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 执行保存
     *
     * @param session
     * @param memberConfirmInfoVO
     * @return
     */
    @RequestMapping("/create/confirm")
    public String saveConfirm(HttpSession session, MemberConfirmInfoVO memberConfirmInfoVO, Model model) {
        //从session读取projectVO的对象
        ProjectVO projectVo = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT_);
        //判断是否为null
        if (projectVo == null) {
            throw new RuntimeException(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
        }
        //把memberConfirmInfoVO设置到projectVo
        projectVo.setMemberConfirmInfoVO(memberConfirmInfoVO);

        //从session取出memberId
        //取出登录信息
        MemberLoginVo memberLoginVO = (MemberLoginVo) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = memberLoginVO.getId();
        //把projectVo保存到数据库
        ResultEntity<String> saveResultEntity = mySQLRemoteService.saveProjectVORemote(projectVo, memberId);
        //判断保存结果
        String result = saveResultEntity.getResult();
        if (ResultEntity.FAILED.equals(result)) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveResultEntity.getMessage());
            return "project-confirm";
        }
        //删除session中的projectVo
        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT_);
        //执行成功跳到完成页面
        return "redirect:http://localhost/project/create/success";
    }

    @RequestMapping("/get/project/detail/{projectId}")
    public String getProjectDetail(@PathVariable("projectId")Integer projectId, Model model) {
        ResultEntity<DetailProject> resultEntity = mySQLRemoteService.getDetailProjectVORemote(projectId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            DetailProject detailProjectVO = resultEntity.getData();
            model.addAttribute("detailProjectVO", detailProjectVO);
        }
        return "project-show-detail";
    }


}
