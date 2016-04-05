package com.jhj.vo.user;

import com.jhj.po.model.user.UserDetailPay;

/**
 *
 * @author :hulj
 * @Date : 2016年4月5日下午6:15:42
 * @Description: 
 *
 */
public class AppUserDetailPayVo extends UserDetailPay {
	
	private String orderTypeName;
	
	private String payTypeName;

	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	
}
