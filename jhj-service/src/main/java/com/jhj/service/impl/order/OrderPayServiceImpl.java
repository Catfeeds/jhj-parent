package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderAppoint;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.dict.ServiceAddonsService;
import com.jhj.service.order.OrderAppointService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderHourAddService;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPayService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderServiceAddonsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.service.users.UserPushBindService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderServiceAddonViewVo;
import com.jhj.vo.order.OrgStaffDispatchVo;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.serviceCharge.PhoneReChargeUtil;

@Service
public class OrderPayServiceImpl implements OrderPayService {
	@Autowired
	private OrdersService ordersService;

	@Autowired
	private OrderPricesService orderPricesService;

	@Autowired
	private OrderHourAddService orderHourAddservice;

	@Autowired
	private UserDetailPayService userDetailPayService;

	@Autowired
	private UserAddrsService userAddrService;

	@Autowired
	OrderLogService orderLogService;

	@Autowired
	private UsersService userService;

	@Autowired
	private OrderDispatchsService orderDispatchService;

	@Autowired
	private OrderServiceAddonsService orderServiceAddonsService;

	@Autowired
	private ServiceAddonsService serviceAddonsService;

	@Autowired
	private OrgStaffsService orgStaffService;

	@Autowired
	private UserCouponsService userCouponsService;

	@Autowired
	private UserPushBindService bindService;

	@Autowired
	private PartnerServiceTypeService partnerServiceTypeService;
	
	@Autowired
    private OrderAppointService orderAppointService;
	
	/**
	 * 钟点工订单支付成功,后续通知功能
	 * 1. 进行派工操作
	 * 2. 进行发送短信
	 * 
	 * @param isChangeDispatch
	 *            是否换人，如果是换人，则需要将原有派工取消，再重新派工.
	 */
	@Override
	public boolean orderPaySuccessToDoForHour(Long userId, Long orderId,  boolean isChangeDispatch) {

		Orders order = ordersService.selectByPrimaryKey(orderId);
		Long addrId = order.getAddrId();
		// 4)如果有优惠劵，则需要将优惠劵变成已使用。
		OrderPrices orderPrice = orderPricesService.selectByOrderId(orderId);
		Long userCouponId = orderPrice.getCouponId();
		if (userCouponId > 0L) {
			UserCoupons userCoupon = userCouponsService.selectByPrimaryKey(userCouponId);
			userCoupon.setIsUsed((short) 1);
			userCoupon.setUsedTime(TimeStampUtil.getNowSecond());
			userCoupon.setOrderNo(order.getOrderNo());
			userCouponsService.updateByPrimaryKey(userCoupon);
		}
		
		Long serviceTypeId = order.getServiceType();
		// 增加判断，是否可以自动派工.
		PartnerServiceType serviceType = partnerServiceTypeService.selectByPrimaryKey(serviceTypeId);

		if (serviceType == null)
			return false;

		if (serviceType.getIsAuto().equals((short) 0))
			return false;
		
		
		
		List<Long> appointStaffIds = new ArrayList<Long>();
		Long serviceDate = order.getServiceDate();
		Double serviceHour = (double) order.getServiceHour();
		
		//找出是否有指定的阿姨。
		OrderDispatchSearchVo orderDispatchSearchVo = new OrderDispatchSearchVo();
		orderDispatchSearchVo.setOrderId(orderId);
		
		List<OrderAppoint> orderAppoints = orderAppointService.selectBySearchVo(orderDispatchSearchVo);
		
		if (!orderAppoints.isEmpty()) {
			for (OrderAppoint oa : orderAppoints) {
				appointStaffIds.add(oa.getStaffId());
			}
		}
		
		int staffNums = order.getStaffNums();
		if (staffNums <= 0) staffNums = 1;
		// 实现派工逻辑，找到 阿姨 id, 或者返回 错误标识符
		
		List<OrgStaffDispatchVo> autoStaffs = orderDispatchService.autoDispatch(addrId, serviceTypeId, serviceDate, serviceHour, staffNums, appointStaffIds);
		
		if (autoStaffs.isEmpty()) return false;

		// 进行派工
		OrgStaffs staff = null;
		for (OrgStaffDispatchVo item : autoStaffs) {
			Long staffId = item.getStaffId();
			int allocate = item.getAllocate();
			String allocateReason = item.getAllocateReason();
			Boolean doOrderDispatch = orderDispatchService.doOrderDispatch(order, serviceDate, serviceHour, staffId, allocate, allocateReason);
			order.setOrgId(item.getOrgId());
		}
		
		order.setOrderStatus(Constants.ORDER_HOUR_STATUS_3);// 更新订单状态---已派工
		order.setUpdateTime(TimeStampUtil.getNowSecond());// 修改 24小时已支付
		ordersService.updateByPrimaryKeySelective(order);

		// 2.插入订单日志表 order_log
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLog.setAction(Constants.ORDER_ACTION_PAY+"-支付成功");
		orderLog.setUserId(userId);
		orderLog.setUserName(order.getMobile());
		orderLog.setUserType((short)0);
		orderLogService.insert(orderLog);

		String beginTimeStr = TimeStampUtil.timeStampToDateStr(order.getServiceDate() * 1000, "MM月-dd日HH:mm");
		String endTimeStr = TimeStampUtil.timeStampToDateStr((long) ((order.getServiceDate() + order.getServiceHour() * 3600) * 1000), "HH:mm");
		String timeStr = beginTimeStr + "-" + endTimeStr;

		// 1) 用户收到派工通知---发送短信
		// String[] contentForUser = new String[] { timeStr };
		// SmsUtil.SendSms(u.getMobile(), "29152", contentForUser);

		// 2)派工成功，为服务人员发送推送消息---推送消息
		

		for (OrgStaffDispatchVo item : autoStaffs) {
			Long staffId = item.getStaffId();
			orderDispatchService.pushToStaff(staffId, "true", "dispatch", orderId, OneCareUtil.getJhjOrderTypeName(order.getOrderType()),
					Constants.ALERT_STAFF_MSG);
			
			//发送短信
			String[] smsContent = new String[] { timeStr };
			SmsUtil.SendSms(item.getMobile(), "114590", smsContent);
		}

		
		return true;
	}

	/**
	 * 深度保洁订单支付成功,后续通知功能
	 * 1. 如果为
	 */
	@Override
	public boolean orderPaySuccessToDoForDeep(Orders order) {
		Long addrId = order.getAddrId();
		// 如果有优惠劵，则需要将优惠劵变成已使用。
		Long orderId = order.getId();
		OrderPrices orderPrice = orderPricesService.selectByOrderId(orderId);
		Long userCouponId = orderPrice.getCouponId();
		if (userCouponId > 0L) {
			UserCoupons userCoupon = userCouponsService.selectByPrimaryKey(userCouponId);
			userCoupon.setIsUsed((short) 1);
			userCoupon.setUsedTime(TimeStampUtil.getNowSecond());
			userCoupon.setOrderNo(order.getOrderNo());
			userCouponsService.updateByPrimaryKey(userCoupon);
		}
		
		Long serviceTypeId = order.getServiceType();
		// 增加判断，是否可以自动派工.
		PartnerServiceType serviceType = partnerServiceTypeService.selectByPrimaryKey(serviceTypeId);

		if (serviceType == null)
			return false;

		if (serviceType.getIsAuto().equals((short) 0))
			return false;
		
		//深度养护需要根据服务子项来计算服务时长，小时
		Double serviceHour = (double) order.getServiceHour();
		
		List<OrderServiceAddons> orderAddons = orderServiceAddonsService.selectByOrderId(orderId);
		
		if (!orderAddons.isEmpty()) {
			serviceHour = (double) 0;
			List<OrderServiceAddonViewVo> orderAddonVos = orderServiceAddonsService.changeToOrderServiceAddons(orderAddons);
			for (OrderServiceAddonViewVo vo : orderAddonVos) {
				Double serviceHourAddon = vo.getServiceHour();
				int itemNum = vo.getItemNum();
				int defaultNum = vo.getDefaultNum();
				
				if (defaultNum > 0) {
					serviceHour+= (serviceHourAddon / defaultNum) * itemNum;
				} else {
					serviceHour+= serviceHourAddon * itemNum;
				}
				
			}
			
			serviceHour = MathBigDecimalUtil.getValueStepHalf(serviceHour, 1);
		}
		
		
		List<Long> appointStaffIds = new ArrayList<Long>();
		// 实现派工逻辑，找到 阿姨 id, 或者返回 错误标识符
		Long serviceDate = order.getServiceDate();
		
		//找出是否有指定的阿姨。
		OrderDispatchSearchVo orderDispatchSearchVo = new OrderDispatchSearchVo();
		orderDispatchSearchVo.setOrderId(orderId);
		
		List<OrderAppoint> orderAppoints = orderAppointService.selectBySearchVo(orderDispatchSearchVo);
		
		if (!orderAppoints.isEmpty()) {
			for (OrderAppoint oa : orderAppoints) {
				appointStaffIds.add(oa.getStaffId());
			}
		}
		
		int staffNums = order.getStaffNums();
		
		List<OrgStaffDispatchVo> autoStaffs = orderDispatchService.autoDispatch(addrId, serviceTypeId, serviceDate, serviceHour, staffNums, appointStaffIds);
	
		if (autoStaffs.isEmpty()) return false;
		
		
		String beginTimeStr = TimeStampUtil.timeStampToDateStr(order.getServiceDate() * 1000, "MM月-dd日HH:mm");
		String endTimeStr = TimeStampUtil.timeStampToDateStr((long) ((order.getServiceDate() + order.getServiceHour() * 3600) * 1000), "HH:mm");
		String timeStr = beginTimeStr + "-" + endTimeStr;
		
		for (int i = 0; i < autoStaffs.size(); i++) {
			OrgStaffDispatchVo staffVo = autoStaffs.get(i);
		
			Long staffId = staffVo.getStaffId();
			int allocate = staffVo.getAllocate();
			String allocateReason = staffVo.getAllocateReason();
		// 进行派工
			Boolean doOrderDispatch = orderDispatchService.doOrderDispatch(order, serviceDate, serviceHour, staffId, allocate, allocateReason);
		
			orderDispatchService.pushToStaff(staffVo.getStaffId(), "true", "dispatch", orderId, OneCareUtil.getJhjOrderTypeName(order.getOrderType()),
					Constants.ALERT_STAFF_MSG);
			
			//发送短信
			String[] smsContent = new String[] { timeStr };
			SmsUtil.SendSms(staffVo.getMobile(), "114590", smsContent);
			
			order.setOrgId(staffVo.getOrgId());
		}
		
		order.setOrderStatus(Constants.ORDER_HOUR_STATUS_3);// 更新订单状态---已派工
		order.setUpdateTime(TimeStampUtil.getNowSecond());// 修改 24小时已支付
															// 的助理单，需要用到这个 修改时间
		ordersService.updateByPrimaryKeySelective(order);

		// 2.插入订单日志表 order_log
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLog.setAction(Constants.ORDER_ACTION_PAY+"-支付成功");
		orderLog.setUserId(order.getUserId());
		orderLog.setUserName(order.getMobile());
		orderLog.setUserType((short)0);
		orderLogService.insert(orderLog);

		return true;
	}
	
	/**
	 * 助理订单支付成功,后续通知功能
	 * 
	 */

	@Override
	public void orderPaySuccessToDoForAm(Orders orders) {

		OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
		searchVo1.setOrderNo(orders.getOrderNo());
		searchVo1.setDispatchStatus((short) 1);
		List<OrderDispatchs> list = orderDispatchService.selectBySearchVo(searchVo1);
		OrderDispatchs orderDispatch = new OrderDispatchs();

		if (list.size() > 0) {
			orderDispatch = list.get(0);
		}

		// 如果有优惠劵，则需要将优惠劵变成已使用。
		Long orderId = orders.getId();
		OrderPrices orderPrice = orderPricesService.selectByOrderId(orderId);
		Long userCouponId = orderPrice.getCouponId();
		if (userCouponId > 0L) {
			UserCoupons userCoupon = userCouponsService.selectByPrimaryKey(userCouponId);
			userCoupon.setIsUsed((short) 1);
			userCoupon.setUsedTime(TimeStampUtil.getNowSecond());
			userCoupon.setOrderNo(orders.getOrderNo());
			userCouponsService.updateByPrimaryKey(userCoupon);
		}


	}	

	/*
	 * 话费充值类 订单，支付成功后的 处理
	 * 
	 * 此处 仅仅 做 发送请求、 判断请求状态
	 * 
	 * 充值是否成功，通过 回调 url 在 OrderServiceRechargeController afterWXPayRecharge() 处理
	 */
	@Override
	public void orderPaySuccessToDoForPhone(Orders order) {

		System.out.println("======WXEND  apix Start========");

		/*
		 * 待充值手机号,
		 * 在 OrderServiceRechargeController 中 设置为 service_content字段
		 */

		String phone = order.getServiceContent();

		String orderNo = order.getOrderNo();

		OrderPrices orderPrice = orderPricesService.selectByOrderNo(orderNo);

		/*
		 * 充值金额，即总金额
		 */
		BigDecimal orderMoney = orderPrice.getOrderMoney();

		/*
		 * 解析 响应报文
		 * 充值接口 报文
		 */

		// 发起 充值
		String paySubmitResponse = PhoneReChargeUtil.paySubmit(phone, orderMoney.intValue(), orderNo);

		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(paySubmitResponse);

			String code = jsonObject.getString("Code");
			String message = jsonObject.getString("Msg");
			if (!code.equals("0") || !message.equals("success")) {

				/*
				 * 请求失败，打印错误信息
				 */
				System.out.println(" !!====!!======recharge fail=====!!=====!!:" + message);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Boolean orderPaySuccessToDoOrderPayExt(Orders orders, OrderPriceExt orderPriceExt) {
		
		//【叮当到家】你好,你收到了用户{1}的订单类型{2}服务支付的补差价款{3}元,请知晓。
		Users u = userService.selectByPrimaryKey(orders.getUserId());
		String mobile = u.getMobile();
		
		String serviceTypeName = "";
		Long serviceTypeId = orders.getServiceType();
		PartnerServiceType serviceType = partnerServiceTypeService.selectByPrimaryKey(serviceTypeId);
		if (serviceType != null) serviceTypeName = serviceType.getName();
		
		String orderPayStr = MathBigDecimalUtil.round2(orderPriceExt.getOrderPay());
		
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderId(orders.getId());
		searchVo.setDispatchStatus((short) 1);
		
		List<OrderDispatchs> orderDispatchs = orderDispatchService.selectBySearchVo(searchVo);
		
		if (orderDispatchs.isEmpty()) return false;
		
		String staffMobile = "";
		
		OrderDispatchs orderDispatch = orderDispatchs.get(0);
		staffMobile = orderDispatch.getStaffMobile();
		
		//发送短信
		String[] smsContent = new String[] { mobile, serviceTypeName, orderPayStr };
		SmsUtil.SendSms(staffMobile, "125079", smsContent);
		
		return true;
	}

}