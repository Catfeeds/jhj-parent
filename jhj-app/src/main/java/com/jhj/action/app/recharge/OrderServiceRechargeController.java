package com.jhj.action.app.recharge;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.UserRefAm;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.service.users.UsersService;
import com.meijia.utils.OrderNoUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.serviceCharge.PhoneReChargeUtil;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年10月13日下午3:54:17
 * @Description: 
 *	
 *		微网站 -- 充值系列 接口
 *		
 *			1* 针对  话费、水电煤气缴费，类似业务的 充值
 *          2* 不同于 用户余额的充值
 *          	余额充值，order_type = 4
 *          	话费系列， order_type = 6
 *          
 */
@Controller
@RequestMapping(value = "/app/order")
public class OrderServiceRechargeController extends BaseController {
	
	@Autowired
	private UsersService userService;
	@Autowired
	private UserRefAmService userRefAmService;
	@Autowired
	private OrdersService orderService;
	@Autowired
	private OrderPricesService orderPriceService;
	@Autowired
	private OrderLogService orderLogService;
	@Autowired
	private OrgStaffsService orgStaService;
	@Autowired
	private UserCouponsService userCouponService;
	@Autowired
	private DictCouponsService dictCouponService;
	
	
	/*
	 *  手机话费充值接口
	 *  
	 * 	@param userId  			用户Id
	 *  @param orderType 		 订单类型，设置为 6, 表示业务类似的 充值类 订单, 有别于 order_type 为 4 的 用户余额充值类订单
	 *  @param chargeMobile  	 需充值的 手机号
	 *  @param chargeMoney		充值金额
	 *  @param payType			用户的支付方式,目前只有 微信支付，payType 默认为2
	 *  
	 *  @param serviceType		服务类型，话费充值 为 12
	 *  @param orderFrom		订单来源，目前都是 微网站，默认值为 1
	 *  @param couponId    	对应user_coupons表的主键 id, 而不是 dict_coupons 表的 主键id !!!!!!!
	 */
	@RequestMapping(value = "recharge_submit",method = RequestMethod.POST)
	public AppResultData<Object> rechargeSubmit(
			@RequestParam("userId") Long userId,
			@RequestParam("orderType") Short orderType,
			@RequestParam("chargeMobile") String chargeMobile,
			@RequestParam("chargeMoney") BigDecimal chargeMoney,
			@RequestParam("realMoney") BigDecimal realMoney,
			@RequestParam("payType") Short payType,
			@RequestParam("serviceType") Long serviceType,
			@RequestParam(value = "couponId", required = false, defaultValue = "0") Long couponId,
			@RequestParam(value = "orderFrom", required = false, defaultValue = "1") Short orderFrom) throws JSONException{
		
		AppResultData<Object> result = new AppResultData<Object>( Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");	
		
		//调用服务商接口，判断当前 账号、金额是否可以充值
		String response = PhoneReChargeUtil.isRecharge(chargeMobile, chargeMoney);
		
		JSONObject jsonObject = new JSONObject(response);
		
		String code = jsonObject.getString("Code");
		
		if(!code.equals("0")){
			result.setStatus(Constants.ERROR_999);
			result.setMsg("暂不支持该号码的充值");
			
			return result;
		}
		
		/**
		 * 2015-11-18 14:36:29  
		 * 	
		 *  如果我方 账户 余额不足，给出提示：
		 * 
		 */
		String myRestMoney = PhoneReChargeUtil.myRestMoney();
		
		
		net.sf.json.JSONObject fromObject = net.sf.json.JSONObject.fromObject(myRestMoney);
		
		if(fromObject.containsKey("Data")){
			
			Object object = fromObject.getJSONObject("Data").get("UserBalance");
			
			if(Double.valueOf(object.toString())<= 500){
				//如果账户余额不足 500, 则提示用户 系统繁忙~~
				
				result.setStatus(Constants.ERROR_999);
				result.setMsg("系统维护中,请稍候");
				return result;
			}
		}
		
		
		
		Users u = userService.selectByPrimaryKey(userId);

		// 判断是否为注册用户，非注册用户返回 999
		if (u == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}
		
		/*
		 * 对于 话费充值类订单，  orders表 会有一些没什么用的 字段，诸如 amId,orgId 等等
		 * 		为防bug, 特设置
		 * 			 新的 order_type = 6 
		 * 			 新的 order_status = 13 ,缴费中
		 *  其余字段，能设置的尽量给出
		 */
		
		UserRefAm userRefAm = userRefAmService.selectByAmId(userId);
		// 调用公共订单号类，生成唯一订单号
		String orderNo = String.valueOf(OrderNoUtil.genOrderNo());
				
		//保存订单信息 
		Orders order = orderService.initOrders();
		order.setMobile(u.getMobile());
		order.setUserId(userId);
		order.setAmId(userRefAm.getStaffId());
		
		order.setOrderType(orderType);
		order.setServiceDate(TimeStampUtil.getNowSecond());
		
		/*
		 * 与之前的 业务 区分，新设置 serviceType 11,表示 话费充值
		 * 
		 * 	 TODO 未来可能会有  水电煤缴费、等 类似的充值类业务， 此处新增业务类型，方便日后 业务列表使用
		 */
		order.setServiceType(serviceType);
		
		//服务内容字段，设置为  充值号码
		order.setServiceContent(chargeMobile);
		
		//与之前的 业务 区分，新设置  13,14,15 三种表示 话费充值过程中的  订单 状态
		order.setOrderStatus(Constants.ORDER_STATUS_13);
		order.setOrderNo(orderNo);
		
		/* !!!!!
		 *  2015-11-2 18:51:12 
		 *  
		 *  为了区分 充值过程中的 各种订单状态, 以及 配合 mybatis 框架 list 参数的处理
		 * 		
		 * 	决定： 话费充值过程中的 每一步 orders 操作, 均设置 相关的 remarks 值, 
		 * 
		 * 			不能为空且最好 设置为 数字值 （mybatis）		
		 * 
		 *  说明：  remarks   
		 *  			10000  ,此时order_status 可能为 13或16(支付超时) -->  取消充值
		 * 				10001  ,此时 order_status 可能为 13或16 -->  表示  微信支付成功,但 充值失败（网络原因等~）				
		 * 				10002  ,此时 order_status 为 14	--> 微信支付成功,充值成功	
		 * 				10003  ,此时 order_status 为15  --> 微信支付成功,充值失败（第三方服务商问题等~有回调,但失败）
		 */
		
		order.setRemarks("10000");
		
//		order.setRemarks(chargeMobile);
		
		OrgStaffs staffs = orgStaService.selectByPrimaryKey(userRefAm.getStaffId());
		order.setOrgId(staffs.getOrgId());
		
		//mybatis xml 需要增加插入后获取last_insert_id;
		orderService.insert(order);
		
		
		/*
		 * 订单价格表
		 */
		
		OrderPrices orderPrice = orderPriceService.initOrderPrices();
		
		orderPrice.setUserId(userId);
		orderPrice.setMobile(u.getMobile());
		orderPrice.setOrderId(order.getId());
		orderPrice.setOrderNo(orderNo);
		
		//coupon 优惠券
		
		/*
		 * 2015-11-3 10:52:54 这里 只能 默认 优惠券 有效, 此时 还未生成 order_price 记录，不能校验
		 */
		if (couponId > 0L) {
			
//			AppResultData<Object> validateResult = null;
//			validateResult = userCouponService.validateCouponForPay(userId , couponId, order.getId());
//			if (validateResult.getStatus() == Constants.ERROR_999) return validateResult;
			
			UserCoupons userCoupon = userCouponService.selectByPrimaryKey(couponId);
			
			//这才是真正的 dictCouponId
			orderPrice.setCouponId(userCoupon.getId());
			
		}else{
			orderPrice.setCouponId(couponId);
		}
		
		orderPrice.setPayType(payType);
		orderPrice.setOrderMoney(chargeMoney);	
		
		/*
		 * 实际支付金额 ： 不同于其他类型的订单，实际金额 与  总金额 等值
		 * 
		 * 	涉及到优惠金额，故而 实际支付金额，在生成订单时，就设置好
		 */
		orderPrice.setOrderPay(realMoney);
		
		orderPriceService.insert(orderPrice);
		
		
		/*
		 * 订单日志
		 */
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLogService.insert(orderLog);

		
		result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, order);
		System.out.println("======== order create =============");
		
		return result;
	}
	
	/*
	 * 
	 *  手机话费充值成功后的 回调  callback_url
	 * 		
	 * 		能且只能 从这里 判断  充值是否成功
	 * 		关于callback_url参数的说明：
				订单充值成功后，我们会向您传递的callback_url发送请求，请求信息如下：
				请求方式： GET
				请求参数：
				state         string     充值状态（0为充值中 1为成功 其他为失败）
				orderid     string     商家订单号 
				ordertime string     订单处理时间 (格式为：yyyyMMddHHmmss  如：20150323140214）)
				sign          string     32位小写md5签名：md5(apix-key + orderid+ ordertime)
				err_msg    string     充值失败时候返回失败信息。成功时为空。
				
				callback_url得到请求信息后，请自行处理系统订单状态。如没收到返回结果，或者在长时间内没有收到充值成功信息，
				可再次发送此笔订单的查询接口查看充值状态(见订单状态查询接口)，如还是没有充值成功请和APIX客服联系，以做进一步处理。
	 * 
	 * 
	 */
	@RequestMapping(value = "recharge_real.json",method = RequestMethod.GET)
	public AppResultData<Object> afterWXPayRecharge(
			HttpServletRequest request,
			@RequestParam("state") String state,
			@RequestParam("orderid") String orderNo,
			@RequestParam("ordertime") String orderTime,
			@RequestParam("sign") String sign,
			@RequestParam("err_msg") String errMsg){
		
		System.out.println("=======apix Start========");
		
		
		AppResultData<Object> result = new AppResultData<Object>( Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");	
		
		System.out.println("!!===!!====!!==CallBack Start==!!===!!====="+state);
		
//		System.out.println("失败原因是:"+request.getParameterValues("err_msg").toString());
		
		Orders order = orderService.selectByOrderNo(orderNo);
		if(state.equals("1")){
			//充值成功
			//修改订单状态
			System.out.println("!!!!!!!!! SUCCESS  PAY !!!!!!!!!!!!!");
			System.out.println("充值成功！");
			
			order.setRemarks("10002");
			order.setOrderStatus(Constants.ORDER_STATUS_14);
			
		}else{
			System.out.println("!!!!!!! Fail Pay !!!!!!!!!");
			//充值失败
			System.out.println("充值失败");
			
			order.setRemarks("10003");
			order.setOrderStatus(Constants.ORDER_STATUS_15);
		}
		System.out.println("充值结束");
		
		order.setUpdateTime(TimeStampUtil.getNowSecond());
		
		/*
		 * 如果有优惠劵，则需要将优惠劵变成已使用。
		 * 
		 * 	优惠券 无论充值成功失败。都应该设置为已使用
		 */
		OrderPrices orderPrice = orderPriceService.selectByOrderNo(orderNo);
		Long userCouponId = orderPrice.getCouponId();
		if (userCouponId > 0L) {
			UserCoupons userCoupon = userCouponService.selectByPrimaryKey(userCouponId);
			userCoupon.setIsUsed((short) 1);
			userCoupon.setUsedTime(TimeStampUtil.getNowSecond());
			userCoupon.setOrderNo(order.getOrderNo());
			userCouponService.updateByPrimaryKey(userCoupon);
		}
	
		
		orderService.updateByPrimaryKeySelective(order);
		
		
		//记录订单日志.
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLogService.insert(orderLog);
		
		return result;
	}
	
 	
	
}
