package com.jhj.vo.chart;

import java.util.HashMap;
import java.util.List;

public class ChartUserOrderVo {
	
	private String series;
	
	private HashMap<String,HashMap<String,String>> data;
	
	private List<HashMap<String,HashMap<String,String>>> dataList;

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public HashMap<String, HashMap<String, String>> getData() {
		return data;
	}

	public void setData(HashMap<String, HashMap<String, String>> data) {
		this.data = data;
	}

	public List<HashMap<String, HashMap<String, String>>> getDataList() {
		return dataList;
	}

	public void setDataList(List<HashMap<String, HashMap<String, String>>> dataList) {
		this.dataList = dataList;
	}
	

	
}
