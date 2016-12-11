package com.jhj.po.model.order;

public class OrderRates {
    private Long id;

    private Long orderId;

    private String orderNo;

    private Long staffId;

    private Long userId;

    private String mobile;

    private int rateArrival;

    private int rateAttitude;

    private int rateSkill;

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

    public int getRateArrival() {
        return rateArrival;
    }

    public void setRateArrival(int rateArrival) {
        this.rateArrival = rateArrival;
    }

    public int getRateAttitude() {
        return rateAttitude;
    }

    public void setRateAttitude(int rateAttitude) {
        this.rateAttitude = rateAttitude;
    }

    public int getRateSkill() {
        return rateSkill;
    }

    public void setRateSkill(int rateSkill) {
        this.rateSkill = rateSkill;
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

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
}