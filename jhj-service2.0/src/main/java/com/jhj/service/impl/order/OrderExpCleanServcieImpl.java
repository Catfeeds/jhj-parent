package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.service.dict.ServiceAddonsService;
import com.jhj.service.order.OrderExpCleanService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderServiceAddonsService;
import com.jhj.vo.dict.JsonServiceAddonsItemVo;

/**
 * @description：
 * @author： kerryg
 * @date:2015年8月3日 
 */
@Service
public class OrderExpCleanServcieImpl implements OrderExpCleanService {
	
	@Autowired
	private OrderPricesService orderPricesService;
	
	@Autowired
	private ServiceAddonsService serviceAddonsService;
	
	@Autowired
	private OrderServiceAddonsService orderServiceAddonsService;

	/**
	 * 获得深度保洁的总价
	 */
	@Override
	public OrderPrices getOrderPriceOfOrderExpClean(Long serviceType, String serviceAddonsDatas) {
		
		OrderPrices orderPrices = orderPricesService.initOrderPrices();
		

		Gson gson = new Gson();

		// 创建一个JsonParser
		JsonParser parser = new JsonParser();

		// 通过JsonParser对象可以把json格式的字符串解析成一个JsonElement对象
		JsonElement el = parser.parse(serviceAddonsDatas);
		
		//订单总价 = 订单单价（price） * 订单数量（itemNum）
		BigDecimal orderMoney =  new BigDecimal(0.0);
		// 把JsonElement对象转换成JsonArray
		if (el.isJsonArray()) {// 数组
			JsonArray jsonArray = el.getAsJsonArray();
			// 遍历JsonArray对象
			JsonServiceAddonsItemVo JsonServiceAddonsItemVo = null;
			Iterator<JsonElement> it = jsonArray.iterator();
			while (it.hasNext()) {
				JsonElement e = it.next();
				// JsonElement转换为JavaBean对象
				JsonServiceAddonsItemVo = gson.fromJson(e,JsonServiceAddonsItemVo.class);
				Long serviceAddonId = JsonServiceAddonsItemVo.getServiceAddonId();
				Short itemNum = JsonServiceAddonsItemVo.getItemNum();
				DictServiceAddons dictServiceAddons = serviceAddonsService.selectByAddId(serviceAddonId);
				BigDecimal price = dictServiceAddons.getPrice();
				BigDecimal itemNumBigDecimal = new BigDecimal(itemNum);
			    orderMoney =orderMoney.add(price.multiply(itemNumBigDecimal));
			}
		} else {// 单个对象
			// 把JsonElement对象转换成JsonObject
			if (el.isJsonObject()) {
				JsonObject jsonObj = el.getAsJsonObject();
				JsonServiceAddonsItemVo JsonServiceAddonsItemVo = gson.fromJson(
						jsonObj, JsonServiceAddonsItemVo.class);
				Long serviceAddonId = JsonServiceAddonsItemVo.getServiceAddonId();
				Short itemNum = JsonServiceAddonsItemVo.getItemNum();
				DictServiceAddons dictServiceAddons = serviceAddonsService.selectByAddId(serviceAddonId);
				BigDecimal price = dictServiceAddons.getPrice();
				BigDecimal itemNumBigDecimal = new BigDecimal(itemNum);
			    orderMoney =price.multiply(itemNumBigDecimal);
			}
		}
		orderPrices.setOrderMoney(orderMoney);
		return orderPrices;
	}

	@Override
	public List<OrderServiceAddons> updateOrderServiceAddons(Long serviceType, String serviceAddonsDatas) {
		
		List<OrderServiceAddons> list   = new ArrayList<OrderServiceAddons>();

		Gson gson = new Gson();

		// 创建一个JsonParser
		JsonParser parser = new JsonParser();

		// 通过JsonParser对象可以把json格式的字符串解析成一个JsonElement对象
		JsonElement el = parser.parse(serviceAddonsDatas);
		
		//订单总价 = 订单单价（price） * 订单数量（itemNum）
		BigDecimal orderMoney =  new BigDecimal(0.0);
		// 把JsonElement对象转换成JsonArray
		if (el.isJsonArray()) {// 数组
			JsonArray jsonArray = el.getAsJsonArray();
			// 遍历JsonArray对象
			JsonServiceAddonsItemVo JsonServiceAddonsItemVo = null;
			Iterator<JsonElement> it = jsonArray.iterator();
			while (it.hasNext()) {
				OrderServiceAddons  orderServiceAddons = orderServiceAddonsService.initOrderServiceAddons();
				JsonElement e = it.next();
				// JsonElement转换为JavaBean对象
				JsonServiceAddonsItemVo = gson.fromJson(e,JsonServiceAddonsItemVo.class);
				Long serviceAddonId = JsonServiceAddonsItemVo.getServiceAddonId();
				Short itemNum = JsonServiceAddonsItemVo.getItemNum();
				DictServiceAddons dictServiceAddons = serviceAddonsService.selectByAddId(serviceAddonId);
				
				
				//为附加服务赋值
				orderServiceAddons.setItemNum(itemNum);
				orderServiceAddons.setItemUnit(dictServiceAddons.getItemUnit());
				orderServiceAddons.setPrice(dictServiceAddons.getPrice());
				orderServiceAddons.setServiceAddonId(serviceAddonId);
				
				list.add(orderServiceAddons);
				
			}
		} else {// 单个对象
			OrderServiceAddons  orderServiceAddons = orderServiceAddonsService.initOrderServiceAddons();
			// 把JsonElement对象转换成JsonObject
			if (el.isJsonObject()) {
				JsonObject jsonObj = el.getAsJsonObject();
				JsonServiceAddonsItemVo JsonServiceAddonsItemVo = gson.fromJson(
						jsonObj, JsonServiceAddonsItemVo.class);
				Long serviceAddonId = JsonServiceAddonsItemVo.getServiceAddonId();
				Short itemNum = JsonServiceAddonsItemVo.getItemNum();
				DictServiceAddons dictServiceAddons = serviceAddonsService.selectByAddId(serviceAddonId);
				
				//为附加服务赋值
				orderServiceAddons.setItemNum(itemNum);
				orderServiceAddons.setItemUnit(dictServiceAddons.getItemUnit());
				orderServiceAddons.setPrice(dictServiceAddons.getPrice());
				orderServiceAddons.setServiceAddonId(serviceAddonId);
				list.add(orderServiceAddons);
				
			}
		}
		return list;
	}
	
	
	

}
