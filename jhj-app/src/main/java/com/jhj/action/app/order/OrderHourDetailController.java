package com.jhj.action.app.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.dict.DictService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderHourDetailService;
import com.jhj.service.order.OrderHourListService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UsersService;
import com.meijia.utils.vo.AppResultData;
import com.jhj.vo.order.OrderHourViewVo;

/**
 *
 * @author :hulj
 * @Date : 2015年7月27日下午5:23:25
 * @Description: 钟点工-订单详情接口
 *
 */
@Controller
@RequestMapping(value = "/app/order")
public class OrderHourDetailController extends BaseController {
	@Autowired
	private OrderHourDetailService orderHourDetailService;

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private OrderHourListService orderHourListService;
	
	
	@Autowired
	private DictService dictService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private OrgStaffsService orgStaffsService;

	@Autowired
	private UserAddrsService userAddrsService;

	@Autowired
	private OrderDispatchsService orderDispatchsService;

	/*
	 * 订单 详情
	 */
	@RequestMapping(value = "order_hour_detail", method = RequestMethod.GET)
	public AppResultData<Object> orderHourDetail(
			@RequestParam("order_no") String orderNo) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		OrderHourViewVo orderHourViewVo = orderHourDetailService
				.getOrderHourDetail(orderNo);

		result.setData(orderHourViewVo);

		return result;
	}

//	/*
//	 * 取消订单
//	 */
//	@RequestMapping(value = "cancle_order_hour", method = RequestMethod.POST)
//	public AppResultData<Object> cancleOrderHour(
//			@RequestParam("order_no") String orderNo) {
//
//		AppResultData<Object> result = new AppResultData<Object>(
//				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
//
//		Orders orders = ordersService.selectByOrderNo(orderNo);
//		// 订单状态设置为已取消   0：已取消  9：已关闭
//		orders.setOrderStatus((short) 0);
//
//		ordersService.updateByPrimaryKeySelective(orders);
//
//		return result;
//	}

	/*
	 * 客户列表
	 */
	@RequestMapping(value = "order_hour_paotui_detail", method = RequestMethod.GET)
	public AppResultData<Object> OrderHourPaoTui(
			@RequestParam("order_no") String orderNo) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		OrderHourViewVo orderHourViewVo = orderHourDetailService
				.getOrderPaotui(orderNo);

		result.setData(orderHourViewVo);

		return result;

	}

	/*
	 * 客户列表详情
	 */
	/*@RequestMapping(value = "get_user_list_detail", method = RequestMethod.GET)
	public AppResultData<Object> getAmOrderDetail(
			@RequestParam("user_id") Long userId) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		// Orders orders = ordersService.selectByUserId(userId);
		List<Orders> orders = ordersService.selectByUserIdList(userId);
		if (orders == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ORDER_NO_NOT_EXIST_MG);
			return result;
		}

		List<Long> userIds = new ArrayList<Long>();
		for (Orders item : orders) {
			userIds.add(item.getId());
		}
		Collections.sort(userIds);

		Orders order = ordersService.selectByPrimaryKey(userIds.get(0));

		OrgStaffs orderStaffs = orgStaffsService.selectByPrimaryKey(order
				.getAmId());

		// 判断是否为注册助理，非注册助理返回 999
		if (orderStaffs == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}

		
		OrderHourViewVo vo = orderHourDetailService.getOrderHourDetail(order
				.getOrderNo());
		result.setData(vo);
		return result;
	}

	@RequestMapping(value = "post_user_edit", method = RequestMethod.POST)
	public AppResultData<Object> savaEditUser(
			@RequestParam("user_id") Long userId,
			@RequestParam("name") String name,
			@RequestParam("mobile") String mobile,
			@RequestParam("sex") String sex,
			@RequestParam("remarks") String remarks) {
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		List<Orders> order = ordersService.selectByUserIdList(userId);

		List<Long> userIds = new ArrayList<Long>();
		for (Orders item : order) {
			userIds.add(item.getId());
		}
		Collections.sort(userIds);
		Orders orders = ordersService.selectByPrimaryKey(userIds.get(0));

		Users users = usersService.selectByUsersId(userId);

		if (users != null) {

			users.setName(name);
			users.setMobile(mobile);
			users.setSex(sex);
			users.setRemarks(remarks);
			usersService.updateByPrimaryKeySelective(users);
		}

		// 备注进行urldecode;
		try {
			remarks = URLDecoder.decode(remarks, Constants.URL_ENCODE);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ERROR_100_MSG);
			return result;
		}

		OrderHourViewVo vo = orderHourDetailService.getOrderHourDetail(orders
				.getOrderNo());
		if (orders.getOrderNo() != null) {
			// 记录订单日志
			ordersService.userOrderAmSuccessTodo(orders.getOrderNo());
			
		}
		vo.setRemarks(remarks);
		result.setData(vo);
		return result;
	}
*/
}
