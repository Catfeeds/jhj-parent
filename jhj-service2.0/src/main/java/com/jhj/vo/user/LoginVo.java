package com.jhj.vo.user;

public class LoginVo {

	private Long id;

	private String mobile;

	
	public LoginVo() {
	}
	
	public LoginVo(Long id, String mobile) {
		super();
		this.id = id;
		this.mobile = mobile;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
