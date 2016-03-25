package com.jhj.vo.staff;

import java.math.BigDecimal;
import java.util.List;

public class OrgStaffsVo {
	
private Long staffId;
	
	private Short staffType;
	
	private Short status;

	private Short sex;
	
	private String name;
	
	private String mobile;
	
	private String headImg;
	
	private Short authStatus;

	private Long totalOrder;
	
	private BigDecimal totalIncoming;
	
	private List<String> skills;

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public Short getStaffType() {
		return staffType;
	}

	public void setStaffType(Short staffType) {
		this.staffType = staffType;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Short getSex() {
		return sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public Short getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(Short authStatus) {
		this.authStatus = authStatus;
	}

	public Long getTotalOrder() {
		return totalOrder;
	}

	public void setTotalOrder(Long totalOrder) {
		this.totalOrder = totalOrder;
	}

	public BigDecimal getTotalIncoming() {
		return totalIncoming;
	}

	public void setTotalIncoming(BigDecimal totalIncoming) {
		this.totalIncoming = totalIncoming;
	}

	public List<String> getSkills() {
		return skills;
	}

	public void setSkills(List<String> skills) {
		this.skills = skills;
	}
	
	
	

}
