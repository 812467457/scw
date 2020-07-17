package cn.yylm.scw.service.impl;

import cn.yylm.scw.entity.po.AddressPO;
import cn.yylm.scw.entity.po.AddressPOExample;
import cn.yylm.scw.entity.po.OrderPO;
import cn.yylm.scw.entity.po.OrderProjectPO;
import cn.yylm.scw.entity.vo.AddressVO;
import cn.yylm.scw.entity.vo.OrderProjectVO;
import cn.yylm.scw.entity.vo.OrderVO;
import cn.yylm.scw.mapper.AddressPOMapper;
import cn.yylm.scw.mapper.OrderPOMapper;
import cn.yylm.scw.mapper.OrderProjectPOMapper;
import cn.yylm.scw.service.api.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderProjectPOMapper orderProjectPOMapper;

    @Resource
    private OrderPOMapper orderPOMapper;

    @Resource
    private AddressPOMapper addressPOMapper;

    @Override
    public OrderProjectVO gteOrderProjectVO(Integer projectId, Integer returnId) {
        return orderProjectPOMapper.selectOrderProjectVO(projectId, returnId);
    }

    @Override
    public List<AddressVO> getAddressVPList(Integer memberId) {
        AddressPOExample addressPOExample = new AddressPOExample();
        addressPOExample.createCriteria().andMemberIdEqualTo(memberId);
        List<AddressPO> addressPOList = addressPOMapper.selectByExample(addressPOExample);

        List<AddressVO> addressVOList = new ArrayList<>();
        for (AddressPO addressPO : addressPOList) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPO, addressVO);
            addressVOList.add(addressVO);
        }

        return addressVOList;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveAddress(AddressVO addressVO) {
        AddressPO addressPO = new AddressPO();
        BeanUtils.copyProperties(addressVO, addressPO);
        addressPOMapper.insert(addressPO);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveOrderVO(OrderVO orderVO) {
        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVO, orderPO);
        OrderProjectPO orderProjectPO = new OrderProjectPO();
        BeanUtils.copyProperties(orderVO.getOrderProjectVO(), orderProjectPO);
        orderPOMapper.insert(orderPO);
        Integer id = orderPO.getId();
        orderProjectPO.setId(id);
        orderProjectPOMapper.insert(orderProjectPO);
    }
}
