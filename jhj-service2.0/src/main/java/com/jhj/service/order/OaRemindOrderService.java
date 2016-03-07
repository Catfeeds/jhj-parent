package com.jhj.service.order;

import java.util.List;

import com.jhj.po.model.order.Orders;
import com.jhj.vo.OaRemindOrderSearchVo;
import com.jhj.vo.order.OaRemindOrderVo;


/*
 * 提醒类订单 service
 */
public interface OaRemindOrderService {
	
  List<Orders>	getRemindOrderList(int pageNo, int pageSize, OaRemindOrderSearchVo searchVo);
  
  OaRemindOrderVo  transVo(Orders order);
}	
