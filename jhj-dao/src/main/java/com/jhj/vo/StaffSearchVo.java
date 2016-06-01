package com.jhj.vo;

import java.util.List;


public class StaffSearchVo {
	  
	private Long orgId;
	  
	private Short staffType;
	
	private String mobile;
	  
	private String name;
	  
	private Short sex;
	  
	private Long amId;
	  
	private Long parentId;
	  
	private List<Long> cloudOrgList;
	
	
	public List<Long> getCloudOrgList() {
		return cloudOrgList;
	}

	public void setCloudOrgList(List<Long> cloudOrgList) {
		this.cloudOrgList = cloudOrgList;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}


	public Long getAmId() {
		return amId;
	}

	
	public void setAmId(Long amId) {
		this.amId = amId;
	}

	
	public Short getSex() {
		return sex;
	}

	
	public void setSex(Short sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile = mobile !=null ? mobile.trim():new String();
	}

	public void setMobile(String mobile) {
		this.mobile = mobile.trim();
	}

	public String getName() {
		return name = name !=null ? name.trim():new String();
	}

	public void setName(String name) {
		this.name = name.trim();
	}
	
	  /**
	 * @return the staffType
	 */
	public Short getStaffType() {
		return staffType;
	}

	/**
	 * @param staffType the staffType to set
	 */
	public void setStaffType(Short staffType) {
		this.staffType = staffType;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	



}
