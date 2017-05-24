package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.dao.bs.OrgStaffsMapper;
import com.jhj.po.dao.order.OrderLogMapper;
import com.jhj.po.dao.order.OrderPricesMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.dao.university.PartnerServiceTypeMapper;
import com.jhj.po.dao.user.UserAddrsMapper;
import com.jhj.po.dao.user.UserCouponsMapper;
import com.jhj.po.dao.user.UsersMapper;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.vo.order.OrderViewVo;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.RandomUtil;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class OrdersServiceImpl implements OrdersService {

	@Autowired
	private OrdersMapper ordersMapper;
	
	@Autowired
	private OrderPricesMapper orderPricesMapper;

	@Autowired
	private OrderLogMapper orderLogMapper;

	@Autowired
	private OrderLogService orderLogService;

	@Autowired
	private OrderPricesService orderPricesService;

	@Autowired
	private OrgStaffsMapper orgStaffsMapper;

	@Autowired
	private UserAddrsMapper userAddrsMapper;

	@Autowired
	private OrderDispatchsService orderDispatchsService;

	@Autowired
	private UsersMapper usersMapper;

	@Autowired
	private UserCouponsMapper userCouponMapper;

	@Autowired
	private PartnerServiceTypeMapper partServiceTypeMapper;
	
	@Autowired
	private UserDetailPayService userDetailPayService;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return ordersMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Orders initOrders() {

		Orders record = new Orders();
		record.setId(0L);
		record.setAmId(0L);
		record.setMobile("");
		record.setUserId(0L);
		record.setCityId(0L);
		record.setOrgId(0L);
		record.setAddrId(0L);
		record.setOrderAddr("");
		record.setOrderType(Constants.ORDER_TYPE_0);// 0 = 钟点工 1 = 深度保洁 2 =
													// 助理预约单
		record.setServiceType(0L);
		record.setServiceContent("");
		record.setServiceDate(0L);
		record.setServiceHour((short) 0);
		record.setStaffNums(0);
		record.setOrderNo("");
		record.setOrderRate((short) 0);// 0 = 好 1 = 一般 2 = 差
		record.setOrderRateContent("");
		record.setOrderStatus(Constants.ORDER_HOUR_STATUS_1);
		record.setOrderFrom(Constants.USER_NET);
		record.setOrderOpFrom(0L);
		record.setRemarks("");
		record.setRemarksConfirm("");

		record.setAddTime(TimeStampUtil.getNowSecond());
		record.setUpdateTime(TimeStampUtil.getNowSecond());
		record.setOrderDoneTime(0L);
		record.setRemarksBussinessConfirm(""); // 运营人员在 后台 对订单详情添加的 备注
		record.setPeriodOrderId(0);

		return record;
	}

	@Override
	public Long insert(Orders record) {
		return ordersMapper.insert(record);
	}

	@Override
	public int insertSelective(Orders record) {
		return ordersMapper.insertSelective(record);
	}

	@Override
	public int updateByPrimaryKeySelective(Orders record) {
		return ordersMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public OrderViewVo changeOrderViewVo(Orders orders) {

		OrderViewVo orderViewVo = new OrderViewVo();

		BeanUtils.copyProperties(orders, orderViewVo);

		if (orderViewVo.getId() > 0L) {

			OrderPrices orderPrices = orderPricesService.selectByOrderId(orderViewVo.getId());
			BigDecimal orderMoney = orderPrices.getOrderMoney();
			orderViewVo.setOrderMoney(orderMoney);
		}

		return orderViewVo;

	}

	@Override
	public Boolean orderAmSuccessTodo(String orderNo) {

		Orders order = ordersMapper.selectByOrderNo(orderNo);

		if (order == null) {
			return false;
		}

		// 记录订单日志.
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLogService.insert(orderLog);

		orderAmPushSms(order);
		return true;
	}

	@Override
	public Boolean orderExpCleanSuccessTodo(String orderNo) {
		Orders order = ordersMapper.selectByOrderNo(orderNo);

		if (order == null) {
			return false;
		}
		// 记录订单日志.
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLogService.insert(orderLog);
		// 深度保洁订单短信推送
		orderExpCleanPushSms(order);
		return true;
	}

	@Override
	public Boolean userOrderAmSuccessTodo(String orderNo) {

		Orders order = ordersMapper.selectByOrderNo(orderNo);

		if (order == null) {
			return false;
		}
		// 记录订单日志.
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLogService.insert(orderLog);
		// 助理订单短信推送
		// userOrderAmPushSms(order);
		return true;
	}

	private boolean orderExpCleanPushSms(Orders order) {

		Short orderStatus = order.getOrderStatus();

		Long userId = order.getUserId();
		Long amId = order.getAmId();
		OrgStaffs orgStaffs = orgStaffsMapper.selectByPrimaryKey(amId);
		// 助理收到待确认的深度保洁订单通知
		if (orderStatus.equals(Constants.ORDER_STATUS_1)) {
			String mobile = orgStaffs.getMobile();

			String code = RandomUtil.randomNumber();
			UserAddrs userAddrs = userAddrsMapper.selectByUserIdAndAddrId(order.getAddrId(), order.getUserId());
			String addrs = userAddrs.getName() + userAddrs.getAddress() + userAddrs.getAddr();
			Long serviceDateLong = order.getServiceDate();
			String serviceDateStr = TimeStampUtil.timeStampToDateStr(serviceDateLong * 1000);
			// String[] content = new String[] { "服务时间：" + serviceDateStr
			// + "服务地点：" + addrs };
			// HashMap<String, String> sendSmsResult = SmsUtil.SendSms(mobile,
			// Constants.GET_AM_EXP_CLEAN_ORDER_ID, content);
		}
		// 用户收到未支付的深度保洁订单通知
		if (orderStatus.equals(Constants.ORDER_STATUS_2)) {
			String mobile = order.getMobile();
			String code = RandomUtil.randomNumber();
			String[] content = new String[] { code, Constants.GET_CODE_MAX_VALID };
			SmsUtil.SendSms(mobile, Constants.GET_USER_NO_PAY_ORDER_ID, content);
		}
		return true;
	}

	private boolean orderAmPushSms(Orders order) {

		Short orderStatus = order.getOrderStatus();

		Long userId = order.getUserId();
		Long amId = order.getAmId();
		OrgStaffs orgStaffs = orgStaffsMapper.selectByPrimaryKey(amId);
		// 发短信给助理
		if (orderStatus.equals(Constants.ORDER_STATUS_1)) {
			String mobile = orgStaffs.getMobile();

			String code = RandomUtil.randomNumber();
			String[] content = new String[] { code, Constants.GET_CODE_MAX_VALID };
			SmsUtil.SendSms(mobile, Constants.GET_CODE_TEMPLE_ID, content);
		}
		// 发短信给用户
		if (orderStatus.equals(Constants.ORDER_STATUS_2)) {
			String mobile = order.getMobile();
			String code = RandomUtil.randomNumber();
			String[] content = new String[] { code, Constants.GET_CODE_MAX_VALID };
			SmsUtil.SendSms(mobile, Constants.GET_CODE_TEMPLE_ID, content);
		}

		return true;
	}

	@Override
	public Orders selectByPrimaryKey(Long id) {

		return ordersMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateUpdateTime(Orders orders) {

		return ordersMapper.updateByUpdateTimeSelective(orders);
	}

	@Override
	public int insert(OrderLog orderLog) {

		return orderLogMapper.insert(orderLog);
	}

	@Override
	public Orders selectByOrderNo(String orderNo) {
		return ordersMapper.selectByOrderNo(orderNo);
	}

	@Override
	public int updateCleanUpdateTime(Orders orders) {

		return ordersMapper.updateByCleanUpdateTimeSelective(orders);
	}

	@Override
	public Boolean userOrderPostBeginSuccessTodo(Orders orders) {

		String mobile = orders.getMobile();
		String code = RandomUtil.randomNumber();
		String[] content = new String[] { code, Constants.GET_CODE_MAX_VALID };
		SmsUtil.SendSms(mobile, Constants.STAFF_POST_BEGIN, content);
		return true;
	}

	@Override
	public Boolean userOrderPostDoneSuccessTodo(Orders orders) {

		String mobile = orders.getMobile();
		String code = RandomUtil.randomNumber();
		String[] content = new String[] { code, Constants.GET_CODE_MAX_VALID };
		SmsUtil.SendSms(mobile, Constants.STAFF_POST_DONE, content);
		return true;
	}

	@Override
	public Boolean userJoinBlackSuccessTodo(String mobile) {

		String code = RandomUtil.randomNumber();
		String[] content = new String[] { code, Constants.GET_CODE_MAX_VALID };
		SmsUtil.SendSms(mobile, Constants.STAFF_JOIN_BLACK, content);
		return true;
	}

	@Override
	public String cancelAmOrder(Orders orders) {

		Short orderStatus = orders.getOrderStatus();

		Long serviceTypeId = orders.getServiceType();

		/*
		 * 助理: 已预约 = 1(可以取消)
		 * 已确认 = 2 (可以取消)
		 * 已支付 = 3（可以取消）
		 * 已派工 = 4（可以取消，退全款）
		 */
		if (orderStatus != 1 && orderStatus != 2 && orderStatus != 3 && orderStatus != 4) {

			PartnerServiceType serviceType = partServiceTypeMapper.selectByPrimaryKey(serviceTypeId);

			String name = serviceType.getName();

			String typeName = OneCareUtil.getJhjOrderStausNameFromOrderType(orders.getOrderType(), orderStatus);

			return typeName + "的" + name + "订单,不能取消";
		}

		// 已支付和 已派工的 订单，需要 处理退款
		if (orderStatus == 3 || orderStatus == 4) {

			OrderPrices orderPrices = orderPricesService.selectByOrderNo(orders.getOrderNo());
			// 获得当前时间时间戳
			Long nowDate = System.currentTimeMillis() / 1000;
			// 服务时间时间戳
			Long serviceDate = orders.getServiceDate();
			// 时间差
			Long hours = (serviceDate - nowDate) / 3600;
			// 订单实际支付金额
			BigDecimal restMoney2 = orderPrices.getOrderPay();

			Short payType = orderPrices.getPayType();

			/**
			 * 2016年5月27日14:51:36
			 * 
			 * 取消订单时, 如果是 现金支付， 不退回余额，只修改状态！！
			 * 
			 * 防止潜在 bug, 现金支付后，一直 取消订单，导致 余额一直变多！！！
			 * 
			 * 现金支付 payType = 6
			 */

			if (hours >= 2 && payType != 6) {
				// 退全款 提示xxx 退回到余额
				Users users = usersMapper.selectByPrimaryKey(orders.getUserId());
				// 获得用户当前余额
				BigDecimal restMoney1 = users.getRestMoney();

				users.setRestMoney(restMoney1.add(restMoney2));
				usersMapper.updateByPrimaryKeySelective(users);

				orderPrices.setOrderPayBack(restMoney2);
				orderPricesService.updateByPrimaryKeySelective(orderPrices);

			} else {
				// 如果超过2小时
				return "服务即将开始,不能退款,如有问题请联系客服";
			}

			/**
			 * 如果用户在支付的时候使用了优惠券，
			 * 那么在取消订单的时候把优惠券的is_used set成0===未使用
			 */
			if (orderPrices.getCouponId() > 0) {

				UserCoupons userCoupons = userCouponMapper.selectByPrimaryKey(orderPrices.getCouponId());

				userCoupons.setIsUsed((short) 0);
				userCoupons.setUsedTime(0L);
				userCoupons.setOrderNo("");
				userCouponMapper.updateByPrimaryKeySelective(userCoupons);
			}

			orders.setOrderStatus(Constants.ORDER_STATUS_0);
			ordersMapper.updateByPrimaryKeySelective(orders);

			// 记录订单日志.
			OrderLog orderLog = orderLogService.initOrderLog(orders);
			orderLogService.insert(orderLog);
		}

		orders.setOrderStatus(Constants.ORDER_STATUS_0);
		ordersMapper.updateByPrimaryKeySelective(orders);

		// 记录订单日志.
		OrderLog orderLog = orderLogService.initOrderLog(orders);
		orderLogService.insert(orderLog);

		// 已预约和已确认订单，不用处理 退款问题
		if (orderStatus == 1 || orderStatus == 2) {
			return "订单取消成功!";
		}

		return "订单取消成功,支付款项已退回您的余额";
	}

	@Override
	public String cancelBaseOrder(Orders orders) {

		Short orderStatus = orders.getOrderStatus();

		Long serviceTypeId = orders.getServiceType();

		/*
		 * 钟点工： 已支付=2 (可以取消，退全款)
		 * 已派工=3 (可以取消，2小时以上退全款，2小时内不退款)
		 */
		if (orderStatus != 2 && orderStatus != 3) {

			PartnerServiceType serviceType = partServiceTypeMapper.selectByPrimaryKey(serviceTypeId);

			String name = serviceType.getName();

			String typeName = OneCareUtil.getJhjOrderStausNameFromOrderType(orders.getOrderType(), orderStatus);

			return typeName + "的" + name + "订单,不能取消订单";
		}

		OrderPrices orderPrices = orderPricesService.selectByOrderNo(orders.getOrderNo());

		// 获得当前时间时间戳
		Long nowDate = System.currentTimeMillis() / 1000;
		// 服务时间时间戳
		Long serviceDate = orders.getServiceDate();
		// 时间差
		Long hours = (serviceDate - nowDate) / 3600;
		// 订单实际支付金额
		BigDecimal restMoney2 = orderPrices.getOrderPay();

		/**
		 * 2016年5月27日14:51:36
		 * 
		 * 取消订单时, 如果是 现金支付， 不退回余额，只修改状态！！
		 * 
		 * 防止潜在 bug, 现金支付后，一直 取消订单，导致 余额一直变多！！！
		 * 
		 * 现金支付 payType = 6
		 */

		Short payType = orderPrices.getPayType();

		if (hours >= 2 && payType != 6) {
			// 退全款 提示xxx 退回到余额
			Users users = usersMapper.selectByPrimaryKey(orders.getUserId());
			// 获得用户当前余额
			BigDecimal restMoney1 = users.getRestMoney();

			users.setRestMoney(restMoney1.add(restMoney2));
			usersMapper.updateByPrimaryKeySelective(users);

			orderPrices.setOrderPayBack(restMoney2);
			orderPricesService.updateByPrimaryKeySelective(orderPrices);

		} else {
			// 服务开始前 2小时内
			return "服务即将开始,不能退款,如有问题请联系客服";
		}

		/**
		 * 如果用户在支付的时候使用了优惠券，
		 * 那么在取消订单的时候把优惠券的is_used set成0===未使用
		 */
		if (orderPrices.getCouponId() > 0) {

			UserCoupons userCoupons = userCouponMapper.selectByPrimaryKey(orderPrices.getCouponId());

			userCoupons.setIsUsed((short) 0);
			userCoupons.setUsedTime(0L);
			userCoupons.setOrderNo("");
			userCouponMapper.updateByPrimaryKeySelective(userCoupons);
		}

		orders.setOrderStatus(Constants.ORDER_STATUS_0);
		ordersMapper.updateByPrimaryKeySelective(orders);

		// 记录订单日志.
		OrderLog orderLog = orderLogService.initOrderLog(orders);
		
		orderLogService.insert(orderLog);

		return "取消成功,支付款项已退回您的余额";
	}

	@Override
	public List<Orders> getNewestOrder(Orders order) {
		
		return ordersMapper.getNewestOrder(order);
	}

	@Override
	public int countUserOrderNum(Long userId) {
		
		return ordersMapper.countUserOrderNum(userId);
	}

	@Override
	public int selectPackEarlyExperience(Long userId, Long serviceTypeId) {
		return ordersMapper.selectPackEarlyExperience(userId,serviceTypeId);
	}

	/**
	 * 后台取消订单
	 * 
	 * @param Long
	 *            orderId 订单ID
	 * 
	 * 
	 * */
	/*@Override
	public int cancelByOrder(Orders order) {
		if (order == null) {
			return -1;
		}
		String orderNo = order.getOrderNo();
		Short orderStatus = order.getOrderStatus();
		OrderPrices orderPrices = orderPricesMapper.selectByOrderNo(orderNo);
		Short payType = orderPrices.getPayType();
		BigDecimal orderPay = orderPrices.getOrderPay();
		BigDecimal orderMoney = orderPrices.getOrderMoney();
		if(orderStatus>=Constants.ORDER_HOUR_STATUS_2 && orderStatus<Constants.ORDER_HOUR_STATUS_7){
			if (payType == Constants.PAY_TYPE_1|| payType == Constants.PAY_TYPE_2 ||payType == Constants.PAY_TYPE_3 || payType == Constants.PAY_TYPE_6 ||payType == Constants.PAY_TYPE_7) {
				commonCancleOrder(order,orderNo);
			}else if(payType==Constants.PAY_TYPE_0){
				commonCancleOrder(order,orderNo);
				Long userId = order.getUserId();
				Users user = usersMapper.selectByPrimaryKey(userId);
				BigDecimal restMoney = user.getRestMoney();
				BigDecimal mon = restMoney.add(orderPay);
				user.setRestMoney(mon);
				usersMapper.updateByPrimaryKeySelective(user);
				
				Long couponId = orderPrices.getCouponId();
				if(couponId>0){
					UserCoupons userCoupons = userCouponMapper.selectByPrimaryKey(couponId);
					userCoupons.setIsUsed((short)0);
					userCoupons.setUsedTime(TimeStampUtil.getNowSecond());
					userCoupons.setOrderNo(null);
					userCouponMapper.updateByPrimaryKeySelective(userCoupons);
				}
				
			}
		}

		return 0;
	}*/
	
	/*public void commonCancleOrder(Orders order,String orderNo){
		//更新订单状态
		order.setOrderStatus(Constants.ORDER_STATUS_0);
		order.setUpdateTime(TimeStampUtil.getNowSecond());
		ordersMapper.updateByPrimaryKeySelective(order);

		//取消派工
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderNo(orderNo);
		searchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(searchVo);
		if (!orderDispatchs.isEmpty()) {
			OrderDispatchs orderDispatch = null;
			Iterator<OrderDispatchs> iterator = orderDispatchs.iterator();
			while (iterator.hasNext()) {
				orderDispatch=iterator.next();
				orderDispatch.setDispatchStatus(Constants.ORDER_DIS_DISABLE);
				orderDispatch.setUpdateTime(TimeStampUtil.getNowSecond());
				orderDispatchsService.updateByPrimaryKeySelective(orderDispatch);
				
			}
		}
		
		//添加订单日志
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLogService.insert(orderLog);
	}*/
}