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
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderAmHourService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrdersService;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;
import com.jhj.vo.order.OrderAmHourViewVo;
import com.jhj.vo.order.OrderDispatchSearchVo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月7日上午10:55:13
 * @Description:
 *               助理端--保洁类 接口
 *
 */
@Controller
@RequestMapping(value = "app/order")
public class OrderAmHourController extends BaseController {

	@Autowired
	private OrdersService orderService;
	@Autowired
	private OrgStaffsService orgStaffService;
	@Autowired
	private OrderAmHourService orderAmHourService;
	@Autowired
	private OrderDispatchsService orderDispatchsService;

	/*
	 * 助理端 -- 保洁类 订单详情接口
	 */
	@RequestMapping(value = "am_Order_Hour_Detail", method = RequestMethod.POST)
	public AppResultData<Object> amOrderHourDetail(@RequestParam("order_no") String orderNo, @RequestParam("am_id") Long amId) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		OrgStaffs orderStaffs = orgStaffService.selectByPrimaryKey(amId);
		// 判断是否为注册助理，非注册助理返回 999
		if (orderStaffs == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}
		Orders orders = orderService.selectByOrderNo(orderNo);

		// if (orders == null) {
		// result.setStatus(Constants.ERROR_999);
		// result.setMsg(ConstantMsg.ORDER_NO_NOT_EXIST_MG);
		// return result;
		// }

		OrderAmHourViewVo orderAmHourView = orderAmHourService.getOrderAmHourView(orderNo, amId);

		result.setData(orderAmHourView);
		return result;
	}

	/*
	 * 助理端-- 保洁类订单调整接口，换人
	 */
	@RequestMapping(value = "am_update_order_hour", method = RequestMethod.POST)
	public AppResultData<Object> amUpdateOrder(@RequestParam("orderNo") String orderNo, @RequestParam("staffId") String staffId) { // Long
																																	// 竟然接收不到
																																	// staffId
																																	// !!!

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderNo(orderNo);
		searchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(searchVo);

		OrderDispatchs orderDispatch = null;
		if (!orderDispatchs.isEmpty()) {
			orderDispatch = orderDispatchs.get(0);
		}

		if (orderDispatch != null) {
			orderDispatch.setStaffId(Long.parseLong(staffId));
			orderDispatch.setUpdateTime(TimeStampUtil.getNowSecond());
			orderDispatchsService.updateByPrimaryKeySelective(orderDispatch);
		}
		result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "修改成功");

		return result;
	}

}
