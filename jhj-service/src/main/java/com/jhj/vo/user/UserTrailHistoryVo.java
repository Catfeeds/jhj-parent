package com.jhj.vo.user;

import com.jhj.po.model.user.UserTrailHistory;

public class UserTrailHistoryVo extends UserTrailHistory {

	
	private String name;
	
	private String mobile;
	
	private String addTimeStr;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddTimeStr() {
		return addTimeStr;
	}

	public void setAddTimeStr(String addTimeStr) {
		this.addTimeStr = addTimeStr;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
