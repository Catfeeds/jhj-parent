package com.jhj.vo.socials;

import com.jhj.po.model.socials.SocialCall;

public class SocialCallVo extends SocialCall {
	
	private String userName;
	
	private String amName;
	
	private String title;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAmName() {
		return amName;
	}

	public void setAmName(String amName) {
		this.amName = amName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
