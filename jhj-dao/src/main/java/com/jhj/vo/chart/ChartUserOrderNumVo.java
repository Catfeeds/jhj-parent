package com.jhj.vo.chart;

import java.util.List;
import java.util.Map;

public class ChartUserOrderNumVo {
	
	private String name;
	
	private List<Map<String,String>> orderFromNum;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Map<String, String>> getOrderFromNum() {
		return orderFromNum;
	}

	public void setOrderFromNum(List<Map<String, String>> orderFromNum) {
		this.orderFromNum = orderFromNum;
	}
	
}
