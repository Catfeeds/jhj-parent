package com.jhj.po.model.dict;

import java.math.BigDecimal;

public class DictServiceAddons {
    private Long serviceAddonId;

    private Long serviceType;

    private String name;

    private String keyword;

    private String tips;

    private BigDecimal price;

    private BigDecimal disPrice;

    private String descUrl;

    private Long addTime;

    private Long updateTime;

    private Short enable;
    
    private String itemUnit;
    
    private Short defaultNum;
    
    
    
	public Short getDefaultNum() {
		return defaultNum;
	}

	public void setDefaultNum(Short defaultNum) {
		this.defaultNum = defaultNum;
	}

	public String getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	public Long getServiceAddonId() {
        return serviceAddonId;
    }

    public void setServiceAddonId(Long serviceAddonId) {
        this.serviceAddonId = serviceAddonId;
    }

    public Long getServiceType() {
        return serviceType;
    }

    public void setServiceType(Long serviceType) {
        this.serviceType = serviceType;
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
}