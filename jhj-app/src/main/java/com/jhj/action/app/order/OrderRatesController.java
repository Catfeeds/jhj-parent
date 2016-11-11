package com.jhj.action.app.order;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.order.OrderRates;
import com.jhj.po.model.order.Orders;
import com.jhj.service.order.OrderRatesService;
import com.jhj.service.order.OrdersService;
import com.jhj.vo.order.JsonOrderRateItemVo;
import com.jhj.vo.order.OrderRateVo;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/app/order")
public class OrderRatesController extends BaseController {

	@Autowired
	private OrderRatesService orderRatesService;

	@Autowired
	private OrdersService ordersService;

	@RequestMapping(value = "post_rate.json", method = RequestMethod.POST)
	public AppResultData<Object> PostExpCleanOrder(@RequestParam("user_id") Long userId, @RequestParam("order_id") Long orderId,
			@RequestParam("rate_arrival") int rateArrival, @RequestParam("rate_attitude") int rateAttitude, @RequestParam("rate_skill") int rateSkill,
			@RequestParam("rate_content") String rateContent) {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		// orderRate = orderRatesService.initOrderRates();

		Orders orders = ordersService.selectByPrimaryKey(orderId);
		if (orders == null) {
			return result;
		}

		return result;
	}

	// 订单详情接口
	@RequestMapping(value = "get_order_rate", method = RequestMethod.GET)
	public AppResultData<Object> GetOrderRate(@RequestParam("order_id") Long orderId) {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");


		return result;
	}
	
	// 订单详情接口
		@RequestMapping(value = "get_staff_rate", method = RequestMethod.GET)
		public AppResultData<Object> GetStaffRate(@RequestParam("order_id") Long orderId) {
			AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");



			return result;
		}
}
