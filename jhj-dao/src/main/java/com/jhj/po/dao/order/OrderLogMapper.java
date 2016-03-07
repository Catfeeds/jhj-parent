package com.jhj.po.dao.order;

import java.util.List;

import com.jhj.po.model.order.OrderLog;
 
public interface OrderLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderLog orderLog);

    int insertSelective(OrderLog record);

    OrderLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderLog record);

    int updateByPrimaryKey(OrderLog record);

	List<OrderLog> selectByOrderNo(String orderNo);
}