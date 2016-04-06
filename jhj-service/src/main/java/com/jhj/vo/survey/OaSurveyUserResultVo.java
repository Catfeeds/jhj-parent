package com.jhj.vo.survey;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.jhj.po.model.survey.SurveyUserRefRecommend;

/*
 * 用户调查结果展示
 */
public class OaSurveyUserResultVo extends SurveyUserRefRecommend{
	
	
	//<计费方式,<服务名称,该服务总价>>
	private Map<Short, Map<String, BigDecimal>> map;
	
	//按年收费的 服务总价
	private BigDecimal yearPrice;
	//按月收费的 服务 总价
	private BigDecimal monthPrice;
	//按次收费的 服务总价
	private BigDecimal timePrice;

	public Map<Short, Map<String, BigDecimal>> getMap() {
		return map;
	}

	public void setMap(Map<Short, Map<String, BigDecimal>> map) {
		this.map = map;
	}

	public BigDecimal getYearPrice() {
		return yearPrice;
	}

	public void setYearPrice(BigDecimal yearPrice) {
		this.yearPrice = yearPrice;
	}

	public BigDecimal getMonthPrice() {
		return monthPrice;
	}

	public void setMonthPrice(BigDecimal monthPrice) {
		this.monthPrice = monthPrice;
	}

	public BigDecimal getTimePrice() {
		return timePrice;
	}

	public void setTimePrice(BigDecimal timePrice) {
		this.timePrice = timePrice;
	}
}
