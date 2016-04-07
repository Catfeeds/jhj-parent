package com.jhj.po.model.cooperate;

public class CooperativeBusiness {
    private Long id;

    private String businessName;

    private String appName;

    private String businessLoginName;

    private String businessPassWord;

    private Short enable;

    private Long roleId;

    private Long addTime;

    private Long updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName == null ? null : businessName.trim();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    public String getBusinessLoginName() {
        return businessLoginName;
    }

    public void setBusinessLoginName(String businessLoginName) {
        this.businessLoginName = businessLoginName == null ? null : businessLoginName.trim();
    }

    public String getBusinessPassWord() {
        return businessPassWord;
    }

    public void setBusinessPassWord(String businessPassWord) {
        this.businessPassWord = businessPassWord == null ? null : businessPassWord.trim();
    }

    public Short getEnable() {
        return enable;
    }

    public void setEnable(Short enable) {
        this.enable = enable;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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