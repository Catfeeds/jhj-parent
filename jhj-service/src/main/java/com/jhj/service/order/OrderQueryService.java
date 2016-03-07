package com.jhj.service.order;

import java.math.BigDecimal;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.order.Orders;
import com.jhj.vo.OrderQuerySearchVo;
import com.jhj.vo.OrderSearchVo;
import com.jhj.vo.order.OrderDetailVo;
import com.jhj.vo.order.OrderListVo;
import com.jhj.vo.order.OrderViewVo;
import com.jhj.vo.order.UserListVo;


public interface OrderQueryService {

	//PageInfo selectByListPage(OrderSearchVo orderSearchVo, int pageNo, int pageSize);

	String getOrderStatusName(Short status);

	//List<OrderViewVo> selectByUserId(Long userId, int pageNo, int pageSize);


	//List<OrderViewVo> getOrderViewList(List<Orders> list);

	OrderViewVo getOrderView(Orders order);

	PageInfo selectByListPage(OrderSearchVo searchVo, int pageNo, int pageSize);

	List<OrderViewVo> getOrderViewList(List<Orders> list);

	UserListVo getUserList(Orders order);

	//. 根据开始时间，接收时间，staff_id ，得出订单总金额
	BigDecimal getTotalOrderMoney(OrderQuerySearchVo vo);
	
	// 根据开始时间，接收时间，staff_id ，得出订单总收入金额
	BigDecimal getTotalOrderIncomeMoney(OrderQuerySearchVo vo);
	
	// . 根据开始时间，接收时间，staff_id ，得出订单总数:
	Long getTotalOrderCount(OrderQuerySearchVo vo);
    //当月订单总数（order_status=7,8）
	Long getTotalOrderCountByMouth(OrderQuerySearchVo searchVo);

	PageInfo selectByListVoPage(OrderSearchVo searchVo,int pageNo, int pageSize);

	OrderListVo getOrderListVo(Orders item);

	OrderDetailVo getOrderDetailVo(Orders item);


	
}