package com.jhj.po.model.order;

import java.math.BigDecimal;

public class OrderDispatchPrices {
    private Long id;

    private Long userId;

    private String mobile;

    private Short isVip;

    private Long orderId;

    private String orderNo;

    private Short orderType;

    private Long serviceTypeId;

    private Short orderStatus;

    private Long addrId;

    private String addr;

    private Long orderTime;

    private Long serviceDate;

    private Double serviceHours;

    private Short staffNum;

    private Long orgId;

    private Long parentId;

    private Long staffId;

    private String staffName;

    private String staffMobile;

    private Short dispatchStatus;

    private Integer userAddrDistance;

    private Short payType;

    private BigDecimal orderMoney;

    private BigDecimal orderPay;

    private BigDecimal orderPayIncoming;

    private Long couponId;

    private BigDecimal orderPayCoupon;

    private BigDecimal orderPayCouponIncoming;

    private BigDecimal orderPayExtDiff;

    private BigDecimal orderPayExtDiffIncoming;

    private BigDecimal orderPayExtOverwork;

    private BigDecimal orderPayExtOverworkIncoming;

    private BigDecimal incomingPercent;

    private BigDecimal totalOrderIncoming;

    private BigDecimal totalOrderDept;

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

    public Short getIsVip() {
        return isVip;
    }

    public void setIsVip(Short isVip) {
        this.isVip = isVip;
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

    public Short getOrderType() {
        return orderType;
    }

    public void setOrderType(Short orderType) {
        this.orderType = orderType;
    }

    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Short getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Short orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getAddrId() {
        return addrId;
    }

    public void setAddrId(Long addrId) {
        this.addrId = addrId;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr == null ? null : addr.trim();
    }

    public Long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Long orderTime) {
        this.orderTime = orderTime;
    }

    public Long getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Long serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Double getServiceHours() {
        return serviceHours;
    }

    public void setServiceHours(Double serviceHours) {
        this.serviceHours = serviceHours;
    }

    public Short getStaffNum() {
        return staffNum;
    }

    public void setStaffNum(Short staffNum) {
        this.staffNum = staffNum;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName == null ? null : staffName.trim();
    }

    public String getStaffMobile() {
        return staffMobile;
    }

    public void setStaffMobile(String staffMobile) {
        this.staffMobile = staffMobile == null ? null : staffMobile.trim();
    }

    public Short getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(Short dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
    }

    public Integer getUserAddrDistance() {
        return userAddrDistance;
    }

    public void setUserAddrDistance(Integer userAddrDistance) {
        this.userAddrDistance = userAddrDistance;
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

    public BigDecimal getOrderPayIncoming() {
        return orderPayIncoming;
    }

    public void setOrderPayIncoming(BigDecimal orderPayIncoming) {
        this.orderPayIncoming = orderPayIncoming;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public BigDecimal getOrderPayCoupon() {
        return orderPayCoupon;
    }

    public void setOrderPayCoupon(BigDecimal orderPayCoupon) {
        this.orderPayCoupon = orderPayCoupon;
    }

    public BigDecimal getOrderPayCouponIncoming() {
        return orderPayCouponIncoming;
    }

    public void setOrderPayCouponIncoming(BigDecimal orderPayCouponIncoming) {
        this.orderPayCouponIncoming = orderPayCouponIncoming;
    }

    public BigDecimal getOrderPayExtDiff() {
        return orderPayExtDiff;
    }

    public void setOrderPayExtDiff(BigDecimal orderPayExtDiff) {
        this.orderPayExtDiff = orderPayExtDiff;
    }

    public BigDecimal getOrderPayExtDiffIncoming() {
        return orderPayExtDiffIncoming;
    }

    public void setOrderPayExtDiffIncoming(BigDecimal orderPayExtDiffIncoming) {
        this.orderPayExtDiffIncoming = orderPayExtDiffIncoming;
    }

    public BigDecimal getOrderPayExtOverwork() {
        return orderPayExtOverwork;
    }

    public void setOrderPayExtOverwork(BigDecimal orderPayExtOverwork) {
        this.orderPayExtOverwork = orderPayExtOverwork;
    }

    public BigDecimal getOrderPayExtOverworkIncoming() {
        return orderPayExtOverworkIncoming;
    }

    public void setOrderPayExtOverworkIncoming(BigDecimal orderPayExtOverworkIncoming) {
        this.orderPayExtOverworkIncoming = orderPayExtOverworkIncoming;
    }

    public BigDecimal getIncomingPercent() {
        return incomingPercent;
    }

    public void setIncomingPercent(BigDecimal incomingPercent) {
        this.incomingPercent = incomingPercent;
    }

    public BigDecimal getTotalOrderIncoming() {
        return totalOrderIncoming;
    }

    public void setTotalOrderIncoming(BigDecimal totalOrderIncoming) {
        this.totalOrderIncoming = totalOrderIncoming;
    }

    public BigDecimal getTotalOrderDept() {
        return totalOrderDept;
    }

    public void setTotalOrderDept(BigDecimal totalOrderDept) {
        this.totalOrderDept = totalOrderDept;
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
}