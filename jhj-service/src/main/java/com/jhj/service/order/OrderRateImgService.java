package com.jhj.service.order;

import java.util.List;

import com.jhj.po.model.order.OrderRateImg;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderSearchVo;

public interface OrderRateImgService {

	int deleteByPrimaryKey(Long id);

	int insertSelective(OrderRateImg record);

	int insert(OrderRateImg record);

	int updateByPrimaryKeySelective(OrderRateImg orderRates);

	int updateByPrimaryKey(OrderRateImg record);

	OrderRateImg initOrderRateImg();

	OrderRateImg selectByPrimaryKey(Long id);

	List<OrderRateImg> selectBySearchVo(OrderSearchVo searchVo);

}
