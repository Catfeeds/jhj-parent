package com.jhj.service.order;

import java.util.List;
import java.util.Map;

import com.jhj.po.model.order.Orders;
import com.jhj.vo.order.OaOrderListVo;
import com.jhj.vo.order.OaOrderSearchVo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月12日上午11:34:51
 * @Description: TODO
 *
 */
public interface OaOrderService {
		
	OaOrderListVo completeVo(Orders orders);

	OaOrderListVo initVO();
	
	OaOrderListVo getOrderVoDetail(String orderNo,Short disStatus); 
	//获取深度保洁订单详情
	OaOrderListVo getOrderExpVoDetail(String orderNo,Short disStatus); 
	//获取钟点工订单详情
	OaOrderListVo getOrderVoDetailHour(String orderNo,Short disStatus); 
	
	Map<String, String> getUserAddrMap(Long userId);

	OaOrderListVo completeNewVo(Orders orders);

	String getOrderGroupCode(Long orderId);
	
}
