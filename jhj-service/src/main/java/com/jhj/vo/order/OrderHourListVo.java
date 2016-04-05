package com.jhj.vo.order;

import com.jhj.po.model.order.Orders;

/**
 *
 * @author :hulj
 * @Date : 2015年8月4日上午11:32:39
 * @Description: 钟点工--订单列表VO
 *
 */
/**
 *
 * @author :hulj
 * @Date : 2015年8月4日下午3:58:03
 * @Description: TODO
 *
 */
public class OrderHourListVo extends Orders {
	
	/*
	 * 订单列表展示字段，，orders表字段
	 */
	
	private String orderHourTypeName;	//钟点工、深度保洁、助理预约单
	
	private String orderHourStatusName;		//订单状态 
	
	/*
	 * 订单详情字段
	 */
	private Long  addrId;
	private String address;//地址 为   name  +  addr (user_addrs表）


	public String getOrderHourStatusName() {
		return orderHourStatusName;
	}

	public void setOrderHourStatusName(String orderHourStatusName) {
		this.orderHourStatusName = orderHourStatusName;
	}

	public String getOrderHourTypeName() {
		return orderHourTypeName;
	}

	public void setOrderHourTypeName(String orderHourTypeName) {
		this.orderHourTypeName = orderHourTypeName;
	}

	@Override
	public Long getAddrId() {
		return addrId;
	}

	@Override
	public void setAddrId(Long addrId) {
		this.addrId = addrId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
	
}
