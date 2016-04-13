package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jhj.po.dao.order.OrderServiceAddonsMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.order.OrderAmTypeChartService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartMapVo;
import com.jhj.vo.chart.ChartSearchVo;
import com.meijia.utils.ChartUtil;
import com.meijia.utils.MathDoubleUtil;
import com.meijia.utils.StringUtil;

/**
 * @description：
 * @author： kerryg
 * @date:2015年9月11日 
 */
@Service
public class OrderAmTypeChartServiceImpl implements OrderAmTypeChartService {
	
	@Autowired
	private OrdersMapper orderMapper;
	
	@Autowired
	private OrderServiceAddonsMapper orderServiceAddonsMapper;
	
	@Autowired
	private PartnerServiceTypeService partService;
	
	/*
	 * 统计助理品类，显示图表 
	 */
	@Override
	public ChartDataVo statChartAmTypeDatas(ChartSearchVo chartSearchVo, List<String> timeSeries) {

		
		ChartDataVo chartDataVo = new ChartDataVo();
		
		if (timeSeries.isEmpty()) return chartDataVo;

		String statType = chartSearchVo.getStatType();
		//1、table列名
		List<String> legendAll = new ArrayList<String>();
		legendAll.add("总单数");
//		legendAll.add("日常家事");
//		legendAll.add("日常家事占比");
//		legendAll.add("代办");
//		legendAll.add("代办占比");
//		legendAll.add("跑腿");
//		legendAll.add("跑腿占比");
//		legendAll.add("陪伴");
//		legendAll.add("陪伴占比");	
//		legendAll.add("其他");
//		legendAll.add("其他占比");	
		
		legendAll.add("贴心家事");
		legendAll.add("贴心家事占比");
		legendAll.add("深度养护");
		legendAll.add("深度养护占比");
		legendAll.add("企业服务");
		legendAll.add("企业服务占比");
		//2、图表legend
		List<String> legend = new ArrayList<String>();
		legend.add("总单数");
//		legend.add("日常家事");
//		legend.add("代办");
//		legend.add("跑腿");
//		legend.add("陪伴");	
//		legend.add("其他");	
		
		legend.add("贴心家事");
		legend.add("深度养护");
		legend.add("企业服务");
		
		chartDataVo.setLegend(JSON.toJSONString(legend));
		//3、x轴初始化
		List<String> xAxis = new ArrayList<String>();
		for (int i =1; i < timeSeries.size(); i++) {
			xAxis.add(ChartUtil.getTimeSeriesName(statType, timeSeries.get(i)));
		}
		chartDataVo.setxAxis(JSON.toJSONString(xAxis));


		//4、初始化表格数据格式.
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
		
		//5、查询SQL获得统计数据 -- 助理品类总数
		List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();
		
		if (chartSearchVo.getStatType().equals("day") ) {
			
			statDatas =orderMapper.statChartAmTypeByDay(chartSearchVo);
		}
		
		if (chartSearchVo.getStatType().equals("month") ) {
			statDatas = orderMapper.statChartAmTypeByMonth(chartSearchVo);
		}	
		
		if (chartSearchVo.getStatType().equals("quarter") ) {
			statDatas = orderMapper.statChartAmTypeByQuarter(chartSearchVo);
		}			
		
		
		/*
		 *   助理服务大类--贴心家事
		 */
		List<PartnerServiceType> txList = partService.selectByParentId(25L);
		
		// 贴心家事 子服务id
		List<Long> txChildIdList = new ArrayList<Long>();
		for (PartnerServiceType partnerServiceType : txList) {
			txChildIdList.add(partnerServiceType.getServiceTypeId());
		}
		
		/*
		 *  助理服务大类--深度养护
		 */
		List<PartnerServiceType> sdList = partService.selectByParentId(26L);
		
		// 深度养护子服务id
		List<Long> sdChildIdList = new ArrayList<Long>();
		for (PartnerServiceType partnerServiceType : sdList) {
			sdChildIdList.add(partnerServiceType.getServiceTypeId());
		}
		
		/*
		 * 助理服务大类--企业服务
		 */
		List<PartnerServiceType> qyList = partService.selectByParentId(27L);
		
		//企业服务子服务id
		List<Long> qyChildIdList = new ArrayList<Long>();
		for (PartnerServiceType partnerServiceType : qyList) {
			qyChildIdList.add(partnerServiceType.getServiceTypeId());
		}
		
		
		//6、循环统计数据，完成表格数据替换
		//6.1.先实现表格数据的替换. 统计助理品类订单.订单总数
			
		for (ChartMapVo chartSqlData : statDatas) {
			//处理表格形式的数据.
			for (Map<String, String> tableDataItem : tableDatas) {
				
				if (tableDataItem.get("series").toString().equals(chartSqlData.getSeries())) {
					
					/*
					 *  2016年4月12日14:42:45
					 * 		
					 * 	 新版的服务类型由  partnerServiceType的 service_type_id 决定
					 * 	
					 * 	 而 统计时，统计的是  具体服务类型所在的 大类
					 * 		如： 金牌保洁初体验-->金牌保洁	
					 * 		        安心托管-->贴心家事	
					 * 
					 *   又  此处是  助理品类，即统计  贴心家事、深度养护、企业服务
					 */
					
					long name =  Long.parseLong(chartSqlData.getName());
					
					//贴心家事 累计和
					Integer txTotal = Integer.valueOf(tableDataItem.get("贴心家事"));
					
					//深度养护累计和
					Integer sdTotal = Integer.valueOf(tableDataItem.get("深度养护"));
					
					//企业服务累计和
					Integer qyTotal = Integer.valueOf(tableDataItem.get("企业服务"));
					
					//神奇的 nullPoint
					Integer  total = chartSqlData.getTotal();
					
					// 如果是贴心家事的 子服务
					if(txChildIdList.contains(name)){
						
						if(total != null){
							txTotal += total;
						}
						
					}
					
					// 如果是深度养护的 子服务
					if(sdChildIdList.contains(name)){
						if(total != null){
							sdTotal += total;
						}
					}
					
					// 如果是企业服务的 子服务
					if(qyChildIdList.contains(name)){
						if(total != null){
							qyTotal += total;
						}
					}
					
//					if (chartSqlData.getName().equals("3"))
//						tableDataItem.put("日常家事", String.valueOf(chartSqlData.getTotal()));
//					if (chartSqlData.getName().toString().equals("4"))
//						tableDataItem.put("代办", String.valueOf(chartSqlData.getTotal()));
//					if (chartSqlData.getName().equals("5"))
//						tableDataItem.put("跑腿", String.valueOf(chartSqlData.getTotal()));
//					if (chartSqlData.getName().toString().equals("6"))
//						tableDataItem.put("陪伴", String.valueOf(chartSqlData.getTotal()));
//					if (chartSqlData.getName().equals("7"))
//						tableDataItem.put("其他", String.valueOf(chartSqlData.getTotal()));
					
					
					tableDataItem.put("贴心家事", txTotal.toString());
					tableDataItem.put("深度养护", sdTotal.toString());
					tableDataItem.put("企业服务", qyTotal.toString());
					
					//计算总单数
					Integer subTotal = Integer.valueOf(tableDataItem.get("总单数"));
					
					if(total != null){
						subTotal = subTotal + total;
					}
					
					tableDataItem.put("总单数", subTotal.toString());
				}
			}
		}
		
		//6.2. 计算助理品类占比率
		Integer tmpSubTotal = 0;
		for (Map<String, String> tableDataItem : tableDatas) {
			tmpSubTotal = Integer.valueOf(tableDataItem.get("总单数"));
			if (tmpSubTotal.equals(0)) {
				
				tableDataItem.put("贴心家事占比", "-");
				tableDataItem.put("深度养护占比", "-");
				tableDataItem.put("企业服务占比", "-");
				
			} else {
				
				Integer tiexinTotal = Integer.valueOf(tableDataItem.get("贴心家事"));
				Integer shenduTotal = Integer.valueOf(tableDataItem.get("深度养护"));
				Integer qiyeTotal = Integer.valueOf(tableDataItem.get("企业服务"));
				
				if (!tiexinTotal.equals(0)) {
					String proportion = MathDoubleUtil.getPercent(tiexinTotal, tmpSubTotal);
					tableDataItem.put("贴心家事占比", proportion);
				} else {
					tableDataItem.put("贴心家事占比", "-");
				}
				
				if (!shenduTotal.equals(0)) {
					String proportion = MathDoubleUtil.getPercent(shenduTotal, tmpSubTotal);
					tableDataItem.put("深度养护占比", proportion);
				} else {
					tableDataItem.put("深度养护占比", "-");
				}
				
				if (!qiyeTotal.equals(0)) {
					String proportion = MathDoubleUtil.getPercent(qiyeTotal, tmpSubTotal);
					tableDataItem.put("企业服务占比", proportion);
				} else {
					tableDataItem.put("企业服务占比", "-");
				}
				
				
			}
			tmpSubTotal = Integer.valueOf(tableDataItem.get("总单数"));	
		}
		
		//7. 去掉第一个tableDataItem;
		if (tableDatas.size() > 0) tableDatas.remove(0);
		
		//8. 根据表格的数据生成图表的数据.
		//初始化图表数据格式
		List<Map<String, Object>> dataItems = new ArrayList<Map<String, Object>>();
		Map<String,Object> chartDataItem = null;
		List<String> datas = null;
		for (int i =0; i < legend.size(); i++) {
			chartDataItem = new HashMap<String,Object>();
			chartDataItem.put("name", legend.get(i));
			chartDataItem.put("type", "line");
			datas = new ArrayList<String>();
			
			for (int j =1; j < timeSeries.size(); j++) {
				for (Map<String, String> tableDataItem : tableDatas) {
					if (timeSeries.get(j).equals(tableDataItem.get("series").toString())) {
						String valueStr = tableDataItem.get(legend.get(i)).toString();
						//Integer v = Integer.valueOf(valueStr);
						datas.add(valueStr);
					}
				}
			}
			chartDataItem.put("data", datas);
			dataItems.add(chartDataItem);
		}
		chartDataVo.setSeries(JSON.toJSONString(dataItems));		
		
		
		//9、最后要把tableData -》时间序列 ，比如有 6，7，8转换为 6月，7月，8月
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
	 * 统计助理品类收入，显示图表 
	 */
	@Override
	public ChartDataVo statServiceTypeDatas(ChartSearchVo chartSearchVo, List<String> timeSeries) {
		
		
		ChartDataVo chartDataVo = new ChartDataVo();
		
		if (timeSeries.isEmpty()) return chartDataVo;
		
		String statType = chartSearchVo.getStatType();
		//1、table列名初始化
		List<String> legendAll = new ArrayList<String>();
		legendAll.add("助理总营业额");
//		legendAll.add("日常家事");
//		legendAll.add("日常家事占比");
//		legendAll.add("代办");
//		legendAll.add("代办占比");
//		legendAll.add("跑腿");
//		legendAll.add("跑腿占比");
//		legendAll.add("陪伴");
//		legendAll.add("陪伴占比");
//		legendAll.add("其他");
//		legendAll.add("其他占比");
		
		
		legendAll.add("贴心家事");
		legendAll.add("贴心家事占比");
		legendAll.add("深度养护");
		legendAll.add("深度养护占比");
		legendAll.add("企业服务");
		legendAll.add("企业服务占比");
		
		//2、图表legend初始化
		List<String> legend = new ArrayList<String>();
		legend.add("助理总营业额");
//		legend.add("日常家事");
//		legend.add("代办");
//		legend.add("跑腿");
//		legend.add("陪伴");	
//		legend.add("其他");	
		
		legend.add("贴心家事");
		legend.add("深度养护");
		legend.add("企业服务");
		
		//3、x轴初始化
		chartDataVo.setLegend(JSON.toJSONString(legend));
		List<String> xAxis = new ArrayList<String>();
		for (int i =1; i < timeSeries.size(); i++) {
			xAxis.add(ChartUtil.getTimeSeriesName(statType, timeSeries.get(i)));
		}
		chartDataVo.setxAxis(JSON.toJSONString(xAxis));
		
		
		//4、初始化表格数据格式.
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
		
		//5、查询SQL获得统计数据 -- 助理品类收入总数
		List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();
		
		if (chartSearchVo.getStatType().equals("day") ) {
			statDatas =orderMapper.statChartServiceTypeByDay(chartSearchVo);
		}
		
		if (chartSearchVo.getStatType().equals("month") ) {
			statDatas = orderMapper.statServiceTypeByMonth(chartSearchVo);
		}	
		
		if (chartSearchVo.getStatType().equals("quarter") ) {
			statDatas = orderMapper.statServiceTypeByQuarter(chartSearchVo);
		}			
		
		
		
		/*
		 *   助理服务大类--贴心家事
		 */
		List<PartnerServiceType> txList = partService.selectByParentId(25L);
		
		// 贴心家事 子服务id
		List<Long> txChildIdList = new ArrayList<Long>();
		for (PartnerServiceType partnerServiceType : txList) {
			txChildIdList.add(partnerServiceType.getServiceTypeId());
		}
		
		/*
		 *  助理服务大类--深度养护
		 */
		List<PartnerServiceType> sdList = partService.selectByParentId(26L);
		
		// 深度养护子服务id
		List<Long> sdChildIdList = new ArrayList<Long>();
		for (PartnerServiceType partnerServiceType : sdList) {
			sdChildIdList.add(partnerServiceType.getServiceTypeId());
		}
		
		/*
		 * 助理服务大类--企业服务
		 */
		List<PartnerServiceType> qyList = partService.selectByParentId(27L);
		
		//企业服务子服务id
		List<Long> qyChildIdList = new ArrayList<Long>();
		for (PartnerServiceType partnerServiceType : qyList) {
			qyChildIdList.add(partnerServiceType.getServiceTypeId());
		}
		
		
		//6循环统计数据，完成表格数据替换
		//6.1.先实现表格数据的替换. 计算助理品类订单. 助理总营业额,
		for (ChartMapVo chartSqlData : statDatas) {
			//处理表格形式的数据.
			for (Map<String, String> tableDataItem : tableDatas) {
				if (tableDataItem.get("series").toString().equals(chartSqlData.getSeries())) {
					
					
					long name =  Long.parseLong(chartSqlData.getName());
					
					//贴心家事 累计和
					BigDecimal txBig = new BigDecimal(tableDataItem.get("贴心家事"));
					
					//深度养护累计和
					BigDecimal sdBig = new BigDecimal(tableDataItem.get("深度养护"));
					
					//企业服务累计和
					BigDecimal qyBig = new BigDecimal(tableDataItem.get("企业服务"));
					
					// 如果是贴心家事的 子服务
					if(txChildIdList.contains(name)){
						
						String totalMoney=String.valueOf(chartSqlData.getTotalMoney());
						//数据库字段竟然有null，真是悲催
						if(!StringUtil.isEmpty(totalMoney.toString())&&(!totalMoney.equals("null"))){
							
							txBig = txBig.add(new BigDecimal(totalMoney));
						}else {
							txBig = txBig.add(new BigDecimal(0));
						}
					}
					
					// 如果是深度养护的 子服务
					if(sdChildIdList.contains(name)){
						
						String totalMoney= String.valueOf(chartSqlData.getTotalMoney());
						//数据库字段竟然有null，真是悲催
						if(!StringUtil.isEmpty(totalMoney.toString())&&(!totalMoney.equals("null"))){
							
							sdBig = sdBig.add(new BigDecimal(totalMoney));
						}else {
							sdBig = sdBig.add(new BigDecimal(0));
						}
					}
					
					// 如果是企业服务的 子服务
					if(qyChildIdList.contains(name)){
						
						String totalMoney= String.valueOf(chartSqlData.getTotalMoney());
						//数据库字段竟然有null，真是悲催
						if(!StringUtil.isEmpty(totalMoney.toString())&&(!totalMoney.equals("null"))){
							qyBig = qyBig.add(new BigDecimal(totalMoney));
						}else {
							qyBig = qyBig.add(new BigDecimal(0));
						}
					}
					
					tableDataItem.put("贴心家事", txBig.toString());
					tableDataItem.put("深度养护", sdBig.toString());
					tableDataItem.put("企业服务", qyBig.toString());
					
					//计算助理总营业额
					BigDecimal subTotal = new BigDecimal(tableDataItem.get("助理总营业额"));
					BigDecimal serviceTypeMoney = chartSqlData.getTotalMoney();
					if(serviceTypeMoney!=null){
						subTotal = subTotal.add(serviceTypeMoney) ;
					}
					tableDataItem.put("助理总营业额", subTotal.toString());
				}   
			}
		}
		
		//6.2. 助理品类收入占比
		BigDecimal tmpSubTotals = new BigDecimal(0);
		for (Map<String, String> tableDataItem : tableDatas) {
			tmpSubTotals = new BigDecimal(tableDataItem.get("助理总营业额"));
			if (tmpSubTotals.equals(0)) {
				tableDataItem.put("贴心家事占比", "-");
				tableDataItem.put("深度养护占比", "-");
				tableDataItem.put("企业服务占比", "-");
			} else {
				Integer jiashiTotal = new BigDecimal(tableDataItem.get("贴心家事")).intValue();
				Integer daibanTotal =new BigDecimal(tableDataItem.get("深度养护")).intValue();
				Integer paotuiTotal = new BigDecimal(tableDataItem.get("企业服务")).intValue();
				Integer tmpSubTotal=  tmpSubTotals.intValue();
				if (!jiashiTotal.equals(0)) {
					String proportion = MathDoubleUtil.getPercent(jiashiTotal, tmpSubTotal);
					tableDataItem.put("贴心家事占比", proportion);
				} else {
					tableDataItem.put("贴心家事占比", "-");
				}
				if (!daibanTotal.equals(0)) {
					String proportion = MathDoubleUtil.getPercent(daibanTotal, tmpSubTotal);
					tableDataItem.put("深度养护占比", proportion);
				} else {
					tableDataItem.put("深度养护占比", "-");
				}
				if (!paotuiTotal.equals(0)) {
					String proportion = MathDoubleUtil.getPercent(paotuiTotal, tmpSubTotal);
					tableDataItem.put("企业服务占比", proportion);
				} else {
					tableDataItem.put("企业服务占比", "-");
				}
				
			}
			tableDataItem.put("助理总营业额", tmpSubTotals.toString());
		}
		
		//7. 去掉第一个tableDataItem;
		if (tableDatas.size() > 0) tableDatas.remove(0);
		
		//8. 根据表格的数据生成图表的数据.
		//初始化图表数据格式
		List<Map<String, Object>> dataItems = new ArrayList<Map<String, Object>>();
		Map<String,Object> chartDataItem = null;
		List<String> datas = null;
		for (int i =0; i < legend.size(); i++) {
			chartDataItem = new HashMap<String,Object>();
			chartDataItem.put("name", legend.get(i));
			chartDataItem.put("type", "line");
			datas = new ArrayList<String>();
			
			for (int j =1; j < timeSeries.size(); j++) {
				for (Map<String, String> tableDataItem : tableDatas) {
					if (timeSeries.get(j).equals(tableDataItem.get("series").toString())) {
						String valueStr = tableDataItem.get(legend.get(i)).toString();
						datas.add(valueStr);
					}
				}
			}
			chartDataItem.put("data", datas);
			dataItems.add(chartDataItem);
		}
		chartDataVo.setSeries(JSON.toJSONString(dataItems));		
		
		//9、最后要把tableData -》时间序列 ，比如有 6，7，8转换为 6月，7月，8月
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
