package com.jhj.service.chart;

import java.util.List;

import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartSearchVo;

public interface RestMoneyChartService {
	//充值卡销售
	ChartDataVo statChartDatas(ChartSearchVo chartSearchVo, List<String> timeSeries);
	
	ChartDataVo userRestMoneyDatas(ChartSearchVo chartSearchVo, List<String> timeSeries);
	
}
