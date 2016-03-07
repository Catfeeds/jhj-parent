package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.bs.DictCouponsMapper;
import com.jhj.po.dao.order.OrderPricesMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.dao.user.UserCouponsMapper;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.service.order.OaPhoneChargeOrderService;
import com.jhj.vo.OaPhoneChargeOrderSearchVo;
import com.jhj.vo.order.OaPhoneChargeOrderVo;
import com.meijia.utils.BeanUtilsExp;

/*
 * 话费充值订单 service
 */
@Service
public class OaPhoneChargeOrderServiceImpl implements OaPhoneChargeOrderService {
	
	@Autowired
	private OrdersMapper orderMapper;
	@Autowired
	private OrderPricesMapper orderPriceMapper;
	@Autowired
	private DictCouponsMapper dictCouponMapper;
	@Autowired
	private UserCouponsMapper userCouponMapper;
	
	
	@Override
	public List<Orders> selectList(int pageNo, int pageSize, OaPhoneChargeOrderSearchVo searchVo) {
		
		PageHelper.startPage(pageNo, pageSize);
		
		List<Orders> orderList = orderMapper.selectPhoneOrderListPage(searchVo);
		
		return orderList;
		
	}

	@Override
	public OaPhoneChargeOrderVo tranVo(Orders order) {
		
		OaPhoneChargeOrderVo vo = new OaPhoneChargeOrderVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(order, vo);
		
		Long id = order.getId();
		
		OrderPrices prices = orderPriceMapper.selectByOrderId(id);
		//充值面值
		vo.setOrderMoney(prices.getOrderMoney()); 
		//实际支付
		vo.setRealMoney(prices.getOrderPay());
		//优惠券
		Long couponId = prices.getCouponId();
		
		if(couponId > 0L){
			
			UserCoupons userCoupons = userCouponMapper.selectByPrimaryKey(couponId);
			
			DictCoupons coupons = dictCouponMapper.selectByPrimaryKey(userCoupons.getCouponId());
			
			BigDecimal value = coupons.getValue();
			
			vo.setCouponValue(value);
		}else{
			vo.setCouponValue(new BigDecimal(0));
		}
		
		
		return vo;
	}

}
