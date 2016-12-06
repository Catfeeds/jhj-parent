package com.jhj.service.impl.chart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jhj.po.dao.order.OrderCardsMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.dao.user.UsersMapper;
import com.jhj.service.chart.RestMoneyChartService;
import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartMapVo;
import com.jhj.vo.chart.ChartSearchVo;
import com.meijia.utils.ChartUtil;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.MathDoubleUtil;

@Service
public class RestMoneyChartServiceImpl implements RestMoneyChartService {

	@Autowired
	private OrdersMapper orderMapper;
	
	@Autowired
	private OrderCardsMapper orderCardsMapper;
	
	@Autowired
    private UsersMapper usersMapper;
	
	
	/**
	 * 充值卡销售图表
	 */
	@Override
	public ChartDataVo statChartDatas(ChartSearchVo chartSearchVo,
			List<String> timeSeries) {

		ChartDataVo chartDataVo = new ChartDataVo();

		if (timeSeries.isEmpty())
			return chartDataVo;

		String statType = chartSearchVo.getStatType();

		// 1. table 列名
		List<String> legendAll = new ArrayList<String>();
		legendAll.add("一千面值");
		legendAll.add("一千面值占比");
		legendAll.add("两千面值");
		legendAll.add("两千面值占比");
		legendAll.add("三千面值");
		legendAll.add("三千面值占比");
		legendAll.add("营业额小计");

		// 2. 统计图 图例
		List<String> legend = new ArrayList<String>();
		legend.add("一千面值");
		legend.add("两千面值");
		legend.add("三千面值");
		chartDataVo.setLegend(JSON.toJSONString(legend));

		// 3.x轴
		List<String> xAxis = new ArrayList<String>();
		for (int i = 1; i < timeSeries.size(); i++) {
			xAxis.add(ChartUtil.getTimeSeriesName(statType, timeSeries.get(i)));
		}
		chartDataVo.setxAxis(JSON.toJSONString(xAxis));
		
		// 4.填充table
		List<HashMap<String, String>> tableDatas = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> tableData = null;
		for (int i = 0; i < timeSeries.size(); i++) {
			tableData = new HashMap<String, String>();
			tableData.put("series", timeSeries.get(i));
			for (int j = 0; j < legendAll.size(); j++) {
				tableData.put(legendAll.get(j), "0");
			}
			tableDatas.add(tableData);
		}

		// 4-1.查询SQL获得统计数据 -- 各类面值总钱数
		List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();

		if (chartSearchVo.getStatType().equals("day")) {
			chartSearchVo.setFormatParam("%c-%e");
			statDatas = orderCardsMapper.saleCardByMonth(chartSearchVo);
		}

		if (chartSearchVo.getStatType().equals("month")) {
			chartSearchVo.setFormatParam("%c");
			statDatas = orderCardsMapper.saleCardByMonth(chartSearchVo);
		}

		if (chartSearchVo.getStatType().equals("quarter")) {
			statDatas = orderCardsMapper.saleCardByQuarter(chartSearchVo);
		}

		//4-2.真实数据填充（table表格），计算每行总计，以及各品类占比
		BigDecimal oneMoney = new BigDecimal(0);
		BigDecimal twopMoney = new BigDecimal(0);
		BigDecimal fiveMoney = new BigDecimal(0);
		
		String str=null,str1 = null;
		for (ChartMapVo chartSqlData : statDatas) {
			for (HashMap<String, String> tableDataItem : tableDatas) {
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
					// 2 = 1000面值    3 = 2000面值      4 =3000面值    
					if (chartSqlData.getName().equals("2")) {
						oneMoney =  chartSqlData.getTotalMoney();
						tableDataItem.put("一千面值", MathBigDecimalUtil.round2(oneMoney));
					}
					if (chartSqlData.getName().equals("3")) {
						twopMoney =  chartSqlData.getTotalMoney();
						tableDataItem.put("两千面值", MathBigDecimalUtil.round2(twopMoney));
					}
					if (chartSqlData.getName().equals("4")) {
						fiveMoney =  chartSqlData.getTotalMoney();
						tableDataItem.put("三千面值", MathBigDecimalUtil.round2(fiveMoney));
					}
				}
				
				BigDecimal moneySum = new BigDecimal(0);
				BigDecimal oneThousandMoney = new BigDecimal(tableDataItem.get("一千面值"));
				BigDecimal twoThousandMoney = new BigDecimal(tableDataItem.get("两千面值"));
				BigDecimal fiveThousandMoney = new BigDecimal(tableDataItem.get("三千面值"));
				//计算百分比
				moneySum = oneThousandMoney.add(twoThousandMoney).add(fiveThousandMoney);
				tableDataItem.put("营业额小计", moneySum.toString());
				if (moneySum.intValue() > 0) {
					tableDataItem.put("一千面值占比", MathDoubleUtil.getPercent(oneThousandMoney.intValue(),moneySum.intValue()));
					tableDataItem.put("两千面值占比", MathDoubleUtil.getPercent(twoThousandMoney.intValue(),moneySum.intValue()));
					tableDataItem.put("三千面值占比", MathDoubleUtil.getPercent(fiveThousandMoney.intValue(),moneySum.intValue()));
				}else {
					tableDataItem.put("一千面值占比","0.00%");
					tableDataItem.put("两千面值占比","0.00%");
					tableDataItem.put("三千面值占比","0.00%");
				}
			}
		}
		
		//5. 去掉第一个tableDataItem;
		if (tableDatas.size() > 0) tableDatas.remove(0);
		
		//6. 根据表格的数据生成图表的数据.
		//初始化图表数据格式
		List<HashMap<String, Object>> dataItems = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> chartDataItem = null;
		List<Integer> datas = null;
		for (int i =0; i < legend.size(); i++) {
			chartDataItem = new HashMap<String,Object>();
			chartDataItem.put("name", legend.get(i));
			chartDataItem.put("type", "bar");
			datas = new ArrayList<Integer>();
			
			for (int j =1; j < timeSeries.size(); j++) {
				for (HashMap<String, String> tableDataItem : tableDatas) {
					if (timeSeries.get(j).equals(tableDataItem.get("series").toString())) {
						String valueStr = tableDataItem.get(legend.get(i)).toString();
						Integer v = Double.valueOf(valueStr).intValue();
						datas.add(v);
					}
				}
			}
			chartDataItem.put("data", datas);
			dataItems.add(chartDataItem);
		}
		chartDataVo.setSeries(JSON.toJSONString(dataItems));		
		
		//最后要把tableData -》时间序列 ，比如有 6，7，8转换为 6月，7月，8月
		for (HashMap<String, String> tableDataItem : tableDatas) {
			String seriesName = tableDataItem.get("series").toString();
			seriesName = ChartUtil.getTimeSeriesName(statType, seriesName);
			tableDataItem.put("series", seriesName);
		}
		
		chartDataVo.setTableDatas(tableDatas);
		return chartDataVo;		
	}
		
	
    /**
     * 用户余额图表
     */
	@Override
	public ChartDataVo userRestMoneyDatas(ChartSearchVo chartSearchVo,
			List<String> timeSeries) {
		
		ChartDataVo chartDataVo = new ChartDataVo();

		if (timeSeries.isEmpty())
			return chartDataVo;

		String statType = chartSearchVo.getStatType();

		// 1. table 列名
		List<String> legendAll = new ArrayList<String>();
		legendAll.add("余额用户");
		legendAll.add("余额总金额");
		//legendAll.add("余额总数");
		legendAll.add("余额不足200");
		legendAll.add("余额不足200占比");
		legendAll.add("余额小于1k");
		legendAll.add("余额小于1k占比");
		legendAll.add("1K<余额<3K");
		legendAll.add("1K<余额<3K占比");
		legendAll.add("余额>3k");
		legendAll.add("余额>3k占比");

		// 2. 统计图 图例
		List<String> legend = new ArrayList<String>();
		legend.add("余额用户");
		//legend.add("余额总金额");
		legend.add("余额不足200");
		legend.add("余额小于1k");
		legend.add("1K<余额<3K");
		legend.add("余额>3k");

		chartDataVo.setLegend(JSON.toJSONString(legend));

		// 3.x轴
		List<String> xAxis = new ArrayList<String>();
		for (int i = 1; i < timeSeries.size(); i++) {
			xAxis.add(ChartUtil.getTimeSeriesName(statType, timeSeries.get(i)));
		}
		chartDataVo.setxAxis(JSON.toJSONString(xAxis));

		// 4.填充table
		List<HashMap<String, String>> tableDatas = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> tableData = null;
		for (int i = 0; i < timeSeries.size(); i++) {
			tableData = new HashMap<String, String>();
			tableData.put("series", timeSeries.get(i));

			for (int j = 0; j < legendAll.size(); j++) {
				tableData.put(legendAll.get(j), "0");
			}
			tableDatas.add(tableData);
		}

		
		/*
		 * 	关于 余额用户数、 余额总金额
		 * 	 二者是需要累加统计的 
		 * 		如   8月 表示  8月之前 的所有~~~~
		 * 		   10月 表示 10月之前的 所有~~~~
		 *  
		 *  不同于其他列展示的 当月（当前时间）对应的 数值。
		 *  
		 *  分析：用相同的 时间条件，可单独写sql,处理.
		 *  
		 */
		
		
		
		// 4-1.查询SQL获得统计数据 -- 用户余额人数count(*)
			List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();

		
		
//			//(1) 余额用户数
//			List<ChartMapVo> haveRestUserNum = new ArrayList<ChartMapVo>();
			
			if (chartSearchVo.getStatType().equals("day")) {
					statDatas = usersMapper.userResyMoneyByDay(chartSearchVo);
					
					
				}

			if (chartSearchVo.getStatType().equals("month")) {
					statDatas = usersMapper.userResyMoneyByMonth(chartSearchVo);
				}

			if (chartSearchVo.getStatType().equals("quarter")) {
					statDatas = usersMapper.userResyMoneyByQuarter(chartSearchVo);
				}
		
			//循环统计数据，完成表格数据替换
			//1.先实现表格数据的替换. 用户有余额总人数

			for (ChartMapVo chartSqlData : statDatas) {
				//处理表格形式的数据.
				for (HashMap<String, String> tableDataItem : tableDatas) {
					String str = tableDataItem.get("series").split("-")[1];
					if (Integer.parseInt(str)==Integer.parseInt(chartSqlData.getSeries())) {

						tableDataItem.put("余额用户", String.valueOf(chartSqlData.getTotal()));
					}
				}
			}
			
			//用户有余额总金额
			statDatas = new ArrayList<ChartMapVo>();

			if (chartSearchVo.getStatType().equals("day")) {
				
				
				statDatas = usersMapper.selectUserAllResyMoneyByDay(chartSearchVo);
			}

			if (chartSearchVo.getStatType().equals("month")) {
				statDatas = usersMapper.selectUserAllResyMoneyByMonth(chartSearchVo);
			}

			if (chartSearchVo.getStatType().equals("quarter")) {
				statDatas = usersMapper.selectUserAllResyMoneyByQuarter(chartSearchVo);
			}
			BigDecimal allRestMoney = new BigDecimal(0);
	
			for (ChartMapVo chartSqlData : statDatas) {
				
				for (HashMap<String, String> tableDataItem : tableDatas) {
					String str = tableDataItem.get("series").split("-")[1];
					if (Integer.parseInt(str)==Integer.parseInt(chartSqlData.getSeries())) {
					
						allRestMoney =  chartSqlData.getTotalMoney();
						tableDataItem.put("余额总金额", MathBigDecimalUtil.round2(allRestMoney));
					
				}	
				}
			}
			// 4-2.查询SQL获得统计数据 -- 余额不足200的 ，用户人数count(*)
			statDatas = new ArrayList<ChartMapVo>();

			if (chartSearchVo.getStatType().equals("day")) {
					statDatas = usersMapper.userResyMoneyLessTwoByDay(chartSearchVo);
				}

			if (chartSearchVo.getStatType().equals("month")) {
					statDatas = usersMapper.userResyMoneyLessTwoByMonth(chartSearchVo);
				}

			if (chartSearchVo.getStatType().equals("quarter")) {
					statDatas = usersMapper.userResyMoneyLessTwoByQuarter(chartSearchVo);
				}
			for (ChartMapVo chartSqlData : statDatas) {
				//处理表格形式的数据.
				for (HashMap<String, String> tableDataItem : tableDatas) {
					String str = tableDataItem.get("series").split("-")[1];
					if (Integer.parseInt(str)==Integer.parseInt(chartSqlData.getSeries())) {	

						tableDataItem.put("余额不足200", String.valueOf(chartSqlData.getTotal()));
					}
				}
			}
			// 4-3.查询SQL获得统计数据 -- 余额小于1000的 ，用户人数count(*)
						statDatas = new ArrayList<ChartMapVo>();

						if (chartSearchVo.getStatType().equals("day")) {
								statDatas = usersMapper.userResyMoneyLessThousandByDay(chartSearchVo);
							}

						if (chartSearchVo.getStatType().equals("month")) {
								statDatas = usersMapper.userResyMoneyLessThousandByMonth(chartSearchVo);
							}

						if (chartSearchVo.getStatType().equals("quarter")) {
								statDatas = usersMapper.userResyMoneyLessThousandByQuarter(chartSearchVo);
							}
						for (ChartMapVo chartSqlData : statDatas) {
							//处理表格形式的数据.
							for (HashMap<String, String> tableDataItem : tableDatas) {
								if (tableDataItem.get("series").toString().equals(chartSqlData.getSeries())) {	

									tableDataItem.put("余额小于1k", String.valueOf(chartSqlData.getTotal()));
								}
							}
						}
						// 4-4.查询SQL获得统计数据 -- 1K<余额<3K ，用户人数count(*)
						statDatas = new ArrayList<ChartMapVo>();

						if (chartSearchVo.getStatType().equals("day")) {
								statDatas = usersMapper.userResyMoneyBetweenByDay(chartSearchVo);
							}

						if (chartSearchVo.getStatType().equals("month")) {
								statDatas = usersMapper.userResyMoneyBetweenByMonth(chartSearchVo);
							}

						if (chartSearchVo.getStatType().equals("quarter")) {
								statDatas = usersMapper.userResyMoneyBetweenByQuarter(chartSearchVo);
							}
						for (ChartMapVo chartSqlData : statDatas) {
							//处理表格形式的数据.
							for (HashMap<String, String> tableDataItem : tableDatas) {
								if (tableDataItem.get("series").toString().equals(chartSqlData.getSeries())) {	

									tableDataItem.put("1K<余额<3K", String.valueOf(chartSqlData.getTotal()));
								}
							}
						}
						// 4-5.查询SQL获得统计数据 -- 余额>3k ，用户人数count(*)
						statDatas = new ArrayList<ChartMapVo>();

						if (chartSearchVo.getStatType().equals("day")) {
								statDatas = usersMapper.userResyMoneyThreeThousandByDay(chartSearchVo);
							}

						if (chartSearchVo.getStatType().equals("month")) {
								statDatas = usersMapper.userResyMoneyThreeThousandByMonth(chartSearchVo);
							}

						if (chartSearchVo.getStatType().equals("quarter")) {
								statDatas = usersMapper.userResyMoneyThreeThousandByQuarter(chartSearchVo);
							}
						for (ChartMapVo chartSqlData : statDatas) {
							//处理表格形式的数据.
							for (HashMap<String, String> tableDataItem : tableDatas) {
								String str = tableDataItem.get("series").split("-")[1];
								if (Integer.parseInt(str)==Integer.parseInt(chartSqlData.getSeries())) {	

									tableDataItem.put("余额>3k", String.valueOf(chartSqlData.getTotal()));
								}
							}
						}
						
						
						// 4-5.各种余额占比
					
						String lessTwoPercent = "0";
						String lessThousandPercent = "0";
						String lessBetweenPercent = "0";
						String MoreThousandPercent = "0";
						
						for (HashMap<String, String> tableDataItem : tableDatas) {
							Integer countTotal = Integer.valueOf(tableDataItem.get("余额用户"));
							
							//每行记录各种余额数量
							Integer lessTwo = Integer.valueOf(tableDataItem.get("余额不足200"));
							Integer lessThousand = Integer.valueOf(tableDataItem.get("余额小于1k"));
							Integer lessBetween = Integer.valueOf(tableDataItem.get("1K<余额<3K"));
							Integer MoreThousand = Integer.valueOf(tableDataItem.get("余额>3k"));
							if (countTotal > 0) {
								
								lessTwoPercent = MathDoubleUtil.getPercent(lessTwo, countTotal);
								tableDataItem.put("余额不足200占比", lessTwoPercent);
								
								lessThousandPercent = MathDoubleUtil.getPercent(lessThousand, countTotal);
								tableDataItem.put("余额小于1k占比", lessThousandPercent);
								
								lessBetweenPercent = MathDoubleUtil.getPercent(lessBetween, countTotal);
								tableDataItem.put("1K<余额<3K占比", lessBetweenPercent);
								
								MoreThousandPercent = MathDoubleUtil.getPercent(MoreThousand, countTotal);
								tableDataItem.put("余额>3k占比", MoreThousandPercent);
								
							}else {
								tableDataItem.put("余额不足200占比", "0.00%");
								tableDataItem.put("余额小于1k占比", "0.00%");
								tableDataItem.put("1K<余额<3K占比", "0.00%");
								tableDataItem.put("余额>3k占比", "0.00%");
							}
						}
						
						
						//5.去掉第一个 tableDataItem ??
						if (tableDatas.size() > 0) tableDatas.remove(0);
						//6.生成 统计图 数据
						
						List<HashMap<String, Object>> dataItems = new ArrayList<HashMap<String, Object>>();
						HashMap<String, Object> chartDataItem = null;
						List<Integer> datas = null;
						for (int i =0; i < legend.size(); i++) {
							chartDataItem = new HashMap<String,Object>();
							chartDataItem.put("name", legend.get(i));
							chartDataItem.put("type", "bar");
							datas = new ArrayList<Integer>();
							
							for (int j =1; j < timeSeries.size(); j++) {
								for (HashMap<String, String> tableDataItem : tableDatas) {
									if (timeSeries.get(j).equals(tableDataItem.get("series").toString())) {
										
										String valueStr = tableDataItem.get(legend.get(i)).toString();
										Integer v = Integer.valueOf(valueStr);
										datas.add(v);
										
										/*String valueStr = tableDataItem.get(legend.get(i)).toString();
										//Integer v = Integer.valueOf(valueStr);
										datas.add(valueStr);*/
										/*String valueStr = tableDataItem.get(legend.get(i)).toString();
										//Integer v = Integer.valueOf(valueStr);
										datas.add(valueStr);*/
									}
								}
							}
							
							chartDataItem.put("data", datas);
							dataItems.add(chartDataItem);
						}
						chartDataVo.setSeries(JSON.toJSONString(dataItems));		
						
						//x轴 刻度 转换
						for (HashMap<String, String> tableDataItem : tableDatas) {
							String seriesName = tableDataItem.get("series").toString();
							seriesName = ChartUtil.getTimeSeriesName(statType, seriesName);
							tableDataItem.put("series", seriesName);
						}
						chartDataVo.setTableDatas(tableDatas);

						return chartDataVo;		

	}

	}
