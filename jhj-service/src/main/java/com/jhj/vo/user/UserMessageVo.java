package com.jhj.vo.user;

import java.util.List;

import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.user.Users;

/**
 *
 * @author :hulj
 * @Date : 2015年8月10日上午10:16:41
 * @Description: 
 *		用户版--我的--基本信息 展示页 VO
 */
public class UserMessageVo extends Users {
	
	private int couponAmount;	//优惠券数量
	
	private List<DictCoupons> couponList;	//优惠券列表

	
	public int getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(int couponAmount) {
		this.couponAmount = couponAmount;
	}

	public List<DictCoupons> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<DictCoupons> couponList) {
		this.couponList = couponList;
	}
	
	
}
