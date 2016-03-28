package com.jhj.vo;


public class UserSearchVo {

    private String mobile;
  
    private String name;
  
    private Short addFrom;
  
    private Long startTime;
  
    private Long endTime;
 	  
	private Long adminId;	// 加载 会员充值 列表页时。需要  过滤 显示  当前登录  用户（管理员、店长）  

	
	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
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
