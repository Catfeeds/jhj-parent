package com.jhj.vo.order;

import com.jhj.po.model.order.OrderLog;

public class OrderLogVo extends OrderLog{
	
	private String userTypeName;
	
	private String addTimeStr;
	
	private String realName;

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public String getAddTimeStr() {
		return addTimeStr;
	}

	public void setAddTimeStr(String addTimeStr) {
		this.addTimeStr = addTimeStr;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
}
