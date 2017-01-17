package com.jhj.vo.order;

import com.jhj.po.model.order.OrderLog;

public class OrderLogVo extends OrderLog{
	
	private String userTypeName;
	
	private String addTimeStr;

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
	

}
