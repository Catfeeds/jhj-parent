package com.jhj.vo.user;

import java.util.List;

import com.jhj.po.model.user.UserDetailPay;


/**
 * @author hulj
 *
 */
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
	
	// 在店长对应 云店下过单的 用户
	private List<Long> userIds;
	
	//支付方式
	private Short payType;
	
	private Short isVip;
	
	//排序
	private String orderByParam;
	
	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
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

	public Short getPayType() {
		return payType;
	}

	public void setPayType(Short payType) {
		this.payType = payType;
	}

	public Short getIsVip() {
		return isVip;
	}

	public void setIsVip(Short isVip) {
		this.isVip = isVip;
	}

	public String getOrderByParam() {
		return orderByParam;
	}

	public void setOrderByParam(String orderByParam) {
		this.orderByParam = orderByParam;
	}

	
}
