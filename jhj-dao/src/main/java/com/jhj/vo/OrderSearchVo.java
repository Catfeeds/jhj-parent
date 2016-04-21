package com.jhj.vo;

public class OrderSearchVo {
	
	private Long amId;
	
	private Long staffId;
	
	private Long userId;
	
	private String mobile;
	
	private String orderNo;

	private Short serviceType;
	
	private Short orderFrom;
	
	private Short orderStatus;
	
	private Long startTime;
	
	private Long endTime;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Short getServiceType() {
		return serviceType;
	}

	public void setServiceType(Short serviceType) {
		this.serviceType = serviceType;
	}

	public Short getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(Short orderFrom) {
		this.orderFrom = orderFrom;
	}

	public Short getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Short orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAmId() {
		return amId;
	}

	public void setAmId(Long amId) {
		this.amId = amId;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}



}
