package com.jhj.vo.dict;

import com.jhj.po.model.dict.DictCardType;
import com.jhj.vo.bs.GiftVo;

public class DictCardTypeVo extends DictCardType{

	private String giftName;
	
	private GiftVo gift;

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public GiftVo getGift() {
		return gift;
	}

	public void setGift(GiftVo gift) {
		this.gift = gift;
	}

}
