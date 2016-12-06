package com.jhj.service.impl.chart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jhj.po.dao.order.OrderPricesMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.service.chart.OrderChartService;
import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartMapVo;
import com.jhj.vo.chart.ChartSearchVo;
import com.meijia.utils.ChartUtil;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.MathDoubleUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年8月31日上午10:38:25
 * @Description: 
 *		运营平台--
 *				市场统计图表
 */
@Service
public class OrderChartServiceImpl implements OrderChartService {

	@Autowired
	private OrdersMapper orderMapper;
	
	@Autowired
	private OrderPricesMapper orderPriceMapper;
	/*
	 * 市场订单图表 service

	 */
	@Override
	public ChartDataVo statChartDatas(ChartSearchVo chartSearchVo, List<String> timeSeries) {
		ChartDataVo chartDataVo = new ChartDataVo();
		
		if (timeSeries.isEmpty()) return chartDataVo;

		String statType = chartSearchVo.getStatType();
		//确认legend;
		List<String> legendAll = new ArrayList<String>();
		legendAll.add("新增订单小计");
		legendAll.add("增长率");
		legendAll.add("微网站来源");
		legendAll.add("App来源");
		legendAll.add("退单数");
		legendAll.add("退单率");
		
		List<String> legend = new ArrayList<String>();
		legend.add("新增订单小计");
		legend.add("微网站来源");
		legend.add("App来源");
		legend.add("退单数");	
		
		chartDataVo.setLegend(JSON.toJSONString(legend));
		
		List<String> xAxis = new ArrayList<String>();
		for (int i =1; i < timeSeries.size(); i++) {
			xAxis.add(ChartUtil.getTimeSeriesName(statType, timeSeries.get(i)));
		}
		
		chartDataVo.setxAxis(JSON.toJSONString(xAxis));

		//初始化表格数据格式.
		List<HashMap<String, String>> tableDatas = new ArrayList<HashMap<String, String>>();
		
		HashMap<String, String> tableData = null;
		for (int i =0; i < timeSeries.size(); i++) {
			tableData = new HashMap<String, String>();
			tableData.put("series", timeSeries.get(i));
			
			for (int j =0; j < legendAll.size(); j++) {
				tableData.put(legendAll.get(j), "0");
			}
			tableDatas.add(tableData);
		}
		
		//查询SQL获得统计数据 -- 订单总数
		List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();
		
		if (chartSearchVo.getStatType().equals("day") ) {
			statDatas = orderMapper.statByDay(chartSearchVo);
		}
		
		if (chartSearchVo.getStatType().equals("month") ) {
			statDatas = orderMapper.statByMonth(chartSearchVo);
		}	
		
		if (chartSearchVo.getStatType().equals("quarter") ) {
			statDatas = orderMapper.statByQuarter(chartSearchVo);
		}			

		//循环统计数据，完成表格数据替换
		//1.先实现表格数据的替换. 计算App来源和微网站来源. 新增订单小计

		for (ChartMapVo chartSqlData : statDatas) {
			//处理表格形式的数据.
			for (Map<String, String> tableDataItem : tableDatas) {
				if (tableDataItem.get("series").toString().equals(chartSqlData.getSeries())) {
					//0代表APP  1 = 微网站来源
					if (chartSqlData.getName().equals("0")){
						tableDataItem.put("App来源", String.valueOf(chartSqlData.getTotal()));
					}
					if(chartSqlData.getName().equals("1")){
						tableDataItem.put("微网站来源", String.valueOf(chartSqlData.getTotal()));
					}
					
					
					//新增订单小计 
					Integer subTotal = Integer.valueOf(tableDataItem.get("新增订单小计"));
					subTotal = subTotal + chartSqlData.getTotal();
					
					tableDataItem.put("新增订单小计", subTotal.toString());
				}
//				System.out.println(tableDataItem.get("新增订单小计"));
			}
		}
		
		//2. 计算增长率
		Integer tmpSubTotal = 0;
		Integer subTotal = 0;
		for (Map<String, String> tableDataItem : tableDatas) {
			
			if (tmpSubTotal.equals(0)) {
				tmpSubTotal = Integer.valueOf(tableDataItem.get("新增订单小计"));
				tableDataItem.put("增长率", "-");
				continue;
			} 
			
			subTotal = Integer.valueOf(tableDataItem.get("新增订单小计"));
			
			if (!subTotal.equals(0)) {
				String incrPercent = MathDoubleUtil.getRiseRate(subTotal, tmpSubTotal);
				tableDataItem.put("增长率", incrPercent);
			} else {
				tableDataItem.put("增长率", "-");
			}
			tmpSubTotal = Integer.valueOf(tableDataItem.get("新增订单小计"));	
		}
		
		//3. 计算退单和退单率
		statDatas = new ArrayList<ChartMapVo>();
		
		List<Short> status = new ArrayList<Short>();
		status.add((short)0);
		chartSearchVo.setStatus(status);
		
		if (chartSearchVo.getStatType().equals("day") ) {
			statDatas = orderMapper.statByDay(chartSearchVo);
		}
		
		if (chartSearchVo.getStatType().equals("month") ) {
			statDatas = orderMapper.statByMonth(chartSearchVo);
		}	
	
		if (chartSearchVo.getStatType().equals("quarter") ) {
			statDatas = orderMapper.statByQuarter(chartSearchVo);
		}			
		
		for (ChartMapVo chartSqlData : statDatas) {
			//处理表格形式的数据.
			for (Map<String, String> tableDataItem : tableDatas) {
				if (tableDataItem.get("series").toString().equals(chartSqlData.getSeries())) {

					tableDataItem.put("退单数", chartSqlData.getTotal().toString());
				}
			}
		}		
		
		//4. 计算退单率

		subTotal = 0;
		Integer subTotalCancel = 0;
		for (Map<String, String> tableDataItem : tableDatas) {
			
			subTotal = Integer.valueOf(tableDataItem.get("新增订单小计"));
			subTotalCancel = Integer.valueOf(tableDataItem.get("退单数"));
			if (subTotal.equals(0) || subTotalCancel.equals(0)) {
				tableDataItem.put("退单率", "-");
				continue;
			} 

			String incrPercent = MathDoubleUtil.getPercent(subTotalCancel, subTotal);
			tableDataItem.put("退单率", incrPercent);

		}
		
		//5. 去掉第一个tableDataItem;
		if (tableDatas.size() > 0) tableDatas.remove(0);
		
		//6. 根据表格的数据生成图表的数据.
		//初始化图表数据格式
		List<Map<String, Object>> dataItems = new ArrayList<Map<String, Object>>();
		Map<String,Object> chartDataItem = null;
		List<Integer> datas = null;
		for (int i =0; i < legend.size(); i++) {
			chartDataItem = new HashMap<String,Object>();
			chartDataItem.put("name", legend.get(i));
			chartDataItem.put("type", "bar");
			datas = new ArrayList<Integer>();
			
			for (int j =1; j < timeSeries.size(); j++) {
				for (Map<String, String> tableDataItem : tableDatas) {
					if (timeSeries.get(j).equals(tableDataItem.get("series").toString())) {
						String valueStr = tableDataItem.get(legend.get(i)).toString();
						Integer v = Integer.valueOf(valueStr);
						datas.add(v);
					}
				}
			}
			chartDataItem.put("data", datas);
			dataItems.add(chartDataItem);
		}
		chartDataVo.setSeries(JSON.toJSONString(dataItems));		
		
		
		//最后要把tableData -》时间序列 ，比如有 6，7，8转换为 6月，7月，8月
		for (Map<String, String> tableDataItem : tableDatas) {
			String seriesName = tableDataItem.get("series").toString();
			seriesName = ChartUtil.getTimeSeriesName(statType, seriesName);
			tableDataItem.put("series", seriesName);
			
			// 得到当前 series 的 开始时间和 结束时间
			HashMap<String,Long> timeDuration = ChartUtil.getTimeDuration(statType, seriesName);
			
			tableDataItem.put("startTime", timeDuration.get("startTime").toString());
			
			tableDataItem.put("endTime", timeDuration.get("endTime").toString());
			
		}
		chartDataVo.setTableDatas(tableDatas);
		
		return chartDataVo;
	}
	
	/**
	 * 订单数求和
	 * 
	 */
	@Override
	public Map<String,Integer> statTotalOrder(ChartSearchVo chartSearchVo) {	
		
		return orderMapper.statTotalOrder(chartSearchVo);
	}
	
	
	
	/*
	 *  订单收入图表
	 */
	@Override
	public ChartDataVo getOrderRevenue(ChartSearchVo chartSearchVo, List<String> timeSeries) {
		
		ChartDataVo chartDataVo = new ChartDataVo();
		
		if (timeSeries.isEmpty()) return chartDataVo;

		String statType = chartSearchVo.getStatType();
		
		List<String> legendAll = new ArrayList<String>();
		legendAll.add("微网站来源");
		legendAll.add("微网站营业额");
		legendAll.add("App来源");
		legendAll.add("App营业额");
		legendAll.add("营业额小计");
		legendAll.add("增长率");
		
		List<String> legend = new ArrayList<String>();    
		legend.add("微网站来源");
		legend.add("App来源");
		
		chartDataVo.setLegend(JSON.toJSONString(legend));
		
		List<String> xAxis = new ArrayList<String>();
		for (int i =1; i < timeSeries.size(); i++) {
			xAxis.add(ChartUtil.getTimeSeriesName(statType, timeSeries.get(i)));
		}
		
		chartDataVo.setxAxis(JSON.toJSONString(xAxis));
		
		//初始化表格数据格式.
		List<HashMap<String, String>> tableDatas = new ArrayList<HashMap<String, String>>();
		
		HashMap<String, String> tableData = null;
		for (int i =0; i < timeSeries.size(); i++) {
			tableData = new HashMap<String, String>();
			tableData.put("series", timeSeries.get(i));
			
			for (int j =0; j < legendAll.size(); j++) {
				tableData.put(legendAll.get(j), "0");
			}
			tableDatas.add(tableData);
		}
		
		// 1. 查询SQL获得统计数据 -- 不同来源的订单数量
		List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();
		if (chartSearchVo.getStatType().equals("day") ) {
			statDatas = orderMapper.orderRevenueByDay(chartSearchVo);
		}
		
		if (chartSearchVo.getStatType().equals("month") ) {
			statDatas = orderMapper.orderRevenueByMonth(chartSearchVo);
		}	
		
		if (chartSearchVo.getStatType().equals("quarter") ) {
			statDatas = orderMapper.orderRevenueByQuarter(chartSearchVo);
		}		
		
		BigDecimal appMoney = new BigDecimal(0);
		BigDecimal webMoney = new BigDecimal(0);
		BigDecimal add = new BigDecimal(0);
		
		//1-1. 替换表格数据   App来源和微网站来源(数量)
		for (ChartMapVo chartSqlData : statDatas) {
			//处理表格形式的数据.
			for (Map<String, String> tableDataItem : tableDatas) {
				if (tableDataItem.get("series").toString().equals(chartSqlData.getSeries())) {
					//0代表APP  1 = 微网站来源
					if (chartSqlData.getName().equals("0")){
						
						tableDataItem.put("App来源", String.valueOf(chartSqlData.getTotal()));
						
						appMoney =  chartSqlData.getTotalMoney();
						
						tableDataItem.put("App营业额", MathBigDecimalUtil.round2(appMoney));
					}
						
					if (chartSqlData.getName().equals("1")){
						tableDataItem.put("微网站来源", String.valueOf(chartSqlData.getTotal()));
						
						webMoney = chartSqlData.getTotalMoney();
						tableDataItem.put("微网站营业额", MathBigDecimalUtil.round2(webMoney));
					}
					
					add = appMoney.add(webMoney);
					
					tableDataItem.put("营业额小计", MathBigDecimalUtil.round2(add));
				}
			}
		}
		
		/*
		 * 2-1. 查询sql  获得 不同时间粒度下 不同来源的 营业额
		 * 	   
		 *    1. 营业额小计
		 *    2. 根据 orderId 得到 不同来源的营业额
		 *    	
		 */
		
		//2-2 计算 增长率
		Double tmpSubTotal = 0.00;
		Double subTotal = 0.00;
		
		for (Map<String, String> tableDataItem : tableDatas) {
			
			if (tmpSubTotal.equals(0.00)) {
				tmpSubTotal = Double.valueOf(tableDataItem.get("营业额小计"));
				
				tableDataItem.put("增长率", "-");
				continue;
			} 
			
			subTotal = Double.valueOf(tableDataItem.get("营业额小计"));
			
			
			if (!subTotal.equals(0.00)) {
//				String incrPercent = MathDoubleUtil.getRiseRate(subTotal, tmpSubTotal);
				
				String incrPercent = MathDoubleUtil.getRiseRate(subTotal.intValue(), tmpSubTotal.intValue());
				tableDataItem.put("增长率", incrPercent);
			} else {
				tableDataItem.put("增长率", "-");
			}
			tmpSubTotal = Double.valueOf(tableDataItem.get("营业额小计"));	
			
		}
		
		//5. 去掉第一个tableDataItem;
		if (tableDatas.size() > 0) tableDatas.remove(0);
		
		//6. 根据表格的数据生成图表的数据.
		//初始化图表数据格式
		List<Map<String, Object>> dataItems = new ArrayList<Map<String, Object>>();
		Map<String,Object> chartDataItem = null;
		List<Integer> datas = null;
		for (int i =0; i < legend.size(); i++) {
			chartDataItem = new HashMap<String,Object>();
			chartDataItem.put("name", legend.get(i));
			chartDataItem.put("type", "bar");
			datas = new ArrayList<Integer>();
			
			for (int j =1; j < timeSeries.size(); j++) {
				for (Map<String, String> tableDataItem : tableDatas) {
					if (timeSeries.get(j).equals(tableDataItem.get("series").toString())) {
						String valueStr = tableDataItem.get(legend.get(i)).toString();
						Integer v = Integer.valueOf(valueStr);
						datas.add(v);
					}
				}
			}
			chartDataItem.put("data", datas);
			dataItems.add(chartDataItem);
		}
		chartDataVo.setSeries(JSON.toJSONString(dataItems));		
		
		
		//最后要把tableData -》时间序列 ，比如有 6，7，8转换为 6月，7月，8月
		for (Map<String, String> tableDataItem : tableDatas) {
			String seriesName = tableDataItem.get("series").toString();
			seriesName = ChartUtil.getTimeSeriesName(statType, seriesName);
			tableDataItem.put("series", seriesName);
			
			// 得到当前 series 的 开始时间和 结束时间
			HashMap<String,Long> timeDuration = ChartUtil.getTimeDuration(statType, seriesName);
			
			tableDataItem.put("startTime", timeDuration.get("startTime").toString());
			
			tableDataItem.put("endTime", timeDuration.get("endTime").toString());
			
		}
		
		chartDataVo.setTableDatas(tableDatas);
		return chartDataVo;
	}
	
	
}
