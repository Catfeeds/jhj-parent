package com.jhj.vo.org;

import java.util.Date;

/**
 *
 * @author :hulj
 * @Date : 2016年5月12日上午11:47:28
 * @Description:
 *
 */
public class LeaveSearchVo {

	private Long orgId;

	private Long parentId;

	private String leaveDateStr; // 假期时间： 周三请 周五的假，此字段为 周五

	private Date leaveDate;

	private Long leaveStartTime; // 假期时间的时间戳，数据库查询用
	
	private Long leaveEndTime;
	
	 // 作为比对时间戳，start > matchStartTime and end < endTimeStartTime
	private Long serviceStartTime; 
	
	 // 作为比对时间戳，OR start > matchEndTime and end < matchEndTime
	private Long serviceEndTime;
	
	

	private Long staffId; // 员工手机号对应的员工id, 已 保证是 在 同一个 门店 ！
	
	private String mobile;

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getLeaveDateStr() {
		return leaveDateStr;
	}

	public void setLeaveDateStr(String leaveDateStr) {
		this.leaveDateStr = leaveDateStr;
	}

	public Long getLeaveStartTime() {
		return leaveStartTime;
	}

	public void setLeaveStartTime(Long leaveStartTime) {
		this.leaveStartTime = leaveStartTime;
	}

	public Long getLeaveEndTime() {
		return leaveEndTime;
	}

	public void setLeaveEndTime(Long leaveEndTime) {
		this.leaveEndTime = leaveEndTime;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}

	public Long getServiceStartTime() {
		return serviceStartTime;
	}

	public void setServiceStartTime(Long serviceStartTime) {
		this.serviceStartTime = serviceStartTime;
	}

	public Long getServiceEndTime() {
		return serviceEndTime;
	}

	public void setServiceEndTime(Long serviceEndTime) {
		this.serviceEndTime = serviceEndTime;
	}

	// private Short leaveDuration; //假期时间段 标识
	//
	// private Long orderServiceDateStart; // 派工时，订单服务时间 （开始）的时间戳
	//
	// private Long orderServiceDateEnd; // 派工时，订单服务时间 （结束）的时间戳
	//
	//
	// // 服务人员排班表里，开始时间(和 leaveDate 比较),确定某个时间下,服务人员的请假状态
	// private Long dispatchDateStartStr;
	//
	// //格式为 日期格式不能直接比较。转换为时间戳
	// private Long dispatchDateEndStr;

}
