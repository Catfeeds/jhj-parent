package com.jhj.service.order;

import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.Orders;
import com.jhj.vo.order.OrderViewVo;

public interface OrdersService {

	Orders initOrders();

	Long insert(Orders record);

	int updateByPrimaryKeySelective(Orders record);

	int updateUpdateTime(Orders orders);

	int insert(OrderLog orderLog);

	int insertSelective(Orders record);

	int deleteByPrimaryKey(Long id);

	int updateCleanUpdateTime(Orders orders);

	Orders selectByPrimaryKey(Long id);

	Orders selectByOrderNo(String orderNo);

	OrderViewVo changeOrderViewVo(Orders orders);

	Boolean orderAmSuccessTodo(String orderNo);

	Boolean orderExpCleanSuccessTodo(String orderNo);

	Boolean userOrderAmSuccessTodo(String orderNo);

	Boolean userOrderPostBeginSuccessTodo(Orders orders);

	Boolean userOrderPostDoneSuccessTodo(Orders orders);

	Boolean userJoinBlackSuccessTodo(String mobile);

	// 取消 基础服务订单（钟点工）
	String cancelBaseOrder(Orders order);

	// 取消 助理 预约订单（助理）
	String cancelAmOrder(Orders order);

	// 在jhj-oa系统中取消订单
//	int cancelByOrder(Orders order);
}