package com.jhj.vo;


public class UserSearchVo {

	  private String mobile;
	  
	  private String name;
	  
	  private Short addFrom;
	  
	  private Long startTime;
	  
	  private Long endTime;
	  
	  

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

	public Short getAddFrom() {
		return addFrom;
	}

	public void setAddFrom(Short addFrom) {
		this.addFrom = addFrom;
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
	


	



}
