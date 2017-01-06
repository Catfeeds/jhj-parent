package com.jhj.service.impl.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.model.order.OrderCards;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserDetailPay;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.job.CleanupUserService;
import com.jhj.service.order.OrderCardsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPriceExtService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrderRatesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OrderCardsVo;
import com.jhj.vo.order.OrderSearchVo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月24日下午4:17:01
 * @Description:
 *               定时任务
 */
@Service
public class CleanupUserServiceImpl implements CleanupUserService {

	@Autowired
	private OrdersService orderService;

	@Autowired
	private OrderQueryService orderQueryService;

	@Autowired
	private OrderCardsService orderCardService;

	@Autowired
	private OrderDispatchsService orderDispatchsService;

	@Autowired
	private UsersService userService;
	
	@Autowired
	private UserDetailPayService userDetailPayService;

	@Autowired
	private OrderRatesService orderRateService;

	@Autowired
	private OrgStaffsService orgStaffService;

	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;

	@Autowired
	private OrderPricesService orderPricesService;

	@Autowired
	private OrgStaffsService orgStaffsService;

	@Autowired
	private PartnerServiceTypeService partnerServiceTypeService;
	
	@Autowired
	private OrderPriceExtService orderPriceExtService;

	/*
	 * 处理用户充值记录
	 * 1. 生成用户消费明细
	 * 2. 增加用户余额
	 */
	@Override
	public void reBuildOrderCards() {
		OrderCardsVo searchVo = new OrderCardsVo();
		searchVo.setOrderStatus((short) 1);
		
		List<OrderCards> list = orderCardService.selectBySearchVo(searchVo);
		
		for (OrderCards orderCard : list) {
			
			if (!orderCard.getPayType().equals(Constants.PAY_TYPE_1) && 
				!orderCard.getPayType().equals(Constants.PAY_TYPE_2) ) {
				continue;
			}
			
			Long userId = orderCard.getUserId();
			Users user = userService.selectByPrimaryKey(userId);
			if (user == null) continue;
			
			//2. 增加余额.
			BigDecimal restMoney = user.getRestMoney();
			restMoney = restMoney.add(orderCard.getCardPay());
			
			user.setRestMoney(restMoney);
			user.setUpdateTime(orderCard.getAddTime());
			userService.updateByPrimaryKey(user);
			
			//1. 记录消费明细
			UserDetailPay userDetailPay = new UserDetailPay();

			userDetailPay.setUserId(user.getId());
			userDetailPay.setMobile(user.getMobile());
			userDetailPay.setOrderId(orderCard.getId());
			userDetailPay.setOrderNo(orderCard.getCardOrderNo());

			userDetailPay.setOrderType(Constants.USER_DETAIL_ORDER_TYPE_1);
			userDetailPay.setPayType(orderCard.getPayType());
			userDetailPay.setOrderMoney(orderCard.getCardMoney());
			userDetailPay.setOrderPay(orderCard.getCardPay());
			userDetailPay.setRestMoney(user.getRestMoney());
			// trade_no
			userDetailPay.setPayAccount("");
			userDetailPay.setTradeNo("");
			userDetailPay.setTradeStatus("");

			userDetailPay.setAddTime(orderCard.getAddTime());
			userDetailPayService.insert(userDetailPay);
			
		}
	}

	/*
	 * 处理用户订单记录
	 * 1. 生成用户消费明细
	 * 2. 如果有余额支付，则减去用户余额.
	 */
	@Override
	public void rebuildOrder() {
		OrderSearchVo searchVo = new OrderSearchVo();
		List<Short> orderTypes = new ArrayList<Short>();
		orderTypes.add((short) 0);
		orderTypes.add((short) 1);
		
		List<Short> orderStatusList = new ArrayList<Short>();
		orderStatusList.add((short) 2);
		orderStatusList.add((short) 3);
		orderStatusList.add((short) 4);
		orderStatusList.add((short) 5);
		orderStatusList.add((short) 6);
		orderStatusList.add((short) 7);
		orderStatusList.add((short) 8);
		searchVo.setOrderStatusList(orderStatusList);
		searchVo.setOrderByProperty(" id asc");
		List<Orders> list = orderQueryService.selectBySearchVo(searchVo);
		
		for (int i = 0 ; i < list.size(); i++ ) {
			Orders order = list.get(i);
			Long userId = order.getUserId();
			Users user = userService.selectByPrimaryKey(userId);
			if (user == null) continue;
			
			Long orderId = order.getId();
			String orderNo = order.getOrderNo();
			OrderPrices orderPrice = orderPricesService.selectByOrderId(orderId);
			if (orderPrice == null) continue;
			//1. 如果为用户余额支付，则减去用户余额
			if (orderPrice.getPayType().equals(Constants.PAY_TYPE_0)) {
				BigDecimal restMoney = user.getRestMoney();
				restMoney = restMoney.subtract(orderPrice.getOrderPay());
				user.setRestMoney(restMoney);
				user.setUpdateTime(orderPrice.getAddTime());
				userService.updateByPrimaryKey(user);
			}

			//1. 记录消费明细
			UserDetailPay userDetailPay = new UserDetailPay();

			userDetailPay.setUserId(user.getId());
			userDetailPay.setMobile(user.getMobile());
			userDetailPay.setOrderId(orderId);
			userDetailPay.setOrderNo(orderNo);

			userDetailPay.setOrderType(Constants.USER_DETAIL_ORDER_TYPE_0);
			userDetailPay.setPayType(orderPrice.getPayType());
			userDetailPay.setOrderMoney(orderPrice.getOrderMoney());
			userDetailPay.setOrderPay(orderPrice.getOrderPay());
			userDetailPay.setRestMoney(user.getRestMoney());
			// trade_no
			userDetailPay.setPayAccount("");
			userDetailPay.setTradeNo("");
			userDetailPay.setTradeStatus("");

			userDetailPay.setAddTime(orderPrice.getUpdateTime());
			userDetailPayService.insert(userDetailPay);
			
		}
	}
	
	/*
	 * 处理用户订单补差价金额
	 * 1. 生成用户消费明细
	 * 2. 如果有余额支付，则减去用户余额.
	 */
	@Override
	public void rebuildOrderPayExt() {
		
		OrderSearchVo orderSearchVo = new OrderSearchVo();
		orderSearchVo.setOrderStatus((short) 2);
		List<OrderPriceExt> list = orderPriceExtService.selectBySearchVo(orderSearchVo);
		
		for (int i = 0 ; i < list.size(); i++ ) {
			OrderPriceExt orderPayExt = list.get(i);
			Long userId = orderPayExt.getUserId();
			Users user = userService.selectByPrimaryKey(userId);
			if (user == null) continue;
			
			Long orderId = orderPayExt.getOrderId();
			String orderNo = orderPayExt.getOrderNo();
			String orderNoExt = orderPayExt.getOrderNoExt();
			
			Orders order = orderService.selectByPrimaryKey(orderId);
			if (order == null) continue;
			Short orderStatus = order.getOrderStatus();
			
			if (orderStatus.equals(Constants.ORDER_HOUR_STATUS_0) ||
				orderStatus.equals(Constants.ORDER_HOUR_STATUS_1) ||
				orderStatus.equals(Constants.ORDER_HOUR_STATUS_9) ) {
				continue;
			}
			
			
			
			//1. 如果为用户余额支付，则减去用户余额
			if (orderPayExt.getPayType().equals(Constants.PAY_TYPE_0)) {
				BigDecimal restMoney = user.getRestMoney();
				restMoney = restMoney.subtract(orderPayExt.getOrderPay());
				user.setRestMoney(restMoney);
				user.setUpdateTime(orderPayExt.getAddTime());
				userService.updateByPrimaryKey(user);
			}

			//1. 记录消费明细
			UserDetailPay userDetailPay = new UserDetailPay();

			userDetailPay.setUserId(user.getId());
			userDetailPay.setMobile(user.getMobile());
			userDetailPay.setOrderId(orderId);
			userDetailPay.setOrderNo(orderNoExt);
			
			Short orderType = Constants.USER_DETAIL_ORDER_TYPE_3;
			if (orderPayExt.getOrderExtType() == 1) orderType = Constants.USER_DETAIL_ORDER_TYPE_4;
			
			userDetailPay.setOrderType(orderType);
			userDetailPay.setPayType(orderPayExt.getPayType());
			userDetailPay.setOrderMoney(orderPayExt.getOrderPay());
			userDetailPay.setOrderPay(orderPayExt.getOrderPay());
			userDetailPay.setRestMoney(user.getRestMoney());
			
			// trade_no
			userDetailPay.setPayAccount("");
			userDetailPay.setTradeNo("");
			userDetailPay.setTradeStatus("");

			userDetailPay.setAddTime(orderPayExt.getUpdateTime());
			userDetailPayService.insert(userDetailPay);
			
		}
	}
	
	
	@Override
	public void rebuildOrderCancel() {
		OrderSearchVo searchVo = new OrderSearchVo();
		List<Short> orderTypes = new ArrayList<Short>();
		orderTypes.add((short) 0);
		orderTypes.add((short) 1);
		
		List<Short> orderStatusList = new ArrayList<Short>();
		orderStatusList.add((short) 0);
		
		searchVo.setOrderStatusList(orderStatusList);
		searchVo.setOrderByProperty(" id asc");
		List<Orders> list = orderQueryService.selectBySearchVo(searchVo);
		
		for (int i = 0 ; i < list.size(); i++ ) {
			Orders order = list.get(i);
			Long userId = order.getUserId();
			Users user = userService.selectByPrimaryKey(userId);
			if (user == null) continue;
			
			Long orderId = order.getId();
			String orderNo = order.getOrderNo();
			OrderPrices orderPrice = orderPricesService.selectByOrderId(orderId);
			if (orderPrice == null) continue;

			//1. 记录消费明细 ,订单支付的情况
			UserDetailPay userDetailPay = new UserDetailPay();

			userDetailPay.setUserId(user.getId());
			userDetailPay.setMobile(user.getMobile());
			userDetailPay.setOrderId(orderId);
			userDetailPay.setOrderNo(orderNo);

			userDetailPay.setOrderType(Constants.USER_DETAIL_ORDER_TYPE_0);
			userDetailPay.setPayType(orderPrice.getPayType());
			userDetailPay.setOrderMoney(orderPrice.getOrderMoney());
			userDetailPay.setOrderPay(orderPrice.getOrderPay());
			userDetailPay.setRestMoney(user.getRestMoney());
			// trade_no
			userDetailPay.setPayAccount("");
			userDetailPay.setTradeNo("");
			userDetailPay.setTradeStatus("");

			userDetailPay.setAddTime(order.getAddTime());
			userDetailPayService.insert(userDetailPay);
			
			//1. 记录消费明细，订单取消的情况
			userDetailPay = new UserDetailPay();

			userDetailPay.setUserId(user.getId());
			userDetailPay.setMobile(user.getMobile());
			userDetailPay.setOrderId(orderId);
			userDetailPay.setOrderNo(orderNo);

			userDetailPay.setOrderType(Constants.USER_DETAIL_ORDER_TYPE_5);
			userDetailPay.setPayType(orderPrice.getPayType());
			userDetailPay.setOrderMoney(orderPrice.getOrderMoney());
			userDetailPay.setOrderPay(orderPrice.getOrderPay());
			userDetailPay.setRestMoney(user.getRestMoney());
			// trade_no
			userDetailPay.setPayAccount("");
			userDetailPay.setTradeNo("");
			userDetailPay.setTradeStatus("");

			userDetailPay.setAddTime(order.getUpdateTime());
			userDetailPayService.insert(userDetailPay);
			
		}
	}

}
