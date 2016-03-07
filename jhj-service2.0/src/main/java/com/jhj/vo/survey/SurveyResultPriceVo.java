package com.jhj.vo.survey;

import java.math.BigDecimal;


/*
 *  提交调查结果后，返回对应的价格详情
 */
public class SurveyResultPriceVo {
	
	private Short surveyPayType;		//支付方式
	
	private BigDecimal sumPrice;		//总价
		
	private BigDecimal discountPrice;	//优惠后的总价
	
	private BigDecimal priceByMonth;	//按月支付价格

	
	public Short getSurveyPayType() {
		return surveyPayType;
	}

	public void setSurveyPayType(Short surveyPayType) {
		this.surveyPayType = surveyPayType;
	}

	public BigDecimal getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(BigDecimal sumPrice) {
		this.sumPrice = sumPrice;
	}

	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}

	public BigDecimal getPriceByMonth() {
		return priceByMonth;
	}

	public void setPriceByMonth(BigDecimal priceByMonth) {
		this.priceByMonth = priceByMonth;
	}
	
}
