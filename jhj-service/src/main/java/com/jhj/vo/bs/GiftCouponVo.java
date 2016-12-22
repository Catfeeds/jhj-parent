package com.jhj.vo.bs;

import java.math.BigDecimal;
import java.util.Date;

import com.jhj.po.model.bs.GiftCoupons;

public class GiftCouponVo extends GiftCoupons {

	private String introduction;
	
	private BigDecimal value;
	
	private Short rangMonth;

	private String serviceType;
	
	private Date fromDate;
	
	private Date toDate;
	
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Short getRangMonth() {
		return rangMonth;
	}

	public void setRangMonth(Short rangMonth) {
		this.rangMonth = rangMonth;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}
