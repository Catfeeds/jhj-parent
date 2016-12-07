package com.jhj.vo.chart;

import java.util.List;

public class ChartSearchVo {
	
	/*
	 * 页面搜索条件
	 */
	private int selectCycle; //周期
	private int searchType; //查询方式 0 = 按周期查询 1 = 按时间范围查询.
	private String startTimeStr;	//开始时间
	private String endTimeStr;	//结束时间	
	private String statType;
	 
	/*
	 * 数据库搜索条件
	 */
	private Long startTime;	//开始时间
	private Long endTime;	//结束时间
	private List<Short> status;
	private List<Long> userIds;
	private Long orgId;
	
	//时间格式参数
	private String formatParam;
	
	private String orderFrom;
	
	private String orderOpFrom;
	
	private String serviceType;
	
	public List<Long> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
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
	public int getSelectCycle() {
		return selectCycle;
	}
	public void setSelectCycle(int selectCycle) {
		this.selectCycle = selectCycle;
	}
	public int getSearchType() {
		return searchType;
	}
	public void setSearchType(int searchType) {
		this.searchType = searchType;
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
	public List<Short> getStatus() {
		return status;
	}
	public void setStatus(List<Short> status) {
		this.status = status;
	}
	public String getStatType() {
		return statType;
	}
	public void setStatType(String statType) {
		this.statType = statType;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getFormatParam() {
		return formatParam;
	}
	public void setFormatParam(String formatParam) {
		this.formatParam = formatParam;
	}
	public String getOrderFrom() {
		return orderFrom;
	}
	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}
	public String getOrderOpFrom() {
		return orderOpFrom;
	}
	public void setOrderOpFrom(String orderOpFrom) {
		this.orderOpFrom = orderOpFrom;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
}
