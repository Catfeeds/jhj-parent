package com.jhj.action.app.order;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPayService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OrgStaffsNewVo;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/app/order")
public class OrderOnlinePayController extends BaseController {

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private OrderPricesService orderPricesService;

	@Autowired
	private OrderPayService orderPayService;

	@Autowired
    private OrderQueryService orderQueryService;
	
	@Autowired
	UsersService userService;

	@Autowired
    private UserDetailPayService userDetailPayService;
	
	@Autowired
	OrderLogService orderLogService;
	
	@Autowired
	private UserCouponsService userCouponService;
	
	
	// 7. 订单在线支付成功同步接口
	/**
	 * 订单在线支付成功同步接口 
	 * @param mobile true string 手机号 
	 * @param order_no true string 订单号 
	 * @param pay_type true int 0 = 余额支付 1 = 支付宝 2 = 微信支付 3 = 智慧支付 4 = 上门刷卡（保留，站位） 
	 * @param notify_id true string 通知ID 
	 * @param notify_time true string 通知时间 
	 * @param trade_no true string 流水号
	 * @param trade_status true string 支付状态 
	 * 				支付宝客户端成功状态为: TRADE_FINISHED 或者为 TRADE_SUCCESS 
	 * 				支付宝网页版成功状态为: success 
	 * 				微信支付成功的状态为: SUCCESS
	 */
	@RequestMapping(value = "online_pay_notify", method = RequestMethod.POST)
	public AppResultData<Object> OnlinePay(
			@RequestParam(value = "user_id", defaultValue = "0") Long userId,
			@RequestParam(value = "mobile", defaultValue = "0")	String mobile,
			@RequestParam("order_no") String orderNo,
			@RequestParam("pay_type") Short payType,
			@RequestParam("notify_id") String notifyId,
			@RequestParam("notify_time") String notifyTime,
			@RequestParam("trade_no") String tradeNo,
			@RequestParam("trade_status") String tradeStatus,
			@RequestParam(value = "pay_account", required = false, defaultValue="") String payAccount) {
		
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		// 判断如果不是正确支付状态，则直接返回.
		Boolean paySuccess = OneCareUtil.isPaySuccess(tradeStatus);
		if (paySuccess == false) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ORDER_PAY_NOT_SUCCESS_MSG);
			return result;
		} else if(tradeStatus.equals("WAIT_BUYER_PAY")) {
			result.setStatus(Constants.SUCCESS_0);
			result.setMsg(ConstantMsg.ORDER_PAY_WAIT_MSG);
			return result;
		}

		Orders order = ordersService.selectByOrderNo(orderNo);

		if (order == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ORDER_NO_NOT_EXIST_MG);
			return result;
		}
		
		Users u = userService.getUserById(order.getUserId());
		
		// 判断是否为注册用户，非注册用户返回 999
		if (u == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}

		
		if (order != null && order.getOrderStatus().equals(Constants.ORDER_STATUS_4)) {
			//更新付款用户账号名
			if (payAccount != null && !payAccount.equals("")) {
				userDetailPayService.updateByPayAccount(tradeNo, payAccount);
			}

			return result;// 订单已支付
		}
		
		Long updateTime = TimeStampUtil.getNow() / 1000;
		
		OrderPrices orderPrice = orderPricesService.selectByOrderId(order.getId());
		
		//更新订单状态.  对于 不是  order_type = 6, 即不是 话费充值类  的 订单做修改
		if(!order.getOrderType().equals(Constants.ORDER_TYPE_6)){
			
			
			
			
		}
		
		//订单支付成功后		
		if (order.getOrderType().equals(Constants.ORDER_TYPE_0)) {
			
			//2016年4月29日11:13:04  钟点工订单，已支付状态为  2
			order.setOrderStatus(Constants.ORDER_HOUR_STATUS_2);
			order.setUpdateTime(updateTime);
			ordersService.updateByPrimaryKeySelective(order);
			//插入订单日志
			OrderLog orderLog = orderLogService.initOrderLog(order);
			orderLogService.insert(orderLog);
			//记录用户消费明细
			userDetailPayService.addUserDetailPayForOrder(u, order, orderPrice, tradeStatus, tradeNo, payAccount);
			
			
			
			orderPayService.orderPaySuccessToDoForHour(u.getId(), order.getId(), new ArrayList<OrgStaffsNewVo>(), false);
		}
		
		if (order.getOrderType().equals(Constants.ORDER_TYPE_1)) {
			orderPayService.orderPaySuccessToDoForDeep(order);
		}		

		if (order.getOrderType().equals(Constants.ORDER_TYPE_2)) {
			
			//2016年4月29日11:13:04  助理订单，已支付状态为 3
			order.setOrderStatus(Constants.ORDER_AM_STATUS_3);
			order.setUpdateTime(updateTime);
			ordersService.updateByPrimaryKeySelective(order);
			//插入订单日志
			OrderLog orderLog = orderLogService.initOrderLog(order);
			orderLogService.insert(orderLog);
			//记录用户消费明细
			userDetailPayService.addUserDetailPayForOrder(u, order, orderPrice, tradeStatus, tradeNo, payAccount);
			
			/*
			 * 2016年4月29日11:18:33  不再做处理。由后台 手动派工
			 */
//			orderPayService.orderPaySuccessToDoForAm(order);
		}		
		
		if(order.getOrderType().equals(Constants.ORDER_TYPE_6)) {
			/*
			 * 话费充值接口  微信支付成功后，调用真正的 充值服务
			 */
			order.setUpdateTime(TimeStampUtil.getNowSecond());
			//作为 微信支付成功的 标志
			order.setRemarks("10001");
			
			ordersService.updateByPrimaryKeySelective(order);
			
			/*
			 * 如果有优惠劵，则需要将优惠劵变成已使用。
			 * 
			 * 	优惠券 无论充值成功失败。都应该设置为已使用
			 */
			OrderPrices orderPrices = orderPricesService.selectByOrderNo(orderNo);
			Long userCouponId = orderPrices.getCouponId();
			if (userCouponId > 0L) {
				UserCoupons userCoupon = userCouponService.selectByPrimaryKey(userCouponId);
				userCoupon.setIsUsed((short) 1);
				userCoupon.setUsedTime(TimeStampUtil.getNowSecond());
				userCoupon.setOrderNo(order.getOrderNo());
				userCouponService.updateByPrimaryKey(userCoupon);
			}
			
			
			orderPayService.orderPaySuccessToDoForPhone(order);
			
			
			
		}
		
		return result;

	}
}
