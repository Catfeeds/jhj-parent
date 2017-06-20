package com.jhj.vo.org;

import java.util.Date;
import java.util.List;

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
	
	private Long staffId; // 员工手机号对应的员工id, 已 保证是 在 同一个 门店 ！
	
	private List<Long> staffIds;
	
	private String mobile;	

	private String leaveDateStr;
	
	private Date leaveDate;
	
	private Date rangeStartDate;
	
	private Date rangeEndDate;
	
	private Date startDate;
	
	//请假状态  1= 请假中  2= 请假取消
    private String leaveStatus;
    
    private String staffName;

	
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

	public String getLeaveDateStr() {
		return leaveDateStr;
	}

	public void setLeaveDateStr(String leaveDateStr) {
		this.leaveDateStr = leaveDateStr;
	}

	public Date getRangeStartDate() {
		return rangeStartDate;
	}

	public void setRangeStartDate(Date rangeStartDate) {
		this.rangeStartDate = rangeStartDate;
	}

	public Date getRangeEndDate() {
		return rangeEndDate;
	}

	public void setRangeEndDate(Date rangeEndDate) {
		this.rangeEndDate = rangeEndDate;
	}

	public String getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(String leaveStatus) {
		this.leaveStatus = leaveStatus;
	}

	public List<Long> getStaffIds() {
		return staffIds;
	}

	public void setStaffIds(List<Long> staffIds) {
		this.staffIds = staffIds;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


}
