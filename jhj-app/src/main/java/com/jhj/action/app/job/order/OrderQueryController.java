package com.jhj.action.app.job.order;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.service.order.OrderQueryService;
import com.jhj.vo.order.OrderQuerySearchVo;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/app/order")
public class OrderQueryController extends BaseController {

	@Autowired
	private OrderQueryService orderQueryService;

	/**  服务人员开工收工状态
	 * @param request
	 * @param userId
	 * @param isWork
	 * @param lat
	 * @param lng
	 * @return
	 */
	@RequestMapping(value = "get_orderMoney",method = RequestMethod.GET)
	public AppResultData<Object> isWork(
		HttpServletRequest request,
		@RequestParam("staff_id") Long staffId,
		@RequestParam("order_status") Short orderStatus){
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());

		Long endTime = 1448899200L;//2015年5月1号
				
		Long startTime = 1430409600L;//2015年12月1号
				
		OrderQuerySearchVo vo = new OrderQuerySearchVo();
		vo.setStaffId(staffId);
		vo.setStartTime(startTime);
		vo.setEndTime(endTime);
		vo.setOrderStatus(orderStatus);
		//vo.setOrderStatus(orderStatus);
		//BigDecimal orderMoney = orderQueryService.getTotalOrderMoney(vo);
		//BigDecimal orderMoney = orderQueryService.getTotalOrderIncomeMoney(vo);
		//Long orderCount = orderQueryService.getTotalOrderCount(vo);
	//	Long totalOnline = orderQueryService.getTotalOnline(vo);
	//	result.setData(totalOnline);
		return result;
	}

}
