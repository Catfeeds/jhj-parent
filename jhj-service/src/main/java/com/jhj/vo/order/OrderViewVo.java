package com.jhj.vo.order;

import java.math.BigDecimal;

import com.jhj.po.model.order.Orders;


public class OrderViewVo extends Orders {
	
	//城市名称
	private String cityName;
	
	//服务类型名称
	private String serviceTypeName;
	
	//是否一单多人
	private Short isMulti;
	
	//订单类型名称
	private String orderTypeName;
	
	//用户称呼
	private String name;

	//用户服务地址
	private String serviceAddr;

	private Short payType;

	private BigDecimal orderMoney;

	private BigDecimal orderPay;
	
	private String orderStatusName;
	
	private String  serviceDateStr;
	
	private String introduction;
	
	
	
	
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getServiceDateStr() {
		return serviceDateStr;
	}

	public void setServiceDateStr(String serviceDateStr) {
		this.serviceDateStr = serviceDateStr;
	}

	public Short getPayType() {
		return payType;
	}

	public void setPayType(Short payType) {
		this.payType = payType;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public BigDecimal getOrderPay() {
		return orderPay;
	}

	public void setOrderPay(BigDecimal orderPay) {
		this.orderPay = orderPay;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public String getServiceAddr() {
		return serviceAddr;
	}

	public void setServiceAddr(String serviceAddr) {
		this.serviceAddr = serviceAddr;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getOrderStatusName() {
		return orderStatusName;
	}

	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}

	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}

	public Short getIsMulti() {
		return isMulti;
	}

	public void setIsMulti(Short isMulti) {
		this.isMulti = isMulti;
	}
	
}
