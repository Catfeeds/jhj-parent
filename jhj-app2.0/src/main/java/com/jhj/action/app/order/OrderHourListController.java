package com.jhj.action.app.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.order.Orders;
import com.jhj.service.dict.ServiceTypeService;
import com.jhj.service.order.OrderHourListService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.meijia.utils.vo.AppResultData;
import com.jhj.vo.order.OrderHourListVo;

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
	private OrderHourListService orderHourListService;
	@Autowired
	private UserAddrsService userAddrService;
	@Autowired
	private ServiceTypeService dictServiceTypeSerivice;
	
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
		
//		List<Orders>  orderHourList = orderHourListService.selectNowOrderHourListByUserId(userId, page, Constants.PAGE_MAX_NUMBER);
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
