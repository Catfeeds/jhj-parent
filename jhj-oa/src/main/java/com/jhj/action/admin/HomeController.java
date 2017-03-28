package com.jhj.action.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.jhj.common.Constants;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.service.chart.OrderChartService;
import com.jhj.service.users.UserChartService;
import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartSearchVo;
import com.meijia.utils.ChartUtil;
import com.meijia.utils.DateUtil;
import com.meijia.utils.TimeStampUtil;


@Controller
@RequestMapping(value = "/home")
public class HomeController extends AdminController {

	@Autowired
	private OrderChartService orderChartService;	
	
	@Autowired
	private UserChartService userChartService;
	
    @AuthPassport
    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, Model model) {
    	
    	ChartSearchVo chartSearchVo = new ChartSearchVo();
		Long startTime = 0L;
		Long endTime = 0L;
		
		//判断店长权限功能.
		Long orgId = AuthHelper.getSessionLoginOrg(request);
		
//		if (orgId != null && orgId !="0") {
//			chartSearchVo.setOrgId(Long.valueOf(orgId));
//		}
		
		if (orgId != null && orgId > 0L) {
			chartSearchVo.setOrgId(orgId);
		}	
    	//总用户数
    	int totalUser = userChartService.statTotalUser(chartSearchVo);
    	
    	//取消,未支付的，已支付的的订单数也是总得统计数
    	Short[] status={3,4,5,6,7,8};
    	chartSearchVo.setStatus(Arrays.asList(status));
    	//总订单数
    	Map<String,Integer> totalOrder = orderChartService.statTotalOrder(chartSearchVo) ; 
    	//今日新增用户数
    	String startTimeStr = DateUtil.getBeginOfDay();
    	String endTimeStr = DateUtil.getEndOfDay();
    	startTime = TimeStampUtil.getMillisOfDayFull(startTimeStr) / 1000;
		endTime = TimeStampUtil.getMillisOfDayFull(endTimeStr) / 1000;
    	
		chartSearchVo.setStartTime(startTime);
		chartSearchVo.setEndTime(endTime);
    	int totalUserToday = userChartService.statTotalUser(chartSearchVo);
    	
    	//今日订单数总数
    	Map<String,Integer> totalOrderToday = orderChartService.statTotalOrder(chartSearchVo);
    	
    	model.addAttribute("totalUser", totalUser);
    	model.addAttribute("totalUserToday", totalUserToday);
    	model.addAttribute("totalOrder", totalOrder.get("total"));
    	model.addAttribute("totalOrderToday", totalOrderToday);
    	
    	//统计最近十天的订单数
    	chartSearchVo.setSearchType(1);
    	chartSearchVo.setSelectCycle(1);
    	Date nowDate = DateUtil.parse(DateUtil.getBeginOfDay());
    	startTimeStr = DateUtil.addDay(nowDate, -10, Calendar.DATE, DateUtil.DEFAULT_PATTERN);
    	
    	String statType = ChartUtil.getStatTypeByTime(startTimeStr, endTimeStr);
		HashMap<String, Long> timeRangeMap = ChartUtil.getTimeRangeByTime(statType, startTimeStr, endTimeStr);
		startTime = Long.valueOf(timeRangeMap.get("startTime"));
		endTime = Long.valueOf(timeRangeMap.get("endTime"));
		
		List<String> timeSeries = new ArrayList<String>();		
		timeSeries = ChartUtil.getTimeSeries(statType, startTime, endTime);
		
		chartSearchVo.setStartTime(startTime);
		chartSearchVo.setEndTime(endTime);
		chartSearchVo.setStatType(statType);
		//根据statType 和 开始结束时间，统计数据
		ChartDataVo chartDatas = orderChartService.statChartDatas(chartSearchVo, timeSeries);
		
		model.addAttribute("chartDatas", chartDatas);
    	
        return "home/index";
    }
    
    @RequestMapping(value = "/success")
    public String success(HttpServletRequest request, Model model, String nextUrl) {
    	model.addAttribute("nextUrl", nextUrl);
    	return "/home/success";
    }

    @RequestMapping(value = "/notfound")
    public ModelAndView notfound() {

    	ModelAndView mv = new ModelAndView();
    	mv.setViewName("404");

    	return mv;
    }
}
