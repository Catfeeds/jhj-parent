package com.jhj.service.users;

import java.util.List;

import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartSearchVo;


public interface UserChartService {
	

	ChartDataVo statUserChartDatas(ChartSearchVo chartSearchVo, List<String> timeSeries);

	ChartDataVo statUserLiveChartDatas(ChartSearchVo chartSearchVo, List<String> timeSeries);
	
	int statTotalUser(ChartSearchVo chartSearchVo); 
	
}
