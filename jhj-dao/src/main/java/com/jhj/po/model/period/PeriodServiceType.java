package com.jhj.po.model.period;

import java.math.BigDecimal;

public class PeriodServiceType {
    private Integer id;

    private String name;

    private Integer serviceTypeId;

    private Integer serviceAddonId;

    private BigDecimal price;

    private BigDecimal vipPrice;

    private Integer num;

    private String punit;

    private String total;

    private Integer enbale;

    private String remarks;

    private Long addTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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

    public String getPunit() {
        return punit;
    }

    public void setPunit(String punit) {
        this.punit = punit == null ? null : punit.trim();
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total == null ? null : total.trim();
    }

    public Integer getEnbale() {
        return enbale;
    }

    public void setEnbale(Integer enbale) {
        this.enbale = enbale;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }
}