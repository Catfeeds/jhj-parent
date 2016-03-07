package com.jhj.oa.vo;

import java.util.List;

import com.jhj.po.model.bs.GiftCoupons;
import com.jhj.po.model.bs.Gifts;


public class GiftsFormVo extends Gifts{
	
	List<GiftCoupons>	giftCoupons;
	
	Long couponId;
	
	

	public List<GiftCoupons> getGiftCoupons() {
		return giftCoupons;
	}

	public void setGiftCoupons(List<GiftCoupons> giftCoupons) {
		this.giftCoupons = giftCoupons;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}
	
	
	
	

}
