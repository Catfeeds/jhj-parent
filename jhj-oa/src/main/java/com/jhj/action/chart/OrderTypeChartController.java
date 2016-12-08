package com.jhj.action.chart;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.jhj.service.chart.ChartTypeService;
import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartSearchVo;
import com.meijia.utils.ChartUtil;
import com.meijia.utils.DateUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年9月12日下午1:49:18
 * @Description: 
 *			市场品类图表、品类收入图表		
 *
 */
@Controller
@RequestMapping(value = "/chart")
public class OrderTypeChartController extends BaseController {
	
	@Autowired
	private ChartTypeService chartTypeService;
	
	/*
	 * 市场品类图表
	 */
	@AuthPassport
	@RequestMapping(value = "chartType",method = RequestMethod.GET)
	public String chartServiceType(ChartSearchVo chartSearchVo,Model model,HttpServletRequest request){
		
		//初始化查询条件，默认搜索条件下的展示
		if (chartSearchVo == null) {
			chartSearchVo = new ChartSearchVo();
		}
		
		if (chartSearchVo.getSelectCycle() == 0)
			/*
			 * 市场品类图表，默认展示为 “按季度统计”
			 */
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
		
		
		startTimeStr = DateUtil.getLastOfYear(DateUtil.getYear());  //2015 得到  2014-12-31
		Date startTimeDate = DateUtil.parse(startTimeStr);
		startTimeStr = DateUtil.addDay(startTimeDate, -2, Calendar.MONTH, DateUtil.DEFAULT_PATTERN);  //得到2014-10-31
		startTimeDate = DateUtil.parse(startTimeStr);
		startTimeStr = DateUtil.getFirstDayOfMonth(DateUtil.getYear(startTimeDate), DateUtil.getMonth(startTimeDate)); //得到 2014-10-01
		
		
		model.addAttribute("searchVo", chartSearchVo);
		
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
		
		ChartDataVo chartDataVo = chartTypeService.chartTypeRevenueData(chartSearchVo, timeSeries);
		
		model.addAttribute("chartDatas", chartDataVo);
		
		return "chart/chartType";
	}
	
	/*
	 *  品类收入图表
	 */
	@AuthPassport
	@RequestMapping(value = "chartTypeRevenue",method = RequestMethod.GET)
	public String chartTypeRevenue(
			ChartSearchVo chartSearchVo,
            Model model,HttpServletRequest request){
		
		//初始化查询条件，默认搜索条件下的展示
		if (chartSearchVo == null) {
			chartSearchVo = new ChartSearchVo();
		}
		
		if (chartSearchVo.getSelectCycle() == 0)
			/*
			 * 市场品类图表，默认展示为 “按季度统计”
			 */
			chartSearchVo.setSelectCycle(6);
		
		//30天前.
		Date nowDate = DateUtil.parse(DateUtil.getBeginOfDay());
		String startTimeStr = DateUtil.addDay(nowDate, -30, Calendar.DATE, DateUtil.DEFAULT_PATTERN);
		String endTimeStr = DateUtil.getToday();
		if (chartSearchVo.getStartTimeStr() == null)	
			chartSearchVo.setStartTimeStr(startTimeStr);
			
		//今天
		if (chartSearchVo.getEndTimeStr() == null)
			chartSearchVo.setEndTimeStr(endTimeStr);
		
		
		startTimeStr = DateUtil.getLastOfYear(DateUtil.getYear());  //2015 得到  2014-12-31
		Date startTimeDate = DateUtil.parse(startTimeStr);
		startTimeStr = DateUtil.addDay(startTimeDate, -2, Calendar.MONTH, DateUtil.DEFAULT_PATTERN);  //得到2014-10-31
		startTimeDate = DateUtil.parse(startTimeStr);
		startTimeStr = DateUtil.getFirstDayOfMonth(DateUtil.getYear(startTimeDate), DateUtil.getMonth(startTimeDate)); //得到 2014-10-01
		
		
		model.addAttribute("searchVo", chartSearchVo);
		
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
		
		ChartDataVo chartDataVo = chartTypeService.chartTypeRevenueData(chartSearchVo, timeSeries);
		
		model.addAttribute("chartDatas", chartDataVo);
		
		return "chart/chartTypeRevenue";
	}
	
	
	
}
