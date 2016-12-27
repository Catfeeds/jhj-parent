package com.jhj.service.impl.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.model.order.OrderCards;
import com.jhj.po.model.user.UserDetailPay;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.job.CleanupUserService;
import com.jhj.service.order.OrderCardsService;
import com.jhj.service.order.OrderDispatchsService;
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
			Long userId = orderCard.getUserId();
			Users user = userService.selectByPrimaryKey(userId);
			if (user == null) continue;
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

			// trade_no
			userDetailPay.setPayAccount("");
			userDetailPay.setTradeNo("");
			userDetailPay.setTradeStatus("");

			userDetailPay.setAddTime(orderCard.getAddTime());
			userDetailPayService.insert(userDetailPay);
			//2. 增加余额.
			BigDecimal restMoney = user.getRestMoney();
			restMoney = restMoney.add(orderCard.getCardPay());
			
			user.setRestMoney(restMoney);
			user.setUpdateTime(orderCard.getAddTime());
			userService.updateByPrimaryKey(user);
			
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
		
		List<Short> orderStatus = new ArrayList<Short>();
		orderStatus.add((short) 2);
		orderStatus.add((short) 3);
		orderStatus.add((short) 4);
		orderStatus.add((short) 5);
		orderStatus.add((short) 6);
		orderStatus.add((short) 7);
		orderStatus.add((short) 8);
		
		
		
		
	}

}
