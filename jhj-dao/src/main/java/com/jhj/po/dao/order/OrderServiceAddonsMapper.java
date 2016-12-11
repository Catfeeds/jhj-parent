package com.jhj.po.dao.order;

import java.util.List;

import com.jhj.po.model.order.OrderServiceAddons;


public interface OrderServiceAddonsMapper {
    int deleteByPrimaryKey(Long id);

    int deleteByOrderNo(String orderNo);

    int insert(OrderServiceAddons record);

    int insertSelective(OrderServiceAddons record);

    OrderServiceAddons selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderServiceAddons record);

    int updateByPrimaryKey(OrderServiceAddons record);

    List<OrderServiceAddons> selectByOrderNo(String orderNo);

	List<OrderServiceAddons> selectByOrderId(Long orderId);
}