package com.jhj.service.order;

import java.util.List;

import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartSearchVo;

/**
 * @description：
 * @author： kerryg
 * @date:2015年9月11日 
 */
public interface OrderAmTypeChartService {
	
	
	ChartDataVo statChartAmTypeDatas(ChartSearchVo chartSearchVo, List<String> timeSeries);

	ChartDataVo statServiceTypeDatas(ChartSearchVo chartSearchVo, List<String> timeSeries);

}
