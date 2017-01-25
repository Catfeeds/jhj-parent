package com.jhj.po.model.order;

import java.math.BigDecimal;

public class OrderPrices {
    private Long id;

    private Long userId;

    private String mobile;

    private Long orderId;

    private String orderNo;

    private Short payType;

    private Long couponId;
    
    private BigDecimal orderOriginPrice;
    
    private BigDecimal orderPrimePrice;

    private BigDecimal orderMoney;

    private BigDecimal orderPay;

    private BigDecimal orderPayBack;

    private BigDecimal orderPayBackFee;

    private Long addTime;

    private Long updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }



    public Short getPayType() {
		return payType;
	}

	public void setPayType(Short payType) {
		this.payType = payType;
	}

	public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
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

    public BigDecimal getOrderPayBack() {
        return orderPayBack;
    }

    public void setOrderPayBack(BigDecimal orderPayBack) {
        this.orderPayBack = orderPayBack;
    }

    public BigDecimal getOrderPayBackFee() {
        return orderPayBackFee;
    }

    public void setOrderPayBackFee(BigDecimal orderPayBackFee) {
        this.orderPayBackFee = orderPayBackFee;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

	public BigDecimal getOrderOriginPrice() {
		return orderOriginPrice;
	}

	public void setOrderOriginPrice(BigDecimal orderOriginPrice) {
		this.orderOriginPrice = orderOriginPrice;
	}

	public BigDecimal getOrderPrimePrice() {
		return orderPrimePrice;
	}

	public void setOrderPrimePrice(BigDecimal orderPrimePrice) {
		this.orderPrimePrice = orderPrimePrice;
	}
}