package com.jhj.po.model.dict;

import java.math.BigDecimal;

public class DictServiceTypes {
    private Long id;

    private String name;

    private String keyword;

    private String tips;

    private String subheadHuodong;

    private String subheadAm;

    private String serviceRelative;

    private String serviceFeature;

    private BigDecimal price;

    private BigDecimal disPrice;

    private String descUrl;

    private Long addTime;

    private Long updateTime;

    private Short enable;

    private Short degreeType;

    private Long toDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips == null ? null : tips.trim();
    }

    public String getSubheadHuodong() {
        return subheadHuodong;
    }

    public void setSubheadHuodong(String subheadHuodong) {
        this.subheadHuodong = subheadHuodong == null ? null : subheadHuodong.trim();
    }

    public String getSubheadAm() {
        return subheadAm;
    }

    public void setSubheadAm(String subheadAm) {
        this.subheadAm = subheadAm == null ? null : subheadAm.trim();
    }

    public String getServiceRelative() {
        return serviceRelative;
    }

    public void setServiceRelative(String serviceRelative) {
        this.serviceRelative = serviceRelative == null ? null : serviceRelative.trim();
    }

    public String getServiceFeature() {
        return serviceFeature;
    }

    public void setServiceFeature(String serviceFeature) {
        this.serviceFeature = serviceFeature == null ? null : serviceFeature.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDisPrice() {
        return disPrice;
    }

    public void setDisPrice(BigDecimal disPrice) {
        this.disPrice = disPrice;
    }

    public String getDescUrl() {
        return descUrl;
    }

    public void setDescUrl(String descUrl) {
        this.descUrl = descUrl == null ? null : descUrl.trim();
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

    public Short getEnable() {
        return enable;
    }

    public void setEnable(Short enable) {
        this.enable = enable;
    }

    public Short getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(Short degreeType) {
        this.degreeType = degreeType;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }
}