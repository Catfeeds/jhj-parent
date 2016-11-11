package com.jhj.service.impl.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.po.dao.order.OrderRatesMapper;
import com.jhj.po.model.order.OrderPriceExt;
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

		OrderRates record = new OrderRates();
		record.setId(0L);
		record.setOrderId(0L);
		record.setOrderNo("");
		record.setStaffId(0L);
		record.setUserId(0L);
		record.setMobile("");
		record.setRateArrival(0);
		record.setRateAttitude(0);
		record.setRateSkill(0);
		record.setRateContent("");
		record.setAddTime(TimeStampUtil.getNow() / 1000);

		return record;

	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		return orderRatesMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrderRates record) {
		return orderRatesMapper.insert(record);
	}

	@Override
	public int insertSelective(OrderRates record) {
		return orderRatesMapper.insertSelective(record);
	}

	@Override
	public OrderRates selectByPrimaryKey(Long id) {
		return orderRatesMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(OrderRates record) {
		return orderRatesMapper.updateByPrimaryKey(record);
	}
	
	@Override
	public int updateByPrimaryKeySelective(OrderRates record) {
		return orderRatesMapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public List<OrderRates> selectBySearchVo(OrderDispatchSearchVo searchVo) {
		return orderRatesMapper.selectBySearchVo(searchVo);
	}
	
	@Override
	public PageInfo selectByListPage(OrderDispatchSearchVo searchVo, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<OrderRates> list = orderRatesMapper.selectByListPage(searchVo);
		PageInfo result = new PageInfo(list);
		return result;
	}

}
