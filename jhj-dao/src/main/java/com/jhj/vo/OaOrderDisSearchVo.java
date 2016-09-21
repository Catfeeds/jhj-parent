package com.jhj.vo;

import java.util.List;

/**
 *
 * @author :hulj
 * @Date : 2015年8月14日下午8:57:43
 * @Description: 
 *
 */
public class OaOrderDisSearchVo extends OaOrderSearchVo{
	
	private String serviceDate;
	
	//根据 staffId 唯一标识一个服务人员, 并找到该服务人员的   请假时间
	private Long staffId;
	
	private String staffName;
	
	private String startTimeStr;
	
	private String endTimeStr;
	
	//根据当前登录角色 确定 门店下的 所有云店
	private List<Long> cloudOrgList;
	
	private List<Long> orgIdList;
	
	public List<Long> getCloudOrgList() {
		return cloudOrgList;
	}

	public void setCloudOrgList(List<Long> cloudOrgList) {
		this.cloudOrgList = cloudOrgList;
	}

	@Override
	public String getStartTimeStr() {
		return startTimeStr;
	}

	@Override
	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	@Override
	public String getEndTimeStr() {
		return endTimeStr;
	}

	@Override
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}


	public String getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public List<Long> getOrgIdList() {
		return orgIdList;
	}

	public void setOrgIdList(List<Long> orgIdList) {
		this.orgIdList = orgIdList;
	}
	
	
}
