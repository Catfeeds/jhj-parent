package com.jhj.vo.order;

import com.jhj.po.model.bs.OrgStaffs;

public class OrgStaffsNewVo extends OrgStaffs {

	// 纬度
	private String lat;

	// 经度
	private String lng;

	// 地址名称
	private String locName;

	// 线路距离(单位是米)
	private int distanceValue;

	// 线路距离(文本描述)
	private String distanceText;

	// 距离时间(单位为秒)
	private int durationValue;

	// 距离时间(文本描述)
	private String durationText;

	
	/*
	 * 后台手动派工 时。展示的 冗余字段
	 */
	
	private Long todayOrderNum; // 2016年3月23日19:13:05 该人员当天的 派工单量

	private String staffOrgName; // 2016年3月30日16:45:11  该服务人员，所在 门店 的名称
	private String staffCloudOrgName; //2016年3月30日16:45:14   该服务人员 所在 云店名称

	

	public String getStaffOrgName() {
		return staffOrgName;
	}

	public void setStaffOrgName(String staffOrgName) {
		this.staffOrgName = staffOrgName;
	}


	public String getStaffCloudOrgName() {
		return staffCloudOrgName;
	}

	public void setStaffCloudOrgName(String staffCloudOrgName) {
		this.staffCloudOrgName = staffCloudOrgName;
	}

	public Long getTodayOrderNum() {
		return todayOrderNum;
	}

	public void setTodayOrderNum(Long todayOrderNum) {
		this.todayOrderNum = todayOrderNum;
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

	public String getLocName() {
		return locName;
	}

	public void setLocName(String locName) {
		this.locName = locName;
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

	public int getDurationValue() {
		return durationValue;
	}

	public void setDurationValue(int durationValue) {
		this.durationValue = durationValue;
	}

	public String getDurationText() {
		return durationText;
	}

	public void setDurationText(String durationText) {
		this.durationText = durationText;
	}

}
