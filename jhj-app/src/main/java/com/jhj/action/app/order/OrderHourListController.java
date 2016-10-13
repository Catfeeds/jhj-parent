package com.jhj.action.app.order;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.order.Orders;
import com.jhj.service.dict.ServiceTypeService;
import com.jhj.service.order.OrderHourListService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.meijia.utils.DateUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;
import com.jhj.vo.order.OrderHourListVo;
import com.jhj.vo.order.OrderSearchVo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月4日上午10:57:28
 * @Description: 用户版--订单列表--可查看 当前 订单、历史订单，  
 * 				
 * 				订单类型 为  钟点工、深度保洁
 *
 */
@Controller
@RequestMapping(value = "/app/order")
public class OrderHourListController extends BaseController {
	
	@Autowired
	private OrdersService orderService;
	
	@Autowired
	private OrderQueryService orderQueryService;
	
	@Autowired
	private OrderHourListService orderHourListService;
	
	@Autowired
	private UserAddrsService userAddrService;
	
	@Autowired
	private ServiceTypeService dictServiceTypeSerivice;
	
	
	// 按照年月查看卡片个数
		/**
		 * @param card_id
		 *            卡片ID, 0 表示新增
		 * @param user_id
		 *            用户ID
		 *
		 * @return CardViewVo
		 */
		@RequestMapping(value = "/user_total_by_month", method = RequestMethod.GET)
		public AppResultData<Object> totalByMonth(
			@RequestParam("user_id") Long userId, 
			@RequestParam("year") int year, 
			@RequestParam("month") int month) {
			
			AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

			String startTimeStr = DateUtil.getFirstDayOfMonth(year, month) + " 00:00:00";
			String endTimeStr = DateUtil.getLastDayOfMonth(year, month) + " 23:59:59";

			Long startTime = TimeStampUtil.getMillisOfDayFull(startTimeStr) / 1000;
			Long endTime = TimeStampUtil.getMillisOfDayFull(endTimeStr) / 1000;

			OrderSearchVo orderSearchVo = new OrderSearchVo();
			orderSearchVo.setUserId(userId);
			orderSearchVo.setStartTime(startTime);
			orderSearchVo.setEndTime(endTime);

			List<HashMap> monthDatas = orderHourListService.userTotalByMonth(orderSearchVo);
			
			result.setData(monthDatas);
			return result;
		}
		
		@RequestMapping(value = "/user_total_order", method = RequestMethod.GET)
		public AppResultData<Object> totalByOrder(
			@RequestParam("user_id") Long userId, 
			@RequestParam("year") int year, 
			@RequestParam("month") int month) {
			
			AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

			String startTimeStr = DateUtil.getFirstDayOfMonth(year, month) + " 00:00:00";
			String endTimeStr = DateUtil.getLastDayOfMonth(year, month) + " 23:59:59";

			Long startTime = TimeStampUtil.getMillisOfDayFull(startTimeStr) / 1000;
			Long endTime = TimeStampUtil.getMillisOfDayFull(endTimeStr) / 1000;

			OrderSearchVo orderSearchVo = new OrderSearchVo();
			orderSearchVo.setUserId(userId);
			orderSearchVo.setStartTime(startTime);
			orderSearchVo.setEndTime(endTime);

			List<HashMap> monthTotalDatas = orderHourListService.userAllTotalByMonth(orderSearchVo);
			
			result.setData(monthTotalDatas);
			return result;
		}
	
	/*
	 * 当前订单
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "user_order_list", method = RequestMethod.GET)
	public AppResultData<Object> userOrderList(
			@RequestParam("user_id") Long userId, 
			@RequestParam(value = "day", required = false, defaultValue = "") String day,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0,
				ConstantMsg.SUCCESS_0_MSG, new String());
		
		//用户当前订单
		OrderSearchVo orderSearchVo = new OrderSearchVo();
		orderSearchVo.setUserId(userId);
		
		if (!StringUtil.isEmpty(day)) {
			Date cal = DateUtil.parse(day);
			String calStr = DateUtil.format(cal, "yyyy-MM-dd");
			String startTimeStr = calStr + " 00:00:00";
			String endTimeStr = calStr + " 23:59:59";

			Long startTime = TimeStampUtil.getMillisOfDayFull(startTimeStr) / 1000;
			Long endTime = TimeStampUtil.getMillisOfDayFull(endTimeStr) / 1000;
			orderSearchVo.setStartTime(startTime);
			
			orderSearchVo.setEndTime(endTime);
		}
		
		
		PageInfo pageInfo = orderQueryService.selectByListPage(orderSearchVo, page,  Constants.PAGE_MAX_NUMBER);
		
		List<Orders> list = pageInfo.getList();
		List<OrderHourListVo> voList = orderHourListService.transOrderHourListVo(list);
		
		result.setData(voList);
		return result;
	}
	
	/*
	 * 当前订单
	 */
	@RequestMapping(value = "orderHourNowList", method = RequestMethod.GET)
	public AppResultData<Object> orderHourNowList(
			@RequestParam("user_id") Long userId, 
			@RequestParam(value = "page", required = false, defaultValue = "1") int page){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0,
				ConstantMsg.SUCCESS_0_MSG, new String());
		
		//用户当前订单
		List<Orders> orderHourList = orderHourListService.selectNowOrderHourListByUserId(userId, page, Constants.PAGE_MAX_NUMBER);
		
		List<OrderHourListVo> voList = orderHourListService.transOrderHourListVo(orderHourList);
		
		result.setData(voList);
		return result;
	}
	
	/*
	 * 历史订单
	 */
	@RequestMapping(value = "orderHourOldList", method = RequestMethod.GET)
	public AppResultData<Object> orderHourOldList(
			@RequestParam("user_id") Long userId, 	
			@RequestParam(value = "page", required = false, defaultValue = "1") int page){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0,
				ConstantMsg.SUCCESS_0_MSG, new String());
		
		List<Orders>  orderHourList = orderHourListService.selectOldOrderHourListByUserId(userId, page, Constants.PAGE_MAX_NUMBER);
		
//		List<Orders>  orderHourList = orderHourListService.selectOldOrderHourListByUserId(userId, page, 3);
		List<OrderHourListVo> voList = orderHourListService.transOrderHourListVo(orderHourList);
		
		result.setData(voList);
		return result;
	}
	
	
}
