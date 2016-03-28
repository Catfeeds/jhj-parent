package com.jhj.vo.user;

import java.math.BigDecimal;

/**
 * @description：
 * @author： kerryg
 * @date:2015年9月23日 
 */
public class UserChargeVo {

	private Short chargeWay;
	
	private BigDecimal chargeMoney;
	
	private Long userId;
	
	private Long dictCardId;
	
	private String userMobile;
	
	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public Long getDictCardId() {
		return dictCardId;
	}

	public void setDictCardId(Long dictCardId) {
		this.dictCardId = dictCardId;
	}

	public Short getChargeWay() {
		return chargeWay;
	}

	public void setChargeWay(Short chargeWay) {
		this.chargeWay = chargeWay;
	}

	public BigDecimal getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(BigDecimal chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
}
