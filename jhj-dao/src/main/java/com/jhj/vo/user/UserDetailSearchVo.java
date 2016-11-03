package com.jhj.vo.user;

import java.util.List;

import com.jhj.po.model.user.UserDetailPay;


public class UserDetailSearchVo extends UserDetailPay{

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
	
	
	private String staffCode;
	
	//办卡推荐人（员工）
	private String staffName;
	
	private Long chargeMoney;
	
	//充值总金额
	private Long totalMoney;
	
	
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

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public Long getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(Long chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	public Long getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Long totalMoney) {
		this.totalMoney = totalMoney;
	}


}
