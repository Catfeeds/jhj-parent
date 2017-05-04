package com.jhj.vo.period;

public class PeriodOrderSearchVo {
 
    private String orderNo;

    private Integer userId;

    private String mobile;

    private Integer addrId;

    private Integer orderStatus;
    
    private Integer payType;

    private Integer orderFrom;

    private String remarks;

    private Long startAddTime;
    
    private Long endAddTime;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getAddrId() {
		return addrId;
	}

	public void setAddrId(Integer addrId) {
		this.addrId = addrId;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(Integer orderFrom) {
		this.orderFrom = orderFrom;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
}