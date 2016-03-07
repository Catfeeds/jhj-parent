package com.jhj.service.order;

import java.util.List;
import java.util.Map;

import com.jhj.po.model.order.Orders;
import com.jhj.vo.OaOrderSearchVo;
import com.jhj.vo.order.OaOrderListNewVo;
import com.jhj.vo.order.OaOrderListVo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月12日上午11:34:51
 * @Description: TODO
 *
 */
public interface OaOrderService {
	
	List<Orders> selectVoByListPage(OaOrderSearchVo oaOrderSearchVo, int pageNo, int pageSize);

//	OaOrderListVo completeVo(Orders orders);
	
	OaOrderListNewVo completeVo(Orders orders);
//	List<OaOrderListVo> completeVo(Orders orders);
	
	OaOrderListVo initVO();
	
	OaOrderListVo getOrderVoDetail(String orderNo,Short disStatus); 
	//获取深度保洁订单详情
	OaOrderListVo getOrderExpVoDetail(String orderNo,Short disStatus); 
	//获取钟点工订单详情
	OaOrderListVo getOrderVoDetailHour(String orderNo,Short disStatus); 
	//获取助理订单详情
	OaOrderListVo getOrderVoDetailAm(String orderNo,Short disStatus,String poiLongitude,String poiLatitude); 
	
	OaOrderListVo getOrderVoDetailAm(String orderNo,Short disStatus); 
	//获取配送订单详情
	OaOrderListVo getOrderVoDetailDel(String orderNo,Short disStatus); 
	//助理 and 配送订单 vo转换
	OaOrderListNewVo completeNewVo(Orders orders);

	Map<String, String> getUserAddrMap(Long userId);
	
}
