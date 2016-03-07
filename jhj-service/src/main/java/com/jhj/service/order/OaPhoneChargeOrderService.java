package com.jhj.service.order;

import java.util.List;

import com.jhj.po.model.order.Orders;
import com.jhj.vo.OaPhoneChargeOrderSearchVo;
import com.jhj.vo.order.OaPhoneChargeOrderVo;


/*
 * 话费充值订单 service
 */
public interface OaPhoneChargeOrderService {
	
	List<Orders>  selectList(int pageNo, int pageSize, OaPhoneChargeOrderSearchVo searchVo);
	
	OaPhoneChargeOrderVo  tranVo(Orders order);
}
