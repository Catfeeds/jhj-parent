package com.jhj.service.impl.chart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.service.chart.PhoneRechargeChartService;
import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartMapVo;
import com.jhj.vo.chart.ChartSearchVo;
import com.meijia.utils.ChartUtil;
import com.meijia.utils.MathDoubleUtil;

@Service
public class PhoneRechargeChartServiceImpl implements PhoneRechargeChartService {
	
	@Autowired
	private OrdersMapper orderMapper;
	
	@Override
	public ChartDataVo getPhoneOrder(ChartSearchVo chartSearchVo, List<String> timeSeries) {
		
		ChartDataVo chartDataVo = new ChartDataVo();
		/*
		 *    组装参数
		 */
		
		if (timeSeries.isEmpty()) return chartDataVo;

		String statType = chartSearchVo.getStatType();
		
		//1. table 列名
		List<String> legendAll = new ArrayList<String>();
		legendAll.add("总单数");
		legendAll.add("20元");
		legendAll.add("20元占比");
		legendAll.add("30元");
		legendAll.add("30元占比");
		legendAll.add("50元");
		legendAll.add("50元占比");
		legendAll.add("100元");
		legendAll.add("100元占比");
		legendAll.add("200元");
		legendAll.add("200元占比");
		//2. 统计图 图例
		List<String> legend = new ArrayList<String>();
		legend.add("总单数");
		legend.add("20元");
		legend.add("30元");
		legend.add("50元");
		legend.add("100元");
		legend.add("200元");
		
		
		
		chartDataVo.setLegend(JSON.toJSONString(legend));
		
		//3. x轴 
		List<String> xAxis = new ArrayList<String>();
		for (int i =1; i < timeSeries.size(); i++) {
			xAxis.add(ChartUtil.getTimeSeriesName(statType, timeSeries.get(i)));
		}
		
		chartDataVo.setxAxis(JSON.toJSONString(xAxis));

		/*
		 * 4. 填充 tables
		 */
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
		
		// 4-1.查询SQL获得统计数据 -- 各类订单总数
		List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();
		
		if (chartSearchVo.getStatType().equals("day") ) {
			
			statDatas = orderMapper.phoneRechargeByDay(chartSearchVo);
		}
		
		if (chartSearchVo.getStatType().equals("month") ) {
			statDatas = orderMapper.phoneRechargeByMonth(chartSearchVo);
		}	
		
		if (chartSearchVo.getStatType().equals("quarter") ) {
			statDatas = orderMapper.phoneRechargeByQuarter(chartSearchVo);
		}
		
		//4-2.真实数据填充（table表格），计算每行总计，以及各品类占比
		
		for (ChartMapVo chartSqlData : statDatas) {
			//处理表格形式的数据.
			for (Map<String, String> tableDataItem : tableDatas) {
				if (tableDataItem.get("series").toString().equals(chartSqlData.getSeries())) {
					//0代表 钟点工  1 = 深度保洁   2= 助理预约单  5= 提醒订单

					if (chartSqlData.getName().equals("20.00"))
						tableDataItem.put("20元", String.valueOf(chartSqlData.getTotal()));
					if (chartSqlData.getName().equals("30.00"))
						tableDataItem.put("30元", String.valueOf(chartSqlData.getTotal()));
					if (chartSqlData.getName().equals("50.00"))
						tableDataItem.put("50元", String.valueOf(chartSqlData.getTotal()));
					if (chartSqlData.getName().equals("100.00"))
						tableDataItem.put("100元", String.valueOf(chartSqlData.getTotal()));
					if (chartSqlData.getName().equals("200.00"))
						tableDataItem.put("200元", String.valueOf(chartSqlData.getTotal()));
					
					//每行记录的总单数
					Integer sumTotal = Integer.valueOf(tableDataItem.get("总单数"));
					sumTotal = sumTotal + chartSqlData.getTotal();
					
					tableDataItem.put("总单数", sumTotal.toString());
				}
			}
		}
		
		//4-3. 计算各品类 占比
		
		String twentyPercent = "0";			//20元占比
		String thirtyPercent = "0";			//30元占比
		String fiftyPercent = "0";			//50元占比
		String oneHundredPercent = "0";		//100元占比
		String twoHundredPercent = "0";		//200元占比
		for (Map<String, String> tableDataItem : tableDatas) {
			
			Integer sumTotal = Integer.valueOf(tableDataItem.get("总单数"));
			
			//每行记录，各类订单数量
			Integer twenty = Integer.valueOf(tableDataItem.get("20元"));
			Integer thirty = Integer.valueOf(tableDataItem.get("30元"));
			Integer fifty = Integer.valueOf(tableDataItem.get("50元"));
			Integer oneHundred = Integer.valueOf(tableDataItem.get("100元"));
			Integer twoHundred = Integer.valueOf(tableDataItem.get("200元"));
			
			
			if(sumTotal > 0){
				
				twentyPercent = MathDoubleUtil.getPercent(twenty,sumTotal);
				tableDataItem.put("20元占比", twentyPercent);
				
				thirtyPercent = MathDoubleUtil.getPercent(thirty, sumTotal);
				tableDataItem.put("30元占比", thirtyPercent);
				
				
				fiftyPercent = MathDoubleUtil.getPercent(fifty,sumTotal);
				tableDataItem.put("50元占比", fiftyPercent);
				
				oneHundredPercent = MathDoubleUtil.getPercent(oneHundred,sumTotal);
				tableDataItem.put("100元占比", oneHundredPercent);
				
				twoHundredPercent = MathDoubleUtil.getPercent(twoHundred, sumTotal);
				tableDataItem.put("200元占比", twoHundredPercent);
				
			}else{
				tableDataItem.put("20元占比","0.00%");
				tableDataItem.put("30元占比","0.00%" );
				tableDataItem.put("50元占比","0.00%");
				tableDataItem.put("100元占比", "0.00%");
				tableDataItem.put("200元占比", "0.00%");
			}
		}
		
		//5.去掉第一个 tableDataItem ??
		if (tableDatas.size() > 0) tableDatas.remove(0);
		//6.生成 统计图 数据
		
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
		
		//x轴 刻度 转换
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
	
	
	
	/*
	 * 话费订单收入统计
	 */
	@Override
	public ChartDataVo getPhoneRevenue(ChartSearchVo chartSearchVo, List<String> timeSeries) {
		
		ChartDataVo chartDataVo = new ChartDataVo();
		/*
		 *    组装参数
		 */
		
		if (timeSeries.isEmpty()) return chartDataVo;

		String statType = chartSearchVo.getStatType();
		
		//1. table 列名
		List<String> legendAll = new ArrayList<String>();
		legendAll.add("总营业额");
		legendAll.add("20元");
		legendAll.add("20元占比");
		legendAll.add("30元");
		legendAll.add("30元占比");
		legendAll.add("50元");
		legendAll.add("50元占比");
		legendAll.add("100元");
		legendAll.add("100元占比");
		legendAll.add("200元");
		legendAll.add("200元占比");
		//2. 统计图 图例
		List<String> legend = new ArrayList<String>();
		legend.add("总营业额");
		legend.add("20元");
		legend.add("30元");
		legend.add("50元");
		legend.add("100元");
		legend.add("200元");
		
		
		
		chartDataVo.setLegend(JSON.toJSONString(legend));
		
		//3. x轴 
		List<String> xAxis = new ArrayList<String>();
		for (int i =1; i < timeSeries.size(); i++) {
			xAxis.add(ChartUtil.getTimeSeriesName(statType, timeSeries.get(i)));
		}
		
		chartDataVo.setxAxis(JSON.toJSONString(xAxis));

		/*
		 * 4. 填充 tables
		 */
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
		
		// 4-1.查询SQL获得统计数据 -- 各类订单总数
		List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();
		
		if (chartSearchVo.getStatType().equals("day") ) {
			
			statDatas = orderMapper.phoneRechargeByDay(chartSearchVo);
		}
		
		if (chartSearchVo.getStatType().equals("month") ) {
			statDatas = orderMapper.phoneRechargeByMonth(chartSearchVo);
		}	
		
		if (chartSearchVo.getStatType().equals("quarter") ) {
			statDatas = orderMapper.phoneRechargeByQuarter(chartSearchVo);
		}
		
		//4-2.真实数据填充（table表格），计算每行总计，以及各品类占比
		
		for (ChartMapVo chartSqlData : statDatas) {
			//处理表格形式的数据.
			for (Map<String, String> tableDataItem : tableDatas) {
				if (tableDataItem.get("series").toString().equals(chartSqlData.getSeries())) {

					if (chartSqlData.getName().equals("20.00"))
						tableDataItem.put("20元", String.valueOf(chartSqlData.getTotalMoney()));
					if (chartSqlData.getName().equals("30.00"))
						tableDataItem.put("30元", String.valueOf(chartSqlData.getTotalMoney()));
					if (chartSqlData.getName().equals("50.00"))
						tableDataItem.put("50元", String.valueOf(chartSqlData.getTotalMoney()));
					if (chartSqlData.getName().equals("100.00"))
						tableDataItem.put("100元", String.valueOf(chartSqlData.getTotalMoney()));
					if (chartSqlData.getName().equals("200.00"))
						tableDataItem.put("200元", String.valueOf(chartSqlData.getTotalMoney()));
					
					//每行记录的总单数
					
					BigDecimal subTotal1 = new BigDecimal(tableDataItem.get("总营业额"));
					BigDecimal sumTotal1 = chartSqlData.getTotalMoney();
					if(sumTotal1!=null){
						subTotal1 = subTotal1.add(sumTotal1) ;
					}
					
					tableDataItem.put("总营业额", subTotal1.toString());
				}
			}
		}
		
		//4-3. 计算各品类 占比
		
		String twentyPercent = "0";			//20元占比
		String thirtyPercent = "0";			//30元占比
		String fiftyPercent = "0";			//50元占比
		String oneHundredPercent = "0";		//100元占比
		String twoHundredPercent = "0";		//200元占比
		for (Map<String, String> tableDataItem : tableDatas) {
			
			Integer sumTotal = new BigDecimal(tableDataItem.get("总营业额")).intValue();
			
			Integer twenty = new BigDecimal(tableDataItem.get("20元")).intValue();
			Integer thirty =new BigDecimal(tableDataItem.get("30元")).intValue();
			Integer fifty = new BigDecimal(tableDataItem.get("50元")).intValue();
			Integer oneHundred = new BigDecimal(tableDataItem.get("100元")).intValue();
			Integer twoHundred = new BigDecimal(tableDataItem.get("200元")).intValue();
			
			
			
			if(sumTotal > 0){
				
				twentyPercent = MathDoubleUtil.getPercent(twenty,sumTotal);
				tableDataItem.put("20元占比", twentyPercent);
				
				thirtyPercent =  MathDoubleUtil.getPercent(thirty, sumTotal);
				tableDataItem.put("30元占比", thirtyPercent);
				
				
				fiftyPercent =  MathDoubleUtil.getPercent(fifty,sumTotal);
				tableDataItem.put("50元占比", fiftyPercent);
				
				oneHundredPercent =  MathDoubleUtil.getPercent(oneHundred,sumTotal);
				tableDataItem.put("100元占比", oneHundredPercent);
				
				twoHundredPercent =  MathDoubleUtil.getPercent(twoHundred, sumTotal);
				tableDataItem.put("200元占比", twoHundredPercent);
				
			}else{
				tableDataItem.put("20元占比","0.00%");
				tableDataItem.put("30元占比","0.00%" );
				tableDataItem.put("50元占比","0.00%");
				tableDataItem.put("100元占比", "0.00%");
				tableDataItem.put("200元占比", "0.00%");
			}
		}
		
		//5.去掉第一个 tableDataItem ??
		if (tableDatas.size() > 0) tableDatas.remove(0);
		//6.生成 统计图 数据
		
		List<Map<String, Object>> dataItems = new ArrayList<Map<String, Object>>();
		Map<String,Object> chartDataItem = null;
		List<String> datas = null;
		for (int i =0; i < legend.size(); i++) {
			chartDataItem = new HashMap<String,Object>();
			chartDataItem.put("name", legend.get(i));
			chartDataItem.put("type", "bar");
			datas = new ArrayList<String>();
			
			for (int j =1; j < timeSeries.size(); j++) {
				for (Map<String, String> tableDataItem : tableDatas) {
					if (timeSeries.get(j).equals(tableDataItem.get("series").toString())) {
						String valueStr = tableDataItem.get(legend.get(i)).toString();
//						Integer v = Integer.valueOf(valueStr);
						
						datas.add(valueStr);
					}
				}
			}
			
			chartDataItem.put("data", datas);
			dataItems.add(chartDataItem);
		}
		chartDataVo.setSeries(JSON.toJSONString(dataItems));		
		
		//x轴 刻度 转换
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
