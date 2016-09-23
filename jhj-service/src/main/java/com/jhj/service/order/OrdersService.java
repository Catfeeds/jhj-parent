package com.jhj.service.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.Orders;
import com.jhj.vo.chart.CoopUserOrderVo;
import com.jhj.vo.order.OrderSearchVo;
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

	List<Orders> selectOrderListByAmId(Long amId, int pageNo, int pageSize);

	List<Orders> selectBySearchVo(OrderSearchVo searchVo);

	PageInfo selectByListPage(OrderSearchVo searchVo, int pageNo, int pageSize);
	
	OrderViewVo changeOrderViewVo(Orders orders);

	int totalIntimacyOrders(Map<String, Long> map);

	List<HashMap> totalByUserIds(List<Long> userIds);

	Boolean orderAmSuccessTodo(String orderNo);

	Boolean orderExpCleanSuccessTodo(String orderNo);

	Boolean userOrderAmSuccessTodo(String orderNo);

	Boolean userOrderPostBeginSuccessTodo(Orders orders);

	Boolean userOrderPostDoneSuccessTodo(Orders orders);

	Boolean userJoinBlackSuccessTodo(String mobile);

	Long totalOrderInUserIds(List<Long> userIds);

	List<CoopUserOrderVo> totalUserAndOrder(List<Long> userIds);

	/*
	 * 2016年5月4日10:32:26 jhj2.1 取消订单 通用 处理
	 */

	// 取消 基础服务订单（钟点工）
	String cancelBaseOrder(Orders order);

	// 取消 助理 预约订单（助理）
	String cancelAmOrder(Orders order);

	// 在jhj-oa系统中取消订单
	int cancelByOrder(Orders order);
}