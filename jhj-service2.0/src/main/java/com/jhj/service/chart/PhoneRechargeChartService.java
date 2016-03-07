package com.jhj.service.chart;

import java.util.List;

import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartSearchVo;

/*
 * 话费充值订单 统计相关 service
 */
public interface PhoneRechargeChartService {

	//话费订单图表
	ChartDataVo getPhoneOrder(ChartSearchVo chartSearchVo, List<String> timeSeries);
	
	//话费订单收入图表
	ChartDataVo getPhoneRevenue(ChartSearchVo chartSearchVo, List<String> timeSeries);
	
	
}
