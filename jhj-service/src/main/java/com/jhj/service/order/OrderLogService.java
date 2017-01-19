package com.jhj.service.order;

import java.util.List;

import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.Orders;
import com.jhj.vo.order.OrderLogVo;


public interface OrderLogService {
	public OrderLog initOrderLog(Orders orders);
    int insert(OrderLog record);
	List<OrderLog> selectByOrderNo(String orderNo);
	
	OrderLogVo transVo(OrderLog orderLog);
}