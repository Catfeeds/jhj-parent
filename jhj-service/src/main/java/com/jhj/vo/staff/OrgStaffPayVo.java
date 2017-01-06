package com.jhj.vo.staff;


public class OrgStaffPayVo {
	
	private Long staffId;

	private String orderTypeName;

	private String OrderPay;

	private String mobile;
	
	private String addTimeStr;

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}

	public String getOrderPay() {
		return OrderPay;
	}

	public void setOrderPay(String orderPay) {
		OrderPay = orderPay;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddTimeStr() {
		return addTimeStr;
	}

	public void setAddTimeStr(String addTimeStr) {
		this.addTimeStr = addTimeStr;
	}

	
	
	
}
