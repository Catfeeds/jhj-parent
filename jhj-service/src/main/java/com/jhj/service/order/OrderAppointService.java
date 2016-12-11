package com.jhj.service.order;

import java.util.HashMap;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.order.OrderAppoint;
import com.jhj.vo.order.OrderDispatchSearchVo;


public interface OrderAppointService {

	int deleteByPrimaryKey(Long id);

	int insertSelective(OrderAppoint record);

	int insert(OrderAppoint record);

	int updateByPrimaryKeySelective(OrderAppoint orderRates);

	int updateByPrimaryKey(OrderAppoint record);

	OrderAppoint initOrderAppoint();

	OrderAppoint selectByPrimaryKey(Long id);

	List<OrderAppoint> selectBySearchVo(OrderDispatchSearchVo searchVo);

}
