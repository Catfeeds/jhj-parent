package com.jhj.service.order;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.Orders;
import com.jhj.vo.order.OrderViewVo;

public interface OrdersService {
	
	Orders initOrders();
	
	Long insert(Orders record);
	
	int updateByPrimaryKeySelective(Orders record);
	
	List<Orders> selectByUserIdList(Long userId);

	Orders selectByUserId(Long userId);

	Orders selectByPrimaryKey(Long id);

	int updateUpdateTime(Orders orders);

	int insert(OrderLog orderLog);

	int insertSelective(Orders record);

	int deleteByPrimaryKey(Long id);

	Boolean orderAmSuccessTodo(String orderNo);
	
	Boolean orderExpCleanSuccessTodo(String orderNo);
	
	List<Orders> getAmOrderList(Long amId);
	
	Orders selectByOrderNo(String orderNo);

	OrderViewVo changeOrderViewVo(Orders orders);

    List<Orders> selectOrderListByAmId(Long amId, int pageNo, int pageSize);
    
	PageInfo searchVoListPage(int pageNo, int pageSize);

	int updateCleanUpdateTime(Orders orders);

	List<Orders> selectAmId(Long amId);

	Orders selectByAmId(Long amId);

	int getIntimacyOrders(Map<String, Long> map);
	
	List<Orders> selectListByAmId(Long amId);

	List<HashMap> totalByUserIds(List<Long> userIds);

	List<Orders> selectByAmIdGroupByUserId(Long amId);

	Boolean userOrderAmSuccessTodo(String orderNo);

	Orders selectbyOrderId(Long orderId);

	List<Orders> selectByOrderStatus();

	Boolean userOrderPostBeginSuccessTodo(Orders orders);

	Boolean userOrderPostDoneSuccessTodo(Orders orders);

	Boolean userJoinBlackSuccessTodo(String mobile);

	

}