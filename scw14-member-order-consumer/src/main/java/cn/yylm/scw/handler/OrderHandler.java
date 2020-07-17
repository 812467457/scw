package cn.yylm.scw.handler;

import cn.yylm.scw.api.MySQLRemoteService;
import cn.yylm.scw.constant.CrowdConstant;
import cn.yylm.scw.entity.po.Member;
import cn.yylm.scw.entity.vo.AddressVO;
import cn.yylm.scw.entity.vo.MemberLoginVo;
import cn.yylm.scw.entity.vo.OrderProjectVO;
import cn.yylm.scw.util.ResultEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderHandler {
    @Resource
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo(
            @PathVariable Integer projectId,
            @PathVariable Integer returnId,
            HttpSession session) {
        ResultEntity<OrderProjectVO> resultEntity = mySQLRemoteService.getOrderProjectVORemote(projectId, returnId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            OrderProjectVO orderProjectVO = resultEntity.getData();
            //保持数据
            session.setAttribute("orderProjectVO", orderProjectVO);
        }

        return "confirm_return";
    }

    @RequestMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(@PathVariable("returnCount") Integer returnCount, HttpSession session) {
        //把回报数量合并到session
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        orderProjectVO.setReturnCount(returnCount);
        session.setAttribute("orderProjectVO", orderProjectVO);

        //取出当前用户id用来查询收货地址
        MemberLoginVo member = (MemberLoginVo) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = member.getId();
        //查询收货地址
        ResultEntity<List<AddressVO>> resultEntity = mySQLRemoteService.getAddressVORemote(memberId);

        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            List<AddressVO> list = resultEntity.getData();
            session.setAttribute("addressVOList", list);
        }

        return "confirm_order";
    }

    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO, HttpSession session) {
        //保存地址信息
        ResultEntity<String> resultEntity = mySQLRemoteService.saveAddressRemote(addressVO);
        System.out.println("地址保存结果" + resultEntity.getResult());
        //取出orderProjectVO
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        //获取returnCount，用来重定向的时候根据returnCount查询
        Integer returnCount = orderProjectVO.getReturnCount();
        return "redirect:http://localhost/order/confirm/order/" + returnCount;

    }

}
