package com.jhj.service.impl.order;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.order.OrderDispatchsMapper;
import com.jhj.po.dao.order.OrderPricesMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.dao.user.UserAddrsMapper;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderHourDetailService;
import com.jhj.service.order.OrderHourListService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OrderHourViewVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.OneCareUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年8月5日上午11:09:38
 * @Description: 
 *		
 *		用户端 -- 保洁类订单 详情
 */
@Service
public class OrderHourDetailServiceImpl implements OrderHourDetailService {
	
	@Autowired
	private OrdersMapper orderMapper;
	@Autowired
	private OrderPricesMapper orderPricesMapper;
	@Autowired
	private OrderDispatchsMapper orderDisMapper;
	@Autowired
	private UserAddrsMapper userAddrsMapper;
	@Autowired
	private OrderHourListService orderHourListService;
	@Autowired
	private DictCouponsService dictCouponService;
	@Autowired
	private OrdersService orderService;
	@Autowired 
	private UsersService usersService;
	
	@Autowired
	private UserCouponsService userCouponService;
	
	@Autowired
	private UserAddrsService userAddrsService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;
	
	
	@Override
	public OrderHourViewVo getOrderHourDetail(String orderNo) {
		
		OrderHourViewVo orderHourViewVo = initOHVO();
		//orders 相关字段
		Orders orders = orderMapper.selectByOrderNo(orderNo);
		BeanUtilsExp.copyPropertiesIgnoreNull(orders, orderHourViewVo);
		
		//orderPrices相关字段    优惠券
		OrderPrices orderPrices = orderPricesMapper.selectByOrderNo(orders.getOrderNo());

		Long couponId = orderPrices.getCouponId();
		
//		orderHourViewVo.setCouponValue(new BigDecimal(0));
//		orderHourViewVo.setCouponName("");
		if (couponId > 0L) {
			//优惠券id
			orderHourViewVo.setCouponId(couponId);
			UserCoupons userCoupons = userCouponService.selectByPrimaryKey(couponId);
			DictCoupons dictCoupon = dictCouponService.selectByPrimaryKey(userCoupons.getCouponId());
			
			//优惠券面值
			if(userCoupons!=null){
				orderHourViewVo.setCouponValue(dictCoupon.getValue());
				orderHourViewVo.setCouponName(dictCoupon.getIntroduction());
			}
		}
		
		orderHourViewVo.setOrderMoney(orderPrices.getOrderMoney());
		//实际支付金额
		orderHourViewVo.setOrderPay(orderPrices.getOrderPay());
		
		
		//门店Id
		OrgStaffs orgStaffs = orgStaffsService.selectOrgIdByStaffId(orders.getAmId());
		Long orgId = orgStaffs.getOrgId();
		orderHourViewVo.setOrgId(orgId);
		
		
		
		//userAddr  服务地址名称
		Long addrId = orders.getAddrId();
		
		UserAddrs userAddrs = userAddrsMapper.selectByPrimaryKey(addrId);
		if(userAddrs!=null){
			orderHourViewVo.setAddrName(userAddrs.getName()+" "+userAddrs.getAddr());
		}
		
		
		//用户姓名
		Users users = usersService.selectByUsersId(orders.getUserId());
		if (users !=null) {
			orderHourViewVo.setName(users.getName());
			//用户性别
			orderHourViewVo.setSex(users.getSex());
			
//			orderHourViewVo.setRemarks(users.getRemarks());
			//账户余额
			orderHourViewVo.setRestMoney(users.getRestMoney());
		}
		
		
		//服务类型名称
		String orderTypeName = OneCareUtil.getJhjOrderTypeName(orders.getOrderType());
		orderHourViewVo.setOrderTypeName(orderTypeName);
		
		//订单状态名称
		
		//2016年2月20日15:57:49  更新为 二期新的 订单状态名称
		
		Short orderType = orders.getOrderType();
		
		Short orderStatus = orders.getOrderStatus();
		
		String orderStatusName = OneCareUtil.getJhjOrderStausNameFromOrderType(orderType, orderStatus);
		
		orderHourViewVo.setOrderStatusName(orderStatusName);
		
		return orderHourViewVo;
	}
	@Override
	public OrderHourViewVo getOrderPaotui(String orderNo) {
	
		OrderHourViewVo orderHourViewVo = initOHVO();
		//orders 相关字段
		Orders orders = orderMapper.selectByOrderNo(orderNo);
		BeanUtilsExp.copyPropertiesIgnoreNull(orders, orderHourViewVo);
		
		//服务类型名称
		String orderTypeName = OneCareUtil.getJhjOrderTypeName(orders.getOrderType());
		orderHourViewVo.setOrderTypeName(orderTypeName);
				
		//订单状态名称
		String orderStatusName = OneCareUtil.getJhjOrderStausNameFromOrderType(orders.getOrderType(),orders.getOrderStatus());
		orderHourViewVo.setOrderStatusName(orderStatusName);
		
		
		orderHourViewVo.setCouponId(0L);
		orderHourViewVo.setCouponValue(new BigDecimal(0));
		orderHourViewVo.setOrderMoney(new BigDecimal(0));
		orderHourViewVo.setOrderPay(new BigDecimal(0));
		//orderPrices相关字段    优惠券
		OrderPrices orderPrices = orderPricesMapper.selectByOrderNo(orders.getOrderNo());
		
		if (orderPrices != null) {
			Long couponId = orderPrices.getCouponId();
			
			if (couponId > 0L) {
 				//优惠券id
				orderHourViewVo.setCouponId(couponId);
				UserCoupons userCoupons = userCouponService.selectByPrimaryKey(couponId);
				DictCoupons dictCoupon = dictCouponService.selectByPrimaryKey(userCoupons.getCouponId());
				
				//优惠券面值
				if(userCoupons!=null){
					orderHourViewVo.setCouponValue(dictCoupon.getValue());
					orderHourViewVo.setCouponName(dictCoupon.getIntroduction());
				}
			}
		
			orderHourViewVo.setOrderMoney(orderPrices.getOrderMoney());
			//实际支付金额
			orderHourViewVo.setOrderPay(orderPrices.getOrderPay());
		}
		
		Users users = usersService.selectByUsersId(orders.getUserId());
		if (users !=null) {
			//账户余额
			orderHourViewVo.setRestMoney(users.getRestMoney());
		}
		
		return orderHourViewVo;
	}
	@Override
	public OrderHourViewVo initOHVO() {
		
		
		Orders orders = orderService.initOrders();
		
		OrderHourViewVo orderHourViewVo = new OrderHourViewVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(orders, orderHourViewVo);
		
		orderHourViewVo.setSex("");
		orderHourViewVo.setName("");
		orderHourViewVo.setCouponId(0L);
		orderHourViewVo.setCouponValue(new BigDecimal(0));
		
		orderHourViewVo.setCouponName("");
		
		orderHourViewVo.setOrderMoney(new BigDecimal(0));
		orderHourViewVo.setOrderPay(new BigDecimal(0));
		orderHourViewVo.setRestMoney(new BigDecimal(0));
		orderHourViewVo.setRemarks("");
		orderHourViewVo.setAddrName("");
		orderHourViewVo.setOrderStatusName("");
		orderHourViewVo.setOrderTypeName("");
		
		return orderHourViewVo;
	}

	

}
