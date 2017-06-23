package com.jhj.vo.staff;

import java.math.BigDecimal;

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
	
	private Long serviceFinishTime;
	
	//订单支付总金额，包括加时，补差价
	private BigDecimal totalOrderPay;
	
	//服务人员订单收入
	private BigDecimal totalStaffIncoming;
	
	private String validateCodeName;
	
	//加时补差价
	private BigDecimal overtimeCompensation;
	
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

	public Long getServiceFinishTime() {
		return serviceFinishTime;
	}

	public void setServiceFinishTime(Long serviceFinishTime) {
		this.serviceFinishTime = serviceFinishTime;
	}

	public BigDecimal getTotalOrderPay() {
		return totalOrderPay;
	}

	public void setTotalOrderPay(BigDecimal totalOrderPay) {
		this.totalOrderPay = totalOrderPay;
	}

	public BigDecimal getTotalStaffIncoming() {
		return totalStaffIncoming;
	}

	public void setTotalStaffIncoming(BigDecimal totalStaffIncoming) {
		this.totalStaffIncoming = totalStaffIncoming;
	}

	public String getValidateCodeName() {
		return validateCodeName;
	}

	public void setValidateCodeName(String validateCodeName) {
		this.validateCodeName = validateCodeName;
	}

	public BigDecimal getOvertimeCompensation() {
		return overtimeCompensation;
	}

	public void setOvertimeCompensation(BigDecimal overtimeCompensation) {
		this.overtimeCompensation = overtimeCompensation;
	}
	
}
