package com.jhj.vo.chart;

import java.math.BigDecimal;

public class ChartUserOrderNumVo {
	
	private Long orderFromId;
	
	private String orderFromName;
	
	private Integer countNum;
	
	private BigDecimal countMoney;

	public Long getOrderFromId() {
		return orderFromId;
	}

	public void setOrderFromId(Long orderFromId) {
		this.orderFromId = orderFromId;
	}

	public String getOrderFromName() {
		return orderFromName;
	}

	public void setOrderFromName(String orderFromName) {
		this.orderFromName = orderFromName;
	}

	public Integer getCountNum() {
		return countNum;
	}

	public void setCountNum(Integer countNum) {
		this.countNum = countNum;
	}

	public BigDecimal getCountMoney() {
		return countMoney;
	}

	public void setCountMoney(BigDecimal countMoney) {
		this.countMoney = countMoney;
	}
	
}
