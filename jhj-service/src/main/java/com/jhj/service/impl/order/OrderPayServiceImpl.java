package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.UserPushBind;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.dict.ServiceAddonsService;
import com.jhj.service.newDispatch.NewDispatchStaffService;
import com.jhj.service.order.DispatchStaffFromOrderService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderHourAddService;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPayService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderServiceAddonsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.service.users.UserPushBindService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OrgStaffsNewVo;
import com.meijia.utils.GsonUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.PushUtil;
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
    private  OrderServiceAddonsService  orderServiceAddonsService;	
    
    @Autowired
    private ServiceAddonsService serviceAddonsService;     
    
	@Autowired
	private OrgStaffsService orgStaffService;    
	
	@Autowired
	private UserCouponsService userCouponsService;	
	
	@Autowired
	private DispatchStaffFromOrderService dispatchStaffFromOrderService;
	
	@Autowired
	private UserPushBindService bindService;
	
	@Autowired
	private NewDispatchStaffService newDisStaService;
	
	/**
	 * 钟点工订单支付成功,后续通知功能
	 * 1. 进行派工操作
	 * 2. 进行发送短信
	 * @param isChangeDispatch  是否换人，如果是换人，则需要将原有派工取消，再重新派工.
	 */
	@Override
	public boolean orderPaySuccessToDoForHour(Long userId, Long orderId, List<OrgStaffsNewVo> orgStaffsNewVos, boolean isChangeDispatch) {
		
		Orders order = ordersService.selectByPrimaryKey(orderId);
		
		//先判断该订单是否已经派过工，如果已经派工成功，则不需要再次派工.
		//如果为换人派工，则需要将原有派工取消后，再重新派工.
		OrderDispatchs orderDispatched = orderDispatchService.selectByOrderNo(order.getOrderNo());
		if (orderDispatched != null && !isChangeDispatch) {
			return true;
		}
		
		
		if (orderDispatched != null && isChangeDispatch) {
			orderDispatched.setDispatchStatus((short) 0);
			orderDispatched.setUpdateTime(TimeStampUtil.getNowSecond());
			orderDispatchService.updateByPrimaryKeySelective(orderDispatched);
		}
		
		Users u = userService.getUserById(userId);
		
		List<OrgStaffs> staList = new ArrayList<OrgStaffs>();
		
		//实现派工逻辑，找到 阿姨 id, 或者返回 错误标识符
		if (orgStaffsNewVos.isEmpty()) {
//			Long startTime = order.getServiceDate();
//			Long endTime = startTime+order.getServiceHour()*3600;
//			//jhj2.0派工逻辑
//			orgStaffsNewVos = dispatchStaffFromOrderService.getNewBestStaffForHour(startTime, endTime, order.getAddrId(), orderId);
			
			List<Long> staIdList = newDisStaService.autoDispatchForBaseOrder(orderId, order.getServiceDate());
			
			if(staIdList.size() == 0){
				//如果无可用派工。则设置 一个 为 0 的 staffId
				staIdList.add(0L);
			}
			
			staList = orgStaffService.selectByids(staIdList);
			
		}
		
//		if (orgStaffsNewVos.isEmpty()) return false;
		
		if(staList.isEmpty()) return false;
		
		//乱序
		Collections.shuffle(staList);
		
		//取第一个
		OrgStaffs staff = staList.get(0);
		
		//随机取一个阿姨.
//		Random r = new Random();
//		int index = r.nextInt(orgStaffsNewVos.size());
//		OrgStaffsNewVo orgStaffsNewVo = orgStaffsNewVos.get(index);
		
		OrderDispatchs orderDispatchs = orderDispatchService.initOrderDisp(); //派工状态默认有效  1
		
		orderDispatchs.setUserId(userId);
		orderDispatchs.setOrgId(staff.getOrgId());
		orderDispatchs.setMobile(u.getMobile());
		orderDispatchs.setOrderId(order.getId());
		orderDispatchs.setOrderNo(order.getOrderNo());
		
		// 服务开始时间， serviceDate（服务时间）的前一小时， 当前秒值 -3600s
		orderDispatchs.setServiceDatePre(order.getServiceDate() - 3600);  
		orderDispatchs.setServiceDate(order.getServiceDate());
		orderDispatchs.setServiceHours(order.getServiceHour());
		
		//更新服务人员与用户地址距离
//TODO		orderDispatchs.setUserAddrDistance(orgStaffsNewVo.getDistanceValue());
		
		Long staffId = staff.getStaffId();
		
		Long addrId = order.getAddrId();
		
		UserAddrs userAddrs = userAddrService.selectByPrimaryKey(addrId);
		
		String latitude = userAddrs.getLatitude();
		
		String longitude = userAddrs.getLongitude();
		
		int distance = newDisStaService.getLatestDistance(latitude, longitude, staffId);
		
		orderDispatchs.setUserAddrDistance(distance);
		
		//工作人员相关
		//orderDispatchs.setStaffId(orgStaffsNewVo.getStaffId());
		//orderDispatchs.setStaffName(orgStaffsNewVo.getName());
		//orderDispatchs.setStaffMobile(orgStaffsNewVo.getMobile());
		
		orderDispatchs.setStaffId(staff.getStaffId());
		orderDispatchs.setStaffName(staff.getName());
		orderDispatchs.setStaffMobile(staff.getMobile());
		
//		orderDispatchs.setAmId(orgStaffsNewVo.getAmId());
				
		orderDispatchService.insertSelective(orderDispatchs);
		
//		order.setAmId(orgStaffsNewVo.getStaffId());//更新订单中助理Id
		order.setOrderStatus(Constants.ORDER_HOUR_STATUS_3);//更新订单状态---已派工
		order.setUpdateTime(TimeStampUtil.getNowSecond());// 修改 24小时已支付 的助理单，需要用到这个 修改时间
		ordersService.updateByPrimaryKeySelective(order);
		
		
		//2.插入订单日志表  order_log
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLogService.insert(orderLog);
		
		String beginTimeStr = TimeStampUtil.timeStampToDateStr(order.getServiceDate() * 1000, "MM月-dd日HH:mm");
		String endTimeStr = TimeStampUtil.timeStampToDateStr( (order.getServiceDate() + order.getServiceHour() * 3600) * 1000, "HH:mm");
		String timeStr = beginTimeStr + "-" + endTimeStr;

		//1) 用户收到派工通知---发送短信
		String[] contentForUser = new String[] { timeStr };
		SmsUtil.SendSms(u.getMobile(),  "29152", contentForUser);
		
		//2)派工成功，为服务人员发送推送消息---推送消息
		if(orderDispatchs.getDispatchStatus()==1){
//			dispatchStaffFromOrderService.pushToStaff(orgStaffsNewVo.getStaffId(), "true","dispatch", orderId, OneCareUtil.getJhjOrderTypeName(order.getOrderType()), Constants.ALERT_STAFF_MSG);
			
			dispatchStaffFromOrderService.pushToStaff(staff.getStaffId(), "true","dispatch", orderId, OneCareUtil.getJhjOrderTypeName(order.getOrderType()), Constants.ALERT_STAFF_MSG);
		}
		//2.1)派工成功,为服务人员发送短信
//		SmsUtil.SendSms(orgStaffsNewVo.getMobile(),  "64746", contentForUser);

		SmsUtil.SendSms(staff.getMobile(), "64746", contentForUser);
		
		//3)支付完成用户rest_money<60发短信---发送短信
		Users user =userService.selectByUsersId(userId);
		BigDecimal restMoney = user.getRestMoney();
		BigDecimal unit = new BigDecimal(60);
		if(restMoney.compareTo(unit)<0){
			String[] content = new String[] {""};
			HashMap<String, String> sendSmsResult = SmsUtil.SendSms(user.getMobile(),
				Constants.NOTICE_USER_REST_MONEY_NOT_ENOUGH, content);
		}
		//4)如果有优惠劵，则需要将优惠劵变成已使用。
		OrderPrices orderPrice = orderPricesService.selectByOrderId(orderId);
		Long userCouponId = orderPrice.getCouponId();
		if (userCouponId > 0L) {
			UserCoupons userCoupon = userCouponsService.selectByPrimaryKey(userCouponId);
			userCoupon.setIsUsed((short) 1);
			userCoupon.setUsedTime(TimeStampUtil.getNowSecond());
			userCoupon.setOrderNo(order.getOrderNo());
			userCouponsService.updateByPrimaryKey(userCoupon);
		}
		return true;
	}
	
	/**
	 * 助理订单支付成功,后续通知功能
	 * 
	 */
	
	@Override
	public void orderPaySuccessToDoForAm(Orders orders) {
		
		
		
		List<OrderDispatchs> list = orderDispatchService.selectByNoAndDisStatus(orders.getOrderNo(),(short)1);
		
		OrderDispatchs orderDispatchs = new OrderDispatchs();
		
		if(list.size() > 0){
			orderDispatchs = list.get(0);
		}
		
		OrgStaffs am = orgStaffService.selectByPrimaryKey(orderDispatchs.getStaffId());
		String[] contentForAy = new String[] {};
		System.out.println("ayi mobile = " + am.getMobile());
		System.out.println(SmsUtil.SendSms(am.getMobile(),  "29164", contentForAy));
		
		
		//如果有优惠劵，则需要将优惠劵变成已使用。
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
		//支付完成用户rest_money<60发短信
		Users user =userService.selectByUsersId(orders.getUserId());
			BigDecimal restMoney = user.getRestMoney();
			BigDecimal unit = new BigDecimal(60);
		if(restMoney.compareTo(unit)<0){
			String[] content = new String[] {};
			HashMap<String, String> sendSmsResult = SmsUtil.SendSms(user.getMobile(),
				Constants.NOTICE_USER_REST_MONEY_NOT_ENOUGH, content);
		}
		
		//2016年1月30日10:39:32  用户支付完  助理订单后，给助理推送消息
//		Long amId = orderDispatchs.getStaffId();
//		
//		UserPushBind userPushBind = bindService.selectByUserId(amId);
//		
//		//助理的 cid
//		String clientId = userPushBind.getClientId();
//		
//		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("cid", clientId);
//		
//		HashMap<String, String> tranParams = new HashMap<String, String>();
//		 
//		tranParams.put("is_show", "true");		
//		tranParams.put("action", "msg");		
//		tranParams.put("remind_title", "助理预约单");
//		tranParams.put("remind_content", "您好，你的助理预约单，用户已经完成支付，请尽快处理.");
//
//		String jsonParams = GsonUtil.GsonString(tranParams);
//		
//		params.put("transmissionContent", jsonParams);
//		try {
//			boolean flag = PushUtil.AndroidPushToSingle(params);
//		} catch (Exception e) {
//			System.out.println("助理预约单支付完成时,推送给助理的消息,出现异常:"+e.getMessage());
//			e.printStackTrace();
//		}
		
		//todo 支付成功给用户短信.
		
		//todo 增加推送给客服人员短信或者其他方式.
		
	}
	
	/**
	 * 深度保洁订单支付成功,后续通知功能
	 * 1. 如果为
	 */
	@Override
	public void orderPaySuccessToDoForDeep(Orders orders) {
		
		//如果有优惠劵，则需要将优惠劵变成已使用。
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
		//支付完成用户rest_money<60发短信
		Users user =userService.selectByUsersId(orders.getUserId());
			BigDecimal restMoney = user.getRestMoney();
			BigDecimal unit = new BigDecimal(60);
		if(restMoney.compareTo(unit)<0){
			String[] content = new String[] {""};
			HashMap<String, String> sendSmsResult = SmsUtil.SendSms(user.getMobile(),
				Constants.NOTICE_USER_REST_MONEY_NOT_ENOUGH, content);
		}
	}
	
	/*
	 *  话费充值类 订单，支付成功后的 处理
	 *  
	 *  	此处 仅仅  做 发送请求、 判断请求状态
	 *  
	 *  	充值是否成功，通过 回调 url 在   OrderServiceRechargeController  afterWXPayRecharge() 处理
	 */
	@Override
	public void orderPaySuccessToDoForPhone(Orders order) {
		
		
		System.out.println("======WXEND  apix Start========");
		
		/*
		 * 待充值手机号, 
				在  OrderServiceRechargeController 中 设置为  service_content字段
		 */
		
		String phone = order.getServiceContent();
		
		String orderNo = order.getOrderNo();
		
		OrderPrices orderPrice = orderPricesService.selectByOrderNo(orderNo);
		
		/*
		 * 充值金额，即总金额
		 * 		
		 */
		BigDecimal orderMoney = orderPrice.getOrderMoney();
		
		
		/*
		 *  解析 响应报文
		 *      充值接口 报文
		 *    	
		 */
		
		//发起 充值
		String paySubmitResponse = PhoneReChargeUtil.paySubmit(phone, orderMoney.intValue(), orderNo);
		
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(paySubmitResponse);
		
			String code = jsonObject.getString("Code");
			String message = jsonObject.getString("Msg");
			if(!code.equals("0") || !message.equals("success")){
				
				/*
				 * 请求失败，打印错误信息
				 */
				System.out.println(" !!====!!======recharge fail=====!!=====!!:"+ message);
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}	
	
}