package com.jhj.service.impl.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.order.OrderLogMapper;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.Orders;
import com.jhj.service.order.OrderLogService;
import com.meijia.utils.TimeStampUtil;

@Service
public class OrderLogServiceImpl implements OrderLogService {

	@Autowired
	private OrderLogMapper orderLogMapper;
	@Override
	public OrderLog initOrderLog(Orders orders) {
		OrderLog orderLog = new OrderLog();
		orderLog.setAddTime(TimeStampUtil.getNow()/1000);
		orderLog.setMobile(orders.getMobile());
		orderLog.setOrderId(orders.getId());
		orderLog.setOrderNo(orders.getOrderNo());
		orderLog.setOrderStatus(orders.getOrderStatus());
		orderLog.setRemarks(orders.getRemarks());
		orderLog.setId(0L);
		return orderLog;
	}

	@Override
	public int insert(OrderLog record) {
		return orderLogMapper.insert(record);
	}

	@Override
	public List<OrderLog> selectByOrderNo(String orderNo) {
		return orderLogMapper.selectByOrderNo(orderNo);
	}
}