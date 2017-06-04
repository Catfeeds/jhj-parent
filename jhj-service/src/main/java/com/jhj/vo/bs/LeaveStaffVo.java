package com.jhj.vo.bs;

import com.jhj.po.model.bs.OrgStaffLeave;

/**
 *
 * @author :hulj
 * @Date : 2016年5月12日下午12:36:51
 * @Description:
 *
 */
public class LeaveStaffVo extends OrgStaffLeave {

	private String staffName; // 请假人的 姓名
	private String staffMobile;
	private String leaveStatus; // 当前的请假状态
	private String excuteStaffName; // 请假批复人 名称

	private Short leaveDuration; // 假期 时间段 标识 0= 8~12 1=8~21 2=12~21

	private String cloudOrgName; // 云店名称

	private String leaveDateStr; // 假期 时间展示

	private String leaveDateEndStr;

	public String getLeaveDateStr() {
		return leaveDateStr;
	}

	public void setLeaveDateStr(String leaveDateStr) {
		this.leaveDateStr = leaveDateStr;
	}

	public String getCloudOrgName() {
		return cloudOrgName;
	}

	public void setCloudOrgName(String cloudOrgName) {
		this.cloudOrgName = cloudOrgName;
	}

	public Short getLeaveDuration() {
		return leaveDuration;
	}

	public void setLeaveDuration(Short leaveDuration) {
		this.leaveDuration = leaveDuration;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffMobile() {
		return staffMobile;
	}

	public void setStaffMobile(String staffMobile) {
		this.staffMobile = staffMobile;
	}

	public String getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(String leaveStatus) {
		this.leaveStatus = leaveStatus;
	}

	public String getExcuteStaffName() {
		return excuteStaffName;
	}

	public void setExcuteStaffName(String excuteStaffName) {
		this.excuteStaffName = excuteStaffName;
	}

	public String getLeaveDateEndStr() {
		return leaveDateEndStr;
	}

	public void setLeaveDateEndStr(String leaveDateEndStr) {
		this.leaveDateEndStr = leaveDateEndStr;
	}

}
