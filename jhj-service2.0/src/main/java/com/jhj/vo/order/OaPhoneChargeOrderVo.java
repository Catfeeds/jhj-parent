package com.jhj.vo.order;

import java.math.BigDecimal;

import com.jhj.po.model.order.Orders;

/*
 * 运营平台--订单管理--话费充值订单
 */
public class OaPhoneChargeOrderVo extends Orders {
	
	//充值面值
	private BigDecimal orderMoney;
	
	//实际支付金额
	private BigDecimal realMoney;
	
	//优惠券面值 ： 未使用/xx元
	private BigDecimal couponValue;

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public BigDecimal getRealMoney() {
		return realMoney;
	}

	public void setRealMoney(BigDecimal realMoney) {
		this.realMoney = realMoney;
	}

	public BigDecimal getCouponValue() {
		return couponValue;
	}

	public void setCouponValue(BigDecimal couponValue) {
		this.couponValue = couponValue;
	}
	
	
}
