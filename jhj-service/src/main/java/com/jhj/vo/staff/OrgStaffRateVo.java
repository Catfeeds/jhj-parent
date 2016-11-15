package com.jhj.vo.staff;

import com.jhj.po.model.order.OrderRates;

public class OrgStaffRateVo extends OrderRates {
			
	private String mobile;
	
	private String userTypeStr;
	
    private String addTimeStr;


	public String getAddTimeStr() {
		return addTimeStr;
	}

	public void setAddTimeStr(String addTimeStr) {
		this.addTimeStr = addTimeStr;
	}

	public String getUserTypeStr() {
		return userTypeStr;
	}

	public void setUserTypeStr(String userTypeStr) {
		this.userTypeStr = userTypeStr;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
