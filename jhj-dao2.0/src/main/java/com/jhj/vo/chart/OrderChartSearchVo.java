package com.jhj.vo.chart;

/**
 *
 * @author :hulj
 * @Date : 2015年9月4日上午11:42:10
 * @Description: 
 *		
 *		市场订单图表  searchVo
 *
 */	
public class OrderChartSearchVo {
	
	
	/*
	 * 搜索条件
	 */
	private int timeDuration;	//时间周期
	private String startTime;	//开始时间
	private String endTime;		//结束时间
	
	/*
	 * 图例
	 */
	
	private Short orderFrom;	//信息 来源
	
	
	public Short getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(Short orderFrom) {
		this.orderFrom = orderFrom;
	}

	public int getTimeDuration() {
		return timeDuration;
	}

	public void setTimeDuration(int timeDuration) {
		this.timeDuration = timeDuration;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	
	
	
}
