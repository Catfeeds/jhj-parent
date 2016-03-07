package com.jhj.vo.user;

import java.math.BigDecimal;

import com.jhj.po.model.user.UserCoupons;

public class UserCouponVo extends UserCoupons {
	
	private String serviceTypeItem;

	private Long userId;

	private String giftName;
	
	private String introduction;
	
	private BigDecimal value;
	
	private BigDecimal maxValue;

	private Short couponType;

	private Short rangType;
	
	private Short rangFrom;	
    
    private String fromDateStr;
    
    private String toDateStr;


	public String getServiceTypeItem() {
		return serviceTypeItem;
	}

	public void setServiceTypeItem(String serviceTypeItem) {
		this.serviceTypeItem = serviceTypeItem;
	}

	@Override
	public Long getUserId() {
		return userId;
	}

	@Override
	public void setUserId(Long userId) {
		this.userId = userId;
	}



	public String getToDateStr() {
		return toDateStr;
	}

	public void setToDateStr(String toDateStr) {
		this.toDateStr = toDateStr;
	}

	public String getFromDateStr() {
		return fromDateStr;
	}

	public void setFromDateStr(String fromDateStr) {
		this.fromDateStr = fromDateStr;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	@Override
	public BigDecimal getValue() {
		return value;
	}

	@Override
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}

	public Short getCouponType() {
		return couponType;
	}

	public void setCouponType(Short couponType) {
		this.couponType = couponType;
	}

	public Short getRangType() {
		return rangType;
	}

	public void setRangType(Short rangType) {
		this.rangType = rangType;
	}

	public Short getRangFrom() {
		return rangFrom;
	}

	public void setRangFrom(Short rangFrom) {
		this.rangFrom = rangFrom;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

}
