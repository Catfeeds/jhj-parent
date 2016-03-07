package com.jhj.service.order;

import java.util.List;

import com.jhj.po.model.order.OrderRates;

public interface OrderRatesService {

	OrderRates selectByOrderId(Long orderId);

	int deleteByOrderId(Long orderId);

	int updateByPrimaryKeySelective(OrderRates orderRates);

	OrderRates initOrderRates();

	int insertByOrderRates(OrderRates orderRates);

	//OrderRates getOrderRates(String rateDatas);

	OrderRates getOrderRates(Long orderId, String rateDatas);

	List<OrderRates> setlectListByOrderId(Long orderId);



}
