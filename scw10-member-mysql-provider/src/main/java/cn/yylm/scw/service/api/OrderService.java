package cn.yylm.scw.service.api;

import cn.yylm.scw.entity.vo.AddressVO;
import cn.yylm.scw.entity.vo.OrderProjectVO;
import cn.yylm.scw.entity.vo.OrderVO;

import java.util.List;

public interface OrderService {
    OrderProjectVO gteOrderProjectVO(Integer projectId, Integer returnId);

    List<AddressVO> getAddressVPList(Integer memberId);

    void saveAddress(AddressVO addressVO);

    void saveOrderVO(OrderVO orderVO);
}
