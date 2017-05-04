package com.jhj.vo.period;

import com.jhj.po.model.period.PeriodOrder;

public class PeriodOrderVo extends PeriodOrder{
	
	private String addrName;
	
	private int totalOrder;

	public String getAddrName() {
		return addrName;
	}

	public void setAddrName(String addrName) {
		this.addrName = addrName;
	}

	public int getTotalOrder() {
		return totalOrder;
	}

	public void setTotalOrder(int totalOrder) {
		this.totalOrder = totalOrder;
	}

}
