package com.jhj.service.order;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.order.Orders;
import com.jhj.vo.order.OrderDetailVo;
import com.jhj.vo.order.OrderListVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.order.OrderViewVo;
import com.jhj.vo.order.UserListVo;


public interface OrderQueryService {
	
	List<Orders> selectBySearchVo(OrderSearchVo searchVo);
	
	PageInfo selectByListPage(OrderSearchVo searchVo, int pageNo, int pageSize);
	
	OrderViewVo getOrderView(Orders order);
	
	String getOrderStatusName(Short status);

	List<OrderViewVo> getOrderViewList(List<Orders> list);

	UserListVo getUserList(Orders order);

	OrderSearchVo getOrderSearchVo(HttpServletRequest request, OrderSearchVo searchVo, Short orderType, Long sessionParentId);

	OrderListVo getOrderListVo(Orders item, Long staffId);

	OrderDetailVo getOrderDetailVo(Orders item, Long staffId);
	
	BigDecimal getTotalOrderPay(OrderSearchVo vo);

	BigDecimal getTotalOrderPayExt(OrderSearchVo vo);
	
}