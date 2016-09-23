package com.jhj.action.job;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.service.job.OrderCrondService;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年8月24日下午3:40:26
 * @Description: 定时任务
 */
@Controller
@RequestMapping(value = "/app/orderCrond")
public class OrderCrondController extends BaseController {

	@Autowired
	private OrderCrondService orderCrondService;

	/*
	 * 1) 钟点工服务开始前2小时提醒, 状态为 ORDER_STATUS_4 流程： a. 找出order表所有状态为ORDER_STATUS_4 ，
	 * order_type为 钟点工， 并且service_date -2 *3600 = 当前时间 b. 发短信.
	 */

	@RequestMapping(value = "job_hour_notice", method = RequestMethod.GET)
	public AppResultData<Object> beforeServiceNotice(HttpServletRequest request) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());

		System.out.println("Remote Host: " + request.getRemoteHost());
		String reqHost = request.getRemoteHost();// 如果用的 IPV6 得到的 将是 0:0.。。。。1

		// String reqHost = request.getRemoteAddr();
		// 限定只有localhost能访问
		if (reqHost.equals("localhost") || reqHost.equals("127.0.0.1")) {

			orderCrondService.noticeBeforeService();

		}

		return result;
	}

	/*
	 * 2）在服务开始时间内，将订单状态改为服务中 ORDER_STATUS_5
	 * 
	 * 流程： 1 找出order表所有状态为ORDER_STATUS_4 ， 当前时间 >= service_date and 当前时间 <=
	 * service_hour*3600 + service_date
	 * 
	 * 2.改状态为 ORDER_STATUS_5 ；
	 */

	@RequestMapping(value = "during_service", method = RequestMethod.GET)
	public AppResultData<Object> duringServcie(HttpServletRequest request) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());

		System.out.println("Remote Host: " + request.getRemoteHost());
		String reqHost = request.getRemoteHost();

		// String reqHost = request.getRemoteAddr();
		// 限定只有localhost能访问
		if (reqHost.equals("localhost") || reqHost.equals("127.0.0.1")) {
			orderCrondService.updateDuringService();
		}

		return result;
	}

	/*
	 * 3）在服务结束时间后，如果订单状态还是ORDER_STATUS_5 ，则改为待评价状态
	 * 
	 * 流程： 1. 找出order表所有状态为ORDER_STATUS_5 ，并且 当前时间 >= service_hour*3600 +
	 * service_date
	 * 
	 * 2. 改状态为 ORDER_STATUS_6 ；
	 */
	@RequestMapping(value = "after_service", method = RequestMethod.GET)
	public AppResultData<Object> afterService(HttpServletRequest request) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());

		System.out.println("Remote Host: " + request.getRemoteHost());
		String reqHost = request.getRemoteHost();

		// String reqHost = request.getRemoteAddr();
		// 限定只有localhost能访问
		if (reqHost.equals("localhost") || reqHost.equals("127.0.0.1")) {
			orderCrondService.updateAfterService();
		}

		return result;
	}

	/*
	 * 4) 在钟点工状态为待支付状态 ORDER_STATUS_3，超过1个小时未支付的订单，将订单状态改为已关闭.
	 * 
	 * 流程： 1. 找出order表所有状态为ORDER_STATUS_3 ，并且 当前时间 >= add_time + 3600 2. 改状态为
	 * ORDER_STATUS_9 ；
	 */

	@RequestMapping(value = "over_time_not_pay", method = RequestMethod.GET)
	public AppResultData<Object> overTimeNotPay(HttpServletRequest request) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());

		System.out.println("Remote Host: " + request.getRemoteHost());
		String reqHost = request.getRemoteHost();

		// String reqHost = request.getRemoteAddr();
		// 限定只有localhost能访问
		if (reqHost.equals("localhost") || reqHost.equals("127.0.0.1")) {
			orderCrondService.updateOverTimeNotPay();
		}

		return result;
	}

	/*
	 * 5)如果订单已结束超过七天，默认评价，并且为好评.
	 * 
	 * a. 找出order表所有状态为ORDER_STATUS_6 ，并且 当前时间 >= sercice_time + hour*3600 + 7 *
	 * 24 * 3600
	 * 
	 * b. 好评 order_rates
	 */
	@RequestMapping(value = "over_serven_day", method = RequestMethod.GET)
	public AppResultData<Object> overServenDay(HttpServletRequest request) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());

		System.out.println("Remote Host: " + request.getRemoteHost());
		String reqHost = request.getRemoteHost();

		// String reqHost = request.getRemoteAddr();
		// 限定只有localhost能访问
		if (reqHost.equals("localhost") || reqHost.equals("127.0.0.1")) {
			orderCrondService.setOrderRateOverServenDay();
		}

		return result;
	}

	/*
	 * 6.助理订单、深度保洁支付后24小时，订单状态自动变为已完成
	 */
	@RequestMapping(value = "am_order_one_day", method = RequestMethod.GET)
	public AppResultData<Object> amOrderOneDay(HttpServletRequest request) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());

		System.out.println("Remote Host: " + request.getRemoteHost());

		String reqHost = request.getRemoteHost();

		// String reqHost = request.getRemoteAddr();
		// 限定只有localhost能访问
		if (reqHost.equals("localhost") || reqHost.equals("127.0.0.1")) {
			orderCrondService.amOrderStatusOverOneDay();
		}

		return result;
	}	
}
