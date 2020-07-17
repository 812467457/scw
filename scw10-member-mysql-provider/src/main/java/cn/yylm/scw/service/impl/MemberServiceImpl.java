package cn.yylm.scw.service.impl;

import cn.yylm.scw.entity.po.Member;
import cn.yylm.scw.entity.po.MemberExample;
import cn.yylm.scw.mapper.MemberMapper;
import cn.yylm.scw.service.api.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
    @Resource
    private MemberMapper memberMapper;

    @Override
    public Member getMemberByLoginAcct(String loginacct) {
        //传教example对象
        MemberExample example = new MemberExample();
        //创建criteria对象
        MemberExample.Criteria criteria = example.createCriteria();
        //封装查询条件
        criteria.andLoginacctEqualTo(loginacct);
        //执行
        List<Member> members = memberMapper.selectByExample(example);
        //返回结果
        if (members == null || members.size() == 0) {
            return null;
        }
        return members.get(0);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveMember(Member member) {
        memberMapper.insertSelective(member);
    }
}
