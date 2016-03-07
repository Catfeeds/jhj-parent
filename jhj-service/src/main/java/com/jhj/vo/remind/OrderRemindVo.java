package com.jhj.vo.remind;

import com.jhj.po.model.order.Orders;

/*
 * 提醒类订单 Vo
 */
public class OrderRemindVo extends Orders {
	
	// 列表页 VO
	
	private String orderStatusName;	//状态名称
	
	private String orderTypeName;	//类型名称
	
	//详情页 Vo
	
	private String userName;	// 该提醒 的 用户 姓名
	
	private String userMobile;	// 用户 的 联系方式
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getOrderStatusName() {
		return orderStatusName;
	}

	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}

	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	
	
}
