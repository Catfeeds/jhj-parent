package com.jhj.service.impl.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.order.OrderLogMapper;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.Orders;
import com.jhj.service.order.OrderLogService;
import com.jhj.vo.order.OrderLogVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
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
		orderLog.setUserId(0L);
		orderLog.setUserName("");
		orderLog.setUserType((short)2);
		orderLog.setAction("");
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

	@Override
	public OrderLogVo transVo(OrderLog orderLog) {
		if(orderLog==null) return null;
		
		OrderLogVo orderLogVo=new OrderLogVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(orderLog, orderLogVo);
		
		String addTimeStr = DateUtil.convTimeStampToStringDate(orderLog.getAddTime(), DateUtil.DEFAULT_FULL_PATTERN);
		orderLogVo.setAddTimeStr(addTimeStr);
		if(orderLog.getUserType()==0){
			orderLogVo.setUserTypeName("用户");
		}
		if(orderLog.getUserType()==1){
			orderLogVo.setUserTypeName("服务人员");
		}
		if(orderLog.getUserType()==2){
			orderLogVo.setUserTypeName("后台管理人员");
		}
		return orderLogVo;
	}
}