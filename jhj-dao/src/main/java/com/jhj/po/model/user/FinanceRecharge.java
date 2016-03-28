package com.jhj.po.model.user;

import java.math.BigDecimal;

public class FinanceRecharge {
    private Long id;

    private Long userId;

    private BigDecimal rechargeValue;

    private BigDecimal restMoneyBefore;

    private BigDecimal restMoneyAfter;

    private Long adminId;

    private String adminName;

    private String adminMobile;

    private String approveMobile;

    private String approveToken;

    private String remarks;

    private Long addTime;

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

    public BigDecimal getRechargeValue() {
        return rechargeValue;
    }

    public void setRechargeValue(BigDecimal rechargeValue) {
        this.rechargeValue = rechargeValue;
    }

    public BigDecimal getRestMoneyBefore() {
        return restMoneyBefore;
    }

    public void setRestMoneyBefore(BigDecimal restMoneyBefore) {
        this.restMoneyBefore = restMoneyBefore;
    }

    public BigDecimal getRestMoneyAfter() {
        return restMoneyAfter;
    }

    public void setRestMoneyAfter(BigDecimal restMoneyAfter) {
        this.restMoneyAfter = restMoneyAfter;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName == null ? null : adminName.trim();
    }

    public String getAdminMobile() {
        return adminMobile;
    }

    public void setAdminMobile(String adminMobile) {
        this.adminMobile = adminMobile == null ? null : adminMobile.trim();
    }

    public String getApproveMobile() {
        return approveMobile;
    }

    public void setApproveMobile(String approveMobile) {
        this.approveMobile = approveMobile == null ? null : approveMobile.trim();
    }

    public String getApproveToken() {
        return approveToken;
    }

    public void setApproveToken(String approveToken) {
        this.approveToken = approveToken == null ? null : approveToken.trim();
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