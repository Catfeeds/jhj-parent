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
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.newDispatch.NewDispatchStaffService;
import com.jhj.service.order.DispatchStaffFromOrderService;
import com.jhj.service.order.OrderHourAddService;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPayService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OrderViewVo;
import com.jhj.vo.order.OrgStaffsNewVo;
import com.meijia.utils.MathBigDeciamlUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/app/order")
public class OrderPayController extends BaseController {

	@Autowired
	UsersService userService;

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private OrderPricesService orderPricesService;

	@Autowired
	private OrderPayService orderPayService;

	@Autowired
    private OrderQueryService orderQueryService;

	@Autowired
	OrderLogService orderLogService;
	
	@Autowired
	private UserDetailPayService userDetailPayService;
	
	@Autowired
	private UserCouponsService userCouponsService;	
	
	@Autowired
	private OrderHourAddService orderHourAddservice;	
	
	@Autowired
	private DictCouponsService dictCouponsService;	
	
	@Autowired
	private DispatchStaffFromOrderService dispatchStaffFromOrderService;

	@Autowired
	private NewDispatchStaffService newDisStaService;
	
	@Autowired
	private OrgStaffsService orgStaService;
	
	// 17.订单支付前接口
	/**
	 * @param mobile true string 手机号 
	 * @param order_id true int 订单id 
	 * @param order_no true string 订单号
	 * @param pay_type true int 支付方式： 0 = 余额支付 1 = 支付宝 2 = 微信支付 3 = 智慧支付(保留,暂不开发) 4 = 上门刷卡（保留，暂不开发） 
	 * @return  OrderViewVo
	 */
	@RequestMapping(value = "post_pay", method = RequestMethod.POST)
	public AppResultData<Object> postPay(
			@RequestParam("user_id") Long userId, 
			@RequestParam("order_no") String orderNo, 
			@RequestParam("order_pay_type") Short orderPayType,
			@RequestParam(value = "user_coupon_id", required = false, defaultValue="0") Long userCouponId) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		Users u = userService.getUserById(userId);

		// 判断是否为注册用户，非注册用户返回 999
		if (u == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}		
		
		Orders order = ordersService.selectByOrderNo(orderNo);
		Long orderId = order.getId();
		if (order == null) return result;
		
		if(order.getOrderStatus() >= 4){
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.HAVE_PAY);
			return result;
		}

		OrderPrices orderPrice = orderPricesService.selectByOrderId(orderId);
		
		if (orderPrice == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg("订单不存在");
			return result;			
		}
		
		//此时 orderPay 和 orderMoney 值是相等的
		BigDecimal orderPay = orderPrice.getOrderMoney();
		BigDecimal orderMoney = orderPrice.getOrderMoney();		
		
		//处理优惠劵，判断优惠劵是否有效的问题
		if (userCouponId > 0L) {
			
			AppResultData<Object> validateResult = null;
			validateResult = userCouponsService.validateCouponForPay(userId , userCouponId, order.getId());
			if (validateResult.getStatus() == Constants.ERROR_999) return validateResult;
			
			UserCoupons userCoupon = userCouponsService.selectByPrimaryKey(userCouponId);
			DictCoupons dictCoupons = dictCouponsService.selectByPrimaryKey(userCoupon.getCouponId());
			BigDecimal couponValue = dictCoupons.getValue();
			
			orderPay = MathBigDeciamlUtil.sub(orderMoney, couponValue);
		}
		
		long updateTime = TimeStampUtil.getNowSecond();

		orderPrice.setOrderMoney(orderPrice.getOrderMoney());
		
		
		if (orderPayType.equals(Constants.PAY_TYPE_0)) {
			//1.先判断用户余额是否够支付
			if(u.getRestMoney().compareTo(orderPay) < 0) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.ERROR_999_MSG_5);
				return result;
			}
		}		
		
		//判断当前是否有满足条件阿姨，没有则返回提示信息.
		Short orderType = order.getOrderType();
		List<OrgStaffs> orgStaffs = new ArrayList<OrgStaffs>();
		List<OrgStaffsNewVo> orgStaffsNewVos = new ArrayList<OrgStaffsNewVo>();
		
		
		// jhj2.1 基础保洁类订单
		if (orderType.equals(Constants.ORDER_TYPE_0)) {
			
//			Long addrId = order.getAddrId();
//			
//			Long startTime = order.getServiceDate();
//			Long endTime = startTime+order.getServiceHour()*3600;
			//jhj2.0派工逻辑
//			orgStaffsNewVos = dispatchStaffFromOrderService.getNewBestStaffForHour(startTime, endTime, order.getAddrId(), orderId);
			
//			if (orgStaffsNewVos.isEmpty()) {
//				
//				//2015-10-16 16:10:34   新需求： 无可用派工,让用户联系助理, 此处用 102 错误码，区分这种情况
//				result.setStatus(Constants.ERROR_102);
//				result.setMsg("当前无可用的派工,请更换您的服务时间.");
//				return result;				
//			}
		}
		
		orderPrice.setOrderPay(orderPay);
		orderPrice.setPayType(orderPayType);
		orderPrice.setUpdateTime(updateTime);
		orderPrice.setCouponId(userCouponId);
		orderPricesService.updateByPrimaryKey(orderPrice);
		
		//如果是余额支付或者需支付金额为0 ,或者是 现金支付
		if (orderPayType.equals(Constants.PAY_TYPE_0) || 
			orderPayType.equals(Constants.PAY_TYPE_6) ||
			orderPay.compareTo(new BigDecimal(0)) == 0) {
			// 1. 扣除用户余额.
			// 2. 用户账号明细增加.
			// 3. 订单状态变为已支付.
			// 4. 订单日志
			if (orderPayType.equals(Constants.PAY_TYPE_0)) {
				u.setRestMoney(u.getRestMoney().subtract(orderPay));
				u.setUpdateTime(updateTime);
				userService.updateByPrimaryKeySelective(u);
			}
			
			/**
			 * 2016年3月24日19:11:16  
			 * 		
			 * 		修改已支付 状态时。。  
			 * 
			 * 	jhj2.1	钟点工(基础保洁类) 修改为 2、  助理单修改为 4
			 */
			
			//TODO  jhj2.1  订单 类型？？？
			if(order.getOrderType() == Constants.ORDER_TYPE_2){
				
				order.setOrderStatus(Constants.ORDER_AM_STATUS_3);//已支付
			}
			
			if(order.getOrderType() == Constants.ORDER_TYPE_0){
				
				order.setOrderStatus(Constants.ORDER_HOUR_STATUS_2);//已支付
			}
			
			
			// 修改 24小时已支付 的助理单，需要用到这个 修改时间
			order.setUpdateTime(TimeStampUtil.getNowSecond());
			ordersService.updateByPrimaryKeySelective(order);
			
			//记录订单日志.
			OrderLog orderLog = orderLogService.initOrderLog(order);
			orderLogService.insert(orderLog);
			
			//记录用户消费明细
			userDetailPayService.addUserDetailPayForOrder(u, order, orderPrice, "", "", "");
			
			//订单支付成功后
			if (order.getOrderType().equals(Constants.ORDER_TYPE_0)) {
				orderPayService.orderPaySuccessToDoForHour(u.getId(), order.getId(), orgStaffsNewVos, false);
				
			}
			
			if (order.getOrderType().equals(Constants.ORDER_TYPE_1)) {
				orderPayService.orderPaySuccessToDoForDeep(order);
			}

			if (order.getOrderType().equals(Constants.ORDER_TYPE_2)) {
				
				/*
				 *  2016年3月30日11:48:14  流程 已经变化。 在 支付后 才会 手动派工。
				 *  
				 *   此处不再 有后续。。 改为 在 oa 手动派工后，处理逻辑
				 */
				
//				orderPayService.orderPaySuccessToDoForAm(order);
			}
		}
		
		OrderViewVo orderViewVo = orderQueryService.getOrderView(order);
		result.setData(orderViewVo);
		
		return result;
	}

}
