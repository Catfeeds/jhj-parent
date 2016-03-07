package com.jhj.vo.university;

/**
 *
 * @author :hulj
 * @Date : 2016年1月15日下午4:20:24
 * @Description:
 *	
 *		app端--叮当大学首页, 该服务人员考核状态
 *		
 */
public class AppStaffTestFirstVo {
	
	private Long serviceTypeId;
	
	private Short testStatus;

	
	/*
	 * 2016年1月25日11:51:16 
	 *  
	 *  要限制考试时间间隔为一天
	 * 	最近一次考试的时间，study_staff_test表，该 staff_id 对应的最新的add_time
	 */
	private Long lastTestTime;
	
	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}
	public Short getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(Short testStatus) {
		this.testStatus = testStatus;
	}

	public Long getLastTestTime() {
		return lastTestTime;
	}

	public void setLastTestTime(Long lastTestTime) {
		this.lastTestTime = lastTestTime;
	}
	
}
