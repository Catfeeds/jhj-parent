package com.jhj.vo.survey;

import java.math.BigDecimal;

/*
 * 运营平台--
 * 			用户调查结果展示,
 * 			
 * 			服务内容VO
 */
public class OaSurveyContentVo {
	
	private String contentName;
	
	private BigDecimal contentSumPrice;
	
	private String measureName;

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public BigDecimal getContentSumPrice() {
		return contentSumPrice;
	}

	public void setContentSumPrice(BigDecimal contentSumPrice) {
		this.contentSumPrice = contentSumPrice;
	}

	public String getMeasureName() {
		return measureName;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}	
	
}
