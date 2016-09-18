package com.jhj.po.model.bs;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class DictCoupons {
    private Long id;

	private String cardNo;

	private String cardPasswd;

	private BigDecimal value;
	
	private BigDecimal maxValue;

	private Short couponType;

	private Short rangType;

	private Short rangFrom;

	private String serviceType;

	private String introduction;

	private String description;
	
	private Short rangMonth;

	private Date fromDate;

	private Date toDate;

	private Long addTime;

	private Long updateTime;
	
	/**
	 * 新增字段
	 * 优惠券使用条件
	 * */
	private int couponsTypeId;
	
	private List<Integer> sendCouponsCondtion;
	
	//优惠券是否能够使用
	private String isValid;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo == null ? null : cardNo.trim();
	}

	public String getCardPasswd() {
		return cardPasswd;
	}

	public void setCardPasswd(String cardPasswd) {
		this.cardPasswd = cardPasswd == null ? null : cardPasswd.trim();
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
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

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType == null ? null : serviceType.trim();
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction == null ? null : introduction.trim();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	public Long getAddTime() {
		return addTime;
	}

	public void setAddTime(Long addTime) {
		this.addTime = addTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Short getCouponType() {
		return couponType;
	}

	public void setCouponType(Short couponType) {
		this.couponType = couponType;
	}

	public BigDecimal getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}

	public Short getRangMonth() {
		return rangMonth;
	}

	public void setRangMonth(Short rangMonth) {
		this.rangMonth = rangMonth;
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

	public int getCouponsTypeId() {
		return couponsTypeId;
	}

	public void setCouponsTypeId(int couponsTypeId) {
		this.couponsTypeId = couponsTypeId;
	}

	public List<Integer> getSendCouponsCondtion() {
		return sendCouponsCondtion;
	}

	public void setSendCouponsCondtion(List<Integer> sendCouponsCondtion) {
		this.sendCouponsCondtion = sendCouponsCondtion;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

}