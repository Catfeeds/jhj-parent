package com.jhj.service.chart;

import java.util.List;
import java.util.Map;

import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartSearchVo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月31日上午10:37:35
 * 
 * 		市场订单图表 、订单收入 图表service
 *
 */
public interface OrderChartService {
	
	//市场订单图表
	ChartDataVo statChartDatas(ChartSearchVo chartSearchVo, List<String> timeSeries);

	Map<String,Integer> statTotalOrder(ChartSearchVo chartSearchVo);

	//订单收入图表
	ChartDataVo getOrderRevenue(ChartSearchVo chartSearchVo,List<String> timeSeries);
	
	//订单来源图表
	ChartDataVo getOrderSrc(ChartSearchVo chartSearchVo,List<String> timeSeries);
	
	//统计订单数量和订单金额
	ChartDataVo getOrderFromCount(ChartSearchVo chartSearchVo, List<String> timeSeries);
	
	//市场人员订单统计
	ChartDataVo getUserOrderCount(ChartSearchVo chartSearchVo, List<String> timeSeries);
}
