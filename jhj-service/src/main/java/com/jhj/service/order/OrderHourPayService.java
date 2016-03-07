package com.jhj.service.order;

import java.util.List;

import com.jhj.po.model.bs.DictCoupons;
import com.jhj.vo.order.OrderHourPayVo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月3日下午4:41:46
 * @Description: 钟点工-支付页面-service
 *
 */
public interface OrderHourPayService {
	
	public List<DictCoupons> getCouponsByUserId(Long userId);
	
	OrderHourPayVo initOrderHourPayVo();
}
