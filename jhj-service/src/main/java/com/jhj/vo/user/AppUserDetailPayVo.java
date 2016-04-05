package com.jhj.vo.user;

import com.jhj.po.model.user.UserDetailPay;

/**
 *
 * @author :hulj
 * @Date : 2016年4月5日下午4:41:38
 * @Description: 
 *	
 *	微网站--我的--余额--消费明细
 */
public class AppUserDetailPayVo extends UserDetailPay {
	
	private String payTypeName;		// 支付方式名称
	
	private String orderTypeName;	// 订单类型名称

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	
}
