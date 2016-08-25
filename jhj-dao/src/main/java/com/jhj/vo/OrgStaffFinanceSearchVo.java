package com.jhj.vo;

import java.util.List;


public class OrgStaffFinanceSearchVo {

	private String mobile;
  
	private Long startTime;
  
	private Long endTime;
	
	private Long orgId;
	
	private List<Long> searchCloudOrgIdList;	// 根据登录角色 的 门店 确定的 云店。。
	
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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

	public String getMobile() {
		return mobile = mobile !=null ? mobile.trim():new String();
	}

	public void setMobile(String mobile) {
		this.mobile = mobile.trim();
	}

	public List<Long> getSearchCloudOrgIdList() {
		return searchCloudOrgIdList;
	}

	public void setSearchCloudOrgIdList(List<Long> searchCloudOrgIdList) {
		this.searchCloudOrgIdList = searchCloudOrgIdList;
	}
}
