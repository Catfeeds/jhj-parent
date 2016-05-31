package com.jhj.vo;

import java.util.List;

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
	
	private Short serviceTypeFlag;
	
	private Short orgId;	// 下拉选择 的 门店id
	
	
	/*
	 * 2016年4月13日11:01:34 
	 * 	jhj2.1 
	 * 	
	 *  助理品类统计的 是 ，助理下的 大类，如 贴心家事、深度养护
	 * 
	 *  而助理订单 列表 的是 大类下的 具体子类。如 贴心家事-->安心托管。。
	 * 
	 * 	此处 下钻时。需要做 大类 -->子类的 转换
	 * 
	 */
	
	private Long  parentServiceType;
	
	private List<Long> childServiceTypeList;		
	
	// 2016年4月19日18:36:13  排班列表，点击进入订单列表
	private String orderNo;	
	
	
	//2016-5-3 17:23:13  钟点工和  助理订单 列表 搜索条件
	private String startTimeStr;
	
	private String endTimeStr;
	
	private Long searchOrgId;			//当前登录 店长 的 门店Id
	private List<Long> searchCloudOrgIdList;	// 根据登录角色 的 门店 确定的 云店。。
	
	private List<Short> searchOrderStatusList;	// 根据登录角色，确定 可以 查看的 订单状态
	
	
	private String remarks;		// 订单的备注查询
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public List<Short> getSearchOrderStatusList() {
		return searchOrderStatusList;
	}
	public void setSearchOrderStatusList(List<Short> searchOrderStatusList) {
		this.searchOrderStatusList = searchOrderStatusList;
	}
	public List<Long> getSearchCloudOrgIdList() {
		return searchCloudOrgIdList;
	}
	public void setSearchCloudOrgIdList(List<Long> searchCloudOrgIdList) {
		this.searchCloudOrgIdList = searchCloudOrgIdList;
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
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Long getParentServiceType() {
		return parentServiceType;
	}
	public void setParentServiceType(Long parentServiceType) {
		this.parentServiceType = parentServiceType;
	}
	public List<Long> getChildServiceTypeList() {
		return childServiceTypeList;
	}
	public void setChildServiceTypeList(List<Long> childServiceTypeList) {
		this.childServiceTypeList = childServiceTypeList;
	}
	public Short getOrgId() {
		return orgId;
	}
	public void setOrgId(Short orgId) {
		this.orgId = orgId;
	}
	public Long getSearchOrgId() {
		return searchOrgId;
	}
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
