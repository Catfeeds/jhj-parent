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
import com.jhj.service.order.OrderAmTypeChartService;
import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartSearchVo;
import com.meijia.utils.ChartUtil;
import com.meijia.utils.DateUtil;

/**
 * @description：
 * @author： kerryg
 * @date:2015年8月28日 
 */
@Controller
@RequestMapping(value = "/chart")
public class OrderAmTypeChartController extends BaseController {

	@Autowired
	private OrderAmTypeChartService orderAmTypeChartService;
	

	/**
	 * 助理品类图表统计
	 * @param chartSearchVo
	 * @param model
	 * @return
	 */
	
	@AuthPassport
	@RequestMapping(value = "chartAmType",method = RequestMethod.GET)
	public String StatChartAmType(HttpServletRequest request,ChartSearchVo chartSearchVo,Model model){
		
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
		//增加对门店的过滤
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);

		if (sessionOrgId > 0L) {
			chartSearchVo.setOrgId(sessionOrgId);
		}
		//根据statType 和 开始结束时间，统计数据
		ChartDataVo chartDatas = orderAmTypeChartService.statChartAmTypeDatas(chartSearchVo, timeSeries);
		
		model.addAttribute("chartDatas", chartDatas);
		return "chart/chartAmType";
	}
	/**
	 * 助理品类收入图表 
	 * @param chartSearchVo
	 * @param model
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "chartServiceType",method = RequestMethod.GET)
	public String StatServiceType(HttpServletRequest request,ChartSearchVo chartSearchVo,Model model){
		
		
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
		//增加对门店的过滤
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);

		if (sessionOrgId > 0L) {
			chartSearchVo.setOrgId(sessionOrgId);
		}
		//根据statType 和 开始结束时间，统计数据
		ChartDataVo chartDatas = orderAmTypeChartService.statServiceTypeDatas(chartSearchVo, timeSeries);
		
		model.addAttribute("chartDatas", chartDatas);
		return "chart/chartServiceType";
	}
}