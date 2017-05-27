package com.jhj.action.app.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderExpCleanService;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrderServiceAddonsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrgStaffDispatchVo;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.OrderNoUtil;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

/**
 * @description：
 * @author： kerryg
 * @date:2015年8月3日
 */
@Controller
@RequestMapping(value = "/app/order")
public class OrderAddController extends BaseController {

	@Autowired
	private UsersService userService;
	
	@Autowired
	private OrdersService orderService;
	
	@Autowired
	private OrderPricesService orderPriceService;
	
	@Autowired
	private OrderExpCleanService orderExpCleanService;
	
	@Autowired
	private OrderQueryService orderQueryService;
	
	@Autowired
	private OrderServiceAddonsService orderServiceAddonsService;

	@Autowired
	private OrgStaffsService orgStaffService;

	@Autowired
	private UserCouponsService userCouponService;

	@Autowired
	private DictCouponsService dictCouponService;

	@Autowired
	private UserAddrsService userAddrService;

	@Autowired
	private OrderDispatchsService orderDispatchService;

	@Autowired
	private OrderLogService orderLogService;
		
	@Autowired
	private PartnerServiceTypeService partnerServiceTypeService;
	
	@Autowired
	private UserDetailPayService userDetailPayService;
	

	@RequestMapping(value = "post_order_add.json", method = RequestMethod.POST)
	public AppResultData<Object> postOrderAdd(
			@RequestParam("orderType") Short orderType,
			@RequestParam("userId") Long userId,
			@RequestParam("serviceType") Long serviceType,
			@RequestParam("serviceDate") String serviceDateStr, 
			@RequestParam("addrId") Long addrId,
			@RequestParam("serviceHour") double serviceHour, 
			@RequestParam("staffNums") int staffNums, 
			@RequestParam("orderPay") BigDecimal orderPay,
			@RequestParam(value = "serviceAddonDatas", required = false, defaultValue = "") String serviceAddonDatas,
			@RequestParam(value = "selectStaffIds", required = false, defaultValue = "") String selectStaffIds,
			@RequestParam("orderPayType") Short orderPayType, 
			@RequestParam("orderFrom") Short orderFrom,
			@RequestParam("orderOpFrom") Long orderOpFrom, 
			@RequestParam(value = "couponsId", required = false, defaultValue = "0") Long couponsId,
			@RequestParam(value = "remarks", required = false, defaultValue = "") String remarks,
			@RequestParam(value = "adminId", required = false, defaultValue = "0") Long adminId,
			@RequestParam(value = "adminName", required = false, defaultValue = "") String adminName,
			@RequestParam(value = "sendSmsToUser", required = false, defaultValue = "0") int sendSmsToUser,
			@RequestParam(value = "periodOrderId", required = false, defaultValue = "0") Integer periodOrderId)
			throws Exception {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		// 获取必要的参数
		Long serviceDate = TimeStampUtil.getMillisOfDayFull(serviceDateStr) / 1000;
		
		if (adminId.equals(0L) || StringUtil.isEmpty(adminName)) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg("您长时间未操作,请重新登录系统后操作.");
			return result;
		}
		
		if (addrId == null || addrId <= 0L) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg("请选择服务地址.");
			return result;
		}

		if (serviceType == null || serviceType <= 0L) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg("请服务子类.");
			return result;
		}

		if (serviceDate == null || serviceDate < TimeStampUtil.getNowSecond()) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg("服务日期不正确.");
			return result;
		}

		if (orderType.equals(Constants.ORDER_TYPE_1)) {
			if (StringUtil.isEmpty(serviceAddonDatas)) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg("服务子项有问题.");
				return result;
			}
		}
		
		List<Long> staffIds = new ArrayList<Long>();
		// 派工人员，再次检查是否被占用.
		if (!StringUtil.isEmpty(selectStaffIds)) {
			String[] staffIdsAry = StringUtil.convertStrToArray(selectStaffIds);
			// 本次派工人员，可为多个.
			
			for (int i = 0; i < staffIdsAry.length; i++) {
				String tmpStaffIdStr = staffIdsAry[i];
				if (StringUtil.isEmpty(tmpStaffIdStr))
					continue;
				Long staffId = Long.valueOf(tmpStaffIdStr);
				staffIds.add(staffId);
				Long startServiceTime = serviceDate - Constants.SERVICE_PRE_TIME;
				// 注意结束时间也要服务结束后 1:59分钟
				Long endServiceTime = (long) (serviceDate + serviceHour * 3600 + Constants.SERVICE_PRE_TIME);
				OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
				searchVo1.setStaffId(staffId);
				searchVo1.setDispatchStatus((short) 1);
				searchVo1.setStartServiceTime(startServiceTime);
				searchVo1.setEndServiceTime(endServiceTime);
				List<OrderDispatchs> ooDisList = orderDispatchService.selectByMatchTime(searchVo1);
	
				for (OrderDispatchs nop : ooDisList) {
					String nowServiceDateStr = TimeStampUtil.timeStampToDateStr(nop.getServiceDate() * 1000, "MM-dd HH:MM");
					result.setStatus(Constants.ERROR_999);
					result.setMsg(nop.getStaffName() + "在" + nowServiceDateStr + "已有其他派工,时间冲突.");
					return result;
				}
			}
		}
		
		
		// 参数价格相关
		BigDecimal orderMoney = orderPay;

		Long userCouponId = 0L;
		if (couponsId !=null && couponsId > 0L) {
			DictCoupons coupons = dictCouponService.selectByPrimaryKey(couponsId);
			UserCoupons userCoupons = userCouponService.initUserCoupons(userId, coupons);
			userCoupons.setIsUsed((short) 1);
			userCoupons.setUsedTime(TimeStampUtil.getNowSecond());
			userCouponService.insert(userCoupons);
			userCouponId = userCoupons.getId();
			orderMoney = orderMoney.add(coupons.getValue());
		}
		
		
		// 派工人数已实际选择的人数为主.
		if (!staffIds.isEmpty()) {
			staffNums = staffIds.size();
		}
		
		
		/**
		 * 开始写入
		 * 1.订单表，
		 * 2.订单价格表，
		 * 3.订单日志表，
		 * 4.进行派工
		 * 5.通知派工人员.发送短信和推送。
		 */
		Users u = userService.selectByPrimaryKey(userId);
		// 1.订单表=====================================================
		// 调用公共订单号类，生成唯一订单号
		String orderNo = String.valueOf(OrderNoUtil.genOrderNo());

		// 保存订单信息
		Orders order = orderService.initOrders();
		order.setMobile(u.getMobile());
		order.setUserId(userId);
		order.setOrderType(orderType);
		order.setServiceType(serviceType);
		order.setServiceDate(serviceDate);
		order.setAddrId(addrId);
		
		UserAddrs userAddrs = userAddrService.selectByPrimaryKey(addrId);
        if (userAddrs != null) {
           order.setOrderAddr(userAddrs.getName() + userAddrs.getAddress() + userAddrs.getAddr());
        }
		
        order.setPeriodOrderId(periodOrderId);
        
		order.setServiceHour(serviceHour);
		order.setStaffNums(staffNums);
		order.setOrderStatus(Constants.ORDER_HOUR_STATUS_1); // 后台下单都是已支付
		order.setOrderNo(orderNo);
		order.setOrderFrom(orderFrom);
		order.setOrderOpFrom(orderOpFrom);

		// 插入订单日志表
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLog.setAction(Constants.ORDER_ACTION_ADD);
		orderLog.setUserId(adminId);
		orderLog.setUserName(adminName);
		orderLog.setUserType((short) 2);
		orderLogService.insert(orderLog);

		PartnerServiceType partnerServiceType = partnerServiceTypeService.selectByPrimaryKey(serviceType);
		// 订单表 。服务内容字段
		String serviceName = partnerServiceType.getName();
		// 如果有附加服务类型，需要存储到order_service_addons表.
		order.setServiceContent(serviceName);
		order.setRemarks(remarks);
		orderService.insert(order);
		Long orderId = order.getId();

		// 2. 订单价格表 ===========================================================
		OrderPrices orderPrice = orderPriceService.initOrderPrices();

		orderPrice.setUserId(userId);
		orderPrice.setMobile(u.getMobile());
		orderPrice.setOrderId(orderId);
		orderPrice.setOrderNo(orderNo);
		orderPrice.setPayType(orderPayType);
		orderPrice.setCouponId(userCouponId);
		orderPrice.setOrderOriginPrice(orderMoney);
		orderPrice.setOrderPrimePrice(orderMoney);
		orderPrice.setOrderMoney(orderMoney);
		orderPrice.setOrderPay(orderPay);
		orderPriceService.insertSelective(orderPrice);

		// 更新为订单已支付
		order.setOrderStatus(Constants.ORDER_HOUR_STATUS_2);
		order.setUpdateTime(TimeStampUtil.getNowSecond());
		orderService.updateByPrimaryKeySelective(order);
		// 插入订单日志
		orderLog = orderLogService.initOrderLog(order);
		orderLog.setAction(Constants.ORDER_ACTION_PAY);
		orderLog.setUserId(userId);
		orderLog.setUserName(u.getMobile());
		orderLog.setUserType((short) 0);
		orderLogService.insert(orderLog);

		// 记录用户消费明细
		userDetailPayService.addUserDetailPayForOrder(u, order, orderPrice, "", "", "");

		// 如果为深度养护，则需要存储服务子项
		if (orderType.equals(Constants.ORDER_TYPE_1)) {
			List<OrderServiceAddons> list = orderExpCleanService.updateOrderServiceAddons(userId, serviceType, serviceAddonDatas, orderOpFrom);
			for (Iterator<OrderServiceAddons> iterator = list.iterator(); iterator.hasNext();) {
				OrderServiceAddons orderServiceAddons = iterator.next();
				orderServiceAddons.setOrderId(order.getId());
				orderServiceAddons.setUserId(u.getId());
				orderServiceAddons.setOrderNo(orderNo);

				orderServiceAddonsService.insertSelective(orderServiceAddons);
			}
		}

		//如果为自动派工的服务品类，没有选择服务人员，则会自动派工.
		if (staffIds.isEmpty()) {
			// 增加判断，是否可以自动派工.
			if (partnerServiceType.getIsAuto().equals((short) 1)) {
				List<Long> appointStaffIds = new ArrayList<Long>();
				List<OrgStaffDispatchVo> autoStaffs = orderDispatchService.autoDispatch(addrId, serviceType, serviceDate, serviceHour, staffNums, appointStaffIds);
				for (OrgStaffDispatchVo item : autoStaffs) {
					staffIds.add(item.getStaffId());
				}
			}
		}
		
		// 如果为多人派工，需要对服务时间进行平均, 仅对深度养护
		if (staffNums > 1 && order.getOrderType().equals(Constants.ORDER_TYPE_1)) {
			serviceHour = MathBigDecimalUtil.getValueStepHalf(serviceHour, staffNums);
			order.setServiceHour(serviceHour);
			orderService.updateByPrimaryKeySelective(order);
		}
		
		
		
		// 如果有选择派工人员，则进行派工, 并且发送短信
		if (!staffIds.isEmpty()) {
			String beginTimeStr = TimeStampUtil.timeStampToDateStr(order.getServiceDate() * 1000, "MM月-dd日HH:mm");
			String endTimeStr = TimeStampUtil.timeStampToDateStr((long) ((order.getServiceDate() + order.getServiceHour() * 3600) * 1000), "HH:mm");
			String timeStr = beginTimeStr + "-" + endTimeStr;
			for (Long staffId : staffIds) {
				int allocate = 0;
				String allocateReason = "合理分配";
				Boolean doOrderDispatch = orderDispatchService.doOrderDispatch(order, serviceDate, serviceHour, staffId, allocate, allocateReason);
	
				OrgStaffs staff = orgStaffService.selectByPrimaryKey(staffId);
				orderDispatchService.pushToStaff(staff.getStaffId(), "true", "dispatch", orderId, OneCareUtil.getJhjOrderTypeName(order.getOrderType()),
						Constants.ALERT_STAFF_MSG);
	
				// 发送短信
				String[] smsContent = new String[] { timeStr };
				SmsUtil.SendSms(staff.getMobile(), "114590", smsContent);
			}
	
			// 更新派工状态为已派工。
			order.setOrderStatus(Constants.ORDER_HOUR_STATUS_3);
			order.setUpdateTime(TimeStampUtil.getNowSecond());
			orderService.updateByPrimaryKeySelective(order);
	
			// 订单日志
			orderLog = orderLogService.initOrderLog(order);
			orderLog.setUserType((short) 2);
			orderLog.setUserId(adminId);
			orderLog.setUserName(adminName);
			orderLog.setAction(Constants.ORDER_ACTION_UPDATE_DISPATCHS_STAFF);
			orderLogService.insert(orderLog);
		}
		
		//根据状态来确定是否发送短信
		if (sendSmsToUser == 1) {
			 String serviceTime = TimeStampUtil.timeStampToDateStr(serviceDate * 1000, "yyyy年MM月dd日HH:mm");
			 
			 String[] paySuccessForUser = new String[] {serviceTime,serviceName};
			 
			 SmsUtil.SendSms(u.getMobile(),  Constants.PAY_SUCCESS_ORDER_SMS, paySuccessForUser);
		}

		return result;
	}

}
