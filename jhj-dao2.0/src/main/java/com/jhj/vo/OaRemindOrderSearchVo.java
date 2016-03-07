package com.jhj.vo;

/**
 *
 * @author :hulj
 * @Date : 2015年10月11日上午10:22:50
 * @Description: 
 *		运营平台--提醒订单，搜索VO
 */
public class OaRemindOrderSearchVo {
	
	private String mobile;	// 手机号
	
	private String time;	//传递参数 日期 值  2015-09-16
	
	private Short orderStatus;
	
	//页面传参，及回显 所用的 参数
	private String startTimeStr;
	private String endTimeStr;
	
	/*
	 * 分析： 数据记录中的 add_time 包含 时、分、秒 的时间戳，
	 * 	前台只能选择到 天， 根据天，得到   该天 的 00:00 ~ 23:59 的时间戳
	 * 	
	 * 	用来做我 数据库 搜索条件
	 */
	
	private Long startTime;	//选择的天 的 开始和结束 时间 时间戳
	private Long endTime;
	
	/**
	 * @return the startTime
	 */
	public Long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Long getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the orderStatus
	 */
	public Short getOrderStatus() {
		return orderStatus;
	}

	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(Short orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * @return the startTimeStr
	 */
	public String getStartTimeStr() {
		return startTimeStr;
	}

	/**
	 * @param startTimeStr the startTimeStr to set
	 */
	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	/**
	 * @return the endTimeStr
	 */
	public String getEndTimeStr() {
		return endTimeStr;
	}

	/**
	 * @param endTimeStr the endTimeStr to set
	 */
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	
	
}
