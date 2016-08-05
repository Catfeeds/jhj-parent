package com.jhj.po.model.dict;

import java.math.BigDecimal;

public class DictCardType {
    private Long id;

    private String name;

    private BigDecimal cardValue;

	private BigDecimal cardPay;
    
    //新增字段
    private BigDecimal sendMoney;
    
    private Long giftId;

    private String description;

    private Long addTime;

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

    public BigDecimal getCardValue() {
        return cardValue;
    }

    public void setCardValue(BigDecimal cardValue) {
        this.cardValue = cardValue;
    }

    public BigDecimal getCardPay() {
        return cardPay;
    }

    public void setCardPay(BigDecimal cardPay) {
        this.cardPay = cardPay;
    }
    
    public BigDecimal getSendMoney() {
		return sendMoney;
	}

	public void setSendMoney(BigDecimal sendMoney) {
		this.sendMoney = sendMoney;
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

	public Long getAddTime() {
		return addTime;
	}

	public void setAddTime(Long addTime) {
		this.addTime = addTime;
	}

	public Long getGiftId() {
		return giftId;
	}

	public void setGiftId(Long giftId) {
		this.giftId = giftId;
	}
}