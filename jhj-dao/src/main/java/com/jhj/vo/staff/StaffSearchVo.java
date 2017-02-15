package com.jhj.vo.staff;

import java.util.List;


public class StaffSearchVo {
	
	private Long staffId;
	
	private List<Long> staffIds;
	
	private Long orgId;
	
	private List<Long> orgIds;
	
	private Long parentId;
	  
	private List<Long> cloudOrgList;
	  
	private Short staffType;
	
	private String mobile;
	  
	private String name;
	  
	private Short sex;
	
	private String cardId;
	  
	private Long amId;
	
	private Integer status;
	
	private Long serviceTypeId;
	
	private List<Long> serviceTypeIds;
	
	private String staffCode;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

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

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public List<Long> getStaffIds() {
		return staffIds;
	}

	public void setStaffIds(List<Long> staffIds) {
		this.staffIds = staffIds;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public List<Long> getServiceTypeIds() {
		return serviceTypeIds;
	}

	public void setServiceTypeIds(List<Long> serviceTypeIds) {
		this.serviceTypeIds = serviceTypeIds;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public List<Long> getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(List<Long> orgIds) {
		this.orgIds = orgIds;
	}
	

}
