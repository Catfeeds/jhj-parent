package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.dao.order.OrderPricesMapper;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.order.OrderAppoint;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.orderReview.JhjSetting;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.dict.ServiceAddonsService;
import com.jhj.service.dict.ServiceTypeService;
import com.jhj.service.order.OrderAppointService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPriceExtService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderServiceAddonsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.orderReview.SettingService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UsersService;
import com.jhj.utils.OrderUtils;
import com.jhj.vo.ServiceAddonSearchVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class OrderPricesServiceImpl implements OrderPricesService {

	@Autowired
	private OrderPricesMapper orderPricesMapper;

	@Autowired
	private OrderServiceAddonsService orderServiceAddonsService;

	@Autowired
	private ServiceTypeService dictServiceTypeService;

	@Autowired
	private OrdersService orderService;

	@Autowired
	private UserCouponsService userCouponsService;

	@Autowired
	private DictCouponsService dictCouponsService;

	@Autowired
	private OrderPriceExtService orderPriceExtService;

	@Autowired
	private OrderDispatchsService orderDispatchService;

	@Autowired
	private UsersService userService;

	@Autowired
	private OrderAppointService orderAppointService;

	@Autowired
	private OrgStaffsService orgStaffsService;

	@Autowired
	private SettingService settingService;
	
	@Autowired
	private PartnerServiceTypeService partnerServiceTypeService;
	
	@Autowired
	private ServiceAddonsService serviceAddonsService;
	

	@Override
	public int deleteByPrimaryKey(Long id) {
		return orderPricesMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrderPrices record) {
		return orderPricesMapper.insert(record);
	}

	@Override
	public int insertSelective(OrderPrices record) {
		return orderPricesMapper.insertSelective(record);
	}

	/*
	 * @Override
	 * public OrderPrices selectByOrderId(Long id) {
	 * return orderPricesMapper.selectByOrderId(id);
	 * }
	 */
	@Override
	public OrderPrices selectByPrimaryKey(Long id) {
		return orderPricesMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(OrderPrices record) {
		return orderPricesMapper.updateByPrimaryKey(record);
	}

	@Override
	public OrderPrices selectByOrderId(Long id) {
		return orderPricesMapper.selectByOrderId(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OrderPrices record) {
		return orderPricesMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public OrderPrices initOrderPrices() {

		OrderPrices record = new OrderPrices();

		record.setId(0L);
		record.setUserId(0L);
		record.setMobile("");
		record.setOrderId(0L);
		record.setOrderNo("");
		record.setPayType((short) Constants.PAY_TYPE_0);

		record.setCouponId(0L); // 优惠券字段，0 = 不使用 >0 使用

		BigDecimal defaultValue = new BigDecimal(0);
		record.setOrderOriginPrice(defaultValue);
		record.setOrderPrimePrice(defaultValue);
		record.setOrderMoney(defaultValue);

		record.setOrderPay(defaultValue);
		record.setOrderPayBack(defaultValue);
		record.setOrderPayBackFee(defaultValue);

		record.setAddTime(TimeStampUtil.getNowSecond());
		record.setUpdateTime(TimeStampUtil.getNowSecond());
		return record;
	}

	@Override
	public List<OrderPrices> selectByOrderIds(List<Long> orderIds) {

		return orderPricesMapper.selectByOrderIds(orderIds);
	}

	@Override
	public OrderPrices selectByOrderIds(String orderNo) {

		return orderPricesMapper.selectByOrderNo(orderNo);
	}

	@Override
	public OrderPrices selectByOrderNo(String orderNo) {
		return orderPricesMapper.selectByOrderNo(orderNo);
	}

	/**
	 * 获取服务订单及优惠券等的金额，返回最终的订单支付金额
	 */
	@Override
	public BigDecimal getPayByOrder(Long orderId, Long userCouponId) {

		BigDecimal orderPayNow = new BigDecimal(0);
		Orders order = orderService.selectByPrimaryKey(orderId);

		if (order == null)
			return orderPayNow;
		OrderPrices orderPrices = this.selectByOrderId(order.getId());

		if (BeanUtilsExp.isNullOrEmpty(orderPrices))
			return orderPayNow;

		BigDecimal orderMoney = orderPrices.getOrderMoney();

		BigDecimal orderPay;

		if (order.getOrderType() == 6) {
			/*
			 * 对于话费充值类订单，实际支付金额在生成时 就是 实际支付金额
			 * 涉及到优惠金额，故而 实际支付金额，在生成订单时，就设置好
			 */
			orderPay = orderPrices.getOrderPay();

		} else {
			orderPay = orderPrices.getOrderMoney();
		}

		// 处理优惠券的情况
		if (userCouponId > 0L && order.getOrderType() != 6) {

			UserCoupons userCoupon = userCouponsService.selectByPrimaryKey(userCouponId);
			DictCoupons dictCoupons = dictCouponsService.selectByPrimaryKey(userCoupon.getCouponId());
			BigDecimal couponValue = dictCoupons.getValue();

			orderPay = MathBigDecimalUtil.sub(orderMoney, couponValue);
		}

		// 实际支付金额
		BigDecimal p1 = new BigDecimal(100);
		BigDecimal p2 = MathBigDecimalUtil.mul(orderPay, p1);
		orderPayNow = MathBigDecimalUtil.round(p2, 0);

		return orderPayNow;
	}

	/**
	 * 获取订单总金额
	 * 订单总金额 = 订单金额 + 订单优惠劵金额 + 订单差价 + 订单加时
	 * 
	 * @param orderPrice
	 * @return
	 */
	@Override
	public BigDecimal getTotalOrderMoney(OrderPrices orderPrice) {
		BigDecimal orderMoney = new BigDecimal(0);
		if (orderPrice == null) return orderMoney;
		orderMoney = orderPrice.getOrderMoney();

		Long orderId = orderPrice.getOrderId();

		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setOrderId(orderId);
		searchVo.setOrderStatus((short) 2);
		List<OrderPriceExt> list = orderPriceExtService.selectBySearchVo(searchVo);

		if (list.isEmpty())
			return orderMoney;

		for (OrderPriceExt item : list) {
			BigDecimal orderPayExt = item.getOrderPay();
			orderMoney = MathBigDecimalUtil.add(orderMoney, orderPayExt);
		}

		return orderMoney;
	}

	/**
	 * 获取订单支付金额
	 * 订单总金额 = 订单金额 + 订单优惠劵金额 + 订单差价 + 订单加时
	 * 
	 * @param orderPrice
	 * @return
	 */
	@Override
	public BigDecimal getTotalOrderPay(OrderPrices orderPrice) {

		BigDecimal orderPay = new BigDecimal(0);

		orderPay = orderPrice.getOrderPay();

		Long orderId = orderPrice.getOrderId();

		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setOrderId(orderId);
		searchVo.setOrderStatus((short) 2);
		List<OrderPriceExt> list = orderPriceExtService.selectBySearchVo(searchVo);

		if (list.isEmpty())
			return orderPay;

		for (OrderPriceExt item : list) {
			BigDecimal orderPayExt = item.getOrderPay();
			orderPay = MathBigDecimalUtil.add(orderPay, orderPayExt);
		}

		return orderPay;
	}

	/**
	 * 获得订单的总收入.
	 * 1. 普通会员 70%
	 * 2. 金牌会员 75%
	 * 3. 会员指定 78%
	 * 订单收入 = 订单支付金额 * 折扣比例 + 订单优惠劵金额 * 折扣比例 + 订单差价金额 * 折扣比例 + 订单加时金额 * 折扣比例
	 * 
	 * @param orders
	 * @params staffId
	 * @return
	 */
//	@Override
//	public BigDecimal getTotalOrderIncoming(Orders order, Long staffId) {
//		Long orderId = order.getId();
//
//		OrderPrices orderPrice = this.selectByOrderId(orderId);
//
//		// 订单支付金额
//		BigDecimal orderPay = orderPrice.getOrderPay();
//
//		// 订单优惠劵金额
//		BigDecimal orderPayCoupon = new BigDecimal(0);
//		Long userCouponId = orderPrice.getCouponId();
//		if (userCouponId > 0L) {
//			UserCoupons userCoupon = userCouponsService.selectByPrimaryKey(userCouponId);
//			Long couponId = userCoupon.getCouponId();
//			DictCoupons dictCoupon = dictCouponsService.selectByPrimaryKey(couponId);
//			orderPayCoupon = dictCoupon.getValue();
//		}
//
//		// 订单补差价金额
//		BigDecimal orderPayExtDiff = orderPriceExtService.getTotalOrderExtPay(order, (short) 0);
//
//		// 订单加时金额
//		BigDecimal orderPayExtOverWork = orderPriceExtService.getTotalOrderExtPay(order, (short) 1);
//
//		// 找出派工，是否为多个
//		OrderDispatchSearchVo orderDispatchSearchVo = new OrderDispatchSearchVo();
//		orderDispatchSearchVo.setOrderId(orderId);
//		orderDispatchSearchVo.setDispatchStatus((short) 1);
//		List<OrderDispatchs> orderDispatchs = orderDispatchService.selectBySearchVo(orderDispatchSearchVo);
//
//		int staffNum = 1;
//		if (!orderDispatchs.isEmpty())
//			staffNum = orderDispatchs.size();
//		
//		BigDecimal incomingPercent = this.getOrderPercent(order, staffId);
//		
//		// 订单金额的折扣比例,先平均在折扣
//		orderPay = MathBigDecimalUtil.div(orderPay, new BigDecimal(staffNum));
//		orderPay = orderPay.multiply(incomingPercent);
//
//		// 订单差价的折扣比例,先平均在折扣
//		orderPayExtDiff = MathBigDecimalUtil.div(orderPayExtDiff, new BigDecimal(staffNum));
//		orderPayExtDiff = orderPayExtDiff.multiply(incomingPercent);
//
//		// 订单加时的折扣比例,先平均在折扣
//		orderPayExtOverWork = MathBigDecimalUtil.div(orderPayExtOverWork, new BigDecimal(staffNum));
//		orderPayExtOverWork = orderPayExtOverWork.multiply(incomingPercent);
//
//		// 订单优惠劵的金额，先平均在折扣
//		orderPayCoupon = MathBigDecimalUtil.div(orderPayCoupon, new BigDecimal(staffNum));
//		BigDecimal couponPercent = new BigDecimal(0.5);
//		orderPayCoupon = orderPayCoupon.multiply(couponPercent);
//
//		// 总收入合计
//		BigDecimal totalOrderPay = new BigDecimal(0);
//		totalOrderPay = totalOrderPay.add(orderPay);
//		totalOrderPay = totalOrderPay.add(orderPayExtDiff);
//		totalOrderPay = totalOrderPay.add(orderPayExtOverWork);
//		totalOrderPay = totalOrderPay.add(orderPayCoupon);
//		totalOrderPay = MathBigDecimalUtil.round(totalOrderPay, 2);
//		return totalOrderPay;
//	}
	
	/**
	 * 获取金牌保洁订单的服务人员收入
	 * 1. 对于订单支付的金额，按照提成配置价格提成，超出小时部分 = 套餐价提成 + （超出小时部分） * 小时提成
	 * 2. 对于补差价和补时的收入，按照比例进行提成。
	 */
	@Override
	public Map<String, String> getTotalOrderIncomingHour(Orders order, Long staffId) {

		Long orderId = order.getId();
		Double serviceHour = order.getServiceHour();
		int staffNum = order.getStaffNums();
		
		Long userId = order.getUserId();
		Users u = userService.selectByPrimaryKey(userId);
		int isVip = u.getIsVip();
		
		Long serviceTypeId = order.getServiceType();
		PartnerServiceType serviceType = partnerServiceTypeService.selectByPrimaryKey(serviceTypeId);

		BigDecimal staffPrice = serviceType.getStaffPrice();
		BigDecimal staffMprice = serviceType.getStaffMprice();
		
		if (isVip == 1) {
			staffPrice = serviceType.getStaffPprice();
			staffMprice = serviceType.getStaffMpprice();
		}
		
		Double minServiceHour = serviceType.getServiceHour();
		
		String incomingStr = "";
		// 总收入合计
		BigDecimal totalOrderPay = new BigDecimal(0);
		
		//计算加时的小时数
		double overWorkHours = 0;
		OrderSearchVo orderSearchVo = new OrderSearchVo();
		orderSearchVo.setOrderId(orderId);
		orderSearchVo.setOrderExtType((short) 1);
		orderSearchVo.setOrderStatus((short) 2);
		List<OrderPriceExt> orderPriceExts = orderPriceExtService.selectBySearchVo(orderSearchVo);
		
		for (OrderPriceExt item : orderPriceExts) {
			overWorkHours = overWorkHours + item.getServiceHour();
		}
		
		totalOrderPay = staffMprice;
		//1. 如果没有加时，则判断是否为最低小时数，如果大于，则 收入 = 套餐价提成 + 超出小时数 * 小时提成。
		if (overWorkHours == 0) {
			if (serviceHour > minServiceHour) {
				Double overHour = serviceHour - minServiceHour;
				BigDecimal tmpOverPay = staffPrice.multiply(new BigDecimal(overHour));
				totalOrderPay = staffMprice.add(tmpOverPay);
			}
		} else {
		    //2. 如果有加时，则并且减去加时后，大于最低小时数，则 收入 = 套餐价提成 + （减去加时后超出小时数） * 小时提成。
			if ( (serviceHour - overWorkHours) > minServiceHour ) {
				Double overHour = serviceHour - overWorkHours - minServiceHour;
				BigDecimal tmpOverPay = staffPrice.multiply(new BigDecimal(overHour));
				totalOrderPay = staffMprice.add(tmpOverPay);
			}
		}
		incomingStr = "订单提成:" +  MathBigDecimalUtil.div(totalOrderPay, new BigDecimal(staffNum));
		
		BigDecimal incomingPercent = this.getOrderPercent(order, staffId);
		// 订单补差价金额 订单差价的折扣比例
		BigDecimal orderPayExtDiff = orderPriceExtService.getTotalOrderExtPay(order, (short) 0);
		orderPayExtDiff = orderPayExtDiff.multiply(incomingPercent);
		if (orderPayExtDiff.compareTo(BigDecimal.ZERO) == 1) {
			totalOrderPay = totalOrderPay.add(orderPayExtDiff);
			incomingStr+= ";订单补差价提成:" +  MathBigDecimalUtil.div(totalOrderPay, new BigDecimal(staffNum));
		}

		// 订单加时金额 订单加时的折扣比例
		BigDecimal orderPayExtOverWork = orderPriceExtService.getTotalOrderExtPay(order, (short) 1);
		orderPayExtOverWork = orderPayExtOverWork.multiply(incomingPercent);
		if (orderPayExtOverWork.compareTo(BigDecimal.ZERO) == 1) {
			totalOrderPay = totalOrderPay.add(orderPayExtOverWork);
			incomingStr+= ";订单加时提成:" +  MathBigDecimalUtil.div(totalOrderPay, new BigDecimal(staffNum));
		}
		
		//最后做一个服务人员平均
		totalOrderPay = MathBigDecimalUtil.div(totalOrderPay, new BigDecimal(staffNum));

		Map<String, String> result = new HashMap<String, String>();
		result.put("incomingStr", incomingStr);
		result.put("totalOrderPay", MathBigDecimalUtil.round2(totalOrderPay));
		return result;
	}
	
	/**
	 * 获取深度保洁订单的服务人员收入
	 * 1. 对于订单支付的金额，按照提成配置价格提成，超出小时部分 = 数量 * 提成
	 * 2. 对于补差价和补时的收入，按照比例进行提成。
	 */
	@Override
	public Map<String, String> getTotalOrderIncomingDeep(Orders order, Long staffId) {

		Long orderId = order.getId();
		int staffNum = order.getStaffNums();
		
		BigDecimal incomingPercent = this.getOrderPercent(order, staffId);
		
	
		
		String incomingStr = "";
		// 总收入合计
		BigDecimal totalOrderPay = new BigDecimal(0);
		
		OrderPrices orderPrice = this.selectByOrderId(orderId);

		// 订单支付金额
		totalOrderPay = orderPrice.getOrderMoney();
		totalOrderPay = totalOrderPay.multiply(incomingPercent);
		incomingStr = "订单提成:" +  MathBigDecimalUtil.div(totalOrderPay, new BigDecimal(staffNum));

		
		// 订单补差价金额 订单差价的折扣比例
		BigDecimal orderPayExtDiff = orderPriceExtService.getTotalOrderExtPay(order, (short) 0);
		orderPayExtDiff = orderPayExtDiff.multiply(incomingPercent);
		if (orderPayExtDiff.compareTo(BigDecimal.ZERO) == 1) {
			totalOrderPay = totalOrderPay.add(orderPayExtDiff);
			incomingStr = ";订单补差价提成:" +  MathBigDecimalUtil.div(totalOrderPay, new BigDecimal(staffNum));
		}

		// 订单加时金额 订单加时的折扣比例
		BigDecimal orderPayExtOverWork = orderPriceExtService.getTotalOrderExtPay(order, (short) 1);
		orderPayExtOverWork = orderPayExtOverWork.multiply(incomingPercent);
		if (orderPayExtOverWork.compareTo(BigDecimal.ZERO) == 1) {
			totalOrderPay = totalOrderPay.add(orderPayExtOverWork);
			incomingStr = ";订单加时提成:" +  MathBigDecimalUtil.div(totalOrderPay, new BigDecimal(staffNum));
		}
		
		//最后做一个服务人员平均
		totalOrderPay = MathBigDecimalUtil.div(totalOrderPay, new BigDecimal(staffNum));

		Map<String, String> result = new HashMap<String, String>();
		result.put("incomingStr", incomingStr);
		result.put("totalOrderPay", MathBigDecimalUtil.round2(totalOrderPay));
		return result;
	}
	

	/**
	 * 获得订单的总欠款
	 * 订单总欠款 = 订单支付金额(如果为现金支付) + 订单加时金额 * 折扣比例
	 * 
	 * @param orders
	 * @params staffId
	 * @return
	 */
	@Override
	public BigDecimal getTotalOrderDept(Orders order, Long staffId) {
		Long orderId = order.getId();

		OrderPrices orderPrice = this.selectByOrderId(orderId);

		// 订单支付金额
		BigDecimal orderPay = new BigDecimal(0);

		if (orderPrice.getPayType().equals(Constants.PAY_TYPE_6)) {
			orderPay = orderPrice.getOrderPay();
		}

		// 订单加时金额
		BigDecimal orderPayExtOverWork = orderPriceExtService.getTotalOrderExtPay(order, (short) 1);

		// 找出派工，是否为多个
		OrderDispatchSearchVo orderDispatchSearchVo = new OrderDispatchSearchVo();
		orderDispatchSearchVo.setOrderId(orderId);
		orderDispatchSearchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> orderDispatchs = orderDispatchService.selectBySearchVo(orderDispatchSearchVo);

		int staffNum = 1;
		if (!orderDispatchs.isEmpty())
			staffNum = orderDispatchs.size();

		// 订单金额平均
		orderPay = MathBigDecimalUtil.div(orderPay, new BigDecimal(staffNum));

		// 订单加时平均
		orderPayExtOverWork = MathBigDecimalUtil.div(orderPayExtOverWork, new BigDecimal(staffNum));

		// 订单欠款合计
		BigDecimal totalOrderDept = new BigDecimal(0);
		totalOrderDept = totalOrderDept.add(orderPay);
		totalOrderDept = totalOrderDept.add(orderPayExtOverWork);

		totalOrderDept = MathBigDecimalUtil.round(totalOrderDept, 2);
		return totalOrderDept;
	}

	// 获取员工折扣
	@Override
	public BigDecimal getOrderPercent(Orders order, Long staffId) {
		
		Long orderId = order.getId();
		// 找出服务人员的折扣比例
		OrgStaffs staffs = orgStaffsService.selectByPrimaryKey(staffId);
		Short level = staffs.getLevel();
		String settingLevel = "-level-" + level.toString();
		Short orderType = order.getOrderType();
		String settingTypeStr = OrderUtils.getOrderSettingType(orderType) + settingLevel;

		JhjSetting settingType = settingService.selectBySettingType(settingTypeStr);

		String settingTypeValue = "0.70";
		if (settingType != null)
			settingTypeValue = settingType.getSettingValue();
		// 提出比例
		BigDecimal incomingPercent = new BigDecimal(settingTypeValue);

		Long userId = order.getUserId();
		Users u = userService.selectByPrimaryKey(userId);
		int isVip = u.getIsVip();
		if (isVip == 1)
			incomingPercent = new BigDecimal(0.75);

		// 判断是否为指定用户
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderId(orderId);
		List<OrderAppoint> orderAppoints = orderAppointService.selectBySearchVo(searchVo);

		if (!orderAppoints.isEmpty()) {
			for (OrderAppoint oa : orderAppoints) {
				if (oa.getStaffId().equals(staffId)) {
					incomingPercent = new BigDecimal(0.78);
				}
			}
		}
		
		return incomingPercent;
	}

}