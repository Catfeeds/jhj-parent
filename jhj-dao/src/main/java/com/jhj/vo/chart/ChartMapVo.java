package com.jhj.vo.chart;

import java.math.BigDecimal;
import java.util.List;


public class ChartMapVo {
	
	
	private String name;
	
	private String series;
	
	private Integer total;
/*	//当月登陆人数总计
	private Integer logintal;
	//截至当前登陆的总人数
	private Integer allLoginTal;*/
	
	private BigDecimal serviceTypeMoney;
	
	private BigDecimal revenue; 
	
	private Long orderId;
	
	private Long userId;

	private List<Long> userIds;
	
	private BigDecimal totalMoney;	//营业额
	
	private String orderFrom;
	
	private String orderOpFrom;
	
	private Short serviceType;
	
	private Short orderStatus;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public List<Long> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	
	public BigDecimal getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}

	public BigDecimal getRevenue() {
		return revenue;
	}

	public void setRevenue(BigDecimal revenue) {
		this.revenue = revenue;
	}


	public BigDecimal getServiceTypeMoney() {
		return serviceTypeMoney;
	}
	public void setServiceTypeMoney(BigDecimal serviceTypeMoney) {
		this.serviceTypeMoney = serviceTypeMoney;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
/*	public Integer getLogintal() {
		return logintal;
	}
	public void setLogintal(Integer logintal) {
		this.logintal = logintal;
	}
	public Integer getAllLoginTal() {
		return allLoginTal;
	}
	public void setAllLoginTal(Integer allLoginTal) {
		this.allLoginTal = allLoginTal;
	}
	*/
	public String getOrderFrom() {
		return orderFrom;
	}
	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}
	public String getOrderOpFrom() {
		return orderOpFrom;
	}
	public void setOrderOpFrom(String orderOpFrom) {
		this.orderOpFrom = orderOpFrom;
	}
	public Short getServiceType() {
		return serviceType;
	}
	public void setServiceType(Short serviceType) {
		this.serviceType = serviceType;
	}
	public Short getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Short orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	
}
