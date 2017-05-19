package com.jhj.action.order;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jhj.action.BaseController;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AccountAuth;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.cooperate.CooperativeBusiness;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.period.PeriodOrder;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.cooperate.CooperateBusinessService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderExpCleanService;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrderServiceAddonsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.period.PeriodOrderService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.dict.CooperativeBusinessSearchVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.period.PeriodOrderSearchVo;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.OrderNoUtil;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :
 * @Date :
 * @Description:后台订单下单及预派工功能
 */
@Controller
@RequestMapping(value = "/order")
public class OrderFormController extends BaseController {

	@Autowired
	private OrdersService orderService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private OrderPricesService OrderPricesService;

	@Autowired
	private PartnerServiceTypeService partnerServiceTypeService;

	@Autowired
	private OrgsService orgService;

	@Autowired
	private OrderDispatchsService orderDispatchsService;

	@Autowired
	private OrderQueryService orderQueryService;

	@Autowired
	private CooperateBusinessService cooperateBusinessService;

	@Autowired
	private OrderLogService orderLogService;

	@Autowired
	private OrderPricesService orderPriceService;

	@Autowired
	private DictCouponsService dictCouponsService;

	@Autowired
	private UserCouponsService userCouponService;
	
	@Autowired
	private OrderExpCleanService orderExpCleanService;
	
	@Autowired
	private OrderServiceAddonsService orderServiceAddonsService;
	
	@Autowired
	private UserDetailPayService userDetailPayService;
	
	@Autowired
	private OrgStaffsService orgStaffService;
	
	@Autowired
	private UserAddrsService userAddrService;
	
	@Autowired
	private PeriodOrderService periodOrderService;

	@AuthPassport
	@RequestMapping(value = "/order-add", method = RequestMethod.GET)
	public String oaOrderHourAdd(HttpServletRequest request, Model model) {

		// 订单的来源
		CooperativeBusinessSearchVo vo = new CooperativeBusinessSearchVo();
		vo.setEnable((short) 1);
		List<CooperativeBusiness> CooperativeBusinessList = cooperateBusinessService.selectCooperativeBusinessVo(vo);
		if (CooperativeBusinessList != null) {
			model.addAttribute("cooperativeBusiness", CooperativeBusinessList);
		}

		AccountAuth accountAuth = AuthHelper.getSessionAccountAuth(request);
		model.addAttribute("accountAuth", accountAuth);

		if (!model.containsAttribute("contentModel")) {
			Orders order = orderService.initOrders();
			model.addAttribute("contentModel", order);
		}

		return "order/orderAdd";
	}

	@AuthPassport
	@RequestMapping(value = "/order-add", method = RequestMethod.POST)
	public String doOrderAdd(Model model, HttpServletRequest request, @ModelAttribute("contentModel") Orders formData, BindingResult result) {

		// 获取必要的参数
		Short orderType = formData.getOrderType();
		Long userId = formData.getUserId();
		Long addrId = formData.getAddrId();
		Long serviceType = formData.getServiceType();
		String serviceDateStr = request.getParameter("serviceDate");
		Long serviceDate = TimeStampUtil.getMillisOfDayFull(serviceDateStr) / 1000;
		int staffNums = formData.getStaffNums();
		
		if (addrId == null || addrId <= 0L) {
			result.addError(new FieldError("contentModel", "remarks", "请选择服务地址."));
			return oaOrderHourAdd(request, model);
		}
		
		if (serviceType == null || serviceType <= 0L) {
			result.addError(new FieldError("contentModel", "remarks", "请服务子类."));
			return oaOrderHourAdd(request, model);
		}
		
		if (serviceDate == null || serviceDate < TimeStampUtil.getNowSecond()) {
			result.addError(new FieldError("contentModel", "remarks", "服务日期不正确."));
			return oaOrderHourAdd(request, model);
		}

		double serviceHour = formData.getServiceHour();
		
		String serviceAddonDatas = request.getParameter("serviceAddonDatas");
		if (orderType.equals(Constants.ORDER_TYPE_1)) {
			if (StringUtil.isEmpty(serviceAddonDatas)) {
				result.addError(new FieldError("contentModel", "remarks", "服务子项有问题."));
				return oaOrderHourAdd(request, model);
			}
		}

		// 参数价格相关
		String orderPayTypeStr = request.getParameter("orderPayType");
		Short orderPayType = Short.valueOf(orderPayTypeStr);
		String orderPayStr = request.getParameter("orderPay");
		BigDecimal orderPay = new BigDecimal(orderPayStr);
		BigDecimal orderMoney = orderPay;

		String couponsIdStr = request.getParameter("couponsId");
		Long userCouponId = 0L;
		if (!StringUtil.isEmpty(couponsIdStr)) {
			Long couponId = Long.valueOf(couponsIdStr);
			
			if (couponId > 0L) {
	
				DictCoupons coupons = dictCouponsService.selectByPrimaryKey(couponId);
				UserCoupons userCoupons = userCouponService.initUserCoupons(userId, coupons);
				userCoupons.setIsUsed((short) 1);
				userCoupons.setUsedTime(TimeStampUtil.getNowSecond());
				userCouponService.insert(userCoupons);
				userCouponId = userCoupons.getId();
				orderMoney = orderMoney.add(coupons.getValue());
			}
		}
		// 其他参数
		Short orderFrom = formData.getOrderFrom();
		Long orderOpFrom = formData.getOrderOpFrom();
		String remarks = formData.getRemarks();

		// 派工人员，再次检查是否被占用.
		String selectStaffIds = request.getParameter("selectStaffIds");
		if (StringUtil.isEmpty(selectStaffIds)) {
			result.addError(new FieldError("contentModel", "remarks", "未选择派工人员"));
			return oaOrderHourAdd(request, model);
		}
		String[] staffIdsAry = StringUtil.convertStrToArray(selectStaffIds);
		// 本次派工人员，可为多个.
		List<Long> staffIds = new ArrayList<Long>();
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
			List<OrderDispatchs> ooDisList = orderDispatchsService.selectByMatchTime(searchVo1);

			for (OrderDispatchs nop : ooDisList) {
				String nowServiceDateStr = TimeStampUtil.timeStampToDateStr(nop.getServiceDate() * 1000, "MM-dd HH:MM");
				result.addError(new FieldError("contentModel", "remarks", nop.getStaffName() + "在" + nowServiceDateStr + "已有其他派工,时间冲突."));
				return oaOrderHourAdd(request, model);
			}
		}
		
		//派工人数已实际选择的人数为主.
		staffNums = staffIds.size();

		/**
		 * 开始写入
		 * 1.订单表，
		 * 2.订单价格表，
		 * 3.订单日志表，
		 * 4.进行派工
		 * 5.通知派工人员.发送短信和推送。
		 */
		AccountAuth auth = AuthHelper.getSessionAccountAuth(request);
		Users u = usersService.selectByPrimaryKey(userId);
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
		
		
		order.setServiceHour(serviceHour);
		order.setStaffNums(staffNums);
		order.setOrderStatus(Constants.ORDER_HOUR_STATUS_1);  //后台下单都是已支付
		order.setOrderNo(orderNo);
		order.setOrderFrom(orderFrom);
		order.setOrderOpFrom(orderOpFrom);
		
		//插入订单日志表
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLog.setAction(Constants.ORDER_ACTION_ADD);			
		orderLog.setUserId(auth.getId());
		orderLog.setUserName(auth.getUsername());
		orderLog.setUserType((short)2);
		orderLogService.insert(orderLog);

		PartnerServiceType partnerServiceType = partnerServiceTypeService.selectByPrimaryKey(serviceType);
		// 订单表 。服务内容字段
		String serviceName = partnerServiceType.getName();
		// 如果有附加服务类型，需要存储到order_service_addons表.
		order.setServiceContent(serviceName);
		order.setRemarks(remarks);
		orderService.insert(order);
		Long orderId = order.getId();
		
		//2. 订单价格表 ===========================================================
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
		
		//更新为订单已支付
		order.setOrderStatus(Constants.ORDER_HOUR_STATUS_2);
		order.setUpdateTime(TimeStampUtil.getNowSecond());
		orderService.updateByPrimaryKeySelective(order);
		// 插入订单日志
		orderLog = orderLogService.initOrderLog(order);
		orderLog.setAction(Constants.ORDER_ACTION_PAY);
		orderLog.setUserId(userId);
		orderLog.setUserName(u.getMobile());
		orderLog.setUserType((short)0);
		orderLogService.insert(orderLog);
		
		// 记录用户消费明细
		userDetailPayService.addUserDetailPayForOrder(u, order, orderPrice, "", "", "");
		
		//如果为深度养护，则需要存储服务子项
		if (orderType.equals(Constants.ORDER_TYPE_1)) {
			List<OrderServiceAddons> list = orderExpCleanService.updateOrderServiceAddons(userId, serviceType, serviceAddonDatas);
			for (Iterator<OrderServiceAddons> iterator = list.iterator(); iterator.hasNext();) {
				OrderServiceAddons orderServiceAddons = iterator.next();
				orderServiceAddons.setOrderId(order.getId());
				orderServiceAddons.setUserId(u.getId());
				orderServiceAddons.setOrderNo(orderNo);
	
				orderServiceAddonsService.insertSelective(orderServiceAddons);
			}
		}
		
		//进行派工, 并且发送短信
		String beginTimeStr = TimeStampUtil.timeStampToDateStr(order.getServiceDate() * 1000, "MM月-dd日HH:mm");
		String endTimeStr = TimeStampUtil.timeStampToDateStr((long) ((order.getServiceDate() + order.getServiceHour() * 3600) * 1000), "HH:mm");
		String timeStr = beginTimeStr + "-" + endTimeStr;
		for (Long staffId : staffIds) {
			int allocate = 0;
			String allocateReason = "合理分配";
			Boolean doOrderDispatch = orderDispatchsService.doOrderDispatch(order, serviceDate, serviceHour, staffId, allocate, allocateReason);
			
			OrgStaffs staff = orgStaffService.selectByPrimaryKey(staffId);
			orderDispatchsService.pushToStaff(staff.getStaffId(), "true", "dispatch", orderId, OneCareUtil.getJhjOrderTypeName(order.getOrderType()),
					Constants.ALERT_STAFF_MSG);
			
			
			//发送短信
			String[] smsContent = new String[] { timeStr };
			SmsUtil.SendSms(staff.getMobile(), "114590", smsContent);
		}
		
		//更新派工状态为已派工。
		order.setOrderStatus(Constants.ORDER_HOUR_STATUS_3);
		order.setUpdateTime(TimeStampUtil.getNowSecond());
		orderService.updateByPrimaryKeySelective(order);
		
		//订单日志
		orderLog = orderLogService.initOrderLog(order);
		orderLog.setUserType((short) 2);
		orderLog.setUserId(auth.getId());
		orderLog.setUserName(auth.getUsername());
		orderLog.setAction(Constants.ORDER_ACTION_UPDATE_DISPATCHS_STAFF);
		orderLogService.insert(orderLog);
		
		
		String returnUrl = "/home/success?nextUrl=";
		String nextUrl = "/order/order-list";
		nextUrl = URLEncoder.encode(nextUrl);
		returnUrl = returnUrl + nextUrl;
		return "redirect:" + returnUrl;
	}
	
	@RequestMapping(value = "/getPeriodOrder",method=RequestMethod.GET)
	@ResponseBody
	public List<PeriodOrder> getPeriodOrder(String mobile){
		
		Users users = usersService.selectByMobile(mobile);
		
		PeriodOrderSearchVo searchVo = new PeriodOrderSearchVo();
		searchVo.setUserId(users.getId().intValue());
		searchVo.setOrderStatus(2);
		List<PeriodOrder> periodOrderList = periodOrderService.selectBySearchVo(searchVo);
		
		return periodOrderList;
		
	}
	
}
