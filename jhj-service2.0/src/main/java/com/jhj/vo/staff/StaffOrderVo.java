package com.jhj.vo.staff;

import java.math.BigDecimal;

public class StaffOrderVo {
	
	private Long staffId;
	//总订单数
	private Long totalOrder;
	//在线小时数
	private Long totalOnline;
	//今日流水
	private BigDecimal totalOrderMoney;
	//今日收入
	private BigDecimal totalIncoming;
	//开工标志 0=收工 1=开工
	private Short isWork;
	public Long getStaffId() {
		return staffId;
	}
	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
	public Long getTotalOrder() {
		return totalOrder;
	}
	public void setTotalOrder(Long totalOrder) {
		this.totalOrder = totalOrder;
	}
	public Long getTotalOnline() {
		return totalOnline;
	}
	public void setTotalOnline(Long totalOnline) {
		this.totalOnline = totalOnline;
	}
	public BigDecimal getTotalOrderMoney() {
		return totalOrderMoney;
	}
	public void setTotalOrderMoney(BigDecimal totalOrderMoney) {
		this.totalOrderMoney = totalOrderMoney;
	}
	public BigDecimal getTotalIncoming() {
		return totalIncoming;
	}
	public void setTotalIncoming(BigDecimal totalIncoming) {
		this.totalIncoming = totalIncoming;
	}
	public Short getIsWork() {
		return isWork;
	}
	public void setIsWork(Short isWork) {
		this.isWork = isWork;
	}
	

	
}
