package com.jhj.vo.user;

import java.util.List;


public class UserSmsNoticeSearchVo {

	
	private Long userId;
	
	private List<Long> userIds;
	
	private Short userType;
	
	private Short lastMonth;
	
	private String smsTemplateId;
	
	private String mobile;
	
	private Long startTime;
	
	private Long endTime;
	

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public Short getUserType() {
		return userType;
	}

	public void setUserType(Short userType) {
		this.userType = userType;
	}

	public String getSmsTemplateId() {
		return smsTemplateId;
	}

	public void setSmsTemplateId(String smsTemplateId) {
		this.smsTemplateId = smsTemplateId;
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

	public Short getLastMonth() {
		return lastMonth;
	}

	public void setLastMonth(Short lastMonth) {
		this.lastMonth = lastMonth;
	}


}
