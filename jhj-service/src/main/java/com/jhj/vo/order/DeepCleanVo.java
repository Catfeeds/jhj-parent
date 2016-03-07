package com.jhj.vo.order;

import java.math.BigDecimal;
import java.util.List;

import com.jhj.po.model.user.UserAddrs;

public class DeepCleanVo extends OrderViewVo {

	private List<OrderServiceAddonViewVo> list;
	
	private List<UserAddrs> userAddrsList;
	
	private Long couponId;
	
	private BigDecimal restMoney;
	
	private BigDecimal couponValue;
	

	public BigDecimal getCouponValue() {
		return couponValue;
	}

	public void setCouponValue(BigDecimal couponValue) {
		this.couponValue = couponValue;
	}

	public BigDecimal getRestMoney() {
		return restMoney;
	}

	public void setRestMoney(BigDecimal restMoney) {
		this.restMoney = restMoney;
	}

	public List<UserAddrs> getUserAddrsList() {
		return userAddrsList;
	}

	public void setUserAddrsList(List<UserAddrs> userAddrsList) {
		this.userAddrsList = userAddrsList;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}
    
	public List<OrderServiceAddonViewVo> getList() {
		return list;
	}

	public void setList(List<OrderServiceAddonViewVo> list) {
		this.list = list;
	}
}
