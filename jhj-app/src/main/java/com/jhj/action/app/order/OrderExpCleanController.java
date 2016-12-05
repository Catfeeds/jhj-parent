package com.jhj.action.app.order;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
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
import com.jhj.po.model.order.OrderAppoint;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.UserRefAm;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderAppointService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderExpCleanService;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrderServiceAddonsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.service.users.UsersService;
import com.meijia.utils.vo.AppResultData;
import com.jhj.vo.order.DeepCleanVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderServiceAddonViewVo;
import com.jhj.vo.order.OrderViewVo;
import com.meijia.utils.OrderNoUtil;
import com.meijia.utils.StringUtil;

/**
 * @description：
 * @author： kerryg
 * @date:2015年8月3日
 */
@Controller
@RequestMapping(value = "/app/order")
public class OrderExpCleanController extends BaseController {

	@Autowired
	private UsersService userService;
	@Autowired
	private OrdersService ordersService;
	@Autowired
	private OrderPricesService orderPricesService;
	@Autowired
	private OrderLogService orderLogService;
	@Autowired
	private OrderExpCleanService orderExpCleanService;
	@Autowired
	private OrderQueryService orderQueryService;
	@Autowired
	private OrderServiceAddonsService orderServiceAddonsService;
	@Autowired
	private UserRefAmService userRefAmService;
	@Autowired
	private OrgStaffsService orgStaffsService;

	@Autowired
	private UserCouponsService userCouponsService;

	@Autowired
	private DictCouponsService dictCouponsService;

	@Autowired
	private UserAddrsService userAddrsService;
	
	@Autowired
    private OrderAppointService orderAppointService;
	
	@Autowired
	private OrderDispatchsService orderDispatchService;

	/**
	 * 深度保洁订单提交接口
	 * 
	 * @param userId
	 * @param serviceType
	 * @param serviceDate
	 * @param addrId
	 * @param serviceAddons
	 * @param orderFrom
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "post_exp_clean_order.json", method = RequestMethod.POST)
	public AppResultData<Object> PostExpCleanOrder(
			@RequestParam("user_id") Long userId, 
			@RequestParam("service_type") Long serviceType,
			@RequestParam("service_date") String serviceDate, 
			@RequestParam("service_hour") double serviceHour,
			@RequestParam("addr_id") Long addrId,
			@RequestParam("service_addons_datas") String serviceAddonsDatas,
			@RequestParam(value = "remarks", required = false, defaultValue = "") String remarks,
			@RequestParam(value = "order_from", required = false, defaultValue = "1") Short orderFrom) throws Exception {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		Users u = userService.selectByPrimaryKey(userId);

		/*
		 * 1.判断是否为注册用户，非注册用户返回 999
		 */
		if (u == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}
		// 根据user_id查询对应的秘书
		UserRefAm userRefAm = userRefAmService.selectByAmId(userId);
		Long amId = userRefAm.getStaffId();
		if (amId == 0) {
			result.setMsg(ConstantMsg.ERROR_100_MSG);
			return result;
		}

		// 1.1.调用公共订单号类，生成唯一订单号
		String orderNo = String.valueOf(OrderNoUtil.genOrderNo());

		// 处理日期 ，前台js 已经将 日期 类型 处理 为 string 时间戳了， 存表 存为 long（int） ,方便统计！
		// 服务日期 ，（yyyy-MM-dd HH:mm）的 时间戳值（秒数）
		long serviceDateTable = Long.parseLong(serviceDate);

		// 1.2.保存订单信息
		Orders order = ordersService.initOrders();
		order.setAmId(amId);
		order.setMobile(u.getMobile());
		order.setUserId(userId);
		order.setOrderStatus(Constants.ORDER_STATUS_1);
		order.setOrderType(Constants.ORDER_TYPE_1);
		order.setServiceType(serviceType);
		order.setServiceDate(serviceDateTable);
		order.setAddrId(addrId);
		order.setOrderFrom(orderFrom);
		order.setOrderNo(orderNo);
		order.setServiceHour(serviceHour);
		OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(amId);
		order.setOrgId(orgStaffs.getOrgId());
		if (!StringUtil.isEmpty(remarks)) {
			order.setRemarks(remarks);
		}
		// mybatis xml 需要增加插入后获取last_insert_id;
		ordersService.insertSelective(order);
		/*
		 * 2.设置订单总金额。插入 order_prices表
		 */
		OrderPrices orderPrices = orderExpCleanService.getOrderPriceOfOrderExpClean(userId, serviceType, serviceAddonsDatas);
		orderPrices.setUserId(userId);
		orderPrices.setMobile(u.getMobile());
		orderPrices.setOrderNo(orderNo);
		orderPrices.setOrderId(order.getId());
		orderPricesService.insertSelective(orderPrices);

		/*
		 * 3.更新订单附加服务表
		 */
		List<OrderServiceAddons> list = orderExpCleanService.updateOrderServiceAddons(userId, serviceType, serviceAddonsDatas);
		for (Iterator<OrderServiceAddons> iterator = list.iterator(); iterator.hasNext();) {
			OrderServiceAddons orderServiceAddons = (OrderServiceAddons) iterator.next();
			orderServiceAddons.setOrderId(order.getId());
			orderServiceAddons.setUserId(u.getId());
			orderServiceAddons.setOrderNo(orderNo);

			orderServiceAddonsService.insertSelective(orderServiceAddons);
		}

		/*
		 * 4.插入订单日志表 order_log,短信通知助理订单信息
		 */
		if (orderNo != null) {
			// 记录订单日志
			ordersService.orderExpCleanSuccessTodo(orderNo);
		}
		OrderViewVo vo = orderQueryService.getOrderView(order);
		result.setData(order);
		return result;
	}

	@RequestMapping(value = "post_exp.json", method = RequestMethod.POST)
	public AppResultData<Object> saveAmOrder(
			@RequestParam("user_id") Long userId, 
			@RequestParam("mobile") String mobile,
			@RequestParam("service_type") Long serviceType, 
			@RequestParam("service_date") String serviceDate, 
			@RequestParam("addr_id") Long addrId,
			@RequestParam("serviceHour") double serviceHour,
			@RequestParam("service_addons_datas") String serviceAddonsDatas,
			@RequestParam(value = "remarks", required = false, defaultValue = "") String remarks,
			@RequestParam(value = "order_from", required = false, defaultValue = "1") Short orderFrom,
			@RequestParam(value = "order_pay", required = false, defaultValue = "0") BigDecimal orderPay,
			@RequestParam(value = "order_op_from", required = false) Long orderOpFrom,
			@RequestParam(value = "staff_id", required = false, defaultValue = "0") Long staffId)
			throws Exception {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		Users u = userService.selectByPrimaryKey(userId);

		/*
		 * 1.判断是否为注册用户，非注册用户返回 999
		 */
		if (u == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}
		// 1.1.调用公共订单号类，生成唯一订单号
		String orderNo = String.valueOf(OrderNoUtil.genOrderNo());

		// 处理日期 ，前台js 已经将 日期 类型 处理 为 string 时间戳了， 存表 存为 long（int） ,方便统计！
		// 服务日期 ，（yyyy-MM-dd HH:mm）的 时间戳值（秒数）
		long serviceDateTable = Long.parseLong(serviceDate);

		// 1.2.保存订单信息
		Orders order = ordersService.initOrders();

		order.setMobile(mobile);
		order.setUserId(userId);
		order.setOrderStatus(Constants.ORDER_STATUS_1);
		
		order.setOrderType(Constants.ORDER_TYPE_1);
		order.setServiceType(serviceType);
		order.setServiceDate(serviceDateTable);
		order.setServiceHour(serviceHour);
		order.setStaffNums(1);
		order.setAddrId(addrId);
		order.setOrderFrom(orderFrom);
		order.setOrderOpFrom(orderOpFrom);
		order.setOrderNo(orderNo);
		if (!StringUtil.isEmpty(remarks)) {
			order.setRemarks(remarks);
		}

		// mybatis xml 需要增加插入后获取last_insert_id;
		ordersService.insertSelective(order);
		/*
		 * 2.设置订单总金额。插入 order_prices表
		 */
		OrderPrices orderPrices = orderExpCleanService.getOrderPriceOfOrderExpClean(userId, serviceType, serviceAddonsDatas);
		
		if (orderFrom.equals((short)2) && orderPay.compareTo(new BigDecimal(0)) == 1) {
			orderPrices.setOrderMoney(orderPay);
			orderPrices.setOrderPay(orderPay);
		}
		
		orderPrices.setUserId(userId);
		orderPrices.setMobile(u.getMobile());
		orderPrices.setOrderNo(orderNo);
		orderPrices.setOrderId(order.getId());
		
		orderPricesService.insertSelective(orderPrices);

		/*
		 * 3.更新订单附加服务表
		 */
		List<OrderServiceAddons> list = orderExpCleanService.updateOrderServiceAddons(userId, serviceType, serviceAddonsDatas);
		for (Iterator<OrderServiceAddons> iterator = list.iterator(); iterator.hasNext();) {
			OrderServiceAddons orderServiceAddons = (OrderServiceAddons) iterator.next();
			orderServiceAddons.setOrderId(order.getId());
			orderServiceAddons.setUserId(u.getId());
			orderServiceAddons.setOrderNo(orderNo);

			orderServiceAddonsService.insertSelective(orderServiceAddons);
		}
		
		//如果有指定的员工，则要插入指定员工表.
		if (!staffId.equals(0L)) {
			OrderAppoint orderAppoint =  orderAppointService.initOrderAppoint();
			orderAppoint.setOrderId(order.getId());
			orderAppoint.setUserId(userId);
			orderAppoint.setStaffId(staffId);
			orderAppointService.insert(orderAppoint);
		}

		/*
		 * 插入订单日志表  order_log
		 */
		OrderLog orderLog = orderLogService.initOrderLog(order);
		
		orderLogService.insert(orderLog);
		
		result.setData(order);
		return result;
	}

	/**
	 * 根据orderNo获取深度保洁订单详情
	 * 
	 * @param orderNo
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "get_exp_clean_order_detail", method = RequestMethod.GET)
	public AppResultData<Object> ExpCleanOrderDetail(@RequestParam("order_no") String orderNo) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		Orders orders = ordersService.selectByOrderNo(orderNo);

		OrderViewVo vo = orderQueryService.getOrderView(orders);

		DeepCleanVo deepCleanVo = new DeepCleanVo();
		deepCleanVo.setCouponValue(new BigDecimal(0));

		// 通过orderNo到order_service_addons查找对应的记录(为了得到ItemUnit、ItemNum的值)
		List<OrderServiceAddons> listPre = orderServiceAddonsService.selectByOrderNo(orderNo);

		List<OrderServiceAddonViewVo> list = orderServiceAddonsService.changeToOrderServiceAddons(listPre);
		// 通过orderNo到order_price查找对应的记录(为了得到CouponId的值)
		OrderPrices orderPrices = orderPricesService.selectByOrderIds(orderNo);

		if (list == null && orderPrices == null) {

			return result;

		}

		try {
			BeanUtils.copyProperties(deepCleanVo, vo);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		deepCleanVo.setCouponId(orderPrices.getCouponId());
		deepCleanVo.setList(list);

		UserCoupons userCoupons = userCouponsService.selectByOrderNo(deepCleanVo.getOrderNo());
		if (userCoupons != null) {
			DictCoupons dictCoupons = dictCouponsService.selectByPrimaryKey(userCoupons.getCouponId());
			if (dictCoupons != null) {
				deepCleanVo.setIntroduction(dictCoupons.getIntroduction());
				deepCleanVo.setCouponValue(dictCoupons.getValue());
			}
		}
		List<UserAddrs> userAddrsList = userAddrsService.selectByUserId(orders.getUserId());
		if (orders != null) {
			Users users = userService.selectByPrimaryKey(orders.getUserId());
			if (users != null) {
				deepCleanVo.setRestMoney(users.getRestMoney());
			}
		}
		deepCleanVo.setUserAddrsList(userAddrsList);
		
		//派工信息
		List<OrderDispatchs> orderDispatchs = new ArrayList<OrderDispatchs>();
		if (orders.getOrderStatus() >= Constants.ORDER_HOUR_STATUS_3) {
			OrderDispatchSearchVo orderDispatchSearchVo = new OrderDispatchSearchVo();
			orderDispatchSearchVo.setOrderId(orders.getId());
			orderDispatchSearchVo.setDispatchStatus((short) 1);
			
			orderDispatchs = orderDispatchService.selectBySearchVo(orderDispatchSearchVo);
		}
		
		
		result.setData(deepCleanVo);

		return result;
	}
}
