package com.jhj.vo.order.year;

import java.math.BigDecimal;

import com.jhj.po.model.order.OrderCustomizationYear;

/**
 *
 * @author :hulj
 * @Date : 2016年4月2日上午11:15:52
 * @Description: 
 *
 */
public class OrderCusYearVo extends OrderCustomizationYear {
	
	private String  userMobile;
	private String  serviceName;
	
	private BigDecimal price;
	private BigDecimal monthPrice;
	private BigDecimal yearPrice;
	
	private Long serviceTimeYear;
	
	private String addTimeStr;
	
	
	public String getAddTimeStr() {
		return addTimeStr;
	}
	public void setAddTimeStr(String addTimeStr) {
		this.addTimeStr = addTimeStr;
	}
	public Long getServiceTimeYear() {
		return serviceTimeYear;
	}
	public void setServiceTimeYear(Long serviceTimeYear) {
		this.serviceTimeYear = serviceTimeYear;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getUserMobile() {
		return userMobile;
	}
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public BigDecimal getMonthPrice() {
		return monthPrice;
	}
	public void setMonthPrice(BigDecimal monthPrice) {
		this.monthPrice = monthPrice;
	}
	public BigDecimal getYearPrice() {
		return yearPrice;
	}
	public void setYearPrice(BigDecimal yearPrice) {
		this.yearPrice = yearPrice;
	}
}
