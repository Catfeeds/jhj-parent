package com.jhj.action.chart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jhj.action.BaseController;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.dao.cooperate.CooperativeBusinessMapper;
import com.jhj.po.model.cooperate.CooperativeBusiness;
import com.jhj.service.chart.OrderChartService;
import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartSearchVo;
import com.jhj.vo.dict.CooperativeBusinessSearchVo;
import com.meijia.utils.ChartUtil;
import com.meijia.utils.DateUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年8月31日上午10:19:37
 * @Description: 
 *		
 *		运营平台--统计
 *					--市场订单图表
 *					--订单收入图表
 *
 *		1. 解析 搜索条件，确定 时间范围
 *
 *		2. 组装 eCharts 参数
 *		
 *		3. sql 查询赋值
 *
 */
@Controller
@RequestMapping("/chart")
public class OrderChartController extends BaseController {
	
	@Autowired
	private OrderChartService orderChartService;
	
	@Autowired
	private CooperativeBusinessMapper businessMapper;
	
	/*
	 * 市场订单图表
	 */
	@AuthPassport
	@RequestMapping(value = "chartOrder",method = RequestMethod.GET)
	public String chartOrder(HttpServletRequest request, ChartSearchVo chartSearchVo,Model model){
		
		//初始化查询条件
		if (chartSearchVo == null) {
			chartSearchVo = new ChartSearchVo();
		}
		
		if (chartSearchVo.getSelectCycle() == 0)
			chartSearchVo.setSelectCycle(3);

		//30天前.
		Date nowDate = DateUtil.parse(DateUtil.getBeginOfDay());
		String startTimeStr = DateUtil.addDay(nowDate, -30, Calendar.DATE, DateUtil.DEFAULT_PATTERN);
		String endTimeStr = DateUtil.getToday();
		if (chartSearchVo.getStartTimeStr() == null)	
			chartSearchVo.setStartTimeStr(startTimeStr);
			
			//今天
		if (chartSearchVo.getEndTimeStr() == null)
			chartSearchVo.setEndTimeStr(endTimeStr);
		
		model.addAttribute("searchVo", chartSearchVo);
		
		//开始计算统计数据
//		ChartSearchVo chartSearchVo = new ChartSearchVo();
		
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
		
		//判断店长权限功能.
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);

		if (sessionOrgId > 0L) {
			chartSearchVo.setOrgId(sessionOrgId);
		}		
		
		//根据statType 和 开始结束时间，统计数据
		ChartDataVo chartDatas = orderChartService.statChartDatas(chartSearchVo, timeSeries);
		
		model.addAttribute("chartDatas", chartDatas);
		return "chart/chartOrder";
	}
	
	/*
	 * 订单收入图表
	 */
	@AuthPassport
	@RequestMapping(value = "chartOrderRevenue",method = RequestMethod.GET)
	public String orderRevenue(ChartSearchVo chartSearchVo,Model model,HttpServletRequest request){
		
		//初始化查询条件
		if (chartSearchVo == null) {
			chartSearchVo = new ChartSearchVo();
		}
		
		if (chartSearchVo.getSelectCycle() == 0)
			//市场收入图表，默认展示季度
			chartSearchVo.setSelectCycle(12);

		//30天前.
		Date nowDate = DateUtil.parse(DateUtil.getBeginOfDay());
		String startTimeStr = DateUtil.addDay(nowDate, -30, Calendar.DATE, DateUtil.DEFAULT_PATTERN);
		String endTimeStr = DateUtil.getToday();
		if (chartSearchVo.getStartTimeStr() == null)	
			chartSearchVo.setStartTimeStr(startTimeStr);
			
		//今天
		if (chartSearchVo.getEndTimeStr() == null)
			chartSearchVo.setEndTimeStr(endTimeStr);
		
		model.addAttribute("searchVo", chartSearchVo);
		
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
			chartSearchVo.setSelectCycle(1);
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
		
		//判断店长权限功能.
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);

		if (sessionOrgId > 0L) {
			chartSearchVo.setOrgId(sessionOrgId);
		}	
		
		
		ChartDataVo chartDatas = orderChartService.getOrderRevenue(chartSearchVo, timeSeries);
		
		model.addAttribute("chartDatas", chartDatas);
		
		return "chart/orderRevenue";
	}
	
	/*
	 *订单来源统计，统计订单的来源的数量，金额
	 *
     */	
	@AuthPassport
	@RequestMapping(value = "chartOrderSrc",method = RequestMethod.GET)
	public String chartOrderSrc(HttpServletRequest request, ChartSearchVo chartSearchVo,Model model){
		
		//初始化查询条件
		if (chartSearchVo == null) {
			chartSearchVo = new ChartSearchVo();
		}
		
		if (chartSearchVo.getSelectCycle() == 0)
			chartSearchVo.setSelectCycle(3);

		//30天前.
		Date nowDate = DateUtil.parse(DateUtil.getBeginOfDay());
		String startTimeStr = DateUtil.addDay(nowDate, -30, Calendar.DATE, DateUtil.DEFAULT_PATTERN);
		String endTimeStr = DateUtil.getToday();
		if (chartSearchVo.getStartTimeStr() == null)	
			chartSearchVo.setStartTimeStr(startTimeStr);
			
			//今天
		if (chartSearchVo.getEndTimeStr() == null)
			chartSearchVo.setEndTimeStr(endTimeStr);
		
		model.addAttribute("searchVo", chartSearchVo);
		
		//开始计算统计数据
//		ChartSearchVo chartSearchVo = new ChartSearchVo();
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
		
		//判断店长权限功能.
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);

		if (sessionOrgId > 0L) {
			chartSearchVo.setOrgId(sessionOrgId);
		}		
		
		//根据statType 和 开始结束时间，统计数据
		ChartDataVo chartDatas = orderChartService.getOrderSrc(chartSearchVo, timeSeries);
		
		model.addAttribute("chartDatas", chartDatas);
		return "chart/chartOrderSrc";
	}
	
	
	/*
	 * 统计订单来源的数量，总订单数，总金额
	 * 
	 * */
	@AuthPassport
	@RequestMapping(value = "chartOrderFromCount",method = RequestMethod.GET)
	public String chartOrderFromNum(Model model, HttpServletRequest request, ChartSearchVo chartSearchVo){
		
		Long startTime = 0L;
		Long endTime = 0L;
		
		String startTimeStr = chartSearchVo.getStartTimeStr();
		if(startTimeStr!=null && !startTimeStr.equals("")){
			startTime = DateUtil.parseFull(startTimeStr+"-1 00:00:00").getTime()/1000;
			Date date = DateUtil.parse(startTimeStr+"-1");
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			String endTimeStr = DateUtil.formatDate(cal.getTime())+" 23:59:59";
			endTime = DateUtil.parse(endTimeStr).getTime()/1000;
			
		}else{
			startTime = DateUtil.curStartDate(0);
//			endTime = DateUtil.curLastDate(0);
			endTime = new Date().getTime()/1000;
		}
		chartSearchVo.setStartTime(startTime);
		chartSearchVo.setEndTime(endTime);
		
		//获得时间周期的数组列表, 7月，8月，9月
		List<String> timeSeries = new ArrayList<String>();		
		timeSeries = ChartUtil.getTimeSeries("day", startTime, endTime);
		
		ChartDataVo chartDatas = orderChartService.getOrderFromCount(chartSearchVo, timeSeries);
		
		model.addAttribute("chartDatas", chartDatas);
		model.addAttribute("searchVo", chartSearchVo);
		
		return "chart/chartOrderFromCount";
	}
	
	/*
	 * 统计订单来源的数量，总订单数，总金额
	 * 
	 * */
	@AuthPassport
	@RequestMapping(value = "chartUserOrderNum",method = RequestMethod.GET)
	public String chartUserOrderNum(Model model, HttpServletRequest request, ChartSearchVo chartSearchVo){
		
		Long startTime = 0L;
		Long endTime = 0L;
		
		String startTimeStr = chartSearchVo.getStartTimeStr();
		if(startTimeStr!=null && !startTimeStr.equals("")){
			startTime = DateUtil.parseFull(startTimeStr+"-1 00:00:00").getTime()/1000;
			Date date = DateUtil.parse(startTimeStr+"-1");
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			String endTimeStr = DateUtil.formatDate(cal.getTime())+" 23:59:59";
			endTime = DateUtil.parse(endTimeStr).getTime()/1000;
			
		}else{
			startTime = DateUtil.curStartDate(0);
//			endTime = DateUtil.curLastDate(0);
			endTime = new Date().getTime()/1000;
		}
		chartSearchVo.setStartTime(startTime);
		chartSearchVo.setEndTime(endTime);
		
		//获得时间周期的数组列表, 7月，8月，9月
		List<String> timeSeries = new ArrayList<String>();		
		timeSeries = ChartUtil.getTimeSeries("day", startTime, endTime);
		
		ChartDataVo chartDatas = orderChartService.getOrderFromCount(chartSearchVo, timeSeries);
		
		model.addAttribute("chartDatas", chartDatas);
		model.addAttribute("searchVo", chartSearchVo);
		
		return "chart/chartUserOrderCount";
	}
	
}
