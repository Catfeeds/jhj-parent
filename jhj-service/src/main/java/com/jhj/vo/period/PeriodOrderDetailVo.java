package com.jhj.vo.period;

import java.util.List;

public class PeriodOrderDetailVo extends PeriodOrderVo {
	
	private List<PeriodOrderAddonsVo> periodOrderAddons;

	public List<PeriodOrderAddonsVo> getPeriodOrderAddons() {
		return periodOrderAddons;
	}

	public void setPeriodOrderAddons(List<PeriodOrderAddonsVo> periodOrderAddons) {
		this.periodOrderAddons = periodOrderAddons;
	}
	


}
