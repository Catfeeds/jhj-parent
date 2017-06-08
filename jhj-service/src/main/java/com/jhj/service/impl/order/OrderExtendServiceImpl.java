package com.jhj.service.impl.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.order.OrderExtendMapper;

import com.jhj.po.model.order.OrderExtend;
import com.jhj.service.order.OrderExtendService;

import com.jhj.service.order.OrdersService;
import com.jhj.vo.order.OrderSearchVo;
import com.meijia.utils.TimeStampUtil;

@Service
public class OrderExtendServiceImpl implements OrderExtendService {

	@Autowired
	private OrderExtendMapper mapper;


	@Override
	public OrderExtend initPo() {
		OrderExtend record = new OrderExtend();
		record.setId(0L);
		record.setUserId(0L);
		record.setOrderId(0L);
		record.setOrderNo("");
		record.setGroupCode("");
		record.setAddTime(TimeStampUtil.getNow() / 1000);
		return record;
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrderExtend record) {
		return mapper.insert(record);
	}

	@Override
	public int insertSelective(OrderExtend record) {
		return mapper.insertSelective(record);
	}

	@Override
	public OrderExtend selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(OrderExtend record) {
		return mapper.updateByPrimaryKey(record);
	}
	
	@Override
	public int updateByPrimaryKeySelective(OrderExtend record) {
		return mapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public OrderExtend selectByOrderId(Long orderId) {
		return mapper.selectByOrderId(orderId);
	}
	
	@Override
	public OrderExtend selectByOrderNo(String orderNo) {
		return mapper.selectByOrderNo(orderNo);
	}
	
}
