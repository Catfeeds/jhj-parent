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
public class OrderRatesController extends BaseController{

	@Autowired
	private OrderRatesService orderRatesService;
	
	@Autowired 
	private OrdersService ordersService;
	
	
	
	@RequestMapping(value = "post_rate.json", method = RequestMethod.POST)
	public AppResultData<Object> PostExpCleanOrder(
			@RequestParam("user_id") Long userId,
			@RequestParam("order_id") Long orderId ,
			@RequestParam("rate_datas") String rateDatas,
			@RequestParam("rate_content") String rateContent
		) {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		//orderRate = orderRatesService.initOrderRates();
		
		
		Orders orders = ordersService.selectbyOrderId(orderId);
		if (orders == null) {
			return result;
		}
		
		//防止重复 评价 操作
		if(orders.getOrderStatus() == 7){
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.HAVE_RATE);
			return result;
		}
		
		
		OrderRates orderRates = new OrderRates();
		//OrderRates orderRates = orderRatesService.getOrderRates(rateDatas);
		orderRates.setOrderId(orderId);
		orderRates.setOrderNo(orders.getOrderNo());
		orderRates.setAmId(orders.getAmId());
		orderRates.setUserId(orders.getUserId());
		orderRates.setMobile(orders.getMobile());
		orderRates.setRateContent(rateContent);
		orderRates.setAddTime(TimeStampUtil.getNow() / 1000);

		Gson gson = new Gson();
		// 创建一个JsonParser
		JsonParser parser = new JsonParser();

		// 通过JsonParser对象可以把json格式的字符串解析成一个JsonElement对象
		JsonElement el = parser.parse(rateDatas);
		
		// 把JsonElement对象转换成JsonArray
		if (el.isJsonArray()) {// 数组
			JsonArray jsonArray = el.getAsJsonArray();
			// 遍历JsonArray对象
			JsonOrderRateItemVo JsonOrderRateItemVo = null;
			Iterator<JsonElement> it = jsonArray.iterator();
			while (it.hasNext()) {
				JsonElement e = it.next();
				// JsonElement转换为JavaBean对象
				JsonOrderRateItemVo = gson.fromJson(e,JsonOrderRateItemVo.class);
				Short rateType = JsonOrderRateItemVo.getRateType();
                Short rateValue = JsonOrderRateItemVo.getRateValue();
                orderRates.setRateType(rateType);
                orderRates.setRateValue(rateValue);
                orderRatesService.insertByOrderRates(orderRates);
			}
		}
		
		//更改订单状态为已评价
		orders.setOrderStatus(Constants.ORDER_STATUS_7);
		ordersService.updateByPrimaryKeySelective(orders);
	return result;
	}
	//订单详情接口
	@RequestMapping(value = "get_rate", method = RequestMethod.GET)
	public AppResultData<Object> GetRate(
			@RequestParam("order_id") Long orderId 
		
		) {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		OrderRateVo orderRateVo = new OrderRateVo();
		List<OrderRates> list = orderRatesService.setlectListByOrderId(orderId);
		
		if (list == null) {
			return result;
		}
		orderRateVo.setList(list);
		result.setData(orderRateVo);
		
		return result;
	}
}
