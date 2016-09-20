package com.jhj.po.model.bs;

import java.math.BigDecimal;

public class OrgStaffFinance {
    private Long id;

    private Long staffId;

    private String mobile;
    
    private BigDecimal totalIncoming;

    private BigDecimal totalCash;
    
    private BigDecimal totalDept;

    private BigDecimal restMoney;
    
    private Short isBlack;

    private Long addTime;

    private Long updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

	public BigDecimal getTotalIncoming() {
        return totalIncoming;
    }

    public void setTotalIncoming(BigDecimal totalIncoming) {
        this.totalIncoming = totalIncoming;
    }

    public BigDecimal getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(BigDecimal totalCash) {
        this.totalCash = totalCash;
    }

    public BigDecimal getTotalDept() {
		return totalDept;
	}

	public void setTotalDept(BigDecimal totalDept) {
		this.totalDept = totalDept;
	}

	public BigDecimal getRestMoney() {
        return restMoney;
    }

    public void setRestMoney(BigDecimal restMoney) {
        this.restMoney = restMoney;
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

	public Short getIsBlack() {
		return isBlack;
	}

	public void setIsBlack(Short isBlack) {
		this.isBlack = isBlack;
	}
}