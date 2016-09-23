package com.jhj.vo.order;

import java.util.List;

public class OrderSearchVo {
	
	private Long amId;
	
	private Long staffId;
	
	private Long userId;
	
	private String mobile;
	
	private Long orderId;
	
	private String orderNo;
	
	private Short orderType;
	
	private List<Short> orderTypes;

	private Short serviceType;
	
	private List<Short> serviceTypes;
	
	private Short orderFrom;
	
	private Short orderStatus;
	
	private List<Short> orderStatusList;
	
	private Short orderRate;
		
	//2016年5月4日17:39:11  某个 订单的 服务时间
	private Long startServiceTime;
	
	private Long endServiceTime;
	
	private Long startServiceHourTime;
	
	private Long endServiceHourTime;
	
	private Long startAddTime;
	
	private Long endAddTime;	
	
	private Long startUpdateTime;
	
	private Long endUpdateTime;	
	
	private Long startTime;
	
	private Long endTime;
	
	private Long parentId;
	
	private Long orgId;
	
	private String addrName;

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

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public List<Short> getOrderStatusList() {
		return orderStatusList;
	}

	public void setOrderStatusList(List<Short> orderStatusList) {
		this.orderStatusList = orderStatusList;
	}

	public Long getStartServiceTime() {
		return startServiceTime;
	}

	public void setStartServiceTime(Long startServiceTime) {
		this.startServiceTime = startServiceTime;
	}

	public Long getEndServiceTime() {
		return endServiceTime;
	}

	public void setEndServiceTime(Long endServiceTime) {
		this.endServiceTime = endServiceTime;
	}

	public Long getStartAddTime() {
		return startAddTime;
	}

	public void setStartAddTime(Long startAddTime) {
		this.startAddTime = startAddTime;
	}

	public Long getEndAddTime() {
		return endAddTime;
	}

	public void setEndAddTime(Long endAddTime) {
		this.endAddTime = endAddTime;
	}

	public Long getStartUpdateTime() {
		return startUpdateTime;
	}

	public void setStartUpdateTime(Long startUpdateTime) {
		this.startUpdateTime = startUpdateTime;
	}

	public Long getEndUpdateTime() {
		return endUpdateTime;
	}

	public void setEndUpdateTime(Long endUpdateTime) {
		this.endUpdateTime = endUpdateTime;
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

	public Short getOrderType() {
		return orderType;
	}

	public void setOrderType(Short orderType) {
		this.orderType = orderType;
	}

	public Short getServiceType() {
		return serviceType;
	}

	public void setServiceType(Short serviceType) {
		this.serviceType = serviceType;
	}

	public List<Short> getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(List<Short> serviceTypes) {
		this.serviceTypes = serviceTypes;
	}

	public Long getStartServiceHourTime() {
		return startServiceHourTime;
	}

	public void setStartServiceHourTime(Long startServiceHourTime) {
		this.startServiceHourTime = startServiceHourTime;
	}

	public Long getEndServiceHourTime() {
		return endServiceHourTime;
	}

	public void setEndServiceHourTime(Long endServiceHourTime) {
		this.endServiceHourTime = endServiceHourTime;
	}

	public List<Short> getOrderTypes() {
		return orderTypes;
	}

	public void setOrderTypes(List<Short> orderTypes) {
		this.orderTypes = orderTypes;
	}

	public Short getOrderRate() {
		return orderRate;
	}

	public void setOrderRate(Short orderRate) {
		this.orderRate = orderRate;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getAddrName() {
		return addrName;
	}

	public void setAddrName(String addrName) {
		this.addrName = addrName;
	}
}
