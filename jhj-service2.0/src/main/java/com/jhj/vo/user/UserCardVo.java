package com.jhj.vo.user;

import java.math.BigDecimal;

import com.jhj.po.model.order.OrderCards;

public class UserCardVo extends OrderCards{
	
	private String cardTypName;
	
	private String description;
	
	
	private BigDecimal restMoney;

	public BigDecimal getRestMoney() {
		return restMoney;
	}

	public void setRestMoney(BigDecimal restMoney) {
		this.restMoney = restMoney;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCardTypName() {
		return cardTypName;
	}

	public void setCardTypName(String cardTypName) {
		this.cardTypName = cardTypName;
	}
	
	

}
