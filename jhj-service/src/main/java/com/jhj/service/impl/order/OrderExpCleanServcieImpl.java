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
import com.jhj.po.model.user.Users;
import com.jhj.service.dict.ServiceAddonsService;
import com.jhj.service.order.OrderExpCleanService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderServiceAddonsService;
import com.jhj.service.users.UsersService;
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
	
	@Autowired
	private UsersService userService;

	/**
	 * 获得深度保洁的总价
	 */
	@Override
	public OrderPrices getOrderPriceOfOrderExpClean(Long userId, Long serviceType, String serviceAddonsDatas, Long orderOpFrom) {
		
		OrderPrices orderPrices = orderPricesService.initOrderPrices();
		
		Gson gson = new Gson();

		// 创建一个JsonParser
		JsonParser parser = new JsonParser();

		// 通过JsonParser对象可以把json格式的字符串解析成一个JsonElement对象
		JsonElement el = parser.parse(serviceAddonsDatas);

		//原价
		BigDecimal orderOriginPay = new BigDecimal(0.0);
		//会员价
		BigDecimal orderPrimePay = new BigDecimal(0.0);
		
		//订单总价 = 订单单价（price） * 订单数量（itemNum）
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
				DictServiceAddons dictServiceAddons = serviceAddonsService.selectByPrimaryKey(serviceAddonId);
				BigDecimal price = dictServiceAddons.getPrice();
				BigDecimal disPrice = dictServiceAddons.getDisPrice();
				BigDecimal aprice = dictServiceAddons.getAprice();
				BigDecimal itemNumBigDecimal = new BigDecimal(itemNum);
				
				
				//线下扫码活动
				if (orderOpFrom.equals(123L) || 
					orderOpFrom.equals(124L) || 
					orderOpFrom.equals(125L) || 
					orderOpFrom.equals(126L)) {
					orderOriginPay = orderOriginPay.add(aprice.multiply(itemNumBigDecimal));
					orderPrimePay = orderPrimePay.add(aprice.multiply(itemNumBigDecimal));
				} else {
					orderOriginPay = orderOriginPay.add(price.multiply(itemNumBigDecimal));
					orderPrimePay = orderPrimePay.add(disPrice.multiply(itemNumBigDecimal));
				}
			}
		} else {// 单个对象
			// 把JsonElement对象转换成JsonObject
			if (el.isJsonObject()) {
				JsonObject jsonObj = el.getAsJsonObject();
				JsonServiceAddonsItemVo JsonServiceAddonsItemVo = gson.fromJson(
						jsonObj, JsonServiceAddonsItemVo.class);
				Long serviceAddonId = JsonServiceAddonsItemVo.getServiceAddonId();
				Short itemNum = JsonServiceAddonsItemVo.getItemNum();
				DictServiceAddons dictServiceAddons = serviceAddonsService.selectByPrimaryKey(serviceAddonId);
				BigDecimal price = dictServiceAddons.getPrice();
				BigDecimal disPrice = dictServiceAddons.getDisPrice();
				BigDecimal aprice = dictServiceAddons.getAprice();
				BigDecimal itemNumBigDecimal = new BigDecimal(itemNum);
				
				//线下扫码活动
				if (orderOpFrom.equals(123L) || 
					orderOpFrom.equals(124L) || 
					orderOpFrom.equals(125L) || 
					orderOpFrom.equals(126L)) {
					orderOriginPay = orderOriginPay.add(aprice.multiply(itemNumBigDecimal));
					orderPrimePay = orderPrimePay.add(aprice.multiply(itemNumBigDecimal));
				} else {
					orderOriginPay = orderOriginPay.add(price.multiply(itemNumBigDecimal));
					orderPrimePay = orderPrimePay.add(disPrice.multiply(itemNumBigDecimal));
				}
				
			}
		}
		
		Users u = userService.selectByPrimaryKey(userId);
		
		int isVip = 0 ;
		if (u != null) isVip = u.getIsVip();
		
		BigDecimal orderMoney =  new BigDecimal(0.0);
		BigDecimal orderPay =  new BigDecimal(0.0);
		
		if (isVip == 0) {
			orderMoney = orderOriginPay;
			orderPay = orderOriginPay;
		}
		
		if (isVip == 1) {
			orderMoney = orderPrimePay;
			orderPay = orderPrimePay;
		}
		
		
		orderPrices.setOrderMoney(orderMoney);
		orderPrices.setOrderPay(orderPay);
		orderPrices.setOrderOriginPrice(orderOriginPay);
		orderPrices.setOrderPrimePrice(orderPrimePay);
		return orderPrices;
	}

	@Override
	public List<OrderServiceAddons> updateOrderServiceAddons(Long userId, Long serviceType, String serviceAddonsDatas, Long orderOpFrom) {
		
		Users u = userService.selectByPrimaryKey(userId);
		
		int isVip = 0 ;
		if (u != null) isVip = u.getIsVip();
		
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
				DictServiceAddons dictServiceAddons = serviceAddonsService.selectByPrimaryKey(serviceAddonId);
				
				
				//为附加服务赋值
				orderServiceAddons.setItemNum(itemNum);
				orderServiceAddons.setItemUnit(dictServiceAddons.getItemUnit());
				
				BigDecimal price = dictServiceAddons.getPrice();
				
				if (isVip == 1) {
					price = dictServiceAddons.getDisPrice();
				}
				
				//线下活动，展现活动价格
				if (orderOpFrom.equals(123L) || orderOpFrom.equals(124L) || 
					orderOpFrom.equals(125L) || orderOpFrom.equals(126L)) {
					price = dictServiceAddons.getAprice();
				}
				
				orderServiceAddons.setPrice(price);
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
				DictServiceAddons dictServiceAddons = serviceAddonsService.selectByPrimaryKey(serviceAddonId);
				
				//为附加服务赋值
				orderServiceAddons.setItemNum(itemNum);
				orderServiceAddons.setItemUnit(dictServiceAddons.getItemUnit());
				
				BigDecimal price = dictServiceAddons.getPrice();
				
				if (isVip == 1) {
					price = dictServiceAddons.getDisPrice();
				}
				
				//线下活动，展现活动价格
				if (orderOpFrom.equals(123L) || orderOpFrom.equals(124L) || 
					orderOpFrom.equals(125L) || orderOpFrom.equals(126L)) {
					price = dictServiceAddons.getAprice();
				}
				
				orderServiceAddons.setPrice(price);
				orderServiceAddons.setServiceAddonId(serviceAddonId);
				list.add(orderServiceAddons);
				
			}
		}
		return list;
	}
	
	
	

}
