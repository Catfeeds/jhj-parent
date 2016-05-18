package com.jhj.vo;

import java.util.List;


public class UserDetailSearchVo {

    private String mobile;
  
    private String orderNo;
    
    /*
     * 页面参数
     */
    private String startTimeStr;
	
	private String endTimeStr;
	
	/*
	 * 数据库参数
	 */
	private Long startTime;	
	
	private Long endTime;
    
	
	// 在店长对应 云店下过单的 用户
	private List<Long> userIdList;
	

	public List<Long> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<Long> userIdList) {
		this.userIdList = userIdList;
	}

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

	public String getOrderNo() {
		return  orderNo = orderNo !=null ? orderNo.trim():new String();
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	

	


	
	


	



}
