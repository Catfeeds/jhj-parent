package com.jhj.service.order;

import com.jhj.vo.order.OrderAmHourViewVo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月7日下午2:09:44
 * @Description:
 * 
 * 		助理端--保洁类订单  service  （订单详情、订单调整）
 *
 */
public interface OrderAmHourService {
	
	OrderAmHourViewVo getOrderAmHourView(String orderNo,Long amId);
	
	OrderAmHourViewVo initOAHVO();
}
