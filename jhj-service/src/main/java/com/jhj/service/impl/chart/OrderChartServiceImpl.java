package com.jhj.service.impl.chart;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
		
//		Short[] status={7,8,9};
//		chartSearchVo.setStatus(Arrays.asList(status));
		
		//查询SQL获得统计数据 -- 订单总数
		List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();
		
		if (chartSearchVo.getStatType().equals("day") ) {
			chartSearchVo.setFormatParam("%c-%e");
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
			int totalNum=0;
			for (ChartMapVo chartSqlData : statDatas) {
			//处理表格形式的数据.
				str = tableDataItem.get("series");
				str1 = chartSqlData.getSeries();
				if (str.equals(str1)) {
					//0代表APP  1 = 微网站来源  2平台来源
					if (chartSqlData.getName().equals("0")){
						if(chartSqlData.getTotal()!=null){
							appNum+=chartSqlData.getTotal();
						}
						tableDataItem.put("App来源", String.valueOf(appNum));
					}
					if(chartSqlData.getName().equals("1")){
						if(chartSqlData.getTotal()!=null){
							webNum+=chartSqlData.getTotal();
						}
						tableDataItem.put("微网站来源", String.valueOf(webNum));
					}
					if(chartSqlData.getName().equals("2")){
						if(chartSqlData.getTotal()!=null){
							thrNum+=chartSqlData.getTotal();
						}
						tableDataItem.put("平台来源", String.valueOf(thrNum));
					}
					if(chartSqlData.getOrderStatus()==0){
						if(chartSqlData.getTotal()!=null){
							cancleNum+=chartSqlData.getTotal();
						}
						tableDataItem.put("退单数", String.valueOf(cancleNum));
					}
					//新增订单小计 
					subTotal = Integer.valueOf(tableDataItem.get("新增订单小计"));
					subTotal = subTotal + chartSqlData.getTotal();
					tableDataItem.put("新增订单小计", subTotal.toString());
					NumberFormat nf=NumberFormat.getPercentInstance(); 
					nf.setMaximumFractionDigits(2);
					float s =(float)cancleNum/(float)subTotal;
					
					tableDataItem.put("退单率", String.valueOf(nf.format(s)));
				}
			}
		}
		
		//查询每个月之前所有的订单总数
		List<ChartMapVo> statData = new ArrayList<ChartMapVo>();
		
		if (chartSearchVo.getStatType().equals("day") ) {
			chartSearchVo.setFormatParam("%c-%e");
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
			String[] str2 = tableDataItem.get("series").split("-");
			for (ChartMapVo chartSqlData : statData) {
				String[] str3 = chartSqlData.getSeries().split("-");
				if (Integer.valueOf(str3[0])<Integer.valueOf(str2[0]) || Integer.valueOf(str3[0]).equals(Integer.valueOf(str2[0])) && Integer.valueOf(str3[1])<=Integer.valueOf(str2[1])){
					totalNum = totalNum + chartSqlData.getTotal();
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
		Short[] status={7,8,9};
		chartSearchVo.setStatus(Arrays.asList(status));
		
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
		
		String[] deepServiceType = {"34","35","36","50","51","52","53","54","55","56","60","61","71"};
		String[] myServiceType = {"62","63","64","65"};
		//1-1. 替换表格数据   App来源和微网站来源(数量)
		String str=null,str1 = null;
		for (Map<String, String> tableDataItem : tableDatas) {
			int jpTotal=0;
			int jcTotoal=0;
			int sdTotal=0;
			int myTotal=0;
			
			BigDecimal jpTotalMoney = new BigDecimal(0);
			BigDecimal jcTotoalMoney = new BigDecimal(0);
			BigDecimal sdTotalMoney = new BigDecimal(0);
			BigDecimal myTotalMoney = new BigDecimal(0);
			
			for (ChartMapVo chartSqlData : statDatas) {
			
				str = tableDataItem.get("series").split("-")[1];
				str1 = chartSqlData.getSeries().split("-")[1];
				
				if (Integer.parseInt(str)==Integer.parseInt(str1)) {
					
					//表格数据
					if(chartSqlData.getOrderType()==0){
						if(chartSqlData.getServiceType()==28){
							jpTotal+=chartSqlData.getTotal();
							jpTotalMoney = jpTotalMoney.add(chartSqlData.getTotalMoney());
							
						}
						if(chartSqlData.getServiceType()==68){
							jcTotoal+=chartSqlData.getTotal();
							jcTotoalMoney = jcTotoalMoney.add(chartSqlData.getTotalMoney());
							
						}
					}
					if(chartSqlData.getOrderType()==1){
						if(Arrays.asList(deepServiceType).contains(String.valueOf(chartSqlData.getServiceType()))){
							sdTotal+=chartSqlData.getTotal();
							sdTotalMoney = sdTotalMoney.add(chartSqlData.getTotalMoney());
							
						}
						if(Arrays.asList(myServiceType).contains(String.valueOf(chartSqlData.getServiceType()))){
							myTotal+=chartSqlData.getTotal();
							myTotalMoney = myTotalMoney.add(chartSqlData.getTotalMoney());
							
						}
					}
					
					//图标数据
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
			tableDataItem.put("金牌保洁订单数", String.valueOf(jpTotal));
			tableDataItem.put("金牌保洁总金额", String.valueOf(jpTotalMoney));
			tableDataItem.put("基础保洁订单数", String.valueOf(jcTotoal));
			tableDataItem.put("基础保洁总金额", String.valueOf(jcTotoalMoney));
			tableDataItem.put("深度养护订单数", String.valueOf(sdTotal));
			tableDataItem.put("深度养护总金额", String.valueOf(sdTotalMoney));
			tableDataItem.put("母婴到家订单数", String.valueOf(myTotal));
			tableDataItem.put("母婴到家总金额", String.valueOf(myTotalMoney));
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
		Short[] status={7,8,9};
		chartSearchVo.setStatus(Arrays.asList(status));
		
		// 1. 查询SQL获得统计数据 -- 不同来源的订单数量
		List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();
		if (chartSearchVo.getStatType().equals("day") ) {
			chartSearchVo.setFormatParam("%c-%e");
			statDatas = orderMapper.getOrderSrc(chartSearchVo);
		}
		
		if (chartSearchVo.getStatType().equals("month") ) {
			chartSearchVo.setFormatParam("%c");
			statDatas = orderMapper.getOrderSrc(chartSearchVo);
		}	
		
		if (chartSearchVo.getStatType().equals("quarter") ) {
			statDatas = orderMapper.getOrderSrc(chartSearchVo);
		}		
		
		//1-1. 替换表格数据   App来源和微网站来源(数量)
		String str=null,str1 = null;
		for (ChartMapVo chartSqlData : statDatas) {
			//处理表格形式的数据.
			for (Map<String, String> tableDataItem : tableDatas) {
				if(chartSearchVo.getSelectCycle()==1){
					str = tableDataItem.get("series").split("-")[1];
					str1 = chartSqlData.getSeries().split("-")[1];
				}else if(chartSearchVo.getSelectCycle()==12){
					str = tableDataItem.get("series").split("-")[1];
					str1 = chartSqlData.getSeries().split("-")[1];
				}else if(chartSearchVo.getSelectCycle()==3 ||chartSearchVo.getSelectCycle()==6){
					str = tableDataItem.get("series").split("-")[1];
					if(chartSearchVo.getSearchType()==0){
						str1 = chartSqlData.getSeries();
					}
					if(chartSearchVo.getSearchType()==1){
						str1 = chartSqlData.getSeries().split("-")[1];
					}
				}
				if (Integer.parseInt(str)==Integer.parseInt(str1)) {
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
