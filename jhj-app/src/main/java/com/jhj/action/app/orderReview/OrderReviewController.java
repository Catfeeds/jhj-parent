package com.jhj.action.app.orderReview;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.orderReview.JhjOrderReview;
import com.jhj.service.orderReview.OrderReviewService;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年8月6日上午10:17:14
 * @Description:	
 * 			完全独立的模块：
 * 				家和家  订单评价
 * 					
 */
@Controller
@RequestMapping("/app/orderReview")
public class OrderReviewController extends BaseController {
	@Autowired
	private OrderReviewService orderReviewService;
	
	
	
	
	/*
	 * 提交评价操作
	 */
	@RequestMapping(value = "generate_order_review", method = RequestMethod.POST)
	public AppResultData<Object> submitOrderReview(
			@RequestParam("staffNo") String staffNo,
			@RequestParam("serviceDate") Long serviceDate,
			@RequestParam("serviceHour") String serviceHour,
			@RequestParam("arriveOnTime") Short arriveOnTime,
			@RequestParam("cleanReview") Short cleanReview,
			@RequestParam("serviceDetail") Short serviceDetail,
			@RequestParam("appearance") Short appearance,
			@RequestParam("afterService") Short afterService,
			@RequestParam("overAllReview") Short overAllReview){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0,
				ConstantMsg.SUCCESS_0_MSG, new String()); 
		
		JhjOrderReview orderReview = orderReviewService.initOrderReview();
		
		orderReview.setStaffNo(staffNo);
		orderReview.setServiceDate(serviceDate);
		orderReview.setServiceHour(serviceHour);
		orderReview.setArrriveOnTime(arriveOnTime);
		orderReview.setCleanReview(cleanReview);
		orderReview.setServiceDetail(serviceDetail);
		orderReview.setAppearance(appearance);
		orderReview.setAfterService(afterService);
		orderReview.setOverAllReview(overAllReview);
		
		
		orderReviewService.insertOrderReview(orderReview);
		
		result = new AppResultData<Object>(Constants.SUCCESS_0,
				ConstantMsg.SUCCESS_0_MSG, new String("评价成功"));
		
		return result;
	}
	
}
