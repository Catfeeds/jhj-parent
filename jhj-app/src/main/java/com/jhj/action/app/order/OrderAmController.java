package com.jhj.action.app.order;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.jhj.service.users.UserLoginedService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.service.users.UserSmsTokenService;
import com.jhj.service.users.UsersService;
import com.meijia.utils.vo.AppResultData;
import com.jhj.vo.OrderSearchVo;
import com.jhj.vo.order.DeepCleanVo;
import com.jhj.vo.order.OrderServiceAddonViewVo;
import com.jhj.vo.order.OrderViewVo;
import com.meijia.utils.OrderNoUtil;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.TimeStampUtil;








import org.apache.commons.beanutils.BeanUtils;

/**
 * @description：
 * @author： kerryg
 * @date:2015年7月28日
 */
@Controller
@RequestMapping(value = "/app/order")
public class OrderAmController extends BaseController {

	@Autowired
	private UsersService userService;

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private OrderExpCleanService orderExpCleanService;
	
	@Autowired
	private OrderPricesService orderPricesService;

	@Autowired
	private OrgStaffsService orgStaffsService;

	@Autowired
	private UserSmsTokenService smsTokenService;

	@Autowired
	private UserLoginedService userLoginedService;

	@Autowired
	private OrderQueryService orderQueryService;

	@Autowired
	private UserRefAmService orderRefAmService;

	@Autowired
	private OrderServiceAddonsService orderServiceAddonsService;
	
	@Autowired
	private UserAddrsService userAddrsService;
	
	@Autowired
	private UserCouponsService userCouponsService;
	
	@Autowired
	private DictCouponsService dictCouponsService;

	@Autowired
	private OrderLogService orderLogService;
	
	@Autowired
	private UserDetailPayService userDetailPayService;
	
	@Autowired
	private OrderDispatchsService disPatchService;
	
	@Autowired
	private PartnerServiceTypeService partService;
	/**
	 * 用户预约下单接口
	 * 
	 * @param request
	 * @param userId
	 * @param serviceType
	 * @param serviceContent
	 * @param orderFrom
	 * @return
	 */

	@RequestMapping(value = "post_user", method = RequestMethod.POST)
	public AppResultData<Object> saveUserOrder(
			Model model,
			HttpServletRequest request,
			@RequestParam("user_id") Long userId,
			@RequestParam("service_type") Long serviceType,
			@RequestParam(value ="service_content",required = false,defaultValue= "") String serviceContent,
			@RequestParam(value = "order_from", required = false, defaultValue = "1") Short orderFrom){

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		Users u = userService.getUserById(userId);

		// 判断是否为注册用户，非注册用户返回 999
		if (u == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}

		// 服务内容进行urldecode;
		try {
			serviceContent = URLDecoder.decode(serviceContent,
					Constants.URL_ENCODE);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ERROR_100_MSG);
			return result;
		}


		// 调用公共订单号类，生成唯一订单号
		String orderNo = String.valueOf(OrderNoUtil.genOrderNo());

		// 保存订单信息
		Orders order = ordersService.initOrders();

		order.setMobile(u.getMobile());
		order.setUserId(u.getId());
		order.setServiceType(serviceType);
		order.setServiceContent(serviceContent);
		order.setOrderNo(orderNo);
		order.setOrderStatus(Constants.ORDER_AM_STATUS_1);
		order.setOrderType(Constants.ORDER_TYPE_2);
		order.setOrderFrom(order.getOrderFrom());
		ordersService.insert(order);

		if (orderNo != null) {
			// 记录订单日志
			ordersService.userOrderAmSuccessTodo(orderNo);
		}

		//  返回orderView
		OrderViewVo vo = orderQueryService.getOrderView(order);

		result = new AppResultData<Object>(Constants.SUCCESS_0,
				ConstantMsg.SUCCESS_0_MSG, vo);
		
		/*
		 *  2016年4月14日10:48:53  
		 *  
		 *  您预定的{1}服务已经受理，感谢您的支持，我们会尽快与您联系，如有任何疑问请拨打010-56429112
		 */
		
		PartnerServiceType type = partService.selectByPrimaryKey(serviceType);
		//服务类型名称
		String name = type.getName();
		
		String[] paySuccessForUser = new String[] {name};
		
		SmsUtil.SendSms(u.getMobile(),  Constants.MESSAGE_SERVICE_ORDER_SUCCESS, paySuccessForUser);
		
		return result;
	}

	/**
	 * 助理预约下单修改展现
	 * 
	 * @param request
	 * @param orderId
	 * @param orderNo
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "get_user_am", method = RequestMethod.POST)
	public AppResultData<Object> Am(HttpServletRequest request,
			@RequestParam("order_id") Long orderId,
			@RequestParam("order_no") Long orderNo) throws ParseException {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		Orders orders = ordersService.selectByPrimaryKey(orderId);

		if (orders == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ERROR_999_MSG_1);
			return result;
		}
		OrderViewVo vo = ordersService.changeOrderViewVo(orders);

		result.setData(vo);
		return result;

	}

	/**
	 * 助理-调整预约下单接口
	 * 
	 * @param id
	 * @param serviceContent
	 * @param orderMoney
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */

	@RequestMapping(value = "post_user_am", method = RequestMethod.POST)
	public AppResultData<Object> saveUserAm(HttpServletRequest request,
			OrderSearchVo searchVo, Model model,
			@RequestParam("order_no") String orderNo,
			@RequestParam("service_content") String serviceContent,
			@RequestParam("order_money") BigDecimal orderMoney)
			throws IllegalAccessException, InvocationTargetException {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
//		boolean isMoneyNum = MathBigDeciamlUtil.decideIsMoneyNum(orderMoney);
//		
//		//后台校验。如果是 负数，返回错误信息
//		if(!isMoneyNum){
//			result = new AppResultData<Object>(Constants.ERROR_999, "订单金额不合法", "");
//			return result;
//		}
		
		// 根据order_id查找记录，没有则返回“订单不存在”
		// Orders orders = ordersService.selectByPrimaryKey(orderId);
		Orders orders = ordersService.selectByOrderNo(orderNo);

		if (orders == null) {
			result = new AppResultData<Object>(Constants.ERROR_999,
					ConstantMsg.ORDER_NO_NOT_EXIST_MG, "");
			return result;
		}
		// 根据订单状态order_status判断是否为 1 （待确认），不是则返回“订单已确认过，不需要重复处理”
		if (orders.getOrderStatus() != 1) {
			result = new AppResultData<Object>(Constants.ERROR_999,
					ConstantMsg.ORDER_NO_NOT_CONFIRM, "");
			return result;
		}
		// 更新新订单信息, 注意更新为两张表 order表的update_time, orderPrice 做更新

		// 服务内容进行urldecode;
		try {
			serviceContent = URLDecoder.decode(serviceContent,
					Constants.URL_ENCODE);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ERROR_100_MSG);
			return result;
		}
		orders.setOrderStatus(Constants.ORDER_STATUS_3);
		orders.setServiceContent(serviceContent);
		ordersService.updateByPrimaryKeySelective(orders);

		OrderPrices record = orderPricesService.selectByOrderId(orders.getId());
		if (record != null) {
			record.setOrderMoney(orderMoney);
			record.setUpdateTime(TimeStampUtil.getNow() / 1000);
			orderPricesService.updateByPrimaryKeySelective(record);
		} else {
			record = orderPricesService.initOrderPrices();
			record.setOrderNo(orders.getOrderNo());
			record.setOrderId(orders.getId());
			record.setMobile(orders.getMobile());
			record.setUserId(orders.getUserId());
			record.setOrderMoney(orderMoney);
			record.setAddTime(TimeStampUtil.getNow() / 1000);
			orderPricesService.insertSelective(record);
			
		}
		// 记录订单状态到order_log表
		if (orderNo != null) {
			// 记录订单日志
			ordersService.userOrderAmSuccessTodo(orderNo);
			
		}

		/*OrderLog orderLog = new OrderLog();
		orderLog.setOrderId(orders.getId());
		orderLog.setMobile(orders.getMobile());
		orderLog.setOrderNo(orders.getOrderNo());
		orderLog.setOrderStatus(orders.getOrderStatus());
		orderLog.setRemarks("");
		orderLog.setAddTime(TimeStampUtil.getNow() / 1000);

		ordersService.insert(orderLog);
*/
		OrderViewVo vo = orderQueryService.getOrderView(orders);

		result = new AppResultData<Object>(Constants.SUCCESS_0,
				ConstantMsg.SUCCESS_0_MSG, vo);
		return result;
	}
    /**
     * 助理修改深度保洁订单
     * @param request
     * @param searchVo
     * @param model
     * @param serviceContent
     * @param orderNo
     * @param orderMoney	//2015-10-31 14:10:33 此处 由于存在  “手动” 价格调整，传参应该是    alter-order-view-1页面的  order_moneys  ！！！
     * @param orderDate
     * @return
     */
	@RequestMapping(value="post_am_clean", method = RequestMethod.POST)
	public AppResultData<Object> saveCleanAm(HttpServletRequest request,
			Model model,
			@RequestParam("order_no") String orderNo,
			@RequestParam("order_moneys") BigDecimal orderMoney,
			@RequestParam("service_addons_datas") String serviceAddonsDatas
			){
		
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		
		
		
		Orders orders = ordersService.selectByOrderNo(orderNo);
		if (orders == null) {
			return result;
		}
		//如果订单不是待确认状态，就提示已经确认过
		if (orders.getOrderStatus() != 1) {
			result = new AppResultData<Object>(Constants.ERROR_100,
					ConstantMsg.ORDER_NO_NOT_CONFIRM, "");
			return result;
		}
		
		Users u = userService.getUserById(orders.getUserId());
		/**
		 * 更新订单信息
		 */
		orders.setOrderStatus(Constants.ORDER_STATUS_3);
		orders.setUpdateTime(TimeStampUtil.getNowSecond());
		ordersService.updateByPrimaryKeySelective(orders);
		OrderPrices record = orderPricesService.selectByOrderId(orders.getId());
		if (record != null) {
			record.setOrderMoney(orderMoney);
			record.setUpdateTime(TimeStampUtil.getNow() / 1000);
			orderPricesService.updateByPrimaryKeySelective(record);
		}else {
			record = orderPricesService.initOrderPrices();
			record.setOrderNo(orders.getOrderNo());
			record.setOrderId(orders.getId());
			record.setMobile(orders.getMobile());
			record.setUserId(orders.getUserId());
			record.setOrderMoney(orderMoney);
			record.setAddTime(TimeStampUtil.getNow() / 1000);
			orderPricesService.insertSelective(record);
		}
		/*
		 * 3.更新订单附加服务表
		 */
		
		int results = orderServiceAddonsService.deleteByOrderNo(orderNo);
		List<OrderServiceAddons> lists = orderExpCleanService.updateOrderServiceAddons(orders.getServiceType(),serviceAddonsDatas);
		for (Iterator iterator = lists.iterator(); iterator.hasNext();) {
			OrderServiceAddons orderServiceAddons = (OrderServiceAddons) iterator.next();
			orderServiceAddons.setOrderId(orders.getId());
			orderServiceAddons.setUserId(u.getId());
			orderServiceAddons.setOrderNo(orderNo);
			if(orderServiceAddons.getItemNum()!=0){
				orderServiceAddonsService.insertSelective(orderServiceAddons);
			}
		}
		
			ordersService.orderExpCleanSuccessTodo(orderNo);
			// 记录订单状态到order_log表
			OrderLog orderLog = new OrderLog();
			orderLog.setOrderId(orders.getId());
			orderLog.setMobile(orders.getMobile());
			orderLog.setOrderNo(orders.getOrderNo());
			orderLog.setOrderStatus(orders.getOrderStatus());
			orderLog.setRemarks("");
			orderLog.setAddTime(TimeStampUtil.getNow() / 1000);

			ordersService.insert(orderLog);
			
			OrderViewVo vo = orderQueryService.getOrderView(orders);
		
			DeepCleanVo deepCleanVo = new DeepCleanVo();
			
			//通过orderNo到order_service_addons查找对应的记录(为了得到ItemUnit、ItemNum的值)
			List<OrderServiceAddons> listPre = orderServiceAddonsService.selectByOrderNo(orderNo);
			List<OrderServiceAddonViewVo> list = orderServiceAddonsService.changeToOrderServiceAddons(listPre);
			try {
				BeanUtils.copyProperties(deepCleanVo,vo);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			//助理调整订单成功给用户发短信
			if(orders.getOrderStatus()==Constants.ORDER_STATUS_3){
				Long serviceDate = orders.getServiceDate();
				String serviceDateString = TimeStampUtil.timeStampToDateStr(serviceDate*1000, " yyyy-MM-dd HH:MM:ss");
				String[] content = new String[] { ""+serviceDateString};
				HashMap<String, String> sendSmsResult = SmsUtil.SendSms(u.getMobile(),
					Constants.AM_CLEAN_NOTICE_CUSTOMER_Message, content);
			}
			deepCleanVo.setCouponId(record.getCouponId());
			deepCleanVo.setList(list);
			result.setData(deepCleanVo);
		return result;
	}
	/**
	 * 根据amId查询助理预约订单
	 * 
	 * @param amId
	 * @return
	 */
	@RequestMapping(value = "get_am_order_list", method = RequestMethod.GET)
	public AppResultData<Object> getAmOrderList(
			@RequestParam("am_id") Long amId,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		// 验证userId是否是秘书
		OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(amId);
		if (orgStaffs == null) {
			return result;
		}

		List<Orders> list = ordersService.selectOrderListByAmId(amId, page,
				Constants.PAGE_MAX_NUMBER);

		List<OrderViewVo> listView = new ArrayList<OrderViewVo>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Orders orders = (Orders) iterator.next();
			OrderViewVo vo = orderQueryService.getOrderView(orders);
			listView.add(vo);
		}
		result.setData(listView);
		return result;
	}

	/**
	 * 深度保洁助理订单展示
	 * @param amId
	 * @param orderNo
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping(value = "get_am_clean_detail", method = RequestMethod.GET)
	public AppResultData<Object> getAmClean(@RequestParam("am_id") Long amId,
			@RequestParam("order_no") String orderNo)
		 {
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
        //验证是否是秘书
		OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(amId);
		if (orgStaffs == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}
        //通过orderNo找到对应的定单
		Orders orders = ordersService.selectByOrderNo(orderNo);
		if (orders == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ORDER_NO_NOT_EXIST_MG);
			return result;
		}
          
		OrderViewVo vo = orderQueryService.getOrderView(orders);
        
		if (vo == null) {

			return result;

		}		
		DeepCleanVo deepCleanVo = new DeepCleanVo();
		 
		//通过orderNo到order_service_addons查找对应的记录(为了得到ItemUnit、ItemNum的值)
		List<OrderServiceAddons> listPre = orderServiceAddonsService.selectByOrderNo(orderNo);
		
		List<OrderServiceAddonViewVo> list = orderServiceAddonsService.changeToOrderServiceAddons(listPre);
		//通过orderNo到order_price查找对应的记录(为了得到CouponId的值)
		OrderPrices orderPrices = orderPricesService.selectByOrderIds(orderNo);

		if (list == null || orderPrices == null) {

			return result;

		}
         
	    try {
			BeanUtils.copyProperties(deepCleanVo,vo);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	    
		deepCleanVo.setCouponId(orderPrices.getCouponId());
		deepCleanVo.setList(list);
		
		UserCoupons  userCoupons = userCouponsService.selectByOrderNo(deepCleanVo.getOrderNo());
		if(userCoupons !=null){
			DictCoupons dictCoupons = dictCouponsService.selectByPrimaryKey(userCoupons.getCouponId());
			if(dictCoupons !=null){
				deepCleanVo.setIntroduction(dictCoupons.getIntroduction());
			}
		}
		List<UserAddrs> userAddrsList = userAddrsService.selectByUserId(orders.getUserId());
		deepCleanVo.setUserAddrsList(userAddrsList);
		result.setData(deepCleanVo);

		return result;
	}

	/**
	 * 助理预约单详情
	 * @param orderNo
	 * @param amId
	 * @return
	 */
	@RequestMapping(value = "get_am_order_detail", method = RequestMethod.GET)
	public AppResultData<Object> getAmOrderDetail(
			@RequestParam("order_no") String orderNo,
			@RequestParam("am_id") Long amId) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		OrgStaffs orderStaffs = orgStaffsService.selectByPrimaryKey(amId);

		// 判断是否为注册助理，非注册助理返回 999
		if (orderStaffs == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}

		Orders orders = ordersService.selectByOrderNo(orderNo);

		if (orders == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ORDER_NO_NOT_EXIST_MG);
			return result;
		}

		OrderViewVo vo = orderQueryService.getOrderView(orders);
		result.setData(vo);
		return result;
	}
	
	/**
	 * 取消订单
	 * @param orderNo
	 * @return
	 */
	@RequestMapping(value="cancle_am_order", method = RequestMethod.POST)
	public AppResultData<Object> cancleAmAlder(
			
			@RequestParam("order_no") String orderNo){
		
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		Orders orders = ordersService.selectByOrderNo(orderNo);
		
		if (orders == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ORDER_NO_NOT_EXIST_MG);
			return result;
		}
		
		// 防止重复 取消
		if(orders.getOrderStatus() == 0){
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.HAVE_CANCLE);
			return result;
		}
		
		
		/*
		 * 取消订单时，对于 已经在 order_dispatches 表中 有 派工  的订单，
		 * 			需要将其派工状态 设置为 无效 0 。
		 *      解决，后台派工列表  空指针 bug
		 */
		OrderDispatchs orderDispatchs = disPatchService.selectByOrderNo(orderNo);
		
		if(orderDispatchs != null){
			orderDispatchs.setDispatchStatus((short)0);
			disPatchService.updateByPrimaryKeySelective(orderDispatchs);
		}
		
		
		
		if (orders.getOrderStatus()==4) {
			
		//	userDetailPayService.addUserDetailPayForOrder();
			
			OrderPrices orderPrices = orderPricesService.selectByOrderNo(orderNo);
			if (orderPrices == null) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.ORDER_NO_NOT_EXIST_MG);
				return result;
			}
			
			//获得当前时间时间戳
			Long nowDate = System.currentTimeMillis()/ 1000;
			//服务时间时间戳
			Long serviceDate = orders.getServiceDate();
            //时间差
		  //  double hours = (double)(serviceDate-nowDate)/3600/1000;
		    Long hours = (serviceDate - nowDate) / 3600;
		    //订单实际支付金额
		    BigDecimal restMoney2 = orderPrices.getOrderPay();
		    
		    if (hours >= 2) {
				//退全款  提示xxx 退回到余额
		    	Users users = userService.selectByUsersId(orders.getUserId());
		    	//获得用户当前余额
		    	BigDecimal restMoney1 = users.getRestMoney();
//		    	System.out.println("余额的总和："+restMoney1.add(restMoney2));
		    	
		    	users.setRestMoney(restMoney1.add(restMoney2));
		    	userService.updateByPrimaryKeySelective(users);
		    	
		    	orderPrices.setOrderPayBack(restMoney2);
		    	orderPricesService.updateByPrimaryKeySelective(orderPrices);
		    	
			}
			//扣除订单金额 提示xxx
		    
		 /**
		  * 如果用户在支付的时候使用了优惠券，
		  * 那么在取消订单的时候把优惠券的is_used set成0===未使用
		  */
			if (orderPrices.getCouponId()>0) {
				
				UserCoupons userCoupons = userCouponsService.selectByPrimaryKey(orderPrices.getCouponId());
				
				userCoupons.setIsUsed((short)0);
				userCoupons.setUsedTime(0L);
				userCoupons.setOrderNo("");
				userCouponsService.updateByPrimaryKeySelective(userCoupons);
			}
		}
		
		if (orders.getOrderStatus()==1||orders.getOrderStatus()==2||orders.getOrderStatus()==3||orders.getOrderStatus()==4) {
			orders.setOrderStatus(Constants.ORDER_STATUS_0);
			ordersService.updateByPrimaryKeySelective(orders);
			
			// 记录订单日志.
			OrderLog orderLog = orderLogService.initOrderLog(orders);
			orderLogService.insert(orderLog);
		}
		
		result.setStatus(Constants.SUCCESS_0);
		result.setMsg("订单已取消");
		return result;
				
	}
}
