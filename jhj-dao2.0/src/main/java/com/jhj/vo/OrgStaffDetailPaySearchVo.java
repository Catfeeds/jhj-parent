package com.jhj.vo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月12日上午11:15:19
 * @Description: 
 *		运营平台--订单管理VO
 */
public class OrgStaffDetailPaySearchVo {
	

	private String mobile;  //手机号
	
	private String startTimeStr;
	
	private String endTimeStr;
	
	private Long startTime;	//选择的天 的 开始和结束 时间 时间戳
	
	private Long endTime;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

}
