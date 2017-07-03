package com.jhj.vo.chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartDataVo {
	
	private String legend;
	
	private String xAxis;
	
	private String series;
	
	private List<HashMap<String, String>> tableDatas;
	
	private Map<String,String> tableMap;
	
	private List<String> businessList;
	
	public String getLegend() {
		return legend;
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}

	public String getxAxis() {
		return xAxis;
	}

	public void setxAxis(String xAxis) {
		this.xAxis = xAxis;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public List<HashMap<String, String>> getTableDatas() {
		return tableDatas;
	}

	public void setTableDatas(List<HashMap<String, String>> tableDatas) {
		this.tableDatas = tableDatas;
	}

	public Map<String, String> getTableMap() {
		return tableMap;
	}

	public void setTableMap(Map<String, String> tableMap) {
		this.tableMap = tableMap;
	}

	public List<String> getBusinessList() {
		return businessList;
	}

	public void setBusinessList(List<String> businessList) {
		this.businessList = businessList;
	}

}
