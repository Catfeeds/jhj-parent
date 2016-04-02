package com.jhj.vo;

import java.util.List;

import com.jhj.po.model.university.PartnerServiceType;

public class PartnerServiceTypeVo extends PartnerServiceType {

	private String priceAndUnit;
	
	private String buttonWord;

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
}