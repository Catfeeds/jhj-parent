package com.jhj.vo.period;

import com.jhj.po.model.period.PeriodOrderAddons;

public class PeriodOrderAddonsVo extends PeriodOrderAddons{

	private String serviceAddonsName;
	
	private String punit;

	public String getServiceAddonsName() {
		return serviceAddonsName;
	}

	public void setServiceAddonsName(String serviceAddonsName) {
		this.serviceAddonsName = serviceAddonsName;
	}

	public String getPunit() {
		return punit;
	}

	public void setPunit(String punit) {
		this.punit = punit;
	}
	
}
