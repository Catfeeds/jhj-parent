package com.jhj.vo.bs;

import java.math.BigDecimal;
import java.util.List;

import com.jhj.po.model.bs.Gifts;

public class GiftVo extends Gifts {

	private List<GiftCouponVo> giftCoupons;
	
	private BigDecimal totalGiftMoney;

	public List<GiftCouponVo> getGiftCoupons() {
		return giftCoupons;
	}

	public void setGiftCoupons(List<GiftCouponVo> giftCoupons) {
		this.giftCoupons = giftCoupons;
	}

	public BigDecimal getTotalGiftMoney() {
		return totalGiftMoney;
	}

	public void setTotalGiftMoney(BigDecimal totalGiftMoney) {
		this.totalGiftMoney = totalGiftMoney;
	}

}
