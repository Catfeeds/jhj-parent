package com.jhj.vo.staff;

import com.jhj.po.model.bs.OrgStaffs;

public class OrgStaffPoiListVo extends OrgStaffs {
	
	private String orgName;
	
	private String parentOrgName;	
	
	private String hukou;
	
    private String lat;

    private String lng;
    
    private String poiName;

    private Long poiTime;
    
    private String poiTimeStr;
    
    private int todayPoiStatus;    //今天是否有轨迹记录.
    
    private String staffLeave;

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

	public String getPoiName() {
		return poiName;
	}

	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}

	public Long getPoiTime() {
		return poiTime;
	}

	public void setPoiTime(Long poiTime) {
		this.poiTime = poiTime;
	}

	public String getPoiTimeStr() {
		return poiTimeStr;
	}

	public void setPoiTimeStr(String poiTimeStr) {
		this.poiTimeStr = poiTimeStr;
	}

	public String getHukou() {
		return hukou;
	}

	public void setHukou(String hukou) {
		this.hukou = hukou;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public int getTodayPoiStatus() {
		return todayPoiStatus;
	}

	public void setTodayPoiStatus(int todayPoiStatus) {
		this.todayPoiStatus = todayPoiStatus;
	}

	public String getStaffLeave() {
		return staffLeave;
	}

	public void setStaffLeave(String staffLeave) {
		this.staffLeave = staffLeave;
	}
    
 
}
