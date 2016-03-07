package com.jhj.service.orderReview;

import com.jhj.po.model.orderReview.JhjOrderReview;

/**
 *
 * @author :hulj
 * @Date : 2015年8月6日上午10:20:24
 * @Description: 
 *		独立 模块：  订单评价 service
 */
public interface OrderReviewService {
	
	JhjOrderReview initOrderReview();
	
	void  insertOrderReview(JhjOrderReview orderReview);
	
}
