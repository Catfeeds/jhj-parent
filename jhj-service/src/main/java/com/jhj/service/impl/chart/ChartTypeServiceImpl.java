package com.jhj.service.impl.chart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.meijia.utils.DateUtil;
import com.meijia.utils.MathBigDecimalUtil;
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
		legendAll.add("基础服务");
		legendAll.add("基础服务占比");
		legendAll.add("深度服务");
		legendAll.add("深度服务占比");
		legendAll.add("母婴到家");
		legendAll.add("母婴到家占比");
		
		//2. 统计图 图例
		List<String> legend = new ArrayList<String>();
		legend.add("基础服务");
		legend.add("深度服务");	
		legend.add("母婴到家");		
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
//		Short[] status={7,8,9};
//		chartSearchVo.setStatus(Arrays.asList(status));
		// 4-1.查询SQL获得统计数据 -- 各类订单总数
		List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();
		
		if (chartSearchVo.getStatType().equals("day") ) {
			chartSearchVo.setFormatParam("%Y-%m-%e");
			statDatas = orderMapper.chartTypeRevenue(chartSearchVo);
		}
		
		if (chartSearchVo.getStatType().equals("month") ) {
			chartSearchVo.setFormatParam("%Y-%m");
			statDatas = orderMapper.chartTypeRevenue(chartSearchVo);
		}	
		
		if (chartSearchVo.getStatType().equals("quarter") ) {
			statDatas = orderMapper.chartTypeRevenue(chartSearchVo);
		}		
		
		Short[] shenduserviceType={34,35,36,50,51,52,53,54,55,56,60,61};
		List<Short> deepType = Arrays.asList(shenduserviceType);
		Short[] muyinserviceType={62,63,64,65};
		List<Short> hourType = Arrays.asList(muyinserviceType);
		String str=null,str1=null;
		//4-2.真实数据填充（table表格），计算每行总计，以及各品类占比
		for (Map<String, String> tableDataItem : tableDatas) {
			Integer hourNum = 0;
			Integer deepNum = 0;
			Integer myNum = 0;
			for (ChartMapVo chartSqlData : statDatas) {
				//处理表格形式的数据.
				str = tableDataItem.get("series");
				str1 = chartSqlData.getSeries();
				if(chartSearchVo.getStatType().equals("day")){
					if(DateUtil.compareDateStr(str,str1)==0){
						Integer total =0;
						if(chartSqlData.getTotal()!=null){
							total = chartSqlData.getTotal();
						}
						//0代表基础服务  1 = 深度服务 母婴到家  
						if (chartSqlData.getName().equals("0")){
							hourNum+=total;
						}
						if(chartSqlData.getName().equals("1")){
							if(deepType.contains(chartSqlData.getServiceType())){
								deepNum+=total;
							}
							if(hourType.contains(chartSqlData.getServiceType())){
								myNum+=total;
							}
						}
					}
					
				}else{
					if (str.equals(str1)) {
						Integer total =0;
						if(chartSqlData.getTotal()!=null){
							total = chartSqlData.getTotal();
						}
						//0代表基础服务  1 = 深度服务 母婴到家  
						if (chartSqlData.getName().equals("0")){
							hourNum+=total;
						}
						if(chartSqlData.getName().equals("1")){
							if(deepType.contains(chartSqlData.getServiceType())){
								deepNum+=total;
							}
							if(hourType.contains(chartSqlData.getServiceType())){
								myNum+=total;
							}
						}
					}
				}
			}
			tableDataItem.put("基础服务", String.valueOf(hourNum));
			tableDataItem.put("深度服务", String.valueOf(deepNum));
			tableDataItem.put("母婴到家", String.valueOf(myNum));
			int totalNum=hourNum+deepNum+myNum;
			tableDataItem.put("总单数", String.valueOf(totalNum));
			if(totalNum>0){
				tableDataItem.put("基础服务占比", String.valueOf(MathDoubleUtil.getPercent(hourNum,totalNum)));
				tableDataItem.put("深度服务占比", String.valueOf(MathDoubleUtil.getPercent(deepNum,totalNum)));
				tableDataItem.put("母婴到家占比", String.valueOf(MathDoubleUtil.getPercent(myNum,totalNum)));
			}else{
				tableDataItem.put("基础服务占比","0.00%");
				tableDataItem.put("深度服务占比","0.00%");
				tableDataItem.put("母婴到家占比", "0.00%");
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
		legendAll.add("基础服务");
		legendAll.add("基础服务营业额");
		legendAll.add("基础服务营业额占比");
		legendAll.add("深度服务");
		legendAll.add("深度服务营业额");
		legendAll.add("深度服务营业额占比");
		legendAll.add("母婴到家");
		legendAll.add("母婴到家营业额");
		legendAll.add("母婴到家营业额占比");
		
		//2. 统计图 图例
		List<String> legend = new ArrayList<String>();
		legend.add("基础服务");
		legend.add("深度服务");
		legend.add("母婴到家");
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
//		Short[] status={7,8};
//		chartSearchVo.setStatus(Arrays.asList(status));
		// 1. 查询SQL获得统计数据 -- 不同来源的订单数量
		List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();
		if (chartSearchVo.getStatType().equals("day") ) {
			chartSearchVo.setFormatParam("%Y-%m-%e");
			statDatas = orderMapper.chartTypeRevenue(chartSearchVo);
		}
		
		if (chartSearchVo.getStatType().equals("month") ) {
			chartSearchVo.setFormatParam("%Y-%m");
			statDatas = orderMapper.chartTypeRevenue(chartSearchVo);
		}	
		
		if (chartSearchVo.getStatType().equals("quarter") ) {
			statDatas = orderMapper.chartTypeRevenue(chartSearchVo);
		}		
		
		Short[] shenduserviceType={34,35,36,50,51,52,53,54,55,56,60,61};
		Short[] muyinserviceType={62,63,64,65};
		
		String str=null,str1 = null;
		
		for (Map<String, String> tableDataItem : tableDatas) {
			Integer hourNum = 0;
			Integer deepNum = 0;
			Integer myNum = 0;
			BigDecimal hourMoney = new BigDecimal(0);
			BigDecimal deepMoney = new BigDecimal(0);
			BigDecimal myMoney = new BigDecimal(0);
			for (ChartMapVo chartSqlData : statDatas) {
			//处理表格形式的数据.
				if(chartSearchVo.getStatType().equals("day")){
					String str2 =tableDataItem.get("series");
					String str3 = chartSqlData.getSeries();
					if(DateUtil.compareDateStr(str3,str2)==0){
						Integer total =0;
						if(chartSqlData.getTotal()!=null){
							total = chartSqlData.getTotal();
						}
						//0代表基础服务  1 = 深度服务 母婴到家  
						if (chartSqlData.getName().equals("0")){
							hourNum+=total;
							tableDataItem.put("基础服务", String.valueOf(hourNum));
							if(chartSqlData.getTotalMoney()!=null){
								hourMoney=hourMoney.add(chartSqlData.getTotalMoney());
							}
							tableDataItem.put("基础服务营业额", MathBigDecimalUtil.round2(hourMoney));
						}
						if(chartSqlData.getName().equals("1")){
							if(Arrays.asList(shenduserviceType).contains(chartSqlData.getServiceType())){
								deepNum+=total;
								tableDataItem.put("深度服务", String.valueOf(deepNum));
								if(chartSqlData.getTotalMoney()!=null){
									deepMoney=deepMoney.add(chartSqlData.getTotalMoney());
								}
								tableDataItem.put("深度服务营业额", MathBigDecimalUtil.round2(deepMoney));
							}
							if(Arrays.asList(muyinserviceType).contains(chartSqlData.getServiceType())){
								myNum+=total;
								tableDataItem.put("母婴到家", String.valueOf(myNum));
								
								if(chartSqlData.getTotalMoney()!=null){
									myMoney=myMoney.add(chartSqlData.getTotalMoney());
								}
								tableDataItem.put("母婴到家营业额", MathBigDecimalUtil.round2(myMoney));
							}
						}
					}
				}else{
					String str2 = tableDataItem.get("series");
					String str3 = chartSqlData.getSeries();
					if (str2.equals(str3)) {
						Integer total =0;
						if(chartSqlData.getTotal()!=null){
							total = chartSqlData.getTotal();
						}
						//0代表基础服务  1 = 深度服务 母婴到家  
						if (chartSqlData.getName().equals("0")){
							hourNum+=total;
							tableDataItem.put("基础服务", String.valueOf(hourNum));
							if(chartSqlData.getTotalMoney()!=null){
								hourMoney=hourMoney.add(chartSqlData.getTotalMoney());
							}
							tableDataItem.put("基础服务营业额", MathBigDecimalUtil.round2(hourMoney));
						}
						if(chartSqlData.getName().equals("1")){
							if(Arrays.asList(shenduserviceType).contains(chartSqlData.getServiceType())){
								deepNum+=total;
								tableDataItem.put("深度服务", String.valueOf(deepNum));
								if(chartSqlData.getTotalMoney()!=null){
									deepMoney=deepMoney.add(chartSqlData.getTotalMoney());
								}
								tableDataItem.put("深度服务营业额", MathBigDecimalUtil.round2(deepMoney));
							}
							if(Arrays.asList(muyinserviceType).contains(chartSqlData.getServiceType())){
								myNum+=total;
								tableDataItem.put("母婴到家", String.valueOf(myNum));
								
								if(chartSqlData.getTotalMoney()!=null){
									myMoney=myMoney.add(chartSqlData.getTotalMoney());
								}
								tableDataItem.put("母婴到家营业额", MathBigDecimalUtil.round2(myMoney));
							}
						}
					}
				}
			}
		}
		
		Map<String,String> totalTable = new HashMap<String,String>();
		Integer tableHourNum = 0;
		Integer tableDeepNum = 0;
		Integer tableMyNum = 0;
		Integer totalNum =0;
		BigDecimal tableHourMoney = new BigDecimal(0);
		BigDecimal tableDeepMoney = new BigDecimal(0);
		BigDecimal tableMyMoney = new BigDecimal(0);
		BigDecimal totalMoney =new BigDecimal(0);
		//5. 去掉第一个tableDataItem;
		if (tableDatas.size() > 0) tableDatas.remove(0);
		for (Map<String, String> tableDataItem : tableDatas) {
			
			tableHourMoney=tableHourMoney.add(new BigDecimal(tableDataItem.get("基础服务营业额")));
			tableDeepMoney=tableDeepMoney.add(new BigDecimal(tableDataItem.get("深度服务营业额")));
			tableMyMoney=tableMyMoney.add(new BigDecimal(tableDataItem.get("母婴到家营业额")));
			
			tableHourNum+=Integer.parseInt(tableDataItem.get("基础服务"));
			tableDeepNum+=Integer.parseInt(tableDataItem.get("深度服务"));
			tableMyNum+=Integer.parseInt(tableDataItem.get("母婴到家"));
			
		}
		totalNum = tableHourNum+tableDeepNum+tableMyNum;
		totalMoney = totalMoney.add(tableHourMoney).add(tableDeepMoney).add(tableMyMoney);
		totalTable.put("总单数", String.valueOf(totalNum));
		totalTable.put("总营业额", String.valueOf(totalMoney));
		totalTable.put("基础服务", String.valueOf(tableHourNum));
		totalTable.put("基础服务营业额", MathBigDecimalUtil.round2(tableHourMoney));
		totalTable.put("基础服务营业额占比", MathDoubleUtil.getPercent(tableHourMoney.intValue(),totalMoney.intValue()));
		totalTable.put("深度服务", String.valueOf(tableDeepNum));
		totalTable.put("深度服务营业额", MathBigDecimalUtil.round2(tableDeepMoney));
		totalTable.put("深度服务营业额占比", MathDoubleUtil.getPercent(tableDeepMoney.intValue(),totalMoney.intValue()));
		totalTable.put("母婴到家", String.valueOf(tableMyNum));
		totalTable.put("母婴到家营业额", MathBigDecimalUtil.round2(tableMyMoney));
		totalTable.put("母婴到家营业额占比",MathDoubleUtil.getPercent(tableMyMoney.intValue(),totalMoney.intValue()));
		chartDataVo.setTableMap(totalTable);
		
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
