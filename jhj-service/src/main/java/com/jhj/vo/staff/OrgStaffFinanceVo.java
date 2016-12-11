package com.jhj.vo.staff;

import java.math.BigDecimal;

import com.jhj.po.model.bs.OrgStaffFinance;

public class OrgStaffFinanceVo extends OrgStaffFinance{
	
	private String name;
	
	private BigDecimal totalCashValid;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getTotalCashValid() {
		return totalCashValid;
	}
	public void setTotalCashValid(BigDecimal totalCashValid) {
		this.totalCashValid = totalCashValid;
	}
}
