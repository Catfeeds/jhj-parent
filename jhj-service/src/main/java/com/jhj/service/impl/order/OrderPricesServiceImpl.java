package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.dao.order.OrderPricesMapper;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderAppoint;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.orderReview.JhjSetting;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.dict.ServiceTypeService;
import com.jhj.service.order.OrderAppointService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPriceExtService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderServiceAddonsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.orderReview.SettingService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UsersService;
import com.jhj.utils.OrderUtils;
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
    private  OrderServiceAddonsService  orderServiceAddonsService;
    
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

	/*@Override
	public OrderPrices selectByOrderId(Long id) {
		return orderPricesMapper.selectByOrderId(id);
	}
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
		record.setPayType((short)Constants.PAY_TYPE_0);
 
		record.setCouponId(0L); //优惠券字段，0 = 不使用    >0 使用
		
		BigDecimal defaultValue = new BigDecimal(0);
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
		
		if (order == null) return orderPayNow;
		OrderPrices orderPrices = this.selectByOrderId(order.getId());
		
		if (BeanUtilsExp.isNullOrEmpty(orderPrices)) return orderPayNow;
		
		BigDecimal orderMoney = orderPrices.getOrderMoney();
		
		BigDecimal orderPay ;
		
		if(order.getOrderType() == 6){
			/*
			 * 对于话费充值类订单，实际支付金额在生成时 就是 实际支付金额
			 * 		涉及到优惠金额，故而 实际支付金额，在生成订单时，就设置好
			 */
			orderPay = orderPrices.getOrderPay();
			
		}else{
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
	 * 获取订单实际价格，需要计算是否有订单的补差价.
	 * @param orderPrice
	 * @return
	 */
	@Override
	public BigDecimal getOrderMoney(OrderPrices orderPrice) {
		BigDecimal orderMoney = new BigDecimal(0);
		
		orderMoney = orderPrice.getOrderMoney();
		
		Long orderId = orderPrice.getOrderId();
		
		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setOrderId(orderId);
		searchVo.setOrderStatus((short) 2);
		List<OrderPriceExt> list = orderPriceExtService.selectBySearchVo(searchVo);
		
		if (list.isEmpty()) return orderMoney;
		
		for (OrderPriceExt item : list) {
			BigDecimal orderPayExt = item.getOrderPay();
			orderMoney = MathBigDecimalUtil.add(orderMoney, orderPayExt);
		}
		
		return orderMoney;
	}
	
	/**
	 * 获取订单实际价格，需要计算是否有订单的补差价.
	 * @param orderPrice
	 * @return
	 */
	@Override
	public BigDecimal getOrderPay(OrderPrices orderPrice) {
		BigDecimal orderPay = new BigDecimal(0);
		
		orderPay = orderPrice.getOrderPay();
		
		Long orderId = orderPrice.getOrderId();
		
		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setOrderId(orderId);
		searchVo.setOrderStatus((short) 2);
		List<OrderPriceExt> list = orderPriceExtService.selectBySearchVo(searchVo);
		
		if (list.isEmpty()) return orderPay;
		
		for (OrderPriceExt item : list) {
			BigDecimal orderPayExt = item.getOrderPay();
			orderPay = MathBigDecimalUtil.add(orderPay, orderPayExt);
		}
		
		return orderPay;
	}
	
	
	/**
	 * 获得员工指定的订单的收入.
	 * 1. 普通会员 70%
	 * 2. 金牌会员 75%
	 * 3. 会员指定 78%
	 * @param orders
	 * @params staffId
	 * @return
	 */
	@Override
	public BigDecimal getOrderIncoming(Orders order, Long staffId) {
		Long orderId = order.getId();
		
		OrderPrices orderPrice = this.selectByOrderId(orderId);
		
		BigDecimal orderPay = this.getOrderPay(orderPrice);
		
		//找出派工，是否为多个
		OrderDispatchSearchVo orderDispatchSearchVo = new OrderDispatchSearchVo();
		orderDispatchSearchVo.setOrderId(orderId);
		orderDispatchSearchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> orderDispatchs = orderDispatchService.selectBySearchVo(orderDispatchSearchVo);
		
		if (orderDispatchs.size() > 1) {
			orderPay = MathBigDecimalUtil.div(orderPay, new BigDecimal(orderDispatchs.size()));
		}
		
		OrgStaffs staffs = orgStaffsService.selectByPrimaryKey(staffId);
		Short level = staffs.getLevel();
		String settingLevel = "-level-" + level.toString();
		Short orderType = order.getOrderType();
		String settingTypeStr = OrderUtils.getOrderSettingType(orderType) + settingLevel;

		JhjSetting settingType = settingService.selectBySettingType(settingTypeStr);

		String settingTypeValue = "0.70";
		if (settingType != null) settingTypeValue = settingType.getSettingValue();
		//提出比例 
		BigDecimal incomingPercent = new BigDecimal(settingTypeValue);
		
		Long userId = order.getUserId();
		Users u = userService.selectByPrimaryKey(userId);
		int isVip = u.getIsVip();
		if (isVip == 1) incomingPercent = new BigDecimal(0.75);
		
		//判断是否为指定用户
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
		
		orderPay = orderPay.multiply(incomingPercent);		
		orderPay = MathBigDecimalUtil.round(orderPay, 2);
		return orderPay;
	}
	
	
	/**
	 * 获得员工指定的订单的实际金额，如果是多个订单，则为平均数.
	 * @param orders
	 * @params staffId
	 * @return
	 */
	@Override
	public BigDecimal getOrderMoneyStaff(Orders order, Long staffId) {
		Long orderId = order.getId();
		
		OrderPrices orderPrice = this.selectByOrderId(orderId);
		
		BigDecimal orderMoney = this.getOrderMoney(orderPrice);
		
		//找出派工，是否为多个
		OrderDispatchSearchVo orderDispatchSearchVo = new OrderDispatchSearchVo();
		orderDispatchSearchVo.setOrderId(orderId);
		orderDispatchSearchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> orderDispatchs = orderDispatchService.selectBySearchVo(orderDispatchSearchVo);
		
		if (orderDispatchs.size() > 1) {
			orderMoney = MathBigDecimalUtil.div(orderMoney, new BigDecimal(orderDispatchs.size()));
		}
		
		
		orderMoney = MathBigDecimalUtil.round(orderMoney, 2);
		return orderMoney;
	}
}