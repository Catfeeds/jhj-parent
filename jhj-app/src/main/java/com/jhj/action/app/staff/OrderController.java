package com.jhj.action.app.staff;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.service.bs.OrgStaffAuthService;
import com.jhj.service.bs.OrgStaffBlackService;
import com.jhj.service.bs.OrgStaffDetailDeptService;
import com.jhj.service.bs.OrgStaffDetailPayService;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffOnlineService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrderStatService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.orderReview.SettingService;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderQuerySearchVo;
import com.jhj.vo.staff.OrderBeginVo;
import com.jhj.vo.staff.StaffOrderVo;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/app/staff/order")
public class OrderController extends BaseController {

	@Autowired
	private OrgStaffsService orgStaffsService;
	
	@Autowired
	private OrderDispatchsService orderDispatchsService;

	@Autowired
	private OrgStaffAuthService orgStaffAuthService;

	@Autowired
	private OrderQueryService orderQueryService;

	@Autowired
	private OrgStaffOnlineService orgStaffOnlineService;

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private OrderPricesService orderPricesService;

	@Autowired
	private OrgStaffDetailDeptService orgStaffDetailDeptService;

	@Autowired
	private OrgStaffDetailPayService orgStaffDetailPayService;

	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;
	
	@Autowired
	private OrderLogService orderLogService;
	
	@Autowired
	private OrgStaffBlackService orgStaffBlackService;
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private OrderStatService orderStatService;

	/**
	 * 当日统计数接口
	 * 
	 * @param request
	 * @param staffId
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "total_today", method = RequestMethod.GET)
	public AppResultData<Object> totalToday(HttpServletRequest request,
			@RequestParam("user_id") Long staffId) throws ParseException {
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());

		StaffOrderVo vo = new StaffOrderVo();
		vo.setStaffId(staffId);
		// 总订单数
		vo.setTotalOrder(0L);
		// 在线小时数
		vo.setTotalOnline(0L);
		// 今日流水
		vo.setTotalOrderMoney(new BigDecimal(0L));
		// 今日收入
		vo.setTotalIncoming(new BigDecimal(0L));
		//开工标志 0=收工 1=开工(如果表中记录为空，则默认为收工状态)
		vo.setIsWork((short)1);

		
		OrderQuerySearchVo searchVo = new OrderQuerySearchVo();
		searchVo.setStaffId(staffId);
		searchVo.setOrderStatus((short) 7);
		
		Long startTime = TimeStampUtil.getBeginOfToday();
		Long endTime = TimeStampUtil.getEndOfToday();
			
		searchVo.setStartTime(startTime);
		searchVo.setEndTime(endTime);
		
	
		
		
		vo.setStaffId(staffId);
		
		// 工作小时数
		vo.setTotalOnline((long) 0);
		
		// 订单总数
		Long totalOrder = orderStatService.getTotalOrderCount(searchVo);
		vo.setTotalOrder(totalOrder);
		
		// 订单总金额
		BigDecimal totalOrderMoney = orderStatService.getTotalOrderMoney(searchVo);
		vo.setTotalOrderMoney(totalOrderMoney);
		// 订单收入总金额
		BigDecimal totalOrderIncoming = orderStatService.getTotalOrderIncomeMoney(searchVo);
		vo.setTotalIncoming(totalOrderIncoming);
		
		result.setData(vo);
		return result;
	}

	/**
	 * 订单--开始服务接口
	 * 
	 * @param request
	 * @param staffId
	 * @param orderId
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "post_begin", method = RequestMethod.POST)
	public AppResultData<Object> postBegin(HttpServletRequest request,
			@RequestParam("staff_id") Long staffId,
			@RequestParam("order_id") Long orderId) throws ParseException {
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());

		Orders orders = ordersService.selectByPrimaryKey(orderId);
		

		if (orders == null) {
			return result;
		}
		orders.setOrderStatus((short) 5);
		ordersService.updateByPrimaryKeySelective(orders);

		OrderBeginVo vo = new OrderBeginVo();
		vo.setOrderId(orderId);
		vo.setStaffId(staffId);
		vo.setOrderStatus(orders.getOrderStatus());
		//开始服务给用户发送短信
//		ordersService.userOrderPostBeginSuccessTodo(orders);
		result.setData(vo);
		return result;
	}

	@RequestMapping(value = "post_done", method = RequestMethod.POST)
	public AppResultData<Object> postDone(HttpServletRequest request,
			@RequestParam("staff_id") Long staffId,
			@RequestParam("order_id") Long orderId) throws ParseException {
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());
		Orders orders = ordersService.selectByPrimaryKey(orderId);
		OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(staffId);
		if (orders == null) {
			return result;
		}
		
		//如果为钟点工，则必须过来服务时间才能点击完成服务.
		if (orders.getOrderType().equals(Constants.ORDER_TYPE_0)) {
			Long ServiceEndTime = (long) (orders.getServiceDate() + orders.getServiceHour() * 3600);
			
			Long nowSecond = TimeStampUtil.getNowSecond();
			
			if (nowSecond < ServiceEndTime) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg("未到服务完成时间点.不能提前完成服务");
				return result;
			}
		}

		// 改变服务状态为已完成
		orders.setOrderStatus((short) 7);
		orders.setUpdateTime(TimeStampUtil.getNow()/1000);
		ordersService.updateByPrimaryKeySelective(orders);
		OrderPrices orderPrices = orderPricesService.selectByOrderId(orderId);
		
		String orderNo = orders.getOrderNo();
		//更新orderdispatchs的更新时间
		
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderNo(orderNo);
		searchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(searchVo);

		OrderDispatchs orderDispatch = null;
		if (!orderDispatchs.isEmpty()) {
			orderDispatch = orderDispatchs.get(0);
		}
		
		if (orderDispatch != null) {
			orderDispatch.setUpdateTime(TimeStampUtil.getNowSecond());
			orderDispatchsService.updateByPrimaryKeySelective(orderDispatch);
		}
		
		//更新服务人员的财务信息，包括财务总表，财务明细，欠款明细，是否加入黑名单
		orgStaffFinanceService.orderDone(orders, orderPrices, orgStaffs);
		
		
		//完成服务给用户发送短信
//        ordersService.userOrderPostDoneSuccessTodo(orders);
        
		return result;
	}
	/**
	 * 助理调整预约接口
	 * @param request
	 * @param model
	 * @param staffId
	 * @param orderId
	 * @param serviceContent
	 * @param orderMoney
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping(value = "post_am", method = RequestMethod.POST)
	public AppResultData<Object> saveUserAm(HttpServletRequest request,
		    Model model,
			@RequestParam("staff_id") Long staffId,
			@RequestParam("order_id") Long orderId,
			@RequestParam("service_content") String serviceContent,
			@RequestParam("order_money") BigDecimal orderMoney)
			throws IllegalAccessException, InvocationTargetException {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		Orders orders = ordersService.selectByPrimaryKey(orderId);

		if (orders == null) {
			result = new AppResultData<Object>(Constants.ERROR_999,
					ConstantMsg.ORDER_NOT_EXIST_MG, "");
			return result;
		}
		// 根据订单状态order_status判断是否为 2 （待确认），不是则返回“订单已确认过，不需要重复处理”
		if (orders.getOrderStatus() != 2) {
			result = new AppResultData<Object>(Constants.ERROR_999,
					ConstantMsg.ORDER_NO_NOT_CONFIRM, "");
			return result;
		}
		// 更新新订单信息, 注意更新为两张表 order表的update_time, orderPrice 做更新

		// 服务内容进行urldecode;
		try {
			serviceContent = URLDecoder.decode(serviceContent,
					Constants.URL_ENCODE);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ERROR_100_MSG);
			return result;
		}
		orders.setOrderStatus(Constants.ORDER_STATUS_3);
		orders.setRemarksConfirm(serviceContent);
		orders.setUpdateTime(TimeStampUtil.getNow() / 1000);
		ordersService.updateByPrimaryKeySelective(orders);

		OrderPrices record = orderPricesService.selectByOrderId(orders.getId());
		if (record != null) {
			record.setOrderMoney(orderMoney);
			record.setUpdateTime(TimeStampUtil.getNow() / 1000);
			orderPricesService.updateByPrimaryKeySelective(record);
		} else {
			record = orderPricesService.initOrderPrices();
			record.setOrderNo(orders.getOrderNo());
			record.setOrderId(orders.getId());
			record.setMobile(orders.getMobile());
			record.setUserId(orders.getUserId());
			record.setOrderMoney(orderMoney);
			record.setAddTime(TimeStampUtil.getNow() / 1000);
			orderPricesService.insertSelective(record);
			
		}
		// 记录订单状态到order_log表
		if (orders.getOrderNo() != null) {
			// 记录订单日志
			ordersService.userOrderAmSuccessTodo(orders.getOrderNo());
		}
	/*	OrderViewVo vo = orderQueryService.getOrderView(orders);

		result = new AppResultData<Object>(Constants.SUCCESS_0,
				ConstantMsg.SUCCESS_0_MSG, vo);*/
		return result;
	}
	/**
	 * 订单类-已取货接口
	 * @param request
	 * @param model
	 * @param staffId
	 * @param orderId
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping(value = "post_pick", method = RequestMethod.POST)
	public AppResultData<Object> postPick(HttpServletRequest request,
		    Model model,
			@RequestParam("staff_id") Long staffId,
			@RequestParam("order_id") Long orderId)
			throws IllegalAccessException, InvocationTargetException {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		Orders orders = ordersService.selectByPrimaryKey(orderId);
		if (orders == null) {
			result = new AppResultData<Object>(Constants.ERROR_999,
					ConstantMsg.ORDER_NOT_EXIST_MG, "");
			return result;
		}
		if (orders.getOrderType()!=3) {
			return result;
		}
		//更新订单表
		orders.setOrderStatus(Constants.ORDER_STATUS_6);
		orders.setUpdateTime(TimeStampUtil.getNow() / 1000);
		ordersService.updateByPrimaryKeySelective(orders);
		// 记录订单日志.
		OrderLog orderLog = orderLogService.initOrderLog(orders);
		orderLogService.insert(orderLog);
		
		return result;
	}
}
