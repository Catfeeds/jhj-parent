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
	
	private Short orderFlag;		//该条明细记录  的 副作用 是    消费(用户钱少了) 还是  充值 (用户钱多了)
	
	private String orderTypeName;
	
	private String payTypeName;

	
	public Short getOrderFlag() {
		return orderFlag;
	}

	public void setOrderFlag(Short orderFlag) {
		this.orderFlag = orderFlag;
	}

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
