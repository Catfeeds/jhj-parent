package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.dao.bs.OrgStaffsMapper;
import com.jhj.po.dao.order.OrderDispatchsMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.dao.user.UserAddrsMapper;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.orderReview.JhjSetting;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPriceExtService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrderStatService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.orderReview.SettingService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.utils.OrderUtils;
import com.jhj.vo.chart.CoopUserOrderVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderQuerySearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class OrderStatServiceImpl implements OrderStatService {

	@Autowired
	private OrdersMapper ordersMapper;
	
	@Autowired
	private OrderDispatchsService orderDispatchService;
	
	@Autowired
	private OrdersService orderService;
	
	@Autowired
	private OrderQueryService orderQueryService;
	
	@Autowired
	private OrderPricesService orderPriceService;

	@Autowired
	private UserAddrsService userAddrsSerivce;
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;
	
	@Autowired
	private OrderPriceExtService orderPriceExtService;
	
	@Autowired
	private UserCouponsService userCouponsService;

	@Autowired
	private DictCouponsService dictCouponsService;

	@Override
	public List<Map<String, Object>> selectOrdersCountByYearAndMonth(Long orgStaffId, String start, String end) {
		Long startLong = TimeStampUtil.getMillisOfDay(start) / 1000;
		Long endLong = TimeStampUtil.getMillisOfDay(end) / 1000;
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();

		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("startTime", startLong);
		conditions.put("endTime", endLong);
		conditions.put("orgStaffId", orgStaffId);
		List<Map<String, Object>> list1 = orderDispatchService.totalByYearAndMonth(conditions);
		int total = 0;
		// boolean flag = true;
		for (Iterator<Map<String, Object>> iterator = list1.iterator(); iterator.hasNext();) {
			total = list1.size();
			Map<String, Object> map = iterator.next();
			Map<String, Object> map1 = new HashMap<String, Object>();
			// String serviceDate = (String)map.get("startTime");
			String orderNo = (String) map.get("order_no");
			Long staffId = (Long) map.get("staff_id");

			OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(staffId);
			Orders orders = orderService.selectByOrderNo(orderNo);
			if(orders!=null){
				Long updateTime = (Long) map.get("update_time");
				String serviceEnd = map.get("service_end").toString();
				if (orders.getOrderType().equals(Constants.ORDER_TYPE_1)) {
					if (orders.getOrderStatus() >= 7) {
						Long servicePlanEnd = (long) (orders.getServiceDate() + orders.getServiceHour() * 3600);
						if (updateTime < servicePlanEnd) {
							// %Y-%m-%d %H:%i:%s
							serviceEnd = TimeStampUtil.timeStampToDateStr(updateTime * 1000);
						}
					}
				}
				
				UserAddrs userAddrs = userAddrsSerivce.selectByPrimaryKey(orders.getAddrId());
				if (userAddrs != null) {
					String detailAddrs = userAddrs.getName() + userAddrs.getAddr();
					map1.put("id", map.get("id"));
					map1.put("start", map.get("serviceTime"));
					map1.put("end", map.get("service_end"));
					map1.put("title", orgStaffs.getName() + "有1个派工," + "  服务地址：" + detailAddrs);
					map1.put("color", "blue");
					
					Short orderType = orders.getOrderType();
					
					// 钟点工订单--钟点工订单列表
					if (orderType == Constants.ORDER_TYPE_0) {
						// map1.put("url","/jhj-oa/order/cal-list?name='agendaDay'&serviceDate="+serviceDate+"&staffId="+orgStaffId);
						
						map1.put("url", "/jhj-oa/order/order-hour-list?orderNo=" + orderNo);
					}
					
					// 助理订单--助理订单列表
					if (orderType == Constants.ORDER_TYPE_2) {
						map1.put("url", "/jhj-oa/order/order-am-list?orderNo=" + orderNo);
					}
				}
				listMap.add(map1);
			}
		}
		return listMap;
	}

	@Override
	public List<HashMap> totalByUserIds(List<Long> id) {
		return ordersMapper.totalByUserIds(id);
	}

	@Override
	public int totalIntimacyOrders(Map<String, Long> map) {
		return ordersMapper.totalIntimacyOrders(map);
	}
	
	@Override
	public Long totalOrderInUserIds(List<Long> userIds) {
		return ordersMapper.totalOrderInUserIds(userIds);
	}

	@Override
	public List<CoopUserOrderVo> totalUserAndOrder(List<Long> userIds) {
		return ordersMapper.totalUserAndOrder(userIds);
	}

	
	// . 根据开始时间，接收时间，staff_id ，得出订单总金额
	@Override
	public BigDecimal getTotalOrderMoney(OrderSearchVo vo) {

		BigDecimal totalMoney = new BigDecimal(0);

		List<Orders> list = orderQueryService.selectBySearchVo(vo);
		
		if (list.isEmpty()) return totalMoney;
		
		for (Orders order : list) {
			OrderPrices orderPrice = orderPriceService.selectByOrderId(order.getId());
			BigDecimal orderPayStaff = orderPriceService.getTotalOrderMoney(orderPrice);
			totalMoney = totalMoney.add(orderPayStaff);
		}

		totalMoney = MathBigDecimalUtil.round(totalMoney, 2);
		
		return totalMoney;

	}	
	
	
	// . 根据开始时间，接收时间，staff_id ，得出订单总金额
	@Override
	public BigDecimal getTotalOrderPay(OrderSearchVo vo) {

		BigDecimal totalMoney = new BigDecimal(0);

		List<Orders> list = orderQueryService.selectBySearchVo(vo);
		
		if (list.isEmpty()) return totalMoney;
		
		for (Orders order : list) {
			OrderPrices orderPrice = orderPriceService.selectByOrderId(order.getId());
			BigDecimal orderPayStaff = orderPriceService.getTotalOrderPay(orderPrice);
			totalMoney = totalMoney.add(orderPayStaff);
		}

		totalMoney = MathBigDecimalUtil.round(totalMoney, 2);
		
		return totalMoney;

	}
	
	// . 根据开始时间，接收时间，staff_id ，得出订单总收入金额
	@Override
	public BigDecimal getTotalOrderIncomeMoney(OrderSearchVo searchVo) {
		
		BigDecimal totalIncomingMoney = new BigDecimal(0);
		
		List<Short> orderStatusList = new ArrayList<Short>();
		orderStatusList.add(Constants.ORDER_HOUR_STATUS_7);
		orderStatusList.add(Constants.ORDER_HOUR_STATUS_8);
		searchVo.setOrderStatusList(orderStatusList);
		
		List<Orders> list = orderQueryService.selectBySearchVo(searchVo);
		
		if (list.isEmpty()) return totalIncomingMoney;
		
		for (Orders order : list) {
			BigDecimal orderIncoming = new BigDecimal(0);
			//2017年3月15 14:00号之前的订单，按照老的计算方法.
			if (order.getOrderDoneTime() <= 1489557600) {
				orderIncoming = orderPriceService.getTotalOrderIncoming(order, searchVo.getStaffId());
			} else {
				Map<String, String> orderIncomingMap = new HashMap<String, String>();
				
				if (order.getOrderType().equals(Constants.ORDER_TYPE_0)) {
					orderIncomingMap = orderPriceService.getTotalOrderIncomingHour(order, searchVo.getStaffId());
				}
				
				if (order.getOrderType().equals(Constants.ORDER_TYPE_1)) {
					orderIncomingMap = orderPriceService.getTotalOrderIncomingDeep(order, searchVo.getStaffId());
				}
				
				String orderIncomingStr = orderIncomingMap.get("totalOrderPay");
				orderIncoming = new BigDecimal(orderIncomingStr);
			}
			totalIncomingMoney = totalIncomingMoney.add(orderIncoming);
		}

		totalIncomingMoney = MathBigDecimalUtil.round(totalIncomingMoney, 2);

		return totalIncomingMoney;
	}
	
	// . 根据开始时间，接收时间，staff_id ，得出订单总数:
	@Override
	public Long getTotalOrderCount(OrderSearchVo vo) {

		return ordersMapper.getTotalOrderCount(vo);
	}

	// 当月订单总数（order_status=7,8)
	@Override
	public Long getTotalOrderCountByMouth(OrderQuerySearchVo searchVo) {

		return ordersMapper.getTotalOrderCountByMouth(searchVo);
	}
	
	
	/*
	 * 根据查询条件获得统计的金额指标，包括
	 * 1. totalOrderMoney    订单总金额
	 * 2. totalOrderPay      订单支付金额
	 * 3. totalOrderCoupon   订单优惠劵金额
	 * 4. totalOrderIncoming 订单总收入
	 * 5. totalOrderPayType0 订单总余额支付
	 * 6. totalOrderPayType1 订单总支付宝支付
	 * 7. totalOrderPayType2 订单总微信支付
	 * 8. totalOrderPayType7 订单总平台已支付
	 * 9. totalOrderPayType6 订单总现金支付
	 * 
	 */
	
	@Override
	public Map<String, String> getTotalOrderMoneyMultiStat(OrderSearchVo searchVo) {

		BigDecimal totalOrderMoney = new BigDecimal(0);
		BigDecimal totalOrderPay = new BigDecimal(0);
		BigDecimal totalOrderCoupon = new BigDecimal(0);
		BigDecimal totalOrderIncoming = new BigDecimal(0);
		BigDecimal totalOrderPayType0 = new BigDecimal(0);
		BigDecimal totalOrderPayType1 = new BigDecimal(0);
		BigDecimal totalOrderPayType2 = new BigDecimal(0);
		BigDecimal totalOrderPayType6 = new BigDecimal(0);
		BigDecimal totalOrderPayType7 = new BigDecimal(0);
		
		Map<String, String> statResult = new HashMap<String, String>();
		statResult.put("totalOrderMoney", "0");
		statResult.put("totalOrderPay", "0");
		statResult.put("totalOrderCoupon", "0");
		statResult.put("totalOrderIncoming", "0");
		statResult.put("totalOrderPayType0", "0");
		statResult.put("totalOrderPayType1", "0");
		statResult.put("totalOrderPayType2", "0");
		statResult.put("totalOrderPayType6", "0");
		statResult.put("totalOrderPayType7", "0");
		
		List<Orders> list = orderQueryService.selectBySearchVo(searchVo);
		
		if (list.isEmpty()) return statResult;
		
		int numDo = 0;
		int numError = 0;
		for (Orders order : list) {
			numDo++;
			Long orderId = order.getId();
			OrderPrices orderPrice = orderPriceService.selectByOrderId(order.getId());
			if (orderPrice == null) {
				numError++;
				continue;
			}
			Short payType = orderPrice.getPayType();
			
			//订单金额
			BigDecimal orderMoney = orderPrice.getOrderMoney();

			//订单支付金额
			BigDecimal orderPay = orderPrice.getOrderPay();
			
			BigDecimal orderPayType0 = new BigDecimal(0);
			//订单余额支付
			if (payType.equals(Constants.PAY_TYPE_0)) {
				orderPayType0 = orderPrice.getOrderPay();
			}
			
			//订单支付宝支付
			BigDecimal orderPayType1 = new BigDecimal(0);
			if (payType.equals(Constants.PAY_TYPE_1)) {
				orderPayType1 = orderPrice.getOrderPay();
			}
			
			//订单微信支付
			BigDecimal orderPayType2 = new BigDecimal(0);
			if (payType.equals(Constants.PAY_TYPE_2)) {
				orderPayType2 = orderPrice.getOrderPay();
			}
			
			//订单总现金支付
			BigDecimal orderPayType6 = new BigDecimal(0);
			if (payType.equals(Constants.PAY_TYPE_6)) {
				orderPayType6 = orderPrice.getOrderPay();
			}
			
			//订单总平台已支付
			BigDecimal orderPayType7 = new BigDecimal(0);
			if (payType.equals(Constants.PAY_TYPE_7)) {
				orderPayType7 = orderPrice.getOrderPay();
			}
			
			
			BigDecimal orderPayCoupon = new BigDecimal(0);
			Long couponId = orderPrice.getCouponId();
			if (couponId > 0L) {
				UserCoupons userCoupon = userCouponsService.selectByPrimaryKey(couponId);
				if (userCoupon != null) orderPayCoupon = userCoupon.getValue();
			}
			
			//订单补差价，订单补时
			OrderSearchVo orderPriceExtSearchVo = new OrderSearchVo();
			orderPriceExtSearchVo.setOrderId(orderId);
			orderPriceExtSearchVo.setOrderStatus((short) 2);
			List<OrderPriceExt> orderPriceExts = orderPriceExtService.selectBySearchVo(orderPriceExtSearchVo);
			
			if (!orderPriceExts.isEmpty()) {
				for (OrderPriceExt op : orderPriceExts) {
					Short opPayType = op.getPayType();
					//订单金额
					orderMoney = orderMoney.add(op.getOrderPay());
					
					//订单支付金额
					orderPay = orderPay.add(op.getOrderPay());
					
					//订单余额支付
					if (opPayType.equals(Constants.PAY_TYPE_0)) {
						orderPayType0 = orderPayType0.add(op.getOrderPay());
					}
					
					//订单总支付宝支付
					if (opPayType.equals(Constants.PAY_TYPE_1)) {
						orderPayType1 = orderPayType1.add(op.getOrderPay());
					}
					
					//订单总微信支付
					if (opPayType.equals(Constants.PAY_TYPE_2)) {
						orderPayType2 = orderPayType2.add(op.getOrderPay());
					}
					
					//订单总现金支付
					if (opPayType.equals(Constants.PAY_TYPE_6)) {
						orderPayType6 = orderPayType6.add(op.getOrderPay());
					}
				}
			}
			
			//计算订单收入, 完成服务和评价的订单才有订单收入.
			BigDecimal orderIncoming = new BigDecimal(0);
			Short orderStatus = order.getOrderStatus();
			if (orderStatus.equals(Constants.ORDER_HOUR_STATUS_7) || orderStatus.equals(Constants.ORDER_HOUR_STATUS_8)) {
				OrderDispatchSearchVo orderDispatchSearchVo = new OrderDispatchSearchVo();
				orderDispatchSearchVo.setOrderId(orderId);
				orderDispatchSearchVo.setDispatchStatus((short) 1);
				
				//如果查单个人，订单收入需要计算一个人即可
				if (searchVo.getStaffId() != null ) {
					orderDispatchSearchVo.setStaffId(searchVo.getStaffId());
				}
				
				List<OrderDispatchs> orderDispatchs = orderDispatchService.selectBySearchVo(orderDispatchSearchVo);
				if (!orderDispatchs.isEmpty()) {
					for (OrderDispatchs od : orderDispatchs) {
						Long staffId = od.getStaffId();

						BigDecimal incomingPercent = orderPriceService.getOrderPercent(order, staffId);
						int staffNum = order.getStaffNums();
						BigDecimal orderPayStaff = MathBigDecimalUtil.div(orderPay, new BigDecimal(staffNum));
						orderPayStaff = orderPayStaff.multiply(incomingPercent);
						BigDecimal orderPayCouponStaff = MathBigDecimalUtil.div(orderPayCoupon, new BigDecimal(staffNum));
						BigDecimal couponPercent = new BigDecimal(0.5);
						orderPayCouponStaff = orderPayCoupon.multiply(couponPercent);
						
						orderIncoming = orderIncoming.add(orderPayStaff);
						orderIncoming = orderIncoming.add(orderPayCouponStaff);
						
					}
				}
			}
			
			
			
			//订单总金额
			totalOrderMoney = totalOrderMoney.add(orderMoney);
			
			//订单支付金额
			totalOrderPay = totalOrderPay.add(orderPay);
			
			//订单优惠劵总金额
			totalOrderCoupon = totalOrderCoupon.add(orderPayCoupon);
			
			//订单收入
			totalOrderIncoming = totalOrderIncoming.add(orderIncoming);
			
			//订单总余额支付
			totalOrderPayType0 = totalOrderPayType0.add(orderPayType0);
			
			//订单总支付宝支付
			totalOrderPayType1 = totalOrderPayType1.add(orderPayType1);
			
			//订单总微信支付
			totalOrderPayType2 = totalOrderPayType2.add(orderPayType2);
			
			//订单总现金支付
			totalOrderPayType6 = totalOrderPayType6.add(orderPayType6);
			
			//订单总平台已支付
			totalOrderPayType7 = totalOrderPayType7.add(orderPayType7);
			
			
		}

		statResult.put("totalOrderMoney", MathBigDecimalUtil.round2(totalOrderMoney));
		statResult.put("totalOrderPay", MathBigDecimalUtil.round2(totalOrderPay));
		statResult.put("totalOrderCoupon", MathBigDecimalUtil.round2(totalOrderCoupon));
		statResult.put("totalOrderIncoming", MathBigDecimalUtil.round2(totalOrderIncoming));
		statResult.put("totalOrderPayType0", MathBigDecimalUtil.round2(totalOrderPayType0));
		statResult.put("totalOrderPayType1", MathBigDecimalUtil.round2(totalOrderPayType1));
		statResult.put("totalOrderPayType2", MathBigDecimalUtil.round2(totalOrderPayType2));
		statResult.put("totalOrderPayType6", MathBigDecimalUtil.round2(totalOrderPayType6));
		statResult.put("totalOrderPayType7", MathBigDecimalUtil.round2(totalOrderPayType7));
		
		System.out.println("totalOrderCount = " + list.size());
		System.out.println("numDo = " + numDo);
		System.out.println("numError = " + numError);
		return statResult;

	}		
	
}
