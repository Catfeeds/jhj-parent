package com.jhj.vo;

public class MsgSearchVo {
	
	private long userId;
	
	private String title;
	
	private short isSend;
	
	private short userType;

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public short getUserType() {
		return userType;
	}

	public void setUserType(short userType) {
		this.userType = userType;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public short getIsSend() {
		return isSend;
	}

	public void setIsSend(short isSend) {
		this.isSend = isSend;
	}
	
	
	
}
