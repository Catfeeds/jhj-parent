package com.jhj.service.impl.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.service.dict.ServiceTypeService;
import com.jhj.service.order.OrderHourListService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.vo.OrderSearchVo;
import com.jhj.vo.order.OrderHourListVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.OneCareUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年8月4日上午11:42:33
 * @Description: TODO
 *
 */
@Service
public class OrderHourListServiceImpl implements OrderHourListService {
	
	@Autowired
	private OrdersMapper orderMapper;
	
	@Autowired
	private UserAddrsService userAddrService;
	@Autowired
	private ServiceTypeService dictServiceTypeSerivice;
	
	@Autowired
	private OrdersService orderService;
	
	@Autowired
	private PartnerServiceTypeService partService;
	/*
	 * 当前订单
	 */
	@Override
	public List<Orders> selectNowOrderHourListByUserId(Long userId,int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		
		OrderSearchVo orderSearchVo = new OrderSearchVo();
		orderSearchVo.setUserId(userId);
		List<Orders> list = orderMapper.selectNowOrderHourByListPage(orderSearchVo);
		
		return list;
	}
	
	/*
	 * 历史订单
	 */
	@Override
	public List<Orders> selectOldOrderHourListByUserId(Long userId,int pageNo, int pageSize) {
	PageHelper.startPage(pageNo, pageSize);
		
		OrderSearchVo orderSearchVo = new OrderSearchVo();
		orderSearchVo.setUserId(userId);
		List<Orders> list = orderMapper.selectOldOrderHourByListPage(orderSearchVo);
		
		return list;
	}

	
	
	
	/*
	 * 设置 vo 相关字段供 展示
	 */
	@Override
	public List<OrderHourListVo> transOrderHourListVo(List<Orders> orderHourList){
		
		
		List<OrderHourListVo> voList = new  ArrayList<OrderHourListVo>();
		
		//可能在滑动到第二页时。已没有记录，则返回null即可
		if(!orderHourList.isEmpty() || orderHourList.size()>0){
			
			for (Orders orders : orderHourList) {
				OrderHourListVo orderHourListVo = initOHLV();
				BeanUtilsExp.copyPropertiesIgnoreNull(orders, orderHourListVo);
				
				//订单类型
				Short orderType = orders.getOrderType();
				
				
				Long serviceType = orders.getServiceType();
				
				PartnerServiceType type = partService.selectByPrimaryKey(serviceType);
				
				//2016-2-20 15:37:11    修改订单状态 名称为 二期 新定义的名称
				orderHourListVo.setOrderHourTypeName(type.getName());
				
				//订单状态
				Short orderStatus = orders.getOrderStatus();
				
				//2016年2月20日15:55:30   已更新为 二期 新定义的订单类型
				orderHourListVo.setOrderHourStatusName(OneCareUtil.
							getJhjOrderStausNameFromOrderType(orderType,orderStatus));
				
				//地址 
				UserAddrs userAddrs = userAddrService.selectByPrimaryKey(orders.getAddrId());
				
				orderHourListVo.setAddrId(orders.getAddrId());
				if(userAddrs!=null){
					orderHourListVo.setAddress(userAddrs.getName()+" "+userAddrs.getAddr());
				}
				
				voList.add(orderHourListVo);
				
			}
		
		}
		return voList;
	}
	@Override
	public OrderHourListVo initOHLV() {
		Orders orders = orderService.initOrders();
		OrderHourListVo  hourListVo = new OrderHourListVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(orders, hourListVo);
		hourListVo.setOrderHourTypeName("");
		hourListVo.setOrderHourStatusName("");
		hourListVo.setAddrId(0L);
		hourListVo.setAddress("");
		
		return hourListVo;
	}
}
