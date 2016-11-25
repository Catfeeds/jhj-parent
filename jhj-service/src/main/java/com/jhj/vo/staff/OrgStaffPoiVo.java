package com.jhj.vo.staff;

public class OrgStaffPoiVo {
	
	private Long staffId;
			
	private String name;
	
	private String mobile;
	
	private String headImg;
	
    private String lat;

    private String lng;
    
    private String poiName;

    private Long poiTime;
    
    private String poiTimeStr;
    
    private int poiStatus;  // 1 = 在线  2= 在途中 3 = 服务中

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
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

	public int getPoiStatus() {
		return poiStatus;
	}

	public void setPoiStatus(int poiStatus) {
		this.poiStatus = poiStatus;
	}

	public String getPoiTimeStr() {
		return poiTimeStr;
	}

	public void setPoiTimeStr(String poiTimeStr) {
		this.poiTimeStr = poiTimeStr;
	}


}
