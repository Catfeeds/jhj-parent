package com.jhj.vo.dict;

import java.util.List;


public class CouponSearchVo {

	private String cardNo;

	private String cardPasswd;
	
	private String  startDate;

	private String endDate;
	
	private Short coupontType;//0 = 兑换码优惠券 ；1 = 充值赠送优惠券 
	
	private List<Long> couponsIdList;
	
	public Short getCoupontType() {
		return coupontType;
	}

	public void setCoupontType(Short coupontType) {
		this.coupontType = coupontType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardPasswd() {
		return cardPasswd;
	}

	public void setCardPasswd(String cardPasswd) {
		this.cardPasswd = cardPasswd;
	}

	public List<Long> getCouponsIdList() {
		return couponsIdList;
	}

	public void setCouponsIdList(List<Long> couponsIdList) {
		this.couponsIdList = couponsIdList;
	}


}
