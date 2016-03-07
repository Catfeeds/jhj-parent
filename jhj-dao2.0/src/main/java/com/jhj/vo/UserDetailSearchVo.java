package com.jhj.vo;


public class UserDetailSearchVo {

	  private String mobile;
	  
	  private String orderNo;

	public String getMobile() {
		return mobile = mobile !=null ? mobile.trim():new String();
	}

	public void setMobile(String mobile) {
		this.mobile = mobile.trim();
	}

	public String getOrderNo() {
		return  orderNo = orderNo !=null ? orderNo.trim():new String();
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	

	


	
	


	



}
