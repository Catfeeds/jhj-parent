package com.jhj.vo.user;

import com.jhj.po.model.user.FinanceRecharge;

/**
 *
 * @author :hulj
 * @Date : 2016年3月28日下午2:04:55
 * @Description: 
 *
 */
public class FinanceRechargeVo extends FinanceRecharge {
	
	private String userName;
	
	private String userMobile;
	
	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
