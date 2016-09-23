package com.jhj.vo.staff;

import java.util.List;


public class StaffAuthSearchVo {
	
	private Long staffId;
	
	private List<Long> staffIds;
	
	private Long serviceTypeId;
	
	private List<Long> serviceTypeIds;
	
	private Short autStatus;

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public List<Long> getServiceTypeIds() {
		return serviceTypeIds;
	}

	public void setServiceTypeIds(List<Long> serviceTypeIds) {
		this.serviceTypeIds = serviceTypeIds;
	}

	public Short getAutStatus() {
		return autStatus;
	}

	public void setAutStatus(Short autStatus) {
		this.autStatus = autStatus;
	}

	public List<Long> getStaffIds() {
		return staffIds;
	}

	public void setStaffIds(List<Long> staffIds) {
		this.staffIds = staffIds;
	}

	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}
	
}
