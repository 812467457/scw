package cn.yylm.scw.service.api;

import cn.yylm.scw.entity.po.Member;

public interface MemberService {

    Member getMemberByLoginAcct(String loginacct);

    void saveMember(Member member);
}
