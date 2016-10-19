package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.dao.order.OrderPricesMapper;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.dict.ServiceTypeService;
import com.jhj.service.order.OrderPriceExtService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderServiceAddonsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserCouponsService;
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
}