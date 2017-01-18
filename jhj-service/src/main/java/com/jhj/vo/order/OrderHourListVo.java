package com.jhj.vo.order;

import java.math.BigDecimal;

import com.jhj.po.model.order.Orders;

/**
 *
 * @author :hulj
 * @Date : 2015年8月4日上午11:32:39
 * @Description: 钟点工--订单列表VO
 *
 */
/**
 *
 * @author :hulj
 * @Date : 2015年8月4日下午3:58:03
 * @Description: TODO
 *
 */
public class OrderHourListVo extends Orders {
	
	/*
	 * 订单列表展示字段，，orders表字段
	 */

	private String orderStatusName;		//订单状态 
	
	private String serviceTypeName;
	
	private String address;//地址 为   name  +  addr (user_addrs表）
	
	private String serviceDateStr;
	
	private BigDecimal orderPay;
	
	private BigDecimal orderOriginPay;
	
	private Long couponId;
	
	private BigDecimal couponValue;
	
	private String staffNames;
	
	private Short payType;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public String getOrderStatusName() {
		return orderStatusName;
	}

	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}

	public String getServiceDateStr() {
		return serviceDateStr;
	}

	public void setServiceDateStr(String serviceDateStr) {
		this.serviceDateStr = serviceDateStr;
	}

	public BigDecimal getOrderPay() {
		return orderPay;
	}

	public void setOrderPay(BigDecimal orderPay) {
		this.orderPay = orderPay;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public BigDecimal getCouponValue() {
		return couponValue;
	}

	public void setCouponValue(BigDecimal couponValue) {
		this.couponValue = couponValue;
	}

	public String getStaffNames() {
		return staffNames;
	}

	public void setStaffNames(String staffNames) {
		this.staffNames = staffNames;
	}

	public Short getPayType() {
		return payType;
	}

	public void setPayType(Short payType) {
		this.payType = payType;
	}

	public BigDecimal getOrderOriginPay() {
		return orderOriginPay;
	}

	public void setOrderOriginPay(BigDecimal orderOriginPay) {
		this.orderOriginPay = orderOriginPay;
	}
}
