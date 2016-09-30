package com.jhj.vo.bs;

import com.jhj.po.model.bs.Orgs;

public class OrgDispatchPoiVo extends Orgs  {
	
	//纬度
	private String lat;
	
	//经度
	private String lng;
		
	//地址名称
	private String name;
	
	//线路距离(单位是米)
	private int	distanceValue;
	
	//线路距离(文本描述)
	private String distanceText;
	
	
	//距离时间(单位为秒)
	private int durationValue;
	
	//距离时间(文本描述)
	private String durationText;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
