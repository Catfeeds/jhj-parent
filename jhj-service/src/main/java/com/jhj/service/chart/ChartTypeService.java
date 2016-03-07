package com.jhj.service.chart;

import java.util.List;

import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartSearchVo;

/*
 * 市场品类图表、品类收入图表 service
 */
public interface ChartTypeService {
	
	//市场品类图表
	ChartDataVo getChartTypeData(ChartSearchVo chartSearchVo,List<String> timeSeries);

	//品类收入图表
	ChartDataVo chartTypeRevenueData(ChartSearchVo chartSearchVo,List<String> timeSeries);
}
