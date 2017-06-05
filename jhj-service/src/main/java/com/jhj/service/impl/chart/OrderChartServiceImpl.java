package com.jhj.service.impl.chart;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jhj.po.dao.cooperate.CooperativeBusinessMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.model.cooperate.CooperativeBusiness;
import com.jhj.service.chart.OrderChartService;
import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartMapVo;
import com.jhj.vo.chart.ChartSearchVo;
import com.jhj.vo.dict.CooperativeBusinessSearchVo;
import com.meijia.utils.ChartUtil;
import com.meijia.utils.DateUtil;
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
	private CooperativeBusinessMapper businessMapper;
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
		legendAll.add("订单总数");
		legendAll.add("新增订单小计");
		legendAll.add("环比增长率");
		legendAll.add("微网站来源");
		legendAll.add("微网站占比");
		legendAll.add("App来源");
		legendAll.add("平台来源");
		legendAll.add("退单数");
		legendAll.add("退单率");
		
		List<String> legend = new ArrayList<String>();
		legend.add("新增订单小计");
		legend.add("微网站来源");
		legend.add("App来源");
		legend.add("平台来源");
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
		
//		Short[] status={7,8};
//		chartSearchVo.setStatus(Arrays.asList(status));
		
		//查询SQL获得统计数据 -- 订单总数
		List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();
		
		if (chartSearchVo.getStatType().equals("day") ) {
			chartSearchVo.setFormatParam("%Y-%m-%e");
			statDatas = orderMapper.statByDay(chartSearchVo);
		}
		
		if (chartSearchVo.getStatType().equals("month") ) {
			chartSearchVo.setFormatParam("%Y-%m");
			statDatas = orderMapper.statByDay(chartSearchVo);
		}	
		
		if (chartSearchVo.getStatType().equals("quarter") ) {
			statDatas = orderMapper.statByDay(chartSearchVo);
		}			

		//循环统计数据，完成表格数据替换
		//1.先实现表格数据的替换. 计算App来源和微网站来源. 新增订单小计
		String str=null,str1 = null;
		for (Map<String, String> tableDataItem : tableDatas) {
			int appNum=0;
			int webNum=0;
			int thrNum=0;
			int cancleNum=0;
			Integer subTotal =1;
			for (ChartMapVo chartSqlData : statDatas) {
			//处理表格形式的数据.
				str = tableDataItem.get("series");
				str1 = chartSqlData.getSeries();
				if(!chartSearchVo.getStatType().equals("day")){
					if (str.equals(str1)) {
						//0代表APP  1 = 微网站来源  2平台来源
						if (chartSqlData.getName().equals("0")){
							if(chartSqlData.getTotal()!=null){
								appNum+=chartSqlData.getTotal();
							}
						}
						if(chartSqlData.getName().equals("1")){
							if(chartSqlData.getTotal()!=null){
								webNum+=chartSqlData.getTotal();
							}
						}
						if(chartSqlData.getName().equals("2")){
							if(chartSqlData.getTotal()!=null){
								thrNum+=chartSqlData.getTotal();
							}
						}
						if(chartSqlData.getOrderStatus()==0){
							if(chartSqlData.getTotal()!=null){
								cancleNum+=chartSqlData.getTotal();
							}
						}
					}
				}else{
					if(DateUtil.compareDateStr(str1, str)==0){
						//0代表APP  1 = 微网站来源  2平台来源
						if (chartSqlData.getName().equals("0")){
							if(chartSqlData.getTotal()!=null){
								appNum+=chartSqlData.getTotal();
							}
						}
						if(chartSqlData.getName().equals("1")){
							if(chartSqlData.getTotal()!=null){
								webNum+=chartSqlData.getTotal();
							}
						}
						if(chartSqlData.getName().equals("2")){
							if(chartSqlData.getTotal()!=null){
								thrNum+=chartSqlData.getTotal();
							}
						}
						if(chartSqlData.getOrderStatus()==0){
							if(chartSqlData.getTotal()!=null){
								cancleNum+=chartSqlData.getTotal();
							}
						}
					}
				}
			}
			tableDataItem.put("App来源", String.valueOf(appNum));
			tableDataItem.put("微网站来源", String.valueOf(webNum));
			tableDataItem.put("平台来源", String.valueOf(thrNum));
			tableDataItem.put("退单数", String.valueOf(cancleNum));
			tableDataItem.put("新增订单小计", String.valueOf(appNum+webNum+thrNum));
			NumberFormat nf=NumberFormat.getPercentInstance(); 
			nf.setMaximumFractionDigits(2);
			
			tableDataItem.put("微网站占比", String.valueOf(nf.format((float)webNum/(float)subTotal)));
			tableDataItem.put("退单率", String.valueOf(nf.format((float)cancleNum/(float)subTotal)));
		}
		
		//查询每个月之前所有的订单总数
		List<ChartMapVo> statData = new ArrayList<ChartMapVo>();
		
		if (chartSearchVo.getStatType().equals("day") ) {
			chartSearchVo.setFormatParam("%Y-%m-%e");
			statData = orderMapper.statByTotal(chartSearchVo);
		}
		
		if (chartSearchVo.getStatType().equals("month") ) {
			chartSearchVo.setFormatParam("%Y-%m");
			statData = orderMapper.statByTotal(chartSearchVo);
		}	
		
		if (chartSearchVo.getStatType().equals("quarter") ) {
			statData = orderMapper.statByTotal(chartSearchVo);
		}			
		
		for (Map<String, String> tableDataItem : tableDatas) {
			int totalNum=0;
			String series = tableDataItem.get("series");
			String[] str2 = series.split("-");
			for (ChartMapVo chartSqlData : statData) {
				String seriesSql = chartSqlData.getSeries();
				String[] str3 = seriesSql.split("-");
				if(!chartSearchVo.getStatType().equals("day")){
					if (Integer.valueOf(str3[0])<Integer.valueOf(str2[0]) || Integer.valueOf(str3[0]).equals(Integer.valueOf(str2[0])) && Integer.valueOf(str3[1])<=Integer.valueOf(str2[1])){
						totalNum = totalNum + chartSqlData.getTotal();
					}
				}else{
					if(DateUtil.parse(series).compareTo(DateUtil.parse(seriesSql))>=0){
						totalNum = totalNum + chartSqlData.getTotal();
					}
				}
			
			}
			tableDataItem.put("订单总数", String.valueOf(totalNum));
		}
		
		
		Integer total =0;
		Integer nextTotal=0;
		for(int i=0,len=tableDatas.size();i<len;i++){
			HashMap<String, String> map = tableDatas.get(i);
			
			if(nextTotal==0){
				total = Integer.valueOf(map.get("新增订单小计"));
			}
			nextTotal = Integer.valueOf(map.get("新增订单小计"));
			
			String percent = MathDoubleUtil.getRiseRate(nextTotal,total);
			map.put("环比增长率", percent);
			total = Integer.valueOf(map.get("新增订单小计"));
			
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
		
		CooperativeBusinessSearchVo vo=new CooperativeBusinessSearchVo();
		vo.setEnable((short)1);
		List<CooperativeBusiness> businessList = businessMapper.selectCooperativeBusinessVo(vo);
		List<String> legendAll = new ArrayList<String>();
		legendAll.add("微网站");
		legendAll.add("来电订单");
		List<String> legend = new ArrayList<String>();
		legend.add("微网站");
		legend.add("来电订单");
		for(int i=0,len=businessList.size();i<len;i++){
			legendAll.add(businessList.get(i).getBusinessName());
			legendAll.add(businessList.get(i).getBusinessName()+"金额");
			legend.add(businessList.get(i).getBusinessName());
		}
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
//		Short[] status={7,8};
//		chartSearchVo.setStatus(Arrays.asList(status));
		
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
		
//		String[] deepServiceType = {"34","35","36","50","51","52","53","54","55","56","60","61","71"};
//		List<String> deepType = Arrays.asList(deepServiceType);
//		String[] myServiceType = {"62","63","64","65"};
//		List<String> hourType = Arrays.asList(myServiceType);
		//1-1. 替换表格数据   App来源和微网站来源(数量)
		String str=null,str1 = null;
		for (Map<String, String> tableDataItem : tableDatas) {
			
			for (ChartMapVo chartSqlData : statDatas) {
				BigDecimal totalMoney = chartSqlData.getTotalMoney();
				if(totalMoney==null){
					totalMoney=new BigDecimal(0);
				}
			
				if(chartSearchVo.getStatType().equals("day")){
					str = tableDataItem.get("series");
					str1 = chartSqlData.getSeries();
					if(DateUtil.compareDateStr(str1,str)==0){
						//图标数据
						if(chartSqlData.getOrderFrom().equals("1") && chartSqlData.getOrderOpFrom().equals("0")){
							tableDataItem.put("微网站", String.valueOf(chartSqlData.getTotal()));
							tableDataItem.put("微网站金额", MathBigDecimalUtil.round2(totalMoney));
						}
						if(chartSqlData.getOrderFrom().equals("2") && chartSqlData.getOrderOpFrom().equals("1")){
							tableDataItem.put("来电订单", String.valueOf(chartSqlData.getTotal()));
							tableDataItem.put("来电订单金额", MathBigDecimalUtil.round2(totalMoney));
						}
						if(chartSqlData.getOrderFrom().equals("2")){
							for(int i=0,len=businessList.size();i<len;i++){
								if(chartSqlData.getOrderOpFrom().equals(String.valueOf(businessList.get(i).getId()))){
									tableDataItem.put(businessList.get(i).getBusinessName(), String.valueOf(chartSqlData.getTotal()));
									tableDataItem.put(businessList.get(i).getBusinessName()+"金额", MathBigDecimalUtil.round2(totalMoney));
									continue;
								}
							}
						}
					}
				}else{
					String str2 = tableDataItem.get("series");
					String str3 = chartSqlData.getSeries();
					if (str2.equals(str3)) {
						//图标数据
						if(chartSqlData.getOrderFrom().equals("1") && chartSqlData.getOrderOpFrom().equals("0")){
							tableDataItem.put("微网站", String.valueOf(chartSqlData.getTotal()));
							tableDataItem.put("微网站金额", MathBigDecimalUtil.round2(totalMoney));
						}
						if(chartSqlData.getOrderFrom().equals("2") && chartSqlData.getOrderOpFrom().equals("1")){
							tableDataItem.put("来电订单", String.valueOf(chartSqlData.getTotal()));
							tableDataItem.put("来电订单金额", MathBigDecimalUtil.round2(totalMoney));
						}
						if(chartSqlData.getOrderFrom().equals("2")){
							for(int i=0,len=businessList.size();i<len;i++){
								if(chartSqlData.getOrderOpFrom().equals(String.valueOf(businessList.get(i).getId()))){
									tableDataItem.put(businessList.get(i).getBusinessName(), String.valueOf(chartSqlData.getTotal()));
									tableDataItem.put(businessList.get(i).getBusinessName()+"金额", MathBigDecimalUtil.round2(totalMoney));
									continue;
								}
							}
						}
					}
				}
			}
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
		
	//订单来源统计
	@Override
	public ChartDataVo getOrderSrc(ChartSearchVo chartSearchVo,List<String> timeSeries) {
		ChartDataVo chartDataVo = new ChartDataVo();
		
		if (timeSeries.isEmpty()) return chartDataVo;
		String statType = chartSearchVo.getStatType();
		
		CooperativeBusinessSearchVo vo=new CooperativeBusinessSearchVo();
		vo.setEnable((short)1);
		List<CooperativeBusiness> businessList = businessMapper.selectCooperativeBusinessVo(vo);
		List<String> legendAll = new ArrayList<String>();
		legendAll.add("微网站");
		legendAll.add("来电订单");
		List<String> legend = new ArrayList<String>();
		legend.add("微网站");
		legend.add("来电订单");
		for(int i=0,len=businessList.size();i<len;i++){
			legendAll.add(businessList.get(i).getBusinessName());
			legendAll.add(businessList.get(i).getBusinessName()+"金额");
			legend.add(businessList.get(i).getBusinessName());
		}
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
//		Short[] status={7,8,9};
//		chartSearchVo.setStatus(Arrays.asList(status));
		
		// 1. 查询SQL获得统计数据 -- 不同来源的订单数量
		List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();
		if (chartSearchVo.getStatType().equals("day") ) {
			chartSearchVo.setFormatParam("%Y-%m-%e");
			statDatas = orderMapper.getOrderSrc(chartSearchVo);
		}
		
		if (chartSearchVo.getStatType().equals("month") ) {
			chartSearchVo.setFormatParam("%Y-%m");
			statDatas = orderMapper.getOrderSrc(chartSearchVo);
		}	
		
		if (chartSearchVo.getStatType().equals("quarter") ) {
			statDatas = orderMapper.getOrderSrc(chartSearchVo);
		}		
		
		//1-1. 替换表格数据   App来源和微网站来源(数量)
		for (ChartMapVo chartSqlData : statDatas) {
			//处理表格形式的数据.
			for (Map<String, String> tableDataItem : tableDatas) {
				
				if(chartSearchVo.getStatType().equals("day")){
					String str2 =tableDataItem.get("series");
					String str3 = chartSqlData.getSeries();
					if(DateUtil.compareDateStr(str3,str2)==0){
						
						//0代表APP  1 = 微网站来源 2=第三方来源
						if(chartSqlData.getOrderFrom().equals("1") && chartSqlData.getOrderOpFrom().equals("0")){
							tableDataItem.put("微网站", String.valueOf(chartSqlData.getTotal()));
							tableDataItem.put("微网站金额", MathBigDecimalUtil.round2(chartSqlData.getTotalMoney()));
						}
						if(chartSqlData.getOrderFrom().equals("2") && chartSqlData.getOrderOpFrom().equals("1")){
							tableDataItem.put("来电订单", String.valueOf(chartSqlData.getTotal()));
							tableDataItem.put("来电订单金额", MathBigDecimalUtil.round2(chartSqlData.getTotalMoney()));
						}
						if(chartSqlData.getOrderFrom().equals("2")){
							for(int i=0,len=businessList.size();i<len;i++){
								if(chartSqlData.getOrderOpFrom().equals(String.valueOf(businessList.get(i).getId()))){
									tableDataItem.put(businessList.get(i).getBusinessName(), String.valueOf(chartSqlData.getTotal()));
									tableDataItem.put(businessList.get(i).getBusinessName()+"金额", MathBigDecimalUtil.round2(chartSqlData.getTotalMoney()));
									continue;
								}
							}
						}
					}
				}else{
					
					String str2 = tableDataItem.get("series");
					String str3 = chartSqlData.getSeries();
					if (str2.equals(str3)) {
						//0代表APP  1 = 微网站来源 2=第三方来源
						if(chartSqlData.getOrderFrom().equals("1") && chartSqlData.getOrderOpFrom().equals("0")){
							tableDataItem.put("微网站", String.valueOf(chartSqlData.getTotal()));
							tableDataItem.put("微网站金额", MathBigDecimalUtil.round2(chartSqlData.getTotalMoney()));
						}
						if(chartSqlData.getOrderFrom().equals("2") && chartSqlData.getOrderOpFrom().equals("1")){
							tableDataItem.put("来电订单", String.valueOf(chartSqlData.getTotal()));
							tableDataItem.put("来电订单金额", MathBigDecimalUtil.round2(chartSqlData.getTotalMoney()));
						}
						if(chartSqlData.getOrderFrom().equals("2")){
							for(int i=0,len=businessList.size();i<len;i++){
								if(chartSqlData.getOrderOpFrom().equals(String.valueOf(businessList.get(i).getId()))){
									tableDataItem.put(businessList.get(i).getBusinessName(), String.valueOf(chartSqlData.getTotal()));
									tableDataItem.put(businessList.get(i).getBusinessName()+"金额", MathBigDecimalUtil.round2(chartSqlData.getTotalMoney()));
									continue;
								}
							}
						}
					}
				}
				
			}
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
	
	
	
	@Override
	public ChartDataVo getOrderFromCount(ChartSearchVo chartSearchVo, List<String> timeSeries) {
		ChartDataVo chartDataVo = new ChartDataVo();
		
		//订单来源
		CooperativeBusinessSearchVo vo=new CooperativeBusinessSearchVo();
		vo.setEnable((short)1);
		List<CooperativeBusiness> businessList = businessMapper.selectCooperativeBusinessVo(vo);
		
		List<String> orderFromNameList = new ArrayList<>();
		
		for(int i=0;i<businessList.size();i++){
			orderFromNameList.add(businessList.get(i).getBusinessName());
		}
		orderFromNameList.add("订单数量");
		orderFromNameList.add("订单总金额");
		
		List<HashMap<String, String>> tableDatas = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> tableData = null;
		for (int i =0; i < timeSeries.size(); i++) {
			tableData = new HashMap<String, String>();
			tableData.put("time", timeSeries.get(i));
			
			for (int j =0; j < orderFromNameList.size(); j++) {
				tableData.put(orderFromNameList.get(j), "0");
			}
			tableDatas.add(tableData);
		}
		
		chartSearchVo.setFormatParam("%Y-%c-%e");;
		List<ChartMapVo> orderFrom = orderMapper.countOrderFrom(chartSearchVo);
		
		Map<String, String> countTableMap = new HashMap<String, String>();
		
		for(HashMap<String, String> tableItem:tableDatas){
			String time = tableItem.get("time");
			for(int i=0,len=businessList.size();i<len;i++){
				BigDecimal totalMoney = new BigDecimal(0);
				String businessName = businessList.get(i).getBusinessName();
				String id = String.valueOf(businessList.get(i).getId());
				
				Integer count = 0;
				Integer orderFromCount = 0;
				
				for(int j=0;j<orderFrom.size();j++){
					ChartMapVo chartMapVo = orderFrom.get(j);
					String series = chartMapVo.getSeries();
					if(time.equals(series) && chartMapVo.getOrderOpFrom().equals(id)){
						count += chartMapVo.getTotal();
						totalMoney = totalMoney.add(chartMapVo.getTotalMoney());
					}
					
					if(chartMapVo.getOrderOpFrom().equals(id)){
						orderFromCount += chartMapVo.getTotal();
					}
				}
				countTableMap.put(businessName+"数量", String.valueOf(orderFromCount));
				tableItem.put(businessName, String.valueOf(count));
				tableItem.put(businessName+"金额", String.valueOf(totalMoney));
			}
		}
		
		//计算各个来源的订单总和订单金额
		for(HashMap<String, String> tableItem:tableDatas){
			Integer count = 0;
			BigDecimal countMoney = new BigDecimal(0);
			
			for(int i=0,len=businessList.size();i<len;i++){
				String name = businessList.get(i).getBusinessName();
				String value = tableItem.get(name);
				count += Integer.valueOf(value);
				countMoney.add(new BigDecimal(tableItem.get(name+"金额")));
			}
			tableItem.put("订单数量", String.valueOf(count));
			tableItem.put("订单金额", String.valueOf(countMoney));
		}
		
		chartDataVo.setTableDatas(tableDatas);
		chartDataVo.setTableMap(countTableMap);
		
		return chartDataVo;
	}
	
	
}
