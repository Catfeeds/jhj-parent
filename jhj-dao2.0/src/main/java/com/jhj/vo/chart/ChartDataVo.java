package com.jhj.vo.chart;

import java.util.HashMap;
import java.util.List;

public class ChartDataVo {
	
	
	private String legend;
	
	private String xAxis;
	
	private String series;
	
	private List<HashMap<String, String>> tableDatas;


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




	
}
