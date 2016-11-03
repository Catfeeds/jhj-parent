package com.jhj.vo.user;

import java.util.List;


public class UserSearchVo {
	
	private Long userId;
	
	private List<Long> userIds;
	
	private List<Long> notUserIds;

    private String mobile;
  
    private String name;
  
    private Long addFrom;
    
    private List<Long> addFroms;
  
	private Long adminId;	// 加载 会员充值 列表页时。需要  过滤 显示  当前登录  用户（管理员、店长）  
	
	
	private Long orgId;	//店长 登录 条件过滤
	
	private List<Long> orgIds;	//根据 店长登录所在的 门店，得到 该门店下对应的 所有云店
	
	/*
	 *  页面参数
	 */
	private String startTimeStr;
	private String endTimeStr;
	
	/*
	 * 页面参数对应的数据库参数
	 */
	private Long startTime;
	private Long endTime;
	
	private int isVip;
	
	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

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

	public Long getAddFrom() {
		return addFrom;
	}

	public void setAddFrom(Long addFrom) {
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

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public List<Long> getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(List<Long> orgIds) {
		this.orgIds = orgIds;
	}

	public List<Long> getAddFroms() {
		return addFroms;
	}

	public void setAddFroms(List<Long> addFroms) {
		this.addFroms = addFroms;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public List<Long> getNotUserIds() {
		return notUserIds;
	}

	public void setNotUserIds(List<Long> notUserIds) {
		this.notUserIds = notUserIds;
	}

	public int getIsVip() {
		return isVip;
	}

	public void setIsVip(int isVip) {
		this.isVip = isVip;
	}

}
