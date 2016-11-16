package com.jhj.po.dao.order;

import java.util.List;

import com.jhj.po.model.order.OrderAppoint;
import com.jhj.vo.order.OrderDispatchSearchVo;

public interface OrderAppointMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderAppoint record);

    int insertSelective(OrderAppoint record);

    OrderAppoint selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderAppoint record);

    int updateByPrimaryKey(OrderAppoint record);
    
    List<OrderAppoint> selectBySearchVo(OrderDispatchSearchVo searchVo);
}