package com.jhj.action.app.staff;

import java.util.ArrayList;
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
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrdersService;
import com.jhj.vo.order.OrderDetailVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderListVo;
import com.jhj.vo.order.OrderSearchVo;
import com.meijia.utils.DateUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;


@Controller
@RequestMapping(value = "/app/staff/order")
public class OrderQuerysController extends BaseController {
	@Autowired
	private OrdersService ordersService;

	@Autowired
    private OrgStaffsService orgStaffsService;
	
	@Autowired
	private OrderQueryService orderQueryService;
	
	@Autowired
	private OrderDispatchsService orderDispatchsService;
	

	/**
	 * 订单列表接口
	 * @param staffId
	 * @param orderForm  0 = 待服务 1 = 服务中  2 = 本月已完成订单.
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "get_list", method = RequestMethod.GET)
	public AppResultData<Object> list(
			@RequestParam("user_id") Long staffId,
			@RequestParam("order_from") Short orderFrom,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		
		List<OrderListVo> orderListVo = new ArrayList<OrderListVo>();
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(staffId);
		
		if (orgStaffs == null) {
			return result;
		}
		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setStaffId(staffId);
		
		List<Short> statusList = new ArrayList<Short>();
		if (orderFrom.equals((short)0)) {
			statusList.add(Constants.ORDER_HOUR_STATUS_3);
		}
		
		if (orderFrom.equals((short)1)) {
			statusList.add(Constants.ORDER_HOUR_STATUS_5);
		}
		
		if (orderFrom.equals((short)2)) {
			statusList.add(Constants.ORDER_HOUR_STATUS_7);
			
			int year = DateUtil.getYear();
			int month = DateUtil.getMonth();
			searchVo.setStartServiceTime(TimeStampUtil.getBeginOfMonth(year, month));
			searchVo.setEndServiceTime(TimeStampUtil.getEndOfMonth(year, month));
		}
		
		

		PageInfo list = orderQueryService.selectByListPage(searchVo, page, Constants.PAGE_MAX_NUMBER);
		//PageInfo list = orderDispatchsService.selectByListVoPage();
		List<Orders> orderList = list.getList();
		for (Orders item : orderList) {
			
			OrderListVo vo = new OrderListVo(); 
			vo = orderQueryService.getOrderListVo(item);
			
			//如果是未接单，则设置服务地址为 ******
			OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
			searchVo1.setOrderNo(vo.getOrderNo());
			searchVo1.setDispatchStatus((short) 1);
			List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(searchVo1);

			OrderDispatchs orderDispatch = null;
			if (!orderDispatchs.isEmpty()) {
				orderDispatch = orderDispatchs.get(0);
			}

			if (orderDispatch != null) {
				Short isApply = orderDispatch.getIsApply();
				if (isApply.equals((short)0)) {
					vo.setServiceAddrDistance("**米");
					vo.setServiceAddr("******");
				}
			}
			
			
			
			orderListVo.add(vo);
		}
		result.setData(orderListVo);
		
		return result;

	}
		
	// 19.订单详情接口
	/**
	 * mobile:手机号 order_id订单ID
	 */
	@RequestMapping(value = "get_detail", method = RequestMethod.GET)
	public AppResultData<Object> detail(
			@RequestParam("staff_id") Long staffId, 
			@RequestParam("order_id") Long orderId) {
		
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		Orders order = ordersService.selectByPrimaryKey(orderId);
		
		if (order == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ORDER_NO_NOT_EXIST_MG);			
			return result;
		}
		
		OrderDetailVo vo = orderQueryService.getOrderDetailVo(order);
		
		result.setData(vo);
		
		//设置接单状态
		OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
		searchVo1.setOrderNo(order.getOrderNo());
		searchVo1.setDispatchStatus((short) 1);
		searchVo1.setStaffId(staffId);
		List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(searchVo1);

		OrderDispatchs orderDispatch = null;
		if (!orderDispatchs.isEmpty()) {
			orderDispatch = orderDispatchs.get(0);
		}
		
		//如果是未接单，则设置服务地址为 ******
		Short isApply = orderDispatch.getIsApply();
		if (isApply.equals((short)0)) {
			orderDispatch.setIsApply((short) 1);
			orderDispatch.setApplyTime(TimeStampUtil.getNowSecond());
			orderDispatchsService.updateByPrimaryKey(orderDispatch);
		}
		return result;
	}	
	
}
