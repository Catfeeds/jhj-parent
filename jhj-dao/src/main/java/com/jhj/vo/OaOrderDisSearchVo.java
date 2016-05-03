package com.jhj.vo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月14日下午8:57:43
 * @Description: 
 *
 */
public class OaOrderDisSearchVo extends OaOrderSearchVo{
	
	private String serviceDate;
	
	private Long staffId;
	
	
	private String staffName;
	
	
	private String startTimeStr;
	
	private String endTimeStr;
	
	
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

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}


	public String getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
	
	
	
}
