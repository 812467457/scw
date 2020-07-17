package cn.yylm.scw.handler;

import cn.yylm.scw.api.MySQLRemoteService;
import cn.yylm.scw.api.RedisRemoteService;
import cn.yylm.scw.config.ShortMessageProperties;
import cn.yylm.scw.constant.CrowdConstant;
import cn.yylm.scw.entity.po.Member;
import cn.yylm.scw.entity.vo.MemberLoginVo;
import cn.yylm.scw.entity.vo.MemberVo;
import cn.yylm.scw.util.CrowdUtil;
import cn.yylm.scw.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Controller
public class MyMemberHandler {
    @Resource
    private ShortMessageProperties shortMessageProperties;

    @Resource
    private RedisRemoteService redisRemoteService;

    @Resource
    private MySQLRemoteService mySQLRemoteService;



    /**
     * 退出登录
     * @param session
     * @return
     */
    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:http://localhost";
    }


    /**
     * 登录
     * @param loginacct
     * @param userpswd
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/auth/member/do/login")
    public String login(
            @RequestParam("loginacct") String loginacct,
            @RequestParam("userpswd") String userpswd,
            Model model,
            HttpSession session) {
        //根据账号查询用户
        ResultEntity<Member> resultEntity = mySQLRemoteService.getMemberByLoginAcctRemote(loginacct);
        //账号不存在
        if (ResultEntity.FAILED.equals(resultEntity.getResult())) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-login";
        }
        //账号存在得到一个member对象
        Member member = resultEntity.getData();
        if (member == null) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }

        //比较密码
        String dataBaseUserPswd = member.getUserpswd();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matchesResult = passwordEncoder.matches(userpswd, dataBaseUserPswd);
        //密码不正确
        if (!matchesResult) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        //密码正确创建一个MemberLoginVo对象并存入session
        MemberLoginVo memberLoginVo = new MemberLoginVo(member.getId(), member.getEmail(), member.getUsername());
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVo);

        return "redirect:http://localhost/auth/member/to/center/page";
        //return "redirect:/localhost/auth/member/to/center/page";
    }

    /**
     * 注册
     *
     * @param memberVo
     * @param model
     * @return
     */
    @RequestMapping("/auth/do/member/register")
    public String register(MemberVo memberVo, Model model) {
        //获取手机号
        String phoneNum = memberVo.getPhoneNum();
        //拼接redis中存储验证码的key
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
        //从redis取出value
        ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKeyRemote(key);
        //检查查询是否有效
        String result = resultEntity.getResult();
        if (ResultEntity.FAILED.equals(result)) {
            //失败
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-reg";
        }
        String redisCode = resultEntity.getData();
        if (redisCode == null) {
            //失败
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_NOT_EXIST);
            return "member-reg";
        }

        //验证验证码是否有效
        String formCode = memberVo.getCode();
        if (!Objects.equals(formCode, redisCode)) {
            //失败
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_PREFIX);
            return "member-reg";
        }

        //如果一致，从redis删除这条验证码
        redisRemoteService.removeRedisKeyRemote(key);

        //执行密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //加密前
        String oldUserpswd = memberVo.getUserpswd();

        //加密后保存到VO
        String newUserpswd = passwordEncoder.encode(oldUserpswd);
        memberVo.setUserpswd(newUserpswd);

        //执行保存
        //创建memberPo对象
        Member member = new Member();

        //复制属性
        BeanUtils.copyProperties(memberVo, member);

        //调用远程方法执行保存
        ResultEntity<Member> saveMemberResultEntity = mySQLRemoteService.saveMember(member);

        //保存失败
        if (ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())) {
            //失败
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveMemberResultEntity.getMessage());
            return "member-reg";
        }

        //重定向到登录页面，防止表单重复提交
        //return "redirect:/auth/member/to/login/page";
        return "redirect:http://auth/member/to/login/page";
    }

    /**
     * 发送验证码
     *
     * @param phoneNum
     * @return
     */
    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum) {
        //发送验证码
        ResultEntity<String> sendMessageResultEntity = CrowdUtil.sendMessage(
                shortMessageProperties.getHost(),
                shortMessageProperties.getPath(),
                phoneNum,
                shortMessageProperties.getAppCode(),
                shortMessageProperties.getSign(),
                shortMessageProperties.getSkin());
        //判断结果
        if (ResultEntity.SUCCESS.equals(sendMessageResultEntity.getResult())) {
            //发送成功 把验证码存入redis
            String code = sendMessageResultEntity.getData();
            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeOut(key, code, 5000L, TimeUnit.MINUTES);
            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())) {
                return ResultEntity.successWithoutData();
            } else {
                return saveCodeResultEntity;
            }
        } else {
            return sendMessageResultEntity;
        }
    }


}
