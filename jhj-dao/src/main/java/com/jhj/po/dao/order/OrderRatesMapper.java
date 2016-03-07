package com.jhj.po.dao.order;

import java.util.List;

import com.jhj.po.model.order.OrderRates;

public interface OrderRatesMapper {
    int deleteByPrimaryKey(Long id);
    
    int deleteByOrderId(Long orderId);

    int insert(OrderRates record);

    int insertSelective(OrderRates record);

    OrderRates selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderRates record);

    int updateByPrimaryKey(OrderRates record);

	OrderRates selectByOrderId(Long orderId);

	List<OrderRates> setlectListByOrderId(Long orderId);
}