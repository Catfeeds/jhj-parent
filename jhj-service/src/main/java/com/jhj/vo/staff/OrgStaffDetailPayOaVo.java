package com.jhj.vo.staff;

import com.jhj.po.model.bs.OrgStaffDetailPay;


public class OrgStaffDetailPayOaVo extends OrgStaffDetailPay{
	

	private String orderTypeName;
	//服务人员姓名
	private String name;

//	private String addTimeStr;
	
	private String payTypeName;
	
	private String userMobile;
	
	private String addr;
	
	private String orderListLink;
	
	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getOrderListLink() {
		return orderListLink;
	}

	public void setOrderListLink(String orderListLink) {
		this.orderListLink = orderListLink;
	}
	
}
