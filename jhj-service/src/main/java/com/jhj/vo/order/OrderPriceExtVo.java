package com.jhj.vo.order;

import java.math.BigDecimal;

import com.jhj.po.model.order.Orders;


public class OrderPriceExtVo extends Orders {
	
	private String orderNoExt;
	
	private int orderPayStatus;
	
	private Short payType;
	
	private BigDecimal orderPay;
	
	private String remarks;

	public String getOrderNoExt() {
		return orderNoExt;
	}

	public void setOrderNoExt(String orderNoExt) {
		this.orderNoExt = orderNoExt;
	}

	public BigDecimal getOrderPay() {
		return orderPay;
	}

	public void setOrderPay(BigDecimal orderPay) {
		this.orderPay = orderPay;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getOrderPayStatus() {
		return orderPayStatus;
	}

	public void setOrderPayStatus(int orderPayStatus) {
		this.orderPayStatus = orderPayStatus;
	}

	public Short getPayType() {
		return payType;
	}

	public void setPayType(Short payType) {
		this.payType = payType;
	}
}
