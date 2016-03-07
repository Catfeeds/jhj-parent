package com.jhj.service.order;

import com.jhj.vo.order.OrderHourViewVo;


/**
 *
 * @author :hulj
 * @Date : 2015年8月5日上午11:06:52
 * @Description: 钟点工-订单详情
 *
 */
public interface OrderHourDetailService {
	
	OrderHourViewVo getOrderHourDetail(String orderNo);
	
	OrderHourViewVo initOHVO();

	OrderHourViewVo getOrderPaotui(String orderNo);

}
