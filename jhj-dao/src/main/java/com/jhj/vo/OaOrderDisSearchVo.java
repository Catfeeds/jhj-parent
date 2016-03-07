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
