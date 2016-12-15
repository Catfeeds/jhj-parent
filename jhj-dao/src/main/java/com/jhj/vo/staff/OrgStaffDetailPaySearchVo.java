package com.jhj.vo.staff;

import java.util.List;

/**
 *
 * @author :hulj
 * @Date : 2015年8月12日上午11:15:19
 * @Description: 
 *		运营平台--订单管理VO
 */
public class OrgStaffDetailPaySearchVo {
	
	private Long staffId;
	
	private String mobile;  //手机号
	
	private Short orderType;
	
	private String startTimeStr;
	
	private String endTimeStr;
	
	private Long startTime;	//选择的天 的 开始和结束 时间 时间戳
	
	private Long endTime;

	private Long orgId;
	
	private List<Long> searchCloudOrgIdList;	// 根据登录角色 的 门店 确定的 云店。。
	
	private Long orderId;
	
	private String orderNo;
	
	private String staffName;
	
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public List<Long> getSearchCloudOrgIdList() {
		return searchCloudOrgIdList;
	}

	public void setSearchCloudOrgIdList(List<Long> searchCloudOrgIdList) {
		this.searchCloudOrgIdList = searchCloudOrgIdList;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public Short getOrderType() {
		return orderType;
	}

	public void setOrderType(Short orderType) {
		this.orderType = orderType;
	}

}
