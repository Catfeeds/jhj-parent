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
import com.jhj.service.chart.ChartTypeService;
import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartMapVo;
import com.jhj.vo.chart.ChartSearchVo;
import com.meijia.utils.ChartUtil;
import com.meijia.utils.MathBigDeciamlUtil;
import com.meijia.utils.MathDoubleUtil;


/*
 * 	市场品类图表 、品类收入 service
 */
@Service
public class ChartTypeServiceImpl implements ChartTypeService {

	@Autowired
	private OrdersMapper orderMapper;
	
	/*
	 * 市场品类图表
	 */
	@Override
	public ChartDataVo getChartTypeData(ChartSearchVo chartSearchVo, List<String> timeSeries) {
		
		ChartDataVo chartDataVo = new ChartDataVo();
		/*
		 *    组装参数
		 */
		
		if (timeSeries.isEmpty()) return chartDataVo;

		String statType = chartSearchVo.getStatType();
		
		//1. table 列名
		List<String> legendAll = new ArrayList<String>();
		legendAll.add("总单数");
		legendAll.add("钟点工");
		legendAll.add("钟点工占比");
//		legendAll.add("深度保洁");
//		legendAll.add("深度保洁占比");
		legendAll.add("助理预约单");
		legendAll.add("助理预约单占比");
		
		legendAll.add("提醒订单");
		legendAll.add("提醒订单占比");
		
		//2. 统计图 图例
		List<String> legend = new ArrayList<String>();
		legend.add("钟点工");
//		legend.add("深度保洁");
		legend.add("助理预约单");	
		
		legend.add("提醒订单");		
		
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
			statDatas = orderMapper.chartTypeByDay(chartSearchVo);
		}
		
		if (chartSearchVo.getStatType().equals("month") ) {
			statDatas = orderMapper.chartTypeByMonth(chartSearchVo);
		}	
		
		if (chartSearchVo.getStatType().equals("quarter") ) {
			statDatas = orderMapper.chartTypeByQuarter(chartSearchVo);
		}
		
		//4-2.真实数据填充（table表格），计算每行总计，以及各品类占比
		
		for (ChartMapVo chartSqlData : statDatas) {
			//处理表格形式的数据.
			for (Map<String, String> tableDataItem : tableDatas) {
				if (tableDataItem.get("series").toString().equals(chartSqlData.getSeries())) {
					//0代表 钟点工  1 = 深度保洁   2= 助理预约单  5= 提醒订单
					if (chartSqlData.getName().equals("0"))
						tableDataItem.put("钟点工", String.valueOf(chartSqlData.getTotal()));
//					if (chartSqlData.getName().equals("1"))
//						tableDataItem.put("深度保洁", String.valueOf(chartSqlData.getTotal()));
					if (chartSqlData.getName().equals("2"))
						tableDataItem.put("助理预约单", String.valueOf(chartSqlData.getTotal()));
					
					if (chartSqlData.getName().equals("5"))
						tableDataItem.put("提醒订单", String.valueOf(chartSqlData.getTotal()));
					
					//每行记录的总单数
					Integer sumTotal = Integer.valueOf(tableDataItem.get("总单数"));
					sumTotal = sumTotal + chartSqlData.getTotal();
					
					tableDataItem.put("总单数", sumTotal.toString());
				}
			}
		}
		
		//4-3. 计算各品类 占比
		
		String orderHourPercent = "0";	//钟点工占比
//		String deepPercent = "0";		//深度保洁占比
		String amPercent = "0";			//助理预约单占比
		
		String remindPercent = "0";		//提醒订单占比
		for (Map<String, String> tableDataItem : tableDatas) {
			
			Integer sumTotal = Integer.valueOf(tableDataItem.get("总单数"));
			
			//每行记录，各类订单数量
			Integer orderHour = Integer.valueOf(tableDataItem.get("钟点工"));
//			Integer deepOrder = Integer.valueOf(tableDataItem.get("深度保洁"));
			Integer amOrder = Integer.valueOf(tableDataItem.get("助理预约单"));
			
			Integer remindOrder = Integer.valueOf(tableDataItem.get("提醒订单"));
			
			if(sumTotal > 0){
				
				orderHourPercent = MathDoubleUtil.getPercent(orderHour,sumTotal);
				tableDataItem.put("钟点工占比", orderHourPercent);
				
//				deepPercent = MathDoubleUtil.getPercent(deepOrder, sumTotal);
//				tableDataItem.put("深度保洁占比", deepPercent);
				
				amPercent = MathDoubleUtil.getPercent(amOrder, sumTotal);
				tableDataItem.put("助理预约单占比", amPercent);
				
				remindPercent = MathDoubleUtil.getPercent(remindOrder, sumTotal);
				tableDataItem.put("提醒订单占比", remindPercent);
			}else{
				tableDataItem.put("钟点工占比","0.00%");
//				tableDataItem.put("深度保洁占比","0.00%" );
				tableDataItem.put("助理预约单占比","0.00%");
				
				tableDataItem.put("提醒订单占比", "0.00%");
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
			chartDataItem.put("type", "line");
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
	 * 品类收入图表
	 */
	@Override
	public ChartDataVo chartTypeRevenueData(ChartSearchVo chartSearchVo, List<String> timeSeries) {
		
		ChartDataVo chartDataVo = new ChartDataVo();
		/*
		 *    组装参数
		 */
		
		if (timeSeries.isEmpty()) return chartDataVo;

		String statType = chartSearchVo.getStatType();
		
		//1. table 列名
		List<String> legendAll = new ArrayList<String>();
		legendAll.add("总单数");
		legendAll.add("总营业额");
		legendAll.add("钟点工");
		legendAll.add("钟点工营业额");
		legendAll.add("钟点工营业额占比");
//		legendAll.add("深度保洁");
//		legendAll.add("深度保洁营业额");
		legendAll.add("钟点工营业额占比");
		legendAll.add("助理预约单");
		legendAll.add("助理预约单营业额");
		legendAll.add("助理预约单营业额占比");
		
		//2. 统计图 图例
		List<String> legend = new ArrayList<String>();
		legend.add("钟点工");
//		legend.add("深度保洁");
		legend.add("助理预约单");	
		
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
		
		
		// 1. 查询SQL获得统计数据 -- 不同来源的订单数量
		List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();
		if (chartSearchVo.getStatType().equals("day") ) {
			statDatas = orderMapper.chartTypeRevenueByDay(chartSearchVo);
		}
		
		if (chartSearchVo.getStatType().equals("month") ) {
			statDatas = orderMapper.chartTypeRevenueByMonth(chartSearchVo);
		}	
		
		if (chartSearchVo.getStatType().equals("quarter") ) {
			statDatas = orderMapper.chartTypeRevenueByQuarter(chartSearchVo);
		}		
		
		BigDecimal hourMoney = new BigDecimal(0);
		BigDecimal deepMoney = new BigDecimal(0);
		BigDecimal amMoney = new BigDecimal(0);
		
		Integer hourNum = 0;
		Integer deepNum = 0;
		Integer amNum = 0;
		
		for (ChartMapVo chartSqlData : statDatas) {
			//处理表格形式的数据.
			for (Map<String, String> tableDataItem : tableDatas) {
				if (tableDataItem.get("series").toString().equals(chartSqlData.getSeries())) {
					//0代表钟点工  1 = 深度保洁  2=助理预约单
					if (chartSqlData.getName().equals("0")){
						
						hourNum =  chartSqlData.getTotal();
						tableDataItem.put("钟点工", String.valueOf(hourNum));
						
						hourMoney =  chartSqlData.getTotalMoney();
						if(hourMoney ==null){
							hourMoney = new BigDecimal(0);
						}
						tableDataItem.put("钟点工营业额", MathBigDeciamlUtil.round2(hourMoney));
					}
						
//					if (chartSqlData.getName().equals("1")){
//						
//						deepNum =  chartSqlData.getTotal();
//						tableDataItem.put("深度保洁", String.valueOf(deepNum));
//						
//						deepMoney =  chartSqlData.getTotalMoney();
//						if(deepMoney ==null){
//							deepMoney = new BigDecimal(0);
//						}
//						tableDataItem.put("深度保洁营业额", MathBigDeciamlUtil.round2(deepMoney));
//					}
					
					if (chartSqlData.getName().equals("2")){
						
						amNum =  chartSqlData.getTotal();
						tableDataItem.put("助理预约单", String.valueOf(amNum));
						
						amMoney =  chartSqlData.getTotalMoney();
						
						if(amMoney ==null){
							amMoney = new BigDecimal(0);
						}
						
						tableDataItem.put("助理预约单营业额", MathBigDeciamlUtil.round2(amMoney));
					}
					
				}
			}
		}
		
		for (Map<String, String> tableDataItem : tableDatas) {
			
			BigDecimal moneyHour = new BigDecimal(tableDataItem.get("钟点工营业额"));
//			BigDecimal moneyDeep = new BigDecimal(tableDataItem.get("深度保洁营业额"));
			BigDecimal moneyAm = new BigDecimal(tableDataItem.get("助理预约单营业额"));
			
//			BigDecimal moneySum = moneyHour.add(moneyDeep).add(moneyAm);
			
			BigDecimal moneySum = moneyHour.add(moneyAm);
			
			tableDataItem.put("总营业额", MathBigDeciamlUtil.round2(moneySum));
			
			//intValue(), 效果 只取整数位，舍弃小数位  1.11  、 1.61  -->  1
			if(moneySum.intValue() > 0 ){
				tableDataItem.put("钟点工营业额占比", MathDoubleUtil.getPercent(moneyHour.intValue(),moneySum.intValue()));
//				tableDataItem.put("深度保洁营业额占比", MathDoubleUtil.getPercent(moneyDeep.intValue(),moneySum.intValue()));
				tableDataItem.put("助理预约单营业额占比", MathDoubleUtil.getPercent(moneyAm.intValue(),moneySum.intValue()));
			}else{
				tableDataItem.put("钟点工营业额占比","0.00%");
//				tableDataItem.put("深度保洁营业额占比","0.00%" );
				tableDataItem.put("助理预约单营业额占比","0.00%");
			}
			
			
			Integer numHour = Integer.valueOf(tableDataItem.get("钟点工"));
//			Integer numDeep = Integer.valueOf(tableDataItem.get("深度保洁"));
			Integer numAm = Integer.valueOf(tableDataItem.get("助理预约单"));
			
//			tableDataItem.put("总单数", String.valueOf(numHour+numDeep+numAm));
			tableDataItem.put("总单数", String.valueOf(numHour+numAm));
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
			chartDataItem.put("type", "line");
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
