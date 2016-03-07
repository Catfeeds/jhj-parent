package com.jhj.service.chart;

import java.util.List;

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

	int statTotalOrder(ChartSearchVo chartSearchVo);

	//订单收入图表
	
	ChartDataVo getOrderRevenue(ChartSearchVo chartSearchVo,List<String> timeSeries);
	
	
}
