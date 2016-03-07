package com.jhj.po.model.user;

public class UserSmsToken {
    private Long id;

    private Long userId;

    private String mobile;

    private Short loginFrom;

    private String smsToken;

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
    
    public Short getLoginFrom() {
		return loginFrom;
	}

	public void setLoginFrom(Short loginFrom) {
		this.loginFrom = loginFrom;
	}

	public String getSmsToken() {
        return smsToken;
    }

    public void setSmsToken(String smsToken) {
        this.smsToken = smsToken == null ? null : smsToken.trim();
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
}