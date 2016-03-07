package com.jhj.action.order;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.order.Orders;
import com.jhj.service.order.OaRemindOrderService;
import com.jhj.vo.OaRemindOrderSearchVo;
import com.jhj.vo.order.OaRemindOrderVo;
import com.meijia.utils.DateUtil;
import com.meijia.utils.StringUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年10月11日上午10:19:49
 * @Description: 
 *		运营平台--订单管理--提醒订单
 */
@Controller
@RequestMapping(value = "/order")
public class OaRemindOrderController extends BaseController {
	
	@Autowired
	private OaRemindOrderService remindService;
	
	/*
	 * 列表展示 提醒类 订单
	 */
	@AuthPassport
	@RequestMapping(value = "/remind-order-list", method = RequestMethod.GET)
	public String getRemindOrderList(HttpServletRequest request,Model model
//			,@RequestParam(value = "startTimeStr", required = false, defaultValue = "") String startTime
//			,@RequestParam(value = "endTimeStr",required = false,defaultValue ="") String endTime
			,OaRemindOrderSearchVo oaRemindOrderSearchVo) throws ParseException{
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		if(oaRemindOrderSearchVo == null){
			oaRemindOrderSearchVo = new OaRemindOrderSearchVo();
		}
		
//		String selectDay = oaRemindOrderSearchVo.getTime();
//		if(!StringUtil.isEmpty(selectDay)){
//			//某天的开始时间
//			String beginOfDay = DateUtil.getBeginOfDay(selectDay);
//			
//			
//			oaRemindOrderSearchVo.setStartTime(DateUtil.getUnixTimeStamp(beginOfDay));
//			
//			//某天的结束时间
//			String endOfDay = DateUtil.getEndOfDay(selectDay);
//			
//			oaRemindOrderSearchVo.setEndTime(DateUtil.getUnixTimeStamp(endOfDay));
//			
//		}
		
		//在提醒订单，点击搜索
		String startTimeStr = oaRemindOrderSearchVo.getStartTimeStr();
		if(!StringUtil.isEmpty(startTimeStr)){
			oaRemindOrderSearchVo.setStartTime(DateUtil.getUnixTimeStamp(DateUtil.getBeginOfDay(startTimeStr)));
		}
		
		String endTimeStr = oaRemindOrderSearchVo.getEndTimeStr();
		if(!StringUtil.isEmpty(endTimeStr)){
			oaRemindOrderSearchVo.setEndTime(DateUtil.getUnixTimeStamp(DateUtil.getEndOfDay(endTimeStr)));
		}
		
		//从统计图表跳转过来,设置 回显值
		String startTimeChart = request.getParameter("startTime");
		if(!StringUtil.isEmpty(startTimeChart)){
			oaRemindOrderSearchVo.setStartTimeStr(DateUtil.timeStamp2Date(startTimeChart, "yyyy-MM-dd"));
		}
		
		String endTimeChart = request.getParameter("endTime");
		if(!StringUtil.isEmpty(endTimeChart)){
			oaRemindOrderSearchVo.setEndTimeStr(DateUtil.timeStamp2Date(endTimeChart, "yyyy-MM-dd"));
		}
		
		List<Orders> orderList = remindService.getRemindOrderList(pageNo, pageSize, oaRemindOrderSearchVo);
		
		Orders orders = null;
		for (int i = 0; i < orderList.size(); i++) {
			orders = orderList.get(i);
			OaRemindOrderVo vo = remindService.transVo(orders);
			orderList.set(i, vo);
		}
		
		PageInfo result = new PageInfo(orderList);	
		
		model.addAttribute("oaRemindOrderListVoModel", result);
		model.addAttribute("oaRemindOrderSearchVoModel", oaRemindOrderSearchVo);
		
		return "order/remindOrderList";
		
	}
	
}
