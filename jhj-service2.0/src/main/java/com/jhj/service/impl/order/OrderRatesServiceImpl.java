package com.jhj.service.impl.order;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;







import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jhj.po.dao.order.OrderRatesMapper;
import com.jhj.po.model.order.OrderRates;
import com.jhj.service.order.OrderRatesService;
import com.jhj.service.order.OrdersService;
import com.jhj.vo.order.JsonOrderRateItemVo;
import com.meijia.utils.TimeStampUtil;


@Service
public class OrderRatesServiceImpl implements OrderRatesService{

	@Autowired
	private OrderRatesMapper orderRatesMapper;
	
	@Autowired
	private OrdersService ordersService;
	
	@Override
	public OrderRates initOrderRates() {
		
		OrderRates orderRates = new OrderRates();
		orderRates.setRateValue((short)0);
		orderRates.setRateType((short)0);
		orderRates.setOrderId(0L);
		orderRates.setAmId(0L);
		orderRates.setMobile("");
		orderRates.setOrderNo("");
		orderRates.setUserId(0L);
		orderRates.setOrderId(0L);
		orderRates.setRateContent("");
		orderRates.setAddTime(TimeStampUtil.getNow()/1000);
		
		return orderRates;
	
	}
	@Override
	public OrderRates selectByOrderId(Long orderId) {
		
		return orderRatesMapper.selectByOrderId(orderId);
	}
	@Override
	public int deleteByOrderId(Long orderId) {
		
		return orderRatesMapper.deleteByOrderId(orderId);
	}
	@Override
	public int updateByPrimaryKeySelective(OrderRates orderRates) {
		
		return orderRatesMapper.updateByPrimaryKeySelective(orderRates);
	}
	@Override
	public int insertByOrderRates(OrderRates orderRates) {
		
		return orderRatesMapper.insertSelective(orderRates);
	}
	@Override
	public OrderRates getOrderRates(Long orderId, String rateDatas) {
		
		OrderRates orderRates = this.initOrderRates();
		
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
                this.insertByOrderRates(orderRates);
			}
		}
		
		return orderRates;
	}
	@Override
	public List<OrderRates> setlectListByOrderId(Long orderId) {
		
		return orderRatesMapper.setlectListByOrderId(orderId);
	}

	

}
