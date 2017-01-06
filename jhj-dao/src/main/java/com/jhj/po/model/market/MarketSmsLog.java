package com.jhj.po.model.market;

public class MarketSmsLog {
    private Integer id;

    private Integer marketSmsId;

    private Long userId;

    private String mobile;

    private String smsResult;

    private String smsMsg;

    private Long addTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMarketSmsId() {
        return marketSmsId;
    }

    public void setMarketSmsId(Integer marketSmsId) {
        this.marketSmsId = marketSmsId;
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

    public String getSmsResult() {
        return smsResult;
    }

    public void setSmsResult(String smsResult) {
        this.smsResult = smsResult == null ? null : smsResult.trim();
    }

    public String getSmsMsg() {
        return smsMsg;
    }

    public void setSmsMsg(String smsMsg) {
        this.smsMsg = smsMsg == null ? null : smsMsg.trim();
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }
}