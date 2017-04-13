package com.jhj.po.model.period;

import java.math.BigDecimal;
import java.util.Date;

public class PeriodOrderAddons {
    private Integer id;

    private Integer userId;

    private Integer periodOrderId;

    private String periodOrderNo;

    private Integer serviceTypeId;

    private Integer serviceAddonId;

    private BigDecimal price;

    private BigDecimal vipPrice;

    private Integer num;

    private Date addTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPeriodOrderId() {
        return periodOrderId;
    }

    public void setPeriodOrderId(Integer periodOrderId) {
        this.periodOrderId = periodOrderId;
    }

    public String getPeriodOrderNo() {
        return periodOrderNo;
    }

    public void setPeriodOrderNo(String periodOrderNo) {
        this.periodOrderNo = periodOrderNo == null ? null : periodOrderNo.trim();
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Integer getServiceAddonId() {
        return serviceAddonId;
    }

    public void setServiceAddonId(Integer serviceAddonId) {
        this.serviceAddonId = serviceAddonId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(BigDecimal vipPrice) {
        this.vipPrice = vipPrice;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}