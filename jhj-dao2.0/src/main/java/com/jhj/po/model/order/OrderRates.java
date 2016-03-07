package com.jhj.po.model.order;

public class OrderRates {
    private Long id;

    private Long orderId;

    private String orderNo;

    private Long amId;

    private Long userId;

    private String mobile;

    private Short rateType;

    private Short rateValue;

    private String rateContent;

    private Long addTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getAmId() {
        return amId;
    }

    public void setAmId(Long amId) {
        this.amId = amId;
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

    public Short getRateType() {
        return rateType;
    }

    public void setRateType(Short rateType) {
        this.rateType = rateType;
    }

    public Short getRateValue() {
        return rateValue;
    }

    public void setRateValue(Short rateValue) {
        this.rateValue = rateValue;
    }

    public String getRateContent() {
        return rateContent;
    }

    public void setRateContent(String rateContent) {
        this.rateContent = rateContent == null ? null : rateContent.trim();
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }
}