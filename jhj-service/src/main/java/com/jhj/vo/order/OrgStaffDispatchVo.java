package com.jhj.vo.order;

import com.jhj.po.model.bs.OrgStaffs;

public class OrgStaffDispatchVo extends OrgStaffs {

	// 纬度
	private String lat;

	// 经度
	private String lng;

	// 地址名称
	private String addrName;

	// 员工距离(单位是米)
	private int distanceValue;

	// 员工距离(文本描述)
	private String distanceText;

	// 距离时间(文本描述)
	private String durationText;
	
	// 云店距离(单位是米)
	private int orgDistanceValue;
	
	// 云店距离(文本描述)
	private String orgDistanceText;
	
	//该人员当天的 派工单量
	private int todayOrderNum; 		

	//该服务人员，所在 门店 的名称
	private String parentOrgName; 	
	
	//该服务人员 所在 云店名称
	private String orgName; 		

	//该服务人员 派工情况描述
	private String dispathStaStr;	

	// 派工情况 标识
	private int dispathStaFlag;		
	
	//无法派工原因
	private String reason;     
	
	//派工依据. 合理分配  效率优先
	private String allocateReason;  
	
	
	public int getDispathStaFlag() {
		return dispathStaFlag;
	}

	public void setDispathStaFlag(int dispathStaFlag) {
		this.dispathStaFlag = dispathStaFlag;
	}

	public String getDispathStaStr() {
		return dispathStaStr;
	}

	public void setDispathStaStr(String dispathStaStr) {
		this.dispathStaStr = dispathStaStr;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public int getDistanceValue() {
		return distanceValue;
	}

	public void setDistanceValue(int distanceValue) {
		this.distanceValue = distanceValue;
	}

	public String getDistanceText() {
		return distanceText;
	}

	public void setDistanceText(String distanceText) {
		this.distanceText = distanceText;
	}

	public String getDurationText() {
		return durationText;
	}

	public void setDurationText(String durationText) {
		this.durationText = durationText;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getOrgDistanceValue() {
		return orgDistanceValue;
	}

	public void setOrgDistanceValue(int orgDistanceValue) {
		this.orgDistanceValue = orgDistanceValue;
	}

	public String getOrgDistanceText() {
		return orgDistanceText;
	}

	public void setOrgDistanceText(String orgDistanceText) {
		this.orgDistanceText = orgDistanceText;
	}

	public String getAllocateReason() {
		return allocateReason;
	}

	public void setAllocateReason(String allocateReason) {
		this.allocateReason = allocateReason;
	}

	public String getAddrName() {
		return addrName;
	}

	public void setAddrName(String addrName) {
		this.addrName = addrName;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public int getTodayOrderNum() {
		return todayOrderNum;
	}

	public void setTodayOrderNum(int todayOrderNum) {
		this.todayOrderNum = todayOrderNum;
	}

}
