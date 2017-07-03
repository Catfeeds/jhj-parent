package com.jhj.vo.chart;

import java.util.List;
import java.util.Map;

/**
 * @author hulj
 *
 */
public class ChartUserOrderVo {
	
	private String name;
	
	private String count;
	
	private List<ChartUserOrderNumVo> list;
	
	private List<String> bussineNameList;
	
	private List<String> bussineIdList;
	
	private List<Map<String,String>> dataList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List<ChartUserOrderNumVo> getList() {
		return list;
	}

	public void setList(List<ChartUserOrderNumVo> list) {
		this.list = list;
	}

	public List<String> getBussineNameList() {
		return bussineNameList;
	}

	public void setBussineNameList(List<String> bussineNameList) {
		this.bussineNameList = bussineNameList;
	}

	public List<String> getBussineIdList() {
		return bussineIdList;
	}

	public void setBussineIdList(List<String> bussineIdList) {
		this.bussineIdList = bussineIdList;
	}

	public List<Map<String, String>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Map<String, String>> dataList) {
		this.dataList = dataList;
	}
	
}
