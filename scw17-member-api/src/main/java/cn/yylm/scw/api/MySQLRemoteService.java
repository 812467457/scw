package cn.yylm.scw.api;


import cn.yylm.scw.entity.po.Member;
import cn.yylm.scw.entity.vo.*;
import cn.yylm.scw.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * MySQL远程调用接口
 */
@FeignClient("SCW-MYSQL-PROVIDER")
public interface MySQLRemoteService {
    @RequestMapping("/get/member/by/loginAcct/remote")
    ResultEntity<Member> getMemberByLoginAcctRemote(@RequestParam("loginacct") String loginacct);

    @RequestMapping("/save/member/remote")
    ResultEntity<Member> saveMember(@RequestBody Member member);

    @RequestMapping("/save/project/vo/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVo, @RequestParam("memberId") Integer memberId);

    @RequestMapping("/get/portal/type/project/data/Remote")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote();

    @RequestMapping("/get/project/detail/remote/{projectId}")
    public ResultEntity<DetailProject> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId);

    @RequestMapping("/get/order/project/VO/remote")
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(@RequestParam("projectId") Integer projectId, @RequestParam("returnId") Integer returnId);

    @RequestMapping("/get/address/vo/remote")
    ResultEntity<List<AddressVO>> getAddressVORemote(@RequestParam("memberId") Integer memberId);

    @RequestMapping("/save/address/remote")
    ResultEntity<String> saveAddressRemote(@RequestBody AddressVO addressVO);
    @RequestMapping("/save/order/remote")
    ResultEntity<String> saveOrderRemote(@RequestBody OrderVO orderVO);
}
