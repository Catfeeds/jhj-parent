package com.jhj.po.model.dict;

import java.math.BigDecimal;

public class DictSeniorType {
    private Long id;

    private String name;

    private BigDecimal seniorPay;

    private Short validMonth;

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

    public BigDecimal getSeniorPay() {
        return seniorPay;
    }

    public void setSeniorPay(BigDecimal seniorPay) {
        this.seniorPay = seniorPay;
    }

	public Long getAddTime() {
		return addTime;
	}

	public void setAddTime(Long addTime) {
		this.addTime = addTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Short getValidMonth() {
		return validMonth;
	}

	public void setValidMonth(Short validMonth) {
		this.validMonth = validMonth;
	}
}