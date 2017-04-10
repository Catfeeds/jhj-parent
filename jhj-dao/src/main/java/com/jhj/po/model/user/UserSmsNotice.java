package com.jhj.po.model.user;

public class UserSmsNotice {
    private Long id;

    private Long userId;

    private String mobile;

    private Short userType;
    
    private Short lastMonth;

    private String smsTemplateId;

    private String remarks;

    private Short isSuceess;

    private String smsReturn;

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

    public Short getUserType() {
        return userType;
    }

    public void setUserType(Short userType) {
        this.userType = userType;
    }

    public String getSmsTemplateId() {
        return smsTemplateId;
    }

    public void setSmsTemplateId(String smsTemplateId) {
        this.smsTemplateId = smsTemplateId == null ? null : smsTemplateId.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public Short getIsSuceess() {
        return isSuceess;
    }

    public void setIsSuceess(Short isSuceess) {
        this.isSuceess = isSuceess;
    }

    public String getSmsReturn() {
        return smsReturn;
    }

    public void setSmsReturn(String smsReturn) {
        this.smsReturn = smsReturn == null ? null : smsReturn.trim();
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

	public Short getLastMonth() {
		return lastMonth;
	}

	public void setLastMonth(Short lastMonth) {
		this.lastMonth = lastMonth;
	}
}