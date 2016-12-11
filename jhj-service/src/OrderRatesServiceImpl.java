package com.jhj.service.impl.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.order.OrderRatesMapper;
import com.jhj.po.model.order.OrderRates;
import com.jhj.service.order.OrderRatesService;
import com.jhj.service.order.OrdersService;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.meijia.utils.TimeStampUtil;

@Service
public class OrderRatesServiceImpl implements OrderRatesService {

	@Autowired
	private OrderRatesMapper orderRatesMapper;

	@Autowired
	private OrdersService ordersService;

	@Override
	public OrderRates initOrderRates() {

		OrderRates orderRates = new OrderRates();

		orderRates.setOrderId(0L);
		orderRates.setOrderNo("");
		orderRates.setStaffId(0L);
		orderRates.setUserId(0L);
		orderRates.setMobile("");
		orderRates.setRateArrival(0);
		orderRates.setRateAttitude(0);
		orderRates.setRateSkill(0);
		orderRates.setRateContent("");
		orderRates.setAddTime(TimeStampUtil.getNow() / 1000);

		return orderRates;

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
	public List<OrderRates> selectBySearchVo(OrderDispatchSearchVo searchVo) {
		return orderRatesMapper.selectBySearchVo(searchVo);
	}

}
