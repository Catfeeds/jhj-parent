package com.jhj.vo;

import com.jhj.po.model.university.PartnerServiceType;

public class PartnerServiceTypeVo extends PartnerServiceType {

	private String priceAndUnit;
	
	private String buttonWord;
	
	private String priceStr;
	
	private String monthPrice;
	
	private String yearPrice;
	
	private String yearTimes;
	

	public String getPriceAndUnit() {
		return priceAndUnit;
	}

	public void setPriceAndUnit(String priceAndUnit) {
		this.priceAndUnit = priceAndUnit;
	}

	public String getButtonWord() {
		return buttonWord;
	}

	public void setButtonWord(String buttonWord) {
		this.buttonWord = buttonWord;
	}

	public String getMonthPrice() {
		return monthPrice;
	}

	public void setMonthPrice(String monthPrice) {
		this.monthPrice = monthPrice;
	}

	public String getYearPrice() {
		return yearPrice;
	}

	public void setYearPrice(String yearPrice) {
		this.yearPrice = yearPrice;
	}

	public String getYearTimes() {
		return yearTimes;
	}

	public void setYearTimes(String yearTimes) {
		this.yearTimes = yearTimes;
	}

	public String getPriceStr() {
		return priceStr;
	}

	public void setPriceStr(String priceStr) {
		this.priceStr = priceStr;
	}
}