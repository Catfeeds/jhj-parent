package com.jhj.service.order;

import com.jhj.po.model.order.OrderExtend;

public interface OrderExtendService {

	int deleteByPrimaryKey(Long id);

	int insertSelective(OrderExtend record);

	int insert(OrderExtend record);

	int updateByPrimaryKeySelective(OrderExtend orderRates);

	int updateByPrimaryKey(OrderExtend record);

	OrderExtend selectByPrimaryKey(Long id);

	OrderExtend initPo();

	OrderExtend selectByOrderId(Long orderId);

	OrderExtend selectByOrderNo(String orderNo);

}
