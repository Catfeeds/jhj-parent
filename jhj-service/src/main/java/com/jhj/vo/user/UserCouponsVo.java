package com.jhj.vo.user;

import java.math.BigDecimal;

import com.jhj.po.model.user.UserCoupons;

public class UserCouponsVo extends UserCoupons{

	private String couponsName;
	
	private BigDecimal maxValue;
	

	public String getCouponsName() {
		return couponsName;
	}

	public void setCouponsName(String couponsName) {
		this.couponsName = couponsName;
	}

	public BigDecimal getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}

}
