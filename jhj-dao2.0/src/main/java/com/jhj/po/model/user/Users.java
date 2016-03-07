package com.jhj.po.model.user;

import java.math.BigDecimal;

public class Users {
    private Long id;

    private String provinceName;

    private String mobile;

    private String thirdType;

    private String openid;

    private String name;
    
    private String remarks;

    private String sex;

    private String headImg;

    private BigDecimal restMoney;

    private Integer score;

    private Short userType;

    private Short addFrom;

    private Long addTime;

    private Long updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName == null ? null : provinceName.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }


    public String getThirdType() {
		return thirdType;
	}

	public void setThirdType(String thirdType) {
		this.thirdType = thirdType;
	}

	public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg == null ? null : headImg.trim();
    }

    public BigDecimal getRestMoney() {
        return restMoney;
    }

    public void setRestMoney(BigDecimal restMoney) {
        this.restMoney = restMoney;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Short getUserType() {
        return userType;
    }

    public void setUserType(Short userType) {
        this.userType = userType;
    }

    public Short getAddFrom() {
        return addFrom;
    }

    public void setAddFrom(Short addFrom) {
        this.addFrom = addFrom;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
    
}