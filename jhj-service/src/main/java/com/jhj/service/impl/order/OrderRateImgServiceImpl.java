package com.jhj.service.impl.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.order.OrderRateImgMapper;
import com.jhj.po.model.order.OrderRateImg;
import com.jhj.service.order.OrderRateImgService;
import com.jhj.service.order.OrdersService;
import com.jhj.vo.order.OrderSearchVo;
import com.meijia.utils.TimeStampUtil;

@Service
public class OrderRateImgServiceImpl implements OrderRateImgService {

	@Autowired
	private OrderRateImgMapper orderRateImgMapper;

	@Autowired
	private OrdersService ordersService;

	@Override
	public OrderRateImg initOrderRateImg() {
		OrderRateImg record = new OrderRateImg();
		record.setId(0L);
		record.setOrderId(0L);
		record.setOrderNo("");
		record.setUserId(0L);
		record.setMobile("");
		record.setImgUrl("");
		record.setAddTime(TimeStampUtil.getNow() / 1000);
		return record;
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		return orderRateImgMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrderRateImg record) {
		return orderRateImgMapper.insert(record);
	}

	@Override
	public int insertSelective(OrderRateImg record) {
		return orderRateImgMapper.insertSelective(record);
	}

	@Override
	public OrderRateImg selectByPrimaryKey(Long id) {
		return orderRateImgMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(OrderRateImg record) {
		return orderRateImgMapper.updateByPrimaryKey(record);
	}
	
	@Override
	public int updateByPrimaryKeySelective(OrderRateImg record) {
		return orderRateImgMapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public List<OrderRateImg> selectBySearchVo(OrderSearchVo searchVo) {
		return orderRateImgMapper.selectBySearchVo(searchVo);
	}
	
}
