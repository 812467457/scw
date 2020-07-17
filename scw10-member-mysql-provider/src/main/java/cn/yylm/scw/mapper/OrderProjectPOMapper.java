package cn.yylm.scw.mapper;

import cn.yylm.scw.entity.po.OrderProjectPO;
import cn.yylm.scw.entity.po.OrderProjectPOExample;

import java.util.List;

import cn.yylm.scw.entity.vo.OrderProjectVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

public interface OrderProjectPOMapper {
    int countByExample(OrderProjectPOExample example);

    int deleteByExample(OrderProjectPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OrderProjectPO record);

    int insertSelective(OrderProjectPO record);

    List<OrderProjectPO> selectByExample(OrderProjectPOExample example);

    OrderProjectPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OrderProjectPO record, @Param("example") OrderProjectPOExample example);

    int updateByExample(@Param("record") OrderProjectPO record, @Param("example") OrderProjectPOExample example);

    int updateByPrimaryKeySelective(OrderProjectPO record);

    int updateByPrimaryKey(OrderProjectPO record);

    OrderProjectVO selectOrderProjectVO(@Param("projectId") Integer projectId, @Param("returnId") Integer returnId);
}