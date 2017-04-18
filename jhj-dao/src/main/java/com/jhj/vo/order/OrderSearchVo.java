package com.jhj.vo.order;

import java.util.List;

public class OrderSearchVo {
	
	private Long amId;
	
	private Long staffId;
	
	private String staffName;
	
	private String staffMobile;
	
	private Long selectStaff;
	
	private Long userId;
	
	private String mobile;
	
	private Long orderId;
	
	private String orderNo;
	
	private Short orderType;
	
	private Short orderExtType;
	
	private List<Short> orderTypes;

	private Short serviceType;
	
	private List<Long> serviceTypes;
	
	private Short orderFrom;
	
	private Long orderOpFrom;
	
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
	
	private Long startOrderDoneTime;
	
	private Long endOrderDoneTime;
	
	private Long startTime;
	
	private Long endTime;
	
	private String startTimeStr;
	
	private String endTimeStr;
	
	private String serviceStartTimeStr;
	
	private String serviceEndTimeStr;
	
	private String startUpdateTimeStr;
	
	private String endUpdateTimeStr;
	
	private String startOrderDoneTimeStr;
	
	private String endOrderDoneTimeStr;
	
	private Long parentId;
	
	private Long orgId;
	
	private String addrName;
	
	private Short isApply;
	
	//排序字段
	private String orderByProperty;
	
	private Short payType; // 支付方式
	
	private Short dispatchStatus;
	
	private	Short adminId;
	
	private String remarks;

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
	
	public Long getOrderOpFrom() {
		return orderOpFrom;
	}

	public void setOrderOpFrom(Long orderOpFrom) {
		this.orderOpFrom = orderOpFrom;
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

	public List<Long> getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(List<Long> serviceTypes) {
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

	public Short getIsApply() {
		return isApply;
	}

	public void setIsApply(Short isApply) {
		this.isApply = isApply;
	}

	public String getOrderByProperty() {
		return orderByProperty;
	}

	public void setOrderByProperty(String orderByProperty) {
		this.orderByProperty = orderByProperty;
	}

	public Short getPayType() {
		return payType;
	}

	public void setPayType(Short payType) {
		this.payType = payType;
	}

	public Short getOrderExtType() {
		return orderExtType;
	}

	public void setOrderExtType(Short orderExtType) {
		this.orderExtType = orderExtType;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffMobile() {
		return staffMobile;
	}

	public void setStaffMobile(String staffMobile) {
		this.staffMobile = staffMobile;
	}

	public Long getSelectStaff() {
		return selectStaff;
	}

	public void setSelectStaff(Long selectStaff) {
		this.selectStaff = selectStaff;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public String getServiceStartTimeStr() {
		return serviceStartTimeStr;
	}

	public void setServiceStartTimeStr(String serviceStartTimeStr) {
		this.serviceStartTimeStr = serviceStartTimeStr;
	}

	public String getServiceEndTimeStr() {
		return serviceEndTimeStr;
	}

	public void setServiceEndTimeStr(String serviceEndTimeStr) {
		this.serviceEndTimeStr = serviceEndTimeStr;
	}

	public Short getDispatchStatus() {
		return dispatchStatus;
	}

	public void setDispatchStatus(Short dispatchStatus) {
		this.dispatchStatus = dispatchStatus;
	}

	public String getStartUpdateTimeStr() {
		return startUpdateTimeStr;
	}

	public void setStartUpdateTimeStr(String startUpdateTimeStr) {
		this.startUpdateTimeStr = startUpdateTimeStr;
	}

	public String getEndUpdateTimeStr() {
		return endUpdateTimeStr;
	}

	public void setEndUpdateTimeStr(String endUpdateTimeStr) {
		this.endUpdateTimeStr = endUpdateTimeStr;
	}

	public Long getStartOrderDoneTime() {
		return startOrderDoneTime;
	}

	public void setStartOrderDoneTime(Long startOrderDoneTime) {
		this.startOrderDoneTime = startOrderDoneTime;
	}

	public Long getEndOrderDoneTime() {
		return endOrderDoneTime;
	}

	public void setEndOrderDoneTime(Long endOrderDoneTime) {
		this.endOrderDoneTime = endOrderDoneTime;
	}

	public String getStartOrderDoneTimeStr() {
		return startOrderDoneTimeStr;
	}

	public void setStartOrderDoneTimeStr(String startOrderDoneTimeStr) {
		this.startOrderDoneTimeStr = startOrderDoneTimeStr;
	}

	public String getEndOrderDoneTimeStr() {
		return endOrderDoneTimeStr;
	}

	public void setEndOrderDoneTimeStr(String endOrderDoneTimeStr) {
		this.endOrderDoneTimeStr = endOrderDoneTimeStr;
	}

	public Short getAdminId() {
		return adminId;
	}

	public void setAdminId(Short adminId) {
		this.adminId = adminId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
