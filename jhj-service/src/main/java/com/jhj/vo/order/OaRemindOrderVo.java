package com.jhj.vo.order;

import com.jhj.po.model.order.Orders;

/*
 * 提醒类订单 Vo
 */
public class OaRemindOrderVo extends Orders {
	
	//用户信息
	private String userName;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
