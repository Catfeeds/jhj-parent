package com.jhj.action.app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.order.OrderCards;
import com.jhj.service.dict.CardTypeService;
import com.jhj.service.order.OrderCardsService;
import com.jhj.service.users.UsersService;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/app/user")
public class UserCardPayController extends BaseController {

	@Autowired
	private UsersService usersService;
	@Autowired
	private OrderCardsService orderCardsService;
	@Autowired
	private CardTypeService cardTypeService;

	// 8. 会员充值在线支付成功同步接口
	/**
	 * 会员充值在线支付成功同步接口
	 * @param mobile  手机号
	 * @param card_order_no  订单号
	 * @param pay_type 支付类型 0 = 余额支付 1 = 支付宝 2 = 微信支付 3 = 智慧支付 4 = 上门刷卡（保留，站位）
	 * @param notify_id  通知ID
	 * @param notify_time 通知时间
	 * @param trade_no 流水号
	 * @param trade_status true 支付状态
	 * 											 支付宝客户端成功状态为: TRADE_FINISHED 或者为 TRADE_SUCCESS
	 *                                           支付宝网页版成功状态为: success
	 *                                           微信支付成功的状态为: SUCCESS
	 *
	 * 1. 判断必选参数
	 * 2. 根据card_order_no,从order_cards找出对应的订单
	 * 3. 判断该订单是否已经支付完成
	 * 1) 判断字段order_status = 1
	 * 4. 如果该订单为未支付的状态，则需要做如下的操作
	 * 1) 操作表user_pay_status ，插入一条新的记录，记录支付的信息
	 * 2) 用户的消费明细记录，操作表为user_detail_pay
	 * 3) 将card_orders 表的状态改变为 order_status = 2 ,已支付状态, 支付状态pay_type = 1 支付宝支付
	 * 4) 用户余额，扣除相应的金额，注意如果有优惠卷的金额，操作表为users
	 * 注意以上4个步骤必须为同一个事务	 *
	 */
	@RequestMapping(value = "card_online_pay", method = RequestMethod.POST)
	public AppResultData<Object> cardOnlinePay(
		@RequestParam(value = "user_id", defaultValue = "0") Long userId, 
		@RequestParam("card_order_no") 	String cardOrderNo,
		@RequestParam("pay_type") 			Short payType,
		@RequestParam("notify_id") 			String notifyId,
		@RequestParam("notify_time") 		String notifyTime,
		@RequestParam("trade_no") 			String tradeNo,
		@RequestParam("trade_status") 		String tradeStatus,
		@RequestParam(value = "pay_account", required = false, defaultValue="") String payAccount) {

		AppResultData<Object> result = new AppResultData<Object>( Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG,"");

		//判断如果不是正确支付状态，则直接返回.
		Boolean paySuccess = OneCareUtil.isPaySuccess(tradeStatus);
		if (paySuccess == false )  {
			return result;
		}

		OrderCards orderCards = orderCardsService.selectByOrderCardsNo(cardOrderNo);


		userId = orderCards.getUserId();
		
		//如果已经付款，则直接返回
		if (orderCards != null && orderCards.getOrderStatus().equals(Constants.PAY_STATUS_1)) {
			return result;
		}
		
		orderCards.setPayType(payType);
		orderCards.setOrderStatus(Constants.PAY_STATUS_1);
		// 更新orders,orderPrices,Users,插入消费明细UserDetailPay
		orderCardsService.updateOrderByOnlinePay(orderCards, tradeNo, tradeStatus, payAccount);
		
		//赠送相应的优惠劵到对于的用户账户
		orderCardsService.sendCoupons(userId, orderCards.getId());
		
		return result;

	}
}
