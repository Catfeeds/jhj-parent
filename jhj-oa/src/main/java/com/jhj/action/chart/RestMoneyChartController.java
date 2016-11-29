package com.jhj.action.chart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.meijia.utils.ChartUtil;
import com.meijia.utils.DateUtil;
import com.jhj.action.BaseController;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.service.chart.RestMoneyChartService;
import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartSearchVo;



@Controller
@RequestMapping(value = "/chart")
public class RestMoneyChartController extends BaseController{

	@Autowired
	private RestMoneyChartService restMoneyChartService;
	
	
	/**
	 * 充值卡销售图表
	 * @param chartSearchVo
	 * @param model
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/chartSaleCard", method = { RequestMethod.GET })
	public String chartSaleCard(ChartSearchVo chartSearchVo, Model model) {
		
		//初始化查询条件
		if (chartSearchVo == null) {
			chartSearchVo = new ChartSearchVo();
		}
		
		if (chartSearchVo.getSelectCycle() == 0){
			chartSearchVo.setSelectCycle(6);
		}
		
		//30天前
		Date nowDate = DateUtil.parse(DateUtil.getBeginOfDay());
		String startTimeStr = DateUtil.addDay(nowDate, -30, Calendar.DATE, DateUtil.DEFAULT_PATTERN);
		String endTimeStr = DateUtil.getBeginOfDay();
		if (chartSearchVo.getStartTimeStr()== null ) {
			chartSearchVo.setStartTimeStr(startTimeStr);
		}
		//今天
				if (chartSearchVo.getEndTimeStr() == null)
					chartSearchVo.setEndTimeStr(endTimeStr);
				
				
				startTimeStr = DateUtil.getLastOfYear(DateUtil.getYear());  //2015 得到  2014-12-31
				Date startTimeDate = DateUtil.parse(startTimeStr);
				startTimeStr = DateUtil.addDay(startTimeDate, -2, Calendar.MONTH, DateUtil.DEFAULT_PATTERN);  //得到2014-10-31
				startTimeDate = DateUtil.parse(startTimeStr);
				startTimeStr = DateUtil.getFirstDayOfMonth(DateUtil.getYear(startTimeDate), DateUtil.getMonth(startTimeDate)); //得到 2014-10-01
				
				
				model.addAttribute("searchVo", chartSearchVo);
		//开始计算统计数据
		
		//根据查询条件计算出开始和结束时间. 
		//注意，为了增长率，必须比实际要的数据往前一个维度,比如
		// 统计周期是 3个月，实际为 2015-06 2015-07 2015-08 ，为了计算06的增长率，则需要加上05月份的
				
		Long startTime = 0L;
		Long endTime = 0L;
		//根据查询条件得出统计粒度为天， 月  ，季度.
		String statType = "day";
				
		if (chartSearchVo.getSearchType() == 0) {
			
			int cycle = chartSearchVo.getSelectCycle();
			statType = ChartUtil.getStatTypeByCycle(cycle);
			HashMap<String, Long> timeRangeMap = ChartUtil.getTimeRangeByCycle(statType, cycle);
			startTime = Long.valueOf(timeRangeMap.get("startTime"));
			endTime = Long.valueOf(timeRangeMap.get("endTime"));
			
		}
		if (chartSearchVo.getSearchType() == 1) {
			startTimeStr = chartSearchVo.getStartTimeStr();
			endTimeStr = chartSearchVo.getEndTimeStr();
			statType = ChartUtil.getStatTypeByTime(startTimeStr, endTimeStr);
			HashMap<String, Long> timeRangeMap = ChartUtil.getTimeRangeByTime(statType, startTimeStr, endTimeStr);
			startTime = Long.valueOf(timeRangeMap.get("startTime"));
			endTime = Long.valueOf(timeRangeMap.get("endTime"));
		}
		
		//获得时间周期的数组列表, 7月，8月，9月
		List<String> timeSeries = new ArrayList<String>();		
		timeSeries = ChartUtil.getTimeSeries(statType, startTime, endTime);
		
		chartSearchVo.setStartTime(startTime);
		chartSearchVo.setEndTime(endTime);
		chartSearchVo.setStatType(statType);
		
		//根据statType 和 开始结束时间，统计数据
		ChartDataVo chartDatas = restMoneyChartService.statChartDatas(chartSearchVo, timeSeries);
		
		model.addAttribute("chartDatas", chartDatas);
	
		return "chart/chartSaleCard";
	}
	
	/**
	 * 用户余额图表
	 * @param chartSearchVo
	 * @param model
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/chartMoreCard", method = { RequestMethod.GET })
	public String userRestMoney(ChartSearchVo chartSearchVo, Model model) {
		
		//初始化查询条件
		if (chartSearchVo == null) {
			chartSearchVo = new ChartSearchVo();
		}
		
		if (chartSearchVo.getSelectCycle() == 0){
			chartSearchVo.setSelectCycle(6);
		}
		
		//30天前
		Date nowDate = DateUtil.parse(DateUtil.getBeginOfDay());
		String startTimeStr = DateUtil.addDay(nowDate, -30, Calendar.DATE, DateUtil.DEFAULT_PATTERN);
		String endTimeStr = DateUtil.getBeginOfDay();
		if (chartSearchVo.getStartTimeStr()== null ) {
			chartSearchVo.setStartTimeStr(startTimeStr);
		}
		//今天
		if (chartSearchVo.getEndTimeStr() == null)
			chartSearchVo.setEndTimeStr(endTimeStr);
		
		
		startTimeStr = DateUtil.getLastOfYear(DateUtil.getYear());  //2015 得到  2014-12-31
		Date startTimeDate = DateUtil.parse(startTimeStr);
		startTimeStr = DateUtil.addDay(startTimeDate, -2, Calendar.MONTH, DateUtil.DEFAULT_PATTERN);  //得到2014-10-31
		startTimeDate = DateUtil.parse(startTimeStr);
		startTimeStr = DateUtil.getFirstDayOfMonth(DateUtil.getYear(startTimeDate), DateUtil.getMonth(startTimeDate)); //得到 2014-10-01
		
		
		model.addAttribute("searchVo", chartSearchVo);
		//开始计算统计数据
		
		//根据查询条件计算出开始和结束时间. 
		//注意，为了增长率，必须比实际要的数据往前一个维度,比如
		// 统计周期是 3个月，实际为 2015-06 2015-07 2015-08 ，为了计算06的增长率，则需要加上05月份的
				
		Long startTime = 0L;
		Long endTime = 0L;
		//根据查询条件得出统计粒度为天， 月  ，季度.
		String statType = "day";
				
		if (chartSearchVo.getSearchType() == 0) {
			
			int cycle = chartSearchVo.getSelectCycle();
			statType = ChartUtil.getStatTypeByCycle(cycle);
			HashMap<String, Long> timeRangeMap = ChartUtil.getTimeRangeByCycle(statType, cycle);
			startTime = Long.valueOf(timeRangeMap.get("startTime"));
			endTime = Long.valueOf(timeRangeMap.get("endTime"));
			
		}
		if (chartSearchVo.getSearchType() == 1) {
			startTimeStr = chartSearchVo.getStartTimeStr();
			endTimeStr = chartSearchVo.getEndTimeStr();
			statType = ChartUtil.getStatTypeByTime(startTimeStr, endTimeStr);
			HashMap<String, Long> timeRangeMap = ChartUtil.getTimeRangeByTime(statType, startTimeStr, endTimeStr);
			startTime = Long.valueOf(timeRangeMap.get("startTime"));
			endTime = Long.valueOf(timeRangeMap.get("endTime"));
		}
		
		//获得时间周期的数组列表, 7月，8月，9月
		List<String> timeSeries = new ArrayList<String>();		
		timeSeries = ChartUtil.getTimeSeries(statType, startTime, endTime);
		
		chartSearchVo.setStartTime(startTime);
		chartSearchVo.setEndTime(endTime);
		chartSearchVo.setStatType(statType);
		
		//根据statType 和 开始结束时间，统计数据
		ChartDataVo chartDatas = restMoneyChartService.userRestMoneyDatas(chartSearchVo, timeSeries);
		
		model.addAttribute("chartDatas", chartDatas);
	
		return "chart/chartMoreCard";
	}
}
