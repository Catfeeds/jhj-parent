package com.jhj.po.dao.order;

import com.jhj.po.model.order.OrderExtend;

public interface OrderExtendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderExtend record);

    int insertSelective(OrderExtend record);

    OrderExtend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderExtend record);

    int updateByPrimaryKey(OrderExtend record);
    
    OrderExtend selectByOrderId(Long orderId);
    
    OrderExtend selectByOrderNo(String orderNo);
}