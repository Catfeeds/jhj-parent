package com.jhj.vo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月12日上午11:15:19
 * @Description: 
 *		运营平台--订单管理VO
 */
public class OaOrderSearchVo {
	
	private Short orderType;	//服务类型
	
	private Short orderRate;	//订单评价
	
	private Short orderStatus;  //订单状态
	
	private Short disStatus;	//派工状态
	
	//数据下钻
	private Long startTime; 	//开始时间
	
	private Long endTime;		//结束时间
	
	private Short searchOrderType;	//订单类型
	
	private Short searchOrderFrom;	//订单来源
	
	private Short searchOrderStatus; //订单状态，ex：统计 退单~~
	
	private Short searchOrderServiceType; 	//订单服务类型
	
	private Long searchOrgId;			//当前登录 店长 的 门店Id
	private Short serviceTypeFlag;
	
	
	/**
	 * @return the searchOrgId
	 */
	public Long getSearchOrgId() {
		return searchOrgId;
	}
	/**
	 * @param searchOrgId the searchOrgId to set
	 */
	public void setSearchOrgId(Long searchOrgId) {
		this.searchOrgId = searchOrgId;
	}
	public Short getServiceTypeFlag() {
		return serviceTypeFlag;
	}
	public void setServiceTypeFlag(Short serviceTypeFlag) {
		this.serviceTypeFlag = serviceTypeFlag;
	}
	public Short getSearchOrderServiceType() {
		return searchOrderServiceType;
	}
	public void setSearchOrderServiceType(Short searchOrderServiceType) {
		this.searchOrderServiceType = searchOrderServiceType;
	}
	public Short getSearchOrderStatus() {
		return searchOrderStatus;
	}
	public void setSearchOrderStatus(Short searchOrderStatus) {
		this.searchOrderStatus = searchOrderStatus;
	}
	public Short getSearchOrderType() {
		return searchOrderType;
	}
	public void setSearchOrderType(Short searchOrderType) {
		this.searchOrderType = searchOrderType;
	}
	public Short getSearchOrderFrom() {
		return searchOrderFrom;
	}

	public void setSearchOrderFrom(Short searchOrderFrom) {
		this.searchOrderFrom = searchOrderFrom;
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

	public Short getDisStatus() {
		return disStatus;
	}

	public void setDisStatus(Short disStatus) {
		this.disStatus = disStatus;
	}

	/**
	 * @return the orderType
	 */
	public Short getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(Short orderType) {
		this.orderType = orderType;
	}

	public Short getOrderRate() {
		return orderRate;
	}

	public void setOrderRate(Short orderRate) {
		this.orderRate = orderRate;
	}

	public Short getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Short orderStatus) {
		this.orderStatus = orderStatus;
	}

	
}
