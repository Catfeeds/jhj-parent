package com.jhj.po.model.order;

import java.math.BigDecimal;

public class OrderCards {
    private Long id;

    private Long userId;

    private String mobile;

    private String cardOrderNo;

    private Long cardType;

    private BigDecimal cardMoney;

    private BigDecimal cardPay;

    private Short payType;

    private Short orderStatus;

    private Long addTime;

    private Long updateTime;
    
    //办卡推荐人
    private String referee;
    
    private Integer parentId;
    
    private Integer orgId;

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

    public String getCardOrderNo() {
        return cardOrderNo;
    }

    public void setCardOrderNo(String cardOrderNo) {
        this.cardOrderNo = cardOrderNo == null ? null : cardOrderNo.trim();
    }

    public Long getCardType() {
        return cardType;
    }

    public void setCardType(Long cardType) {
        this.cardType = cardType;
    }

    public BigDecimal getCardMoney() {
        return cardMoney;
    }

    public void setCardMoney(BigDecimal cardMoney) {
        this.cardMoney = cardMoney;
    }

    public BigDecimal getCardPay() {
        return cardPay;
    }

    public void setCardPay(BigDecimal cardPay) {
        this.cardPay = cardPay;
    }

    public Short getPayType() {
        return payType;
    }

    public void setPayType(Short payType) {
        this.payType = payType;
    }

    public Short getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Short orderStatus) {
        this.orderStatus = orderStatus;
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

	public String getReferee() {
		return referee;
	}

	public void setReferee(String referee) {
		this.referee = referee;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
    
}