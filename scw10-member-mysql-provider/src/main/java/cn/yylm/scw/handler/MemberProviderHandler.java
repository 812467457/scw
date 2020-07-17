package cn.yylm.scw.handler;

import cn.yylm.scw.constant.CrowdConstant;
import cn.yylm.scw.entity.po.Member;
import cn.yylm.scw.service.api.MemberService;
import cn.yylm.scw.util.ResultEntity;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MemberProviderHandler {
    @Resource
    private MemberService memberService;

    @RequestMapping("/get/member/by/loginAcct/remote")
    public ResultEntity<Member> getMemberByLoginAcctRemote(@RequestParam(value = "loginacct") String loginacct){
        try {
            //调用本地service
            Member member = memberService.getMemberByLoginAcct(loginacct);
            return ResultEntity.successWithData(member);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/save/member/remote")
    ResultEntity<Member> saveMember(@RequestBody Member member){
        try {
            memberService.saveMember(member);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            //账号被占用的异常
            if (e instanceof DuplicateKeyException) {
                return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
            return ResultEntity.failed(e.getMessage());
        }
    }
}
