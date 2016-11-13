package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.dao.order.OrderDispatchsMapper;
import com.jhj.po.dao.order.OrderPricesMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.dao.user.UserAddrsMapper;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderHourDetailService;
import com.jhj.service.order.OrderHourListService;
import com.jhj.service.order.OrderPriceExtService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OrderHourViewVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.Week;

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
	private OrderPricesService orderPriceService;
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
	private PartnerServiceTypeService partService;
	
	@Autowired
	private UserCouponsService userCouponService;
	
	@Autowired
	private UserAddrsService userAddrsService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;
	
	@Autowired
	private OrderPriceExtService orderPriceExtService;
	
	
	@Override
	public OrderHourViewVo getOrderHourDetail(String orderNo) {
		
		OrderHourViewVo orderHourViewVo = initOHVO();
		//orders 相关字段
		Orders orders = orderMapper.selectByOrderNo(orderNo);
		BeanUtilsExp.copyPropertiesIgnoreNull(orders, orderHourViewVo);
		
		//订单状态名称		
		Short orderType = orders.getOrderType();
		Long serviceType = orders.getServiceType();

		PartnerServiceType type = partService.selectByPrimaryKey(serviceType);

		if (type != null) {
			orderHourViewVo.setServiceTypeName(type.getName());
		} else {
			orderHourViewVo.setServiceTypeName(OneCareUtil.getJhjOrderTypeName(orderType));
		}
		
		//orderPrices相关字段    优惠券
		OrderPrices orderPrices = orderPriceService.selectByOrderNo(orders.getOrderNo());

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
		
		BigDecimal orderMoney = orderPriceService.getOrderMoney(orderPrices);
		BigDecimal orderPay = orderPriceService.getOrderPay(orderPrices);
		orderHourViewVo.setOrderMoney(orderMoney);
		//实际支付金额
		orderHourViewVo.setOrderPay(orderPay);
				
		//userAddr  服务地址名称
		Long addrId = orders.getAddrId();
		
		UserAddrs userAddrs = userAddrsMapper.selectByPrimaryKey(addrId);
		if(userAddrs!=null){
			orderHourViewVo.setAddrName(userAddrs.getName()+" "+userAddrs.getAddr());
		}
		
		
		//用户姓名
		Users users = usersService.selectByPrimaryKey(orders.getUserId());
		if (users !=null) {
			orderHourViewVo.setName(users.getName());
			//用户性别
			orderHourViewVo.setSex(users.getSex());
			
//			orderHourViewVo.setRemarks(users.getRemarks());
			//账户余额
			orderHourViewVo.setRestMoney(users.getRestMoney());
		}
		
		
		//订单类型名称
		String orderTypeName = OneCareUtil.getJhjOrderTypeName(orders.getOrderType());
		orderHourViewVo.setOrderTypeName(orderTypeName);
		
		
		
		Short orderStatus = orders.getOrderStatus();
		
		String orderStatusName = OneCareUtil.getJhjOrderStausNameFromOrderType(orderType, orderStatus);
		
		orderHourViewVo.setOrderStatusName(orderStatusName);
		
		//服务日期，格式为 yyy-MM-dd(周几) hh:mm
		Long serviceDateTime = orders.getServiceDate();
		
		Long now = TimeStampUtil.getNowSecond();
		if (serviceDateTime < now && orderStatus == 1) {
			orderStatus = Constants.ORDER_HOUR_STATUS_0;
			orderHourViewVo.setOrderStatus(Constants.ORDER_HOUR_STATUS_0);
		}
		
		// 订单状态
		orderHourViewVo.setOrderStatusName(OneCareUtil.getJhjOrderStausNameFromOrderType(orderType, orderStatus));

		String serviceDatePart1 = TimeStampUtil.timeStampToDateStr(serviceDateTime * 1000, "yyyy-MM-dd");
		String serviceDatePart2 = TimeStampUtil.timeStampToDateStr(serviceDateTime * 1000, "HH:mm");
		Date serviceDate = TimeStampUtil.timeStampToDate(serviceDateTime * 1000);
		Week w = DateUtil.getWeek(serviceDate);
		
		String serviceDateStr = serviceDatePart1 + "(" + w.getChineseName() + ")" + " " + serviceDatePart2;
		orderHourViewVo.setServiceDateStr(serviceDateStr);
		
		
		String overWorkStr = orderPriceExtService.getOverWorkStr(orders.getId());
		orderHourViewVo.setOverWorkStr(overWorkStr);
		
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
		OrderPrices orderPrices = orderPriceService.selectByOrderNo(orders.getOrderNo());
		
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
		
		Users users = usersService.selectByPrimaryKey(orders.getUserId());
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
		orderHourViewVo.setServiceTypeName("");
		orderHourViewVo.setServiceDateStr("");
		
		return orderHourViewVo;
	}

	

}
