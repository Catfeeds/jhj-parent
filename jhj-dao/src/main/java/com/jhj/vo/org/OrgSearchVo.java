package com.jhj.vo.org;

public class OrgSearchVo {
	
	private Long orgId;
	
	private Long parentId;
	
	private Short isCloud;
	
	private String orgName;
	
	private String poiAddress;
	
	private Short orgStatus;

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

	public String getPoiAddress() {
		return poiAddress;
	}

	public void setPoiAddress(String poiAddress) {
		this.poiAddress = poiAddress;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Short getIsCloud() {
		return isCloud;
	}

	public void setIsCloud(Short isCloud) {
		this.isCloud = isCloud;
	}

	public Short getOrgStatus() {
		return orgStatus;
	}

	public void setOrgStatus(Short orgStatus) {
		this.orgStatus = orgStatus;
	}

}
