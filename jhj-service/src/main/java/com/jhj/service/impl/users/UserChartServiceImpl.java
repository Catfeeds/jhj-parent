package com.jhj.service.impl.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.dao.user.UserLoginedMapper;
import com.jhj.po.dao.user.UsersMapper;
import com.jhj.po.model.user.Users;
import com.jhj.service.users.UserChartService;
import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartMapVo;
import com.jhj.vo.chart.ChartSearchVo;
import com.meijia.utils.ChartUtil;
import com.meijia.utils.DateUtil;
import com.meijia.utils.MathDoubleUtil;
import com.meijia.utils.StringUtil;

/**
 * @description：
 * @author： kerryg
 * @date:2015年9月15日 
 */
@Service
public class UserChartServiceImpl implements UserChartService {

	@Autowired
	private UsersMapper usersMapper;

	@Autowired
	private OrdersMapper orderMapper;
	
	@Autowired
	private UserLoginedMapper userLoginedMapper;
	
	/**
	 * 市场用户图表统计
	 */
	@Override
	public ChartDataVo statUserChartDatas(ChartSearchVo chartSearchVo, List<String> timeSeries) {

		ChartDataVo chartDataVo = new ChartDataVo();

		if (timeSeries.isEmpty())
			return chartDataVo;
		
		String statType = chartSearchVo.getStatType();
		// 确认legend;
		List<String> legendAll = new ArrayList<String>();
		legendAll.add("增长率");
		legendAll.add("新增用户小计");
		legendAll.add("会员用户小计");
		legendAll.add("复购用户小计");
		legendAll.add("会员转化率");
		legendAll.add("复购率");
		legendAll.add("总人数");

		List<String> legend = new ArrayList<String>();
		legend.add("新增用户小计");
		legend.add("会员用户小计");
		legend.add("复购用户小计");
		// 设置chart的legend
		chartDataVo.setLegend(JSON.toJSONString(legend));

		List<String> xAxis = new ArrayList<String>();
		for (int i = 1; i < timeSeries.size(); i++) {
			xAxis.add(ChartUtil.getTimeSeriesName(statType, timeSeries.get(i)));
		}
		
		// 设置chart的xAxis
		chartDataVo.setxAxis(JSON.toJSONString(xAxis));

		// 初始化表格数据格式.
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
		
		// 查询SQL获得统计数据 -- 用户总数，vip用户总数
		List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();
		
		// 3. 会员转化率中添加的复购率
		List<ChartMapVo> chartMapVos  = new ArrayList<ChartMapVo>();
		
		//复购率
		List<ChartMapVo> chartMapVoRate= new ArrayList<ChartMapVo>();

		if (chartSearchVo.getStatType().equals("day")) {
			chartSearchVo.setFormatParam("%Y-%m-%e");
			statDatas = usersMapper.statByDay(chartSearchVo);
			chartMapVos = orderMapper.totalByRate(chartSearchVo);
			chartMapVoRate = orderMapper.totalByRateOrder(chartSearchVo);
		}

		if (chartSearchVo.getStatType().equals("month")) {
			chartSearchVo.setFormatParam("%Y-%m");
			statDatas = usersMapper.statByDay(chartSearchVo);
			chartMapVos = orderMapper.totalByRate(chartSearchVo);
			chartMapVoRate = orderMapper.totalByRateOrder(chartSearchVo);
		}

		if (chartSearchVo.getStatType().equals("quarter")) {
			statDatas = usersMapper.statByQuarter(chartSearchVo);
			chartMapVos = orderMapper.totalByRate(chartSearchVo);
			chartMapVoRate = orderMapper.totalByRateOrder(chartSearchVo);
		}
		
		List<ChartMapVo> totalNum  = new ArrayList<ChartMapVo>();
		ChartSearchVo vo=new ChartSearchVo();
		vo.setEndTime(chartSearchVo.getEndTime());
		if (chartSearchVo.getStatType().equals("day")) {
			vo.setFormatParam("%Y-%m-%e");
			totalNum = usersMapper.totalNum(vo);
		}

		if (chartSearchVo.getStatType().equals("month")) {
			vo.setFormatParam("%Y-%m");
			totalNum = usersMapper.totalNum(vo);
		}

		if (chartSearchVo.getStatType().equals("quarter")) {
			vo.setSelectCycle(12);
			totalNum = usersMapper.totalNum(vo);
		}
		
		
		// 循环统计数据，完成表格数据替换
		for (Map<String, String> tableDataItem : tableDatas) {
			Integer total=0;
			Integer totalVip=0;
			//处理vip用户数据
			for (ChartMapVo chartSqlData : statDatas) {
			// 处理表格形式的数据.
				String str = tableDataItem.get("series");
				String str1 = chartSqlData.getSeries();
				if(chartSearchVo.getStatType().equals("day")){
					if(DateUtil.compareDateStr(str1, str)==0){
						total = total + chartSqlData.getTotal();
						if(chartSqlData.getTotalVip()!=null){
							totalVip = totalVip + chartSqlData.getTotalVip(); 
						}
					}
				}else{
					if (str.equals(str1)) {
						// 新增订单小计
						total = total + chartSqlData.getTotal();
						
						if(chartSqlData.getTotalVip()!=null){
							totalVip = totalVip + chartSqlData.getTotalVip(); 
						}
					}
				}
			}
			
			//会员中需要添加的复购率数据
			Integer totalRate=0;
			for (ChartMapVo chartSqlData : chartMapVos) {
				String str = tableDataItem.get("series");
				String str1 = chartSqlData.getSeries();
				if(chartSearchVo.getStatType().equals("day")){
					if(DateUtil.compareDateStr(str1, str)==0){
						totalRate = totalRate + chartSqlData.getTotal();
					}
				}else{
					if (str.equals(str1)) {
						totalRate = totalRate + chartSqlData.getTotal();
					}
				}
			}
			
			//复购率
			Integer totalRateOrder=0;
			for (ChartMapVo chartSqlData : chartMapVoRate) {
				String str = tableDataItem.get("series");
				String str1 = chartSqlData.getSeries();
				if(chartSearchVo.getStatType().equals("day")){
					if(DateUtil.compareDateStr(str1, str)==0){
						totalRateOrder = totalRateOrder + chartSqlData.getTotal();
					}
				}else{
					if (str.equals(str1)) {
						totalRateOrder = totalRateOrder + chartSqlData.getTotal();
					}
				}
			}
			
			Integer num=0;
			for (ChartMapVo chartSqlData : totalNum) {
				if(chartSearchVo.getStatType().equals("day")){
					String str2 =tableDataItem.get("series");
					String str3 = chartSqlData.getSeries();
					if(DateUtil.compareDateStr(str3,str2)>=0){
						num = num + chartSqlData.getTotal();
					}
				}else{
					String[] str2 = tableDataItem.get("series").split("-");
					String[] str3 = chartSqlData.getSeries().split("-");
					if ((Integer.valueOf(str3[0])<Integer.valueOf(str2[0])) || Integer.valueOf(str3[0]).equals(Integer.valueOf(str2[0])) && Integer.valueOf(str3[1])<=Integer.valueOf(str2[1])) {
						num = num + chartSqlData.getTotal();
					}
				}
			}
			
			tableDataItem.put("复购用户小计", totalRateOrder.toString());
			tableDataItem.put("总人数", num.toString());
			
			if(total>0){
				if(chartSearchVo.getStatType().equals("quarter")){
					tableDataItem.put("复购用户小计", Integer.valueOf(Math.round(totalRateOrder/2)).toString());
					tableDataItem.put("复购率", MathDoubleUtil.getPercent(Math.round(totalRateOrder/2), num));
				}else{
					tableDataItem.put("复购用户小计", totalRateOrder.toString());
					tableDataItem.put("复购率", MathDoubleUtil.getPercent(totalRateOrder, num));
				}
			}else{
				tableDataItem.put("复购率", "0.00%");
			}
			
			totalVip = totalVip+totalRate;
			
			tableDataItem.put("新增用户小计", total.toString());
			tableDataItem.put("会员用户小计", totalVip.toString());
			if(total>0){
				tableDataItem.put("会员转化率",MathDoubleUtil.getPercent(totalVip,total) );
			}else{
				tableDataItem.put("会员转化率","0.00%" );
			}
		}
		
		// 2. 计算增长率
		Integer tmpSubTotal = 0;
		Integer subTotal = 0;
		for (Map<String, String> tableDataItem : tableDatas) {

			if (tmpSubTotal.equals(0)) {
				tmpSubTotal = Integer.valueOf(tableDataItem.get("总人数"));
				tableDataItem.put("增长率", "-");
				continue;
			}

			subTotal = Integer.valueOf(tableDataItem.get("新增用户小计"));

			if (!subTotal.equals(0)) {
//				String incrPercent = MathDoubleUtil.getRiseRate(subTotal, tmpSubTotal);
				//新增用户小计/上个月总人数
				String incrPercent = MathDoubleUtil.getPercent(subTotal, tmpSubTotal);
				tableDataItem.put("增长率", incrPercent);
			} else {
				tableDataItem.put("增长率", "-");
			}
			tmpSubTotal = Integer.valueOf(tableDataItem.get("总人数"));
		}
		
	
		// 4. 去掉第一个tableDataItem;
		if (tableDatas.size() > 0)
			tableDatas.remove(0);

		// 5. 根据表格的数据生成图表的数据.
		// 初始化图表数据格式
		List<Map<String, Object>> dataItems = new ArrayList<Map<String, Object>>();
		Map<String, Object> chartDataItem = null;
		List<String> datas = null;
		for (int i = 0; i < legend.size(); i++) {
			chartDataItem = new HashMap<String, Object>();
			chartDataItem.put("name", legend.get(i));
			chartDataItem.put("type", "bar");
			datas = new ArrayList<String>();

			for (int j = 1; j < timeSeries.size(); j++) {
				for (Map<String, String> tableDataItem : tableDatas) {
					if (timeSeries.get(j).equals(tableDataItem.get("series").toString())) {
						String valueStr = tableDataItem.get(legend.get(i)).toString();
						// Integer v = Integer.valueOf(valueStr);
						datas.add(valueStr);
					}
				}
			}
			chartDataItem.put("data", datas);
			dataItems.add(chartDataItem);
		}
		chartDataVo.setSeries(JSON.toJSONString(dataItems));

		// 最后要把tableData -》时间序列 ，比如有 6，7，8转换为 6月，7月，8月
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
	 * 统计用户数
	 */
	@Override
	public int statTotalUser(ChartSearchVo chartSearchVo) {
		int totalUser = 0;

		totalUser = usersMapper.statTotalUser(chartSearchVo);

		return totalUser;
	}

	@Override
	public ChartDataVo statUserLiveChartDatas(ChartSearchVo chartSearchVo, List<String> timeSeries) {

		ChartDataVo chartDataVo = new ChartDataVo();

		if (timeSeries.isEmpty())
			return chartDataVo;

		String statType = chartSearchVo.getStatType();
		// 确认legend;
		List<String> legendAll = new ArrayList<String>();
		legendAll.add("增长率");
		legendAll.add("微网站来源");
		legendAll.add("App来源");
		legendAll.add("新增用户小计");
		legendAll.add("当月活跃人数");
		legendAll.add("活跃总人数");
		legendAll.add("活跃度");

		List<String> legend = new ArrayList<String>();
		legend.add("微网站来源");
		legend.add("App来源");
		legend.add("新增用户小计");

		chartDataVo.setLegend(JSON.toJSONString(legend));

		List<String> xAxis = new ArrayList<String>();
		for (int i = 1; i < timeSeries.size(); i++) {
			xAxis.add(ChartUtil.getTimeSeriesName(statType, timeSeries.get(i)));
		}

		chartDataVo.setxAxis(JSON.toJSONString(xAxis));

		// 初始化表格数据格式.
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

		// 查询SQL获得统计数据 -- 用户总数
		List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();

		if (chartSearchVo.getStatType().equals("day")) {
			statDatas = usersMapper.statByDay(chartSearchVo);
		}

		if (chartSearchVo.getStatType().equals("month")) {
			statDatas = usersMapper.statByMonth(chartSearchVo);
		}

		if (chartSearchVo.getStatType().equals("quarter")) {
			statDatas = usersMapper.statByQuarter(chartSearchVo);
		}

		// 循环统计数据，完成表格数据替换
		// 1.先实现表格数据的替换. 计算App来源和微网站来源. 新增订单小计
		String str=null,str1 = null;
		for (ChartMapVo chartSqlData : statDatas) {
			// 处理表格形式的数据.
			for (Map<String, String> tableDataItem : tableDatas) {
				str = tableDataItem.get("series").split("-")[1];
				str1 = chartSqlData.getSeries().split("-")[1];
				if (Integer.parseInt(str)==Integer.parseInt(str1)) {
					// 0代表APP 1 = 微网站来源
					if (chartSqlData.getName().equals("0"))
						tableDataItem.put("App来源", String.valueOf(chartSqlData.getTotal()));
					if (chartSqlData.getName().toString().equals("1"))
						tableDataItem.put("微网站来源", String.valueOf(chartSqlData.getTotal()));

					// 新增订单小计
					Integer subTotal = Integer.valueOf(tableDataItem.get("新增用户小计"));
					subTotal = subTotal + chartSqlData.getTotal();

					tableDataItem.put("新增用户小计", subTotal.toString());
				}
			}
		}

		// 2. 计算增长率
		Integer tmpSubTotal = 0;
		Integer subTotal = 0;
		for (Map<String, String> tableDataItem : tableDatas) {

			if (tmpSubTotal.equals(0)) {
				tmpSubTotal = Integer.valueOf(tableDataItem.get("新增用户小计"));
				tableDataItem.put("增长率", "-");
				continue;
			}

			subTotal = Integer.valueOf(tableDataItem.get("新增用户小计"));

			if (!subTotal.equals(0)) {
				String incrPercent = MathDoubleUtil.getRiseRate(subTotal, tmpSubTotal);
				tableDataItem.put("增长率", incrPercent);
			} else {
				tableDataItem.put("增长率", "-");
			}
			tmpSubTotal = Integer.valueOf(tableDataItem.get("新增用户小计"));
		}
		// 3. 计算用户转换率
		// 不同时间粒度统计新用户个数
		List<ChartMapVo> statDataes = new ArrayList<ChartMapVo>();
		//按天统计
		if (chartSearchVo.getStatType().equals("day")) {
			statDataes = usersMapper.statUserIdsByDay(chartSearchVo);
		}
		//按月统计
		if (chartSearchVo.getStatType().equals("month")) {
			statDataes = usersMapper.statUserIdsByMonth(chartSearchVo);
		}
		//按季度统计
		if (chartSearchVo.getStatType().equals("quarter")) {
			statDataes = usersMapper.statUserIdsByQuarter(chartSearchVo);
		}
		List<Users> list = usersMapper.getUserIds(chartSearchVo);
		List<Long> userIds = new ArrayList<Long>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Users users = (Users) iterator.next();
			userIds.add(users.getId());
			chartSearchVo.setUserIds(userIds);
		}
		List<ChartMapVo> chartMapVos  = new ArrayList<ChartMapVo>();
		//不同时间粒度统计新下单子的用户个数 
		//按天统计
		if (chartSearchVo.getStatType().equals("day")) {
			chartMapVos = orderMapper.totalByDay(chartSearchVo);
		}
		//按月统计
		if (chartSearchVo.getStatType().equals("month")) {
			chartMapVos = orderMapper.totalByMonth(chartSearchVo);
		}
		//按季度统计
		if (chartSearchVo.getStatType().equals("quarter")) {
			chartMapVos = orderMapper.totalByQuarter(chartSearchVo);
		}
		//3.1、不同时间粒度下，新用户个数循环
		for (ChartMapVo chartSqlData : chartMapVos) {
			for (ChartMapVo statatas : statDataes) {
			//3.2、下过单子的新用户个数
				if (statatas.getSeries().equals(chartSqlData.getSeries())){
					//3.3、计算转换率
					String changeRat = MathDoubleUtil.getPercent(chartSqlData.getTotal(),statatas.getTotal());
						//3.4、循环为客户转换率赋值
						for (Map<String, String> tableDataItem : tableDatas) {
							if (tableDataItem.get("series").toString().equals(chartSqlData.getSeries())) {
								if(!StringUtil.isEmpty(changeRat) ){
									tableDataItem.put("活跃度", changeRat);
								} else {
									tableDataItem.put("活跃度", "-");
								}
							}
					}
			}
		}
		}
		
		//当月活跃总人数
		List<ChartMapVo> statDatae = new ArrayList<ChartMapVo>();
		
		if (chartSearchVo.getStatType().equals("day")) {
			statDatae = userLoginedMapper.selectUserLoginTotalByDay(chartSearchVo);
		}
		if (chartSearchVo.getStatType().equals("month")) {
			statDatae = userLoginedMapper.selectUserLoginTotalByMonth(chartSearchVo);
		}
		if (chartSearchVo.getStatType().equals("quarter")) {
			statDatae = userLoginedMapper.selectUserLoginTotalByQuarter(chartSearchVo);
		}
		for (ChartMapVo chartSqlData : statDatae) {
			//处理表格形式的数据.
			for (HashMap<String, String> tableDataItem : tableDatas) {
				if (tableDataItem.get("series").toString().equals(chartSqlData.getSeries())) {	

					tableDataItem.put("当月活跃人数", String.valueOf(chartSqlData.getTotal()));
				}
			}
		}
		
		//截止到当月活跃总人数
        List<ChartMapVo> statDataess = new ArrayList<ChartMapVo>();
		
		if (chartSearchVo.getStatType().equals("day")) {
			statDataess = userLoginedMapper.selectUserAllLoginTotalByDay(chartSearchVo);
		}
		if (chartSearchVo.getStatType().equals("month")) {
			statDataess = userLoginedMapper.selectUserAllLoginTotalByMonth(chartSearchVo);
		}
		if (chartSearchVo.getStatType().equals("quarter")) {
			statDataess = userLoginedMapper.selectUserAllLoginTotalByQuarter(chartSearchVo);
		}
		for (ChartMapVo chartSqlData : statDataess) {
			//处理表格形式的数据.
			for (HashMap<String, String> tableDataItem : tableDatas) {
				if (tableDataItem.get("series").toString().equals(chartSqlData.getSeries())) {	

					Integer allLoginTotal = Integer.valueOf(tableDataItem.get("活跃总人数"));
					allLoginTotal = allLoginTotal + chartSqlData.getTotal();
					tableDataItem.put("活跃总人数",allLoginTotal.toString());
				}
			}
		}
		
		
		//活跃度 （当月活跃总人数/截止到当月活跃总人数）
		
		String loginPercent = "0";
		for (HashMap<String, String> tableDataItem : tableDatas) {
			
			//当月活跃总人数
			Integer  loginTotal= Integer.valueOf(tableDataItem.get("当月活跃人数"));
			
			//截止到当月活跃总人数
			Integer allLoginTotal = Integer.valueOf(tableDataItem.get("活跃总人数"));

			if (allLoginTotal > 0) {
				
				loginPercent = MathDoubleUtil.getPercent(loginTotal, allLoginTotal);
				tableDataItem.put("活跃度", loginPercent);
				
			}else {
				tableDataItem.put("活跃度", "0.00%");
				
			}
		}
		
		// 4. 去掉第一个tableDataItem;
		if (tableDatas.size() > 0)
			tableDatas.remove(0);

		// 5. 根据表格的数据生成图表的数据.
		// 初始化图表数据格式
		List<Map<String, Object>> dataItems = new ArrayList<Map<String, Object>>();
		Map<String, Object> chartDataItem = null;
		List<String> datas = null;
		for (int i = 0; i < legend.size(); i++) {
			chartDataItem = new HashMap<String, Object>();
			chartDataItem.put("name", legend.get(i));
			chartDataItem.put("type", "bar");
			datas = new ArrayList<String>();

			for (int j = 1; j < timeSeries.size(); j++) {
				for (Map<String, String> tableDataItem : tableDatas) {
					if (timeSeries.get(j).equals(tableDataItem.get("series").toString())) {
						String valueStr = tableDataItem.get(legend.get(i)).toString();
						// Integer v = Integer.valueOf(valueStr);
						datas.add(valueStr);
					}
				}
			}
			chartDataItem.put("data", datas);
			dataItems.add(chartDataItem);
		}
		chartDataVo.setSeries(JSON.toJSONString(dataItems));

		// 最后要把tableData -》时间序列 ，比如有 6，7，8转换为 6月，7月，8月
		for (Map<String, String> tableDataItem : tableDatas) {
			String seriesName = tableDataItem.get("series").toString();
			seriesName = ChartUtil.getTimeSeriesName(statType, seriesName);
			tableDataItem.put("series", seriesName);
		}
		chartDataVo.setTableDatas(tableDatas);
		return chartDataVo;
	}

}
