package com.jhj.action.app.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jhj.action.app.BaseController;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UsersService;


@Controller
@RequestMapping(value = "/app/order")
public class OrderConfirmController extends BaseController {
	@Autowired
	private UsersService userService;
	
	@Autowired
	private OrdersService ordersService;

/*	@Autowired
    private OrderQueryService orderQueryService;
	
	// 订单确认接口
	*//**
	 * mobile:手机号 order_id订单ID
	 *//*
	@RequestMapping(value = "post_confirm", method = RequestMethod.POST)
	public AppResultData<Object> detail(
			@RequestParam("user_id") Long userId, 
			@RequestParam("order_no") String orderNo) {
		
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		Users u = userService.getUserById(userId);

		// 判断是否为注册用户，非注册用户返回 999
		if (u == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}		
		
		Orders orders = orderQueryService.selectByOrderNo(String.valueOf(orderNo));
		
		if (orders == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ORDER_NO_NOT_EXIST_MG);			
			return result;
		}
		
		orders.setOrderStatus(Constants.ORDER_STATUS_2_CONFIRM_DONE);
		orders.setUpdateTime(TimeStampUtil.getNowSecond());
		ordersService.updateByPrimaryKeySelective(orders);
		
		//订单确认后的操作
		ordersService.orderConfirmTodo(orderNo);
		
		OrderViewVo orderViewVo = orderQueryService.getOrderView(orders);
		result.setData(orderViewVo);
		
		return result;
	}*/
}
