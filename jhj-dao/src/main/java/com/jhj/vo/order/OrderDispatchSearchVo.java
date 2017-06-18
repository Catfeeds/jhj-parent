package com.jhj.vo.order;

import java.util.List;

public class OrderDispatchSearchVo {
		
	private Long staffId;
	
	private List<Long> staffIds;
	
	private String staffMobile;
	
	private String staffName;
	
	private Long userId;
	
	private String mobile;
	
	private Long orderId;
	
	private List<Long> orderIds;
	
	private String orderNo;
	
	private Long orgId;
	
	private Long parentId;
	
	private List<Long> orgIds;
	
	private List<Long> parentIds;
				
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
	
	private Short dispatchStatus;
	
	private Short isApply;
	
	private Long serviceTypeId;
	
	private List<Short> orderStatusList;
	
	private Long startServiceFinishTime;
	
	private Long endServiceFinishTime;

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getStaffMobile() {
		return staffMobile;
	}

	public void setStaffMobile(String staffMobile) {
		this.staffMobile = staffMobile;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public List<Long> getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(List<Long> orgIds) {
		this.orgIds = orgIds;
	}

	public List<Long> getParentIds() {
		return parentIds;
	}

	public void setParentIds(List<Long> parentIds) {
		this.parentIds = parentIds;
	}

	public Short getDispatchStatus() {
		return dispatchStatus;
	}

	public void setDispatchStatus(Short dispatchStatus) {
		this.dispatchStatus = dispatchStatus;
	}

	public List<Long> getStaffIds() {
		return staffIds;
	}

	public void setStaffIds(List<Long> staffIds) {
		this.staffIds = staffIds;
	}

	public List<Long> getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(List<Long> orderIds) {
		this.orderIds = orderIds;
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

	public Short getIsApply() {
		return isApply;
	}

	public void setIsApply(Short isApply) {
		this.isApply = isApply;
	}

	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public List<Short> getOrderStatusList() {
		return orderStatusList;
	}

	public void setOrderStatusList(List<Short> orderStatusList) {
		this.orderStatusList = orderStatusList;
	}

	public Long getStartServiceFinishTime() {
		return startServiceFinishTime;
	}

	public void setStartServiceFinishTime(Long startServiceFinishTime) {
		this.startServiceFinishTime = startServiceFinishTime;
	}

	public Long getEndServiceFinishTime() {
		return endServiceFinishTime;
	}

	public void setEndServiceFinishTime(Long endServiceFinishTime) {
		this.endServiceFinishTime = endServiceFinishTime;
	}

	
}
