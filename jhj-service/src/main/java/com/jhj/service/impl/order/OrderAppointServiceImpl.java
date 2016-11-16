package com.jhj.service.impl.order;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.order.OrderAppointMapper;
import com.jhj.po.model.order.OrderAppoint;
import com.jhj.service.order.OrderAppointService;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.meijia.utils.TimeStampUtil;

@Service
public class OrderAppointServiceImpl implements OrderAppointService {

	@Autowired
	private OrderAppointMapper orderAppointMapper;



	@Override
	public OrderAppoint initOrderAppoint() {

		OrderAppoint record = new OrderAppoint();
		record.setId(0L);
		record.setOrderId(0L);
		record.setStaffId(0L);
		record.setUserId(0L);
		record.setAddTime(TimeStampUtil.getNow() / 1000);
		return record;

	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		return orderAppointMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrderAppoint record) {
		return orderAppointMapper.insert(record);
	}

	@Override
	public int insertSelective(OrderAppoint record) {
		return orderAppointMapper.insertSelective(record);
	}

	@Override
	public OrderAppoint selectByPrimaryKey(Long id) {
		return orderAppointMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(OrderAppoint record) {
		return orderAppointMapper.updateByPrimaryKey(record);
	}
	
	@Override
	public int updateByPrimaryKeySelective(OrderAppoint record) {
		return orderAppointMapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public List<OrderAppoint> selectBySearchVo(OrderDispatchSearchVo searchVo) {
		return orderAppointMapper.selectBySearchVo(searchVo);
	}	
}
