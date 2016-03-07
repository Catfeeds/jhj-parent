package com.jhj.vo.user;

import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.Users;

public class UserAppVo extends Users {
	
	private boolean hasUserAddr;
	
	private UserAddrs defaultUserAddr;
	
	private int totalCoupon;
	
	private String totalCouponSpan;
	
	private String amServiceTimeSpan;
	
	private String restMoneySpan;
	
	//登录时，获得当前用户的助理信息
	private Long amId;
	
	private String amMobile;
	
	
	public Long getAmId() {
		return amId;
	}

	public void setAmId(Long amId) {
		this.amId = amId;
	}

	public String getAmMobile() {
		return amMobile;
	}

	public void setAmMobile(String amMobile) {
		this.amMobile = amMobile;
	}

	public boolean isHasUserAddr() {
		return hasUserAddr;
	}

	public void setHasUserAddr(boolean hasUserAddr) {
		this.hasUserAddr = hasUserAddr;
	}

	public int getTotalCoupon() {
		return totalCoupon;
	}

	public void setTotalCoupon(int totalCoupon) {
		this.totalCoupon = totalCoupon;
	}

	public String getTotalCouponSpan() {
		return totalCouponSpan;
	}

	public void setTotalCouponSpan(String totalCouponSpan) {
		this.totalCouponSpan = totalCouponSpan;
	}

	public String getAmServiceTimeSpan() {
		return amServiceTimeSpan;
	}

	public void setAmServiceTimeSpan(String amServiceTimeSpan) {
		this.amServiceTimeSpan = amServiceTimeSpan;
	}

	public String getRestMoneySpan() {
		return restMoneySpan;
	}

	public void setRestMoneySpan(String restMoneySpan) {
		this.restMoneySpan = restMoneySpan;
	}

	public UserAddrs getDefaultUserAddr() {
		return defaultUserAddr;
	}

	public void setDefaultUserAddr(UserAddrs defaultUserAddr) {
		this.defaultUserAddr = defaultUserAddr;
	}
}
