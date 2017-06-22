package com.jhj.vo.chart;

import java.util.List;

public class ChartUserOrderVo {
	
	private String time;
	
	private List<ChartUserOrderNumVo> data;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<ChartUserOrderNumVo> getData() {
		return data;
	}

	public void setData(List<ChartUserOrderNumVo> data) {
		this.data = data;
	}

}
