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
import com.jhj.po.model.order.OrderAppoint;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.ValidateService;
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
import com.jhj.vo.order.DeepCleanVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderServiceAddonViewVo;
import com.jhj.vo.order.OrderViewVo;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.OrderNoUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.vo.AppResultData;

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
	
	@Autowired
	private ValidateService validateService;
	
	@Autowired
	private OrderLogService orderLogService;

	@RequestMapping(value = "post_exp.json", method = RequestMethod.POST)
	public AppResultData<Object> saveAmOrder(
			@RequestParam("user_id") Long userId, 
			@RequestParam("mobile") String mobile,
			@RequestParam("service_type") Long serviceType, 
			@RequestParam("service_date") String serviceDate, 
			@RequestParam("addr_id") Long addrId,
			@RequestParam("serviceHour") double serviceHour,
			@RequestParam("service_addons_datas") String serviceAddonsDatas,
			@RequestParam(value = "staff_nums", required = false, defaultValue = "1") int staffNums,
			@RequestParam(value = "remarks", required = false, defaultValue = "") String remarks,
			@RequestParam(value = "order_from", required = false, defaultValue = "1") Short orderFrom,
			@RequestParam(value = "order_pay", required = false, defaultValue = "0") BigDecimal orderPay,
			@RequestParam(value = "order_op_from", required = false, defaultValue = "0") Long orderOpFrom,
			@RequestParam(value = "staff_id", required = false, defaultValue = "0") Long staffId,
			@RequestParam(value ="coupons_id",required =false) Long couponsId,
			@RequestParam(value ="userid",required =false) Long user_id,
			@RequestParam(value ="user_name",required =false) String username)
			throws Exception {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		AppResultData<Object> v = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		v = validateService.validateUser(userId);
		if (v.getStatus() == Constants.ERROR_999) {
			return v;
		}
		
		//判断用户地址是否存在
		v = validateService.validateUserAddr(userId, addrId);
		if (v.getStatus() == Constants.ERROR_999) {
			return v;
		}
		
		
		
		Users u = userService.selectByPrimaryKey(userId);

		
		// 1.1.调用公共订单号类，生成唯一订单号
		String orderNo = String.valueOf(OrderNoUtil.genOrderNo());

		// 处理日期 ，前台js 已经将 日期 类型 处理 为 string 时间戳了， 存表 存为 long（int） ,方便统计！
		// 服务日期 ，（yyyy-MM-dd HH:mm）的 时间戳值（秒数）
		long serviceDateTable = Long.parseLong(serviceDate);

		// 1.2.保存订单信息
		Orders order = ordersService.initOrders();
		
		if (staffNums > 1 ) {
			serviceHour = MathBigDecimalUtil.getValueStepHalf(serviceHour, staffNums);
			
		}
		order.setServiceHour(serviceHour);
		order.setMobile(mobile);
		order.setUserId(userId);
		order.setOrderStatus(Constants.ORDER_STATUS_1);
		
		order.setOrderType(Constants.ORDER_TYPE_1);
		order.setServiceType(serviceType);
		order.setServiceDate(serviceDateTable);
		order.setStaffNums(staffNums);
		order.setAddrId(addrId);
		
		UserAddrs userAddrs = userAddrsService.selectByPrimaryKey(addrId);
        if (userAddrs != null) {
             order.setOrderAddr(userAddrs.getName() + userAddrs.getAddress() + userAddrs.getAddr());
        }
		
		order.setOrderFrom(orderFrom);
		order.setOrderOpFrom(orderOpFrom);
		order.setOrderNo(orderNo);
		if (!StringUtil.isEmpty(remarks)) {
			order.setRemarks(remarks);
		}

		Orders or = new Orders();
		or.setUserId(order.getUserId());
		or.setOrderType(order.getOrderType());
		or.setOrderStatus(order.getOrderStatus());
		List<Orders> newestOrder = ordersService.getNewestOrder(or);
		if(newestOrder!=null && newestOrder.size()>0){
			Long addTime = newestOrder.get(0).getAddTime();
			if(order.getAddTime()-addTime<10){
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.FREQUENT_OPERATION);
				return result;
			}
		}
		// mybatis xml 需要增加插入后获取last_insert_id;
		ordersService.insertSelective(order);
		/*
		 * 2.设置订单总金额。插入 order_prices表
		 */
		OrderPrices orderPrices = orderExpCleanService.getOrderPriceOfOrderExpClean(userId, serviceType, serviceAddonsDatas, orderOpFrom);
		
		if (orderFrom.equals((short)2) && orderPay.compareTo(new BigDecimal(0)) == 1) {
			orderPrices.setOrderMoney(orderPay);
			orderPrices.setOrderPay(orderPay);
			orderPrices.setOrderOriginPrice(orderPay);
			orderPrices.setOrderPrimePrice(orderPay);
		}
		
		orderPrices.setUserId(userId);
		orderPrices.setMobile(u.getMobile());
		orderPrices.setOrderNo(orderNo);
		orderPrices.setOrderId(order.getId());
		
		orderPricesService.insertSelective(orderPrices);

		/*
		 * 3.更新订单附加服务表
		 */
		List<OrderServiceAddons> list = orderExpCleanService.updateOrderServiceAddons(userId, serviceType, serviceAddonsDatas, orderOpFrom);
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
		if(order.getOrderFrom()==1){
			orderLog.setAction(Constants.ORDER_ACTION_ADD);
			orderLog.setUserId(userId);
			orderLog.setUserName(u.getMobile());
			orderLog.setUserType((short)0);
		}
		if(order.getOrderFrom()==2){
			orderLog.setAction(Constants.ORDER_ACTION_ADD);
			orderLog.setUserId(user_id);
			orderLog.setUserName(username);
			orderLog.setUserType((short)2);
		}
		
		orderLogService.insert(orderLog);
		  
		
		/* CouponsId,新增参数，判断用户下单时候有使用优惠，如果有使用优惠券，则在user_conpos表中插入该用户对应得优惠券信息
		 * 
		 * */
		if(couponsId!=null && couponsId>0){
			DictCoupons coupons = dictCouponsService.selectByPrimaryKey(couponsId);
			UserCoupons userCoupons = userCouponsService.initUserCoupons(userId, coupons);
			userCouponsService.insert(userCoupons);
		}
		
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
			e.printStackTrace();
		} catch (InvocationTargetException e) {
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
//		List<UserAddrs> userAddrsList = userAddrsService.selectByUserId(orders.getUserId());
		if (orders != null) {
			Users users = userService.selectByPrimaryKey(orders.getUserId());
			if (users != null) {
				deepCleanVo.setRestMoney(users.getRestMoney());
			}
		}
		
		//派工信息
		List<OrderDispatchs> orderDispatchs = new ArrayList<OrderDispatchs>();
		if (orders.getOrderStatus() >= Constants.ORDER_HOUR_STATUS_3) {
			OrderDispatchSearchVo orderDispatchSearchVo = new OrderDispatchSearchVo();
			orderDispatchSearchVo.setOrderId(orders.getId());
			orderDispatchSearchVo.setDispatchStatus((short) 1);
			orderDispatchs = orderDispatchService.selectBySearchVo(orderDispatchSearchVo);
			deepCleanVo.setOrderDispatchs(orderDispatchs);
		}
		
		result.setData(deepCleanVo);

		return result;
	}
}
