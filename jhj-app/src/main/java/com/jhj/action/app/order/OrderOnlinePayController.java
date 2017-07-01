package com.jhj.action.app.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.order.OrderCards;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.period.PeriodOrder;
import com.jhj.po.model.share.OrderShare;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.order.OrderCardsService;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPayService;
import com.jhj.service.order.OrderPriceExtService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.period.PeriodOrderService;
import com.jhj.service.share.OrderShareService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OrgStaffDispatchVo;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.SmsUtil;
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
	private OrderPriceExtService orderPriceExtService;

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
	
	@Autowired
	private OrderCardsService orderCardsService;
	
	@Autowired
	private PeriodOrderService periodOrderService;
	
	@Autowired
	private OrderShareService orderShareService;

	// 7. 订单在线支付成功同步接口
	/**
	 * 订单在线支付成功同步接口
	 * 
	 * @param mobile
	 *            true string 手机号
	 * @param order_no
	 *            true string 订单号
	 * @param pay_type
	 *            true int 0 = 余额支付 1 = 支付宝 2 = 微信支付 3 = 智慧支付 4 = 上门刷卡（保留，站位）
	 * @param notify_id
	 *            true string 通知ID
	 * @param notify_time
	 *            true string 通知时间
	 * @param trade_no
	 *            true string 流水号
	 * @param trade_status
	 *            true string 支付状态
	 *            支付宝客户端成功状态为: TRADE_FINISHED 或者为 TRADE_SUCCESS
	 *            支付宝网页版成功状态为: success
	 *            微信支付成功的状态为: SUCCESS
	 */
	@RequestMapping(value = "online_pay_notify", method = RequestMethod.POST)
	public AppResultData<Object> OnlinePay(@RequestParam(value = "user_id", defaultValue = "0") Long userId,
			@RequestParam(value = "mobile", defaultValue = "0") String mobile, @RequestParam("order_no") String orderNo,
			@RequestParam("pay_type") Short payType, @RequestParam(value = "pay_order_type", required = false, defaultValue = "0") int payOrderType,
			@RequestParam("notify_id") String notifyId, @RequestParam("notify_time") String notifyTime, @RequestParam("trade_no") String tradeNo,
			@RequestParam("trade_status") String tradeStatus, @RequestParam(value = "pay_account", required = false, defaultValue = "") String payAccount,
			@RequestParam(value = "body", required = false, defaultValue = "") String shareUserId) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		// 判断如果不是正确支付状态，则直接返回.
		Boolean paySuccess = OneCareUtil.isPaySuccess(tradeStatus);
		if (paySuccess == false) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ORDER_PAY_NOT_SUCCESS_MSG);
			return result;
		} else if (tradeStatus.equals("WAIT_BUYER_PAY")) {
			result.setStatus(Constants.SUCCESS_0);
			result.setMsg(ConstantMsg.ORDER_PAY_WAIT_MSG);
			return result;
		}

		// payOrderType 订单支付类型 0 = 订单支付 1= 充值支付 2 = 手机话费类充值 3 = 订单补差价
		Orders order = ordersService.selectByOrderNo(orderNo);

		if (order == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ORDER_PAY_NOT_SUCCESS_MSG);
			return result;
		}

		Users u = userService.selectByPrimaryKey(order.getUserId());

		// 判断是否为注册用户，非注册用户返回 999
		if (u == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}

		if (order != null ) {
			if ( !order.getOrderStatus().equals(Constants.ORDER_HOUR_STATUS_1)) {
				// 更新付款用户账号名
				if (payAccount != null && !payAccount.equals("")) {
					userDetailPayService.updateByPayAccount(tradeNo, payAccount);
				}
	
				return result;// 订单已支付
			}
		}

		Long updateTime = TimeStampUtil.getNow() / 1000;

		OrderPrices orderPrice = orderPricesService.selectByOrderId(order.getId());

		if (order.getOrderType().equals(Constants.ORDER_TYPE_0)) {

			// 2016年4月29日11:13:04 钟点工订单，已支付状态为 2
			order.setOrderStatus(Constants.ORDER_HOUR_STATUS_2);
			order.setUpdateTime(updateTime);
			ordersService.updateByPrimaryKeySelective(order);
			// 插入订单日志
			OrderLog orderLog = orderLogService.initOrderLog(order);
			orderLog.setAction(Constants.ORDER_ACTION_PAY);
			orderLog.setUserId(userId);
			orderLog.setUserName(u.getMobile());
			orderLog.setUserType((short)0);
			orderLogService.insert(orderLog);
			// 记录用户消费明细
			userDetailPayService.addUserDetailPayForOrder(u, order, orderPrice, tradeStatus, tradeNo, payAccount);

			orderPayService.orderPaySuccessToDoForHour(u.getId(), order.getId(), false);
		}

		if (order.getOrderType().equals(Constants.ORDER_TYPE_1)) {

			order.setOrderStatus(Constants.ORDER_HOUR_STATUS_2);
			order.setUpdateTime(updateTime);
			ordersService.updateByPrimaryKeySelective(order);
			// 插入订单日志
			OrderLog orderLog = orderLogService.initOrderLog(order);
			orderLog.setAction(Constants.ORDER_ACTION_PAY);
			orderLog.setUserId(userId);
			orderLog.setUserName(u.getMobile());
			orderLog.setUserType((short)0);
			orderLogService.insert(orderLog);
			// 记录用户消费明细
			userDetailPayService.addUserDetailPayForOrder(u, order, orderPrice, tradeStatus, tradeNo, payAccount);

			orderPayService.orderPaySuccessToDoForDeep(order);
		}
		
		if(shareUserId!=null && !shareUserId.equals("")){
			List<OrderShare> orderShareList = orderShareService.selectByShareId(Integer.parseInt(shareUserId));
			OrderShare os = orderShareService.selectByShareIdAndUserId(Integer.parseInt(shareUserId), userId.intValue());
			if(orderShareList!=null && orderShareList.size()>0 && os==null){
				OrderShare orderShare = orderShareList.get(0);
				userCouponService.shareSuccessSendCoupons(orderShare,order.getUserId());
				orderShare.setUserId(userId.intValue());
				orderShare.setOrderNo(orderNo);
				orderShare.setOrderId(order.getId().intValue());
				orderShareService.updateByPrimaryKeySelective(orderShare);
			}
		}
		
		return result;

	}

	// 7. 订单在线支付成功同步接口
	/**
	 * 订单补差价在线支付成功同步接口
	 * 
	 * @param mobile
	 *            true string 手机号
	 * @param order_no
	 *            true string 订单号
	 * @param pay_type
	 *            true int 0 = 余额支付 1 = 支付宝 2 = 微信支付 3 = 智慧支付 4 = 上门刷卡（保留，站位）
	 * @param notify_id
	 *            true string 通知ID
	 * @param notify_time
	 *            true string 通知时间
	 * @param trade_no
	 *            true string 流水号
	 * @param trade_status
	 *            true string 支付状态
	 *            支付宝客户端成功状态为: TRADE_FINISHED 或者为 TRADE_SUCCESS
	 *            支付宝网页版成功状态为: success
	 *            微信支付成功的状态为: SUCCESS
	 */
	@RequestMapping(value = "online_pay_notify_ext", method = RequestMethod.POST)
		public AppResultData<Object> OnlinePayExt(
				@RequestParam(value = "user_id", defaultValue = "0") Long userId,
				@RequestParam(value = "mobile", defaultValue = "0")	String mobile,
				@RequestParam("order_no_ext") String orderNoExt,
				@RequestParam("pay_type") Short payType,
				@RequestParam(value = "pay_order_type", required = false, defaultValue = "3") int payOrderType,
				@RequestParam("notify_id") String notifyId,
				@RequestParam("notify_time") String notifyTime,
				@RequestParam("trade_no") String tradeNo,
				@RequestParam("trade_status") String tradeStatus,
				@RequestParam(value = "pay_account", required = false, defaultValue="") String payAccount,
				@RequestParam(value = "body", required = false, defaultValue = "") String shareUserId) {
			
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
			
			// payOrderType 订单支付类型 0 = 订单支付 1= 充值支付 2 = 手机话费类充值 3 = 订单补差价
			OrderPriceExt orderPriceExt = orderPriceExtService.selectByOrderNoExt(orderNoExt);
			
			if (orderPriceExt == null) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.ORDER_PAY_NOT_SUCCESS_MSG);
				return result;
			}
			Orders order = ordersService.selectByPrimaryKey(orderPriceExt.getOrderId());

			if (order == null) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.ORDER_PAY_NOT_SUCCESS_MSG);
				return result;
			}
					
			Users u = userService.selectByPrimaryKey(order.getUserId());
			
			// 判断是否为注册用户，非注册用户返回 999
			if (u == null) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
				return result;
			}

			
			if (orderPriceExt != null && orderPriceExt.getOrderStatus() == 2) {
				//更新付款用户账号名
				if (payAccount != null && !payAccount.equals("")) {
					userDetailPayService.updateByPayAccount(tradeNo, payAccount);
				}
				return result;// 订单已支付
			}
			
			//2记录用户消费明细
			userDetailPayService.addUserDetailPayForOrderPayExt(u, order, orderPriceExt, tradeStatus, tradeNo, payAccount);
			
			//更新订单差价为已支付
			orderPriceExt.setOrderStatus(2);
			orderPriceExt.setUpdateTime(TimeStampUtil.getNowSecond());
			orderPriceExtService.updateByPrimaryKey(orderPriceExt);
			
			//更新通知服务人员.
			orderPayService.orderPaySuccessToDoOrderPayExt(order, orderPriceExt);
			
			if(shareUserId!=null && !shareUserId.equals("")){
				List<OrderShare> orderShareList = orderShareService.selectByShareId(Integer.parseInt(shareUserId));
				OrderShare os = orderShareService.selectByShareIdAndUserId(Integer.parseInt(shareUserId), userId.intValue());
				if(orderShareList!=null && orderShareList.size()>0 && os==null){
					OrderShare orderShare = orderShareList.get(0);
					userCouponService.shareSuccessSendCoupons(orderShare,order.getUserId());
					orderShare.setUserId(userId.intValue());
					orderShare.setOrderNo(order.getOrderNo());
					orderShare.setOrderId(order.getId().intValue());
					orderShareService.updateByPrimaryKeySelective(orderShare);
				}
			}
			
			return result;

		}
	
	/*
	 * 充值卡在線支付寶支付
	 * 
	 * */
	@RequestMapping(value = "online_pay_cardOrder_notify", method = RequestMethod.POST)
	public AppResultData<Object> OnlinePayCardOrder(@RequestParam(value = "user_id", defaultValue = "0") Long userId,
			@RequestParam(value = "mobile", defaultValue = "0") String mobile, @RequestParam("order_no") String orderNo,
			@RequestParam("pay_type") Short payType, @RequestParam(value = "pay_order_type", required = false, defaultValue = "1") int payOrderType,
			@RequestParam("notify_id") String notifyId, @RequestParam("notify_time") String notifyTime, @RequestParam("trade_no") String tradeNo,
			@RequestParam("trade_status") String tradeStatus, @RequestParam(value = "pay_account", required = false, defaultValue = "") String payAccount) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		// 判断如果不是正确支付状态，则直接返回.
		Boolean paySuccess = OneCareUtil.isPaySuccess(tradeStatus);
		if (paySuccess == false) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ORDER_PAY_NOT_SUCCESS_MSG);
			return result;
		} else if (tradeStatus.equals("WAIT_BUYER_PAY")) {
			result.setStatus(Constants.SUCCESS_0);
			result.setMsg(ConstantMsg.ORDER_PAY_WAIT_MSG);
			return result;
		}

		// payOrderType 订单支付类型 0 = 订单支付 1= 充值支付 2 = 手机话费类充值 3 = 订单补差价
		OrderCards orderCards = orderCardsService.selectByOrderCardsNo(orderNo);

		if (orderCards == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ORDER_PAY_NOT_SUCCESS_MSG);
			return result;
		}
		if (orderCards != null && orderCards.getOrderStatus().equals(Constants.PAY_STATUS_1)) {
			return result;
		}

		Users u = userService.selectByPrimaryKey(orderCards.getUserId());

		// 判断是否为注册用户，非注册用户返回 999
		if (u == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}

		Long updateTime = TimeStampUtil.getNow() / 1000;

		orderCards.setOrderStatus(Constants.PAY_STATUS_1);
		orderCards.setUpdateTime(updateTime);
		// 更新orders,orderPrices,Users,插入消费明细UserDetailPay
		orderCardsService.updateOrderByOnlinePay(orderCards, tradeNo, tradeStatus, payAccount);
		
		//赠送相应的优惠劵到对于的用户账户
		orderCardsService.sendCoupons(u.getId(), orderCards.getId());
		
		/*
		 * 2016年4月15日17:06:44  新增短信
		 * 
		 * 充值成功短信通知
		 * 
		 */
		//充值时间
		
		String serviceTime = TimeStampUtil.timeStampToDateStr(TimeStampUtil.getNow(), "MM月-dd日HH:mm");
		
		//充值金额
		BigDecimal value = orderCards.getCardPay();
		
		String[] paySuccessForUser = new String[] {serviceTime,value.toString()};
		
		SmsUtil.SendSms(u.getMobile(),  Constants.MESSAGE_CHARGE_PAY_SUCCESS, paySuccessForUser);

		return result;

	}
	
	//定制支付
	@RequestMapping(value = "online_pay_period_notify", method = RequestMethod.POST)
	public AppResultData<Object> OnlinePayPeriodOrder(@RequestParam(value = "user_id", defaultValue = "0") Long userId,
			@RequestParam(value = "mobile", defaultValue = "0") String mobile, @RequestParam("order_no") String orderNo,
			@RequestParam("pay_type") Short payType, @RequestParam(value = "pay_order_type", required = false, defaultValue = "1") int payOrderType,
			@RequestParam("notify_id") String notifyId, @RequestParam("notify_time") String notifyTime, @RequestParam("trade_no") String tradeNo,
			@RequestParam("trade_status") String tradeStatus, @RequestParam(value = "pay_account", required = false, defaultValue = "") String payAccount,
			@RequestParam(value = "body", required = false, defaultValue = "") String shareUserId) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		// 判断如果不是正确支付状态，则直接返回.
		Boolean paySuccess = OneCareUtil.isPaySuccess(tradeStatus);
		if (paySuccess == false) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ORDER_PAY_NOT_SUCCESS_MSG);
			return result;
		} else if (tradeStatus.equals("WAIT_BUYER_PAY")) {
			result.setStatus(Constants.SUCCESS_0);
			result.setMsg(ConstantMsg.ORDER_PAY_WAIT_MSG);
			return result;
		}

		// payOrderType 订单支付类型 0 = 订单支付 1= 充值支付 2 = 手机话费类充值 3 = 订单补差价 4=定制
		PeriodOrder periodOrder = periodOrderService.selectByOrderNo(orderNo);

		if (periodOrder == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ORDER_PAY_NOT_SUCCESS_MSG);
			return result;
		}

		Users u = userService.selectByPrimaryKey(periodOrder.getUserId().longValue());

		// 判断是否为注册用户，非注册用户返回 999
		if (u == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}

		if (periodOrder != null ) {
			if (periodOrder.getOrderStatus()!=1) {
				// 更新付款用户账号名
				if (payAccount != null && !payAccount.equals("")) {
					userDetailPayService.updateByPayAccount(tradeNo, payAccount);
				}
	
				return result;// 订单已支付
			}
		}

		Long updateTime = TimeStampUtil.getNow() / 1000;

		OrderPrices orderPrice = orderPricesService.selectByOrderNo(orderNo);

		// 2016年4月29日11:13:04 钟点工订单，已支付状态为 2
		periodOrder.setOrderStatus(2);
		periodOrder.setUpdateTime(updateTime);
		periodOrderService.updateByPrimaryKeySelective(periodOrder);
		// 插入订单日志
		OrderLog orderLog = orderLogService.initOrderLog(periodOrder);
		orderLog.setAction(Constants.PERIOD_ORDER_ACTION_PAY);
		orderLog.setUserId(userId);
		orderLog.setUserName(u.getMobile());
		orderLog.setUserType((short)0);
		orderLogService.insert(orderLog);
		// 记录用户消费明细
		userDetailPayService.addUserDetailPayForOrder(u, periodOrder, orderPrice, tradeStatus, tradeNo, payAccount);

		if(shareUserId!=null && !shareUserId.equals("")){
			List<OrderShare> orderShareList = orderShareService.selectByShareId(Integer.parseInt(shareUserId));
			OrderShare os = orderShareService.selectByShareIdAndUserId(Integer.parseInt(shareUserId), userId.intValue());
			if(orderShareList!=null && orderShareList.size()>0 && os==null){
				OrderShare orderShare = orderShareList.get(0);
				userCouponService.shareSuccessSendCoupons(orderShare,periodOrder.getUserId().longValue());
				orderShare.setUserId(userId.intValue());
				orderShare.setOrderNo(orderNo);
				orderShare.setOrderId(periodOrder.getId().intValue());
				orderShareService.updateByPrimaryKeySelective(orderShare);
			}
		}
		
		return result;

	}
}
