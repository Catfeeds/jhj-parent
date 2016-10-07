package com.jhj.vo.order;

import java.math.BigDecimal;

public class OrderListVo {

	private Long staffId;
	
	private Short orderType;
	
	private String orderTypeName;
	
	private Long orderId;
	
	private String orderNo;
	
	private Long serviceTypeId;
	
	private String serviceTypeName;
	
	private BigDecimal orderMoney;
	
	private BigDecimal orderIncoming;
	
	private String serviceAddr;
	
	private String serviceAddrDistance;
	
	private String pickAddr;
	
	private String pickAddrDistance;
	
	private String serviceContent;
	
	private String serviceDate;
	
	private String buttonWord;

	
	private Short orderStatus;
	
	private String orderStatusStr;
	
	private String serviceAddrLat;

	private String serviceAddrLng;
	
	private String pickAddrLat;
	
	private String pickAddrLng;
	
	private String serviceHour;
	
	private String remarks;
	
	private String remarksConfirm;
	
	private Short payType;
	
	private String payTypeName;
	
	private String mobile;
	
	
	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public Short getOrderType() {
		return orderType;
	}

	public void setOrderType(Short orderType) {
		this.orderType = orderType;
	}

	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	

	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public BigDecimal getOrderIncoming() {
		return orderIncoming;
	}

	public void setOrderIncoming(BigDecimal orderIncoming) {
		this.orderIncoming = orderIncoming;
	}

	public String getServiceAddr() {
		return serviceAddr;
	}

	public void setServiceAddr(String serviceAddr) {
		this.serviceAddr = serviceAddr;
	}

	public String getPickAddr() {
		return pickAddr;
	}

	public void setPickAddr(String pickAddr) {
		this.pickAddr = pickAddr;
	}

	public String getServiceContent() {
		return serviceContent;
	}

	public void setServiceContent(String serviceContent) {
		this.serviceContent = serviceContent;
	}

	public String getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getButtonWord() {
		return buttonWord;
	}

	public void setButtonWord(String buttonWord) {
		this.buttonWord = buttonWord;
	}


	public String getServiceAddrLat() {
		return serviceAddrLat;
	}

	public void setServiceAddrLat(String serviceAddrLat) {
		this.serviceAddrLat = serviceAddrLat;
	}

	public String getServiceAddrLng() {
		return serviceAddrLng;
	}

	public void setServiceAddrLng(String serviceAddrLng) {
		this.serviceAddrLng = serviceAddrLng;
	}

	public String getPickAddrLat() {
		return pickAddrLat;
	}

	public void setPickAddrLat(String pickAddrLat) {
		this.pickAddrLat = pickAddrLat;
	}

	public String getPickAddrLng() {
		return pickAddrLng;
	}

	public void setPickAddrLng(String pickAddrLng) {
		this.pickAddrLng = pickAddrLng;
	}


	public Short getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Short orderStatus) {
		this.orderStatus = orderStatus;
	}
	


	public String getOrderStatusStr() {
		return orderStatusStr;
	}

	public void setOrderStatusStr(String orderStatusStr) {
		this.orderStatusStr = orderStatusStr;
	}

	public String getServiceHour() {
		return serviceHour;
	}

	public void setServiceHour(String serviceHour) {
		this.serviceHour = serviceHour;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getRemarksConfirm() {
		return remarksConfirm;
	}

	public void setRemarksConfirm(String remarksConfirm) {
		this.remarksConfirm = remarksConfirm;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getServiceAddrDistance() {
		return serviceAddrDistance;
	}

	public void setServiceAddrDistance(String serviceAddrDistance) {
		this.serviceAddrDistance = serviceAddrDistance;
	}

	public String getPickAddrDistance() {
		return pickAddrDistance;
	}

	public void setPickAddrDistance(String pickAddrDistance) {
		this.pickAddrDistance = pickAddrDistance;
	}

	public Short getPayType() {
		return payType;
	}

	public void setPayType(Short payType) {
		this.payType = payType;
	}	
}
