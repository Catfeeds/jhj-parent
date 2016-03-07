package com.jhj.service.impl.remind;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.dao.user.UsersMapper;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.Users;
import com.jhj.service.order.OrdersService;
import com.jhj.service.remind.OrderRemindService;
import com.jhj.vo.OrderSearchVo;
import com.jhj.vo.remind.OrderRemindVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.OneCareUtil;

/*
 *  提醒类订单 service
 */
@Service
public class OrderRemindServiceImpl implements OrderRemindService{
	
	@Autowired
	private OrdersMapper orderMapper;
	@Autowired
	private OrdersService orderService;
	@Autowired
	private UsersMapper userMapper;
	
	//当前 提醒
	@Override
	public List<Orders> selectNowRemind(Long userId,int pageNo,int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		
		OrderSearchVo orderSearchVo = new OrderSearchVo();
		orderSearchVo.setUserId(userId);
		
		List<Orders> list = orderMapper.selectNowRemind(orderSearchVo);
		
		return list;
	}

	// 历史 提醒
	@Override
	public List<Orders> selectOldRemind(Long userId,int pageNo,int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		
		OrderSearchVo orderSearchVo = new OrderSearchVo();
		orderSearchVo.setUserId(userId);
		
		List<Orders> list = orderMapper.selectOldRemind(orderSearchVo);
		
		return list;
	}

	
	
	@Override
	public OrderRemindVo initRemindVo() {
		
		Orders orders = orderService.initOrders();
		
		OrderRemindVo orderRemindVo = new OrderRemindVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(orders,orderRemindVo);
		
		orderRemindVo.setOrderStatusName("");
		orderRemindVo.setOrderTypeName("");
		
		
		return orderRemindVo;
	}

	/*
	 * 列表页 展示  VO
	 */
	@Override
	public List<OrderRemindVo> transListToVO(List<Orders> orderList) {
		
		List<OrderRemindVo> remindList = new ArrayList<OrderRemindVo>();
		
		if(!orderList.isEmpty() || orderList.size()>0){
			for (Orders orders : orderList) {
				
				OrderRemindVo remindVo = initRemindVo();
				
				BeanUtilsExp.copyPropertiesIgnoreNull(orders, remindVo);
				// 类型名称
				Short orderType = orders.getOrderType();
				String orderTypeName = OneCareUtil.getJhjOrderTypeName(orderType);
				
				remindVo.setOrderTypeName(orderTypeName);
				
				//状态名称
				Short orderStatus = orders.getOrderStatus();
				String stausName = OneCareUtil.getJhjOrderStausName(orderStatus);
				remindVo.setOrderStatusName(stausName);
				
				remindList.add(remindVo);
			}
		}
		
		return remindList;
	}

	/*
	 *  提醒类 订单 详情 (用户版、助理版)
	 */
	
	@Override
	public OrderRemindVo getRemindDetail(String orderNo) {
		
		Orders orders = orderMapper.selectByOrderNo(orderNo);
		
		//详情页 VO
		OrderRemindVo remindVo = initRemindVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(orders, remindVo);
		// 类型名称
		Short orderType = orders.getOrderType();
		String orderTypeName = OneCareUtil.getJhjOrderTypeName(orderType);
		
		remindVo.setOrderTypeName(orderTypeName);
		
		//状态名称
		Short orderStatus = orders.getOrderStatus();
		String stausName = OneCareUtil.getJhjOrderStausName(orderStatus);
		
		remindVo.setOrderStatusName(stausName);
		
		// 用户信息 （助理版需要）
		
		Long userId = orders.getUserId();
		
		Users users = userMapper.selectByPrimaryKey(userId);
		
		remindVo.setUserName(users.getName());
		remindVo.setUserMobile(users.getMobile());
		
		return remindVo;
	}

	@Override
	public Long getRemindCountToDo(Long userId) {
		Long remindCountToDo = orderMapper.getRemindCountToDo(userId);
		
		return remindCountToDo;
	}
	
	
	
	/******************	助理端	*********************/
	
	@Override
	public List<OrderRemindVo> selectNowRemindAm(Long amId, int pageNo, int pageSize) {
		
		PageHelper.startPage(pageNo, pageSize);
		
		OrderSearchVo orderSearchVo = new OrderSearchVo();
		orderSearchVo.setAmId(amId);
		
		List<Orders> lists = orderMapper.selectNowRemindAm(orderSearchVo);
		
		List<OrderRemindVo> voList = transListToVO(lists);
		
		return voList;
	}

	

}
