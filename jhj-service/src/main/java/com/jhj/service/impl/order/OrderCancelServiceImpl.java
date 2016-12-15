package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.bs.OrgStaffDetailDept;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.UserDetailPay;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.OrgStaffDetailDeptService;
import com.jhj.service.bs.OrgStaffDetailPayService;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderCancelService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPriceExtService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.service.users.UsersService;
import com.jhj.utils.OrderUtils;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.staff.OrgStaffDetailPaySearchVo;
import com.jhj.vo.user.UserDetailSearchVo;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

@Service
public class OrderCancelServiceImpl implements OrderCancelService {

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private OrderPricesService orderPricesService;
	
	@Autowired
	private OrderPriceExtService orderPriceExtService;

	@Autowired
	private OrderDispatchsService orderDispatchService;

	@Autowired
	private UsersService userService;

	@Autowired
	private UserCouponsService userCouponsService;

	@Autowired
	private UserDetailPayService userDetailPayService;
	
	@Autowired
	private DictCouponsService dictCouponsService;
	
	@Autowired
	private OrgStaffDetailPayService orgStaffDetailPayService;
	
	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;
	
	@Autowired
	private OrgStaffDetailDeptService orgStaffDetailDeptService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;

	// 取消派工
	@Override
	public AppResultData<Object> cancleOrderDone(Orders orders) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		Long orderId = orders.getId();
		Long userId = orders.getUserId();
		Short orderStatus = orders.getOrderStatus();

		if (orderStatus.equals(Constants.ORDER_STATUS_0)) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg("此订单已经取消过.");
		}
		
		
		UserDetailSearchVo userSearchVo = new UserDetailSearchVo();
		userSearchVo.setUserId(userId);
		userSearchVo.setOrderId(orderId);
		userSearchVo.setOrderType(Constants.USER_DETAIL_ORDER_TYPE_5);
		List<UserDetailPay> list = userDetailPayService.selectBySearchVo(userSearchVo);
		if (!list.isEmpty()) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg("此订单已经取消过.");
		}

		// 如果订单状态 = 未支付，则更新订单状态为取消即可
		if (orderStatus.equals(Constants.ORDER_HOUR_STATUS_1)) {
			orders.setOrderStatus(Constants.ORDER_STATUS_0);
			orders.setUpdateTime(TimeStampUtil.getNowSecond());
			ordersService.updateByPrimaryKeySelective(orders);
			return result;
		}

		OrderPrices orderPrice = orderPricesService.selectByOrderId(orderId);
		// 如果订单状态 = 已支付，步骤
		// 1.退还用户金额
		// 2 退还用户优惠劵
		// 3.记录用户账号明细，
		// 4.订单状态改为取消
		if (orderStatus.equals(Constants.ORDER_HOUR_STATUS_2)) {
			this.cancelOrderForUserRestMoney(orders, orderPrice);
			this.cancelOrderForUserCoupon(orderPrice);
		}

		// 如果订单状态 = 已派工和开始服务，
		// 1.退还用户金额
		// 2 退还用户优惠劵
		// 3.记录用户账号明细，
		// 4.服务人员已派工状态改为不可用
		// 5.订单状态改为取消
		if (orderStatus.equals(Constants.ORDER_HOUR_STATUS_3) || orderStatus.equals(Constants.ORDER_HOUR_STATUS_5)) {
			this.cancelOrderForUserRestMoney(orders, orderPrice);
			this.cancelOrderForUserCoupon(orderPrice);
			this.cancelOrderForStaffDispatch(orders);
			this.cancelOrderForStatus(orders);
		}

		// 如果订单状态 = 已完成或者已评价
		// 1.退还用户金额
		// 2 退还用户优惠劵
		// 3.记录用户账号明细，
		// 4.服务人员已派工状态改为不可用
		// 5.服务人员收入减少
		// 6.服务人员欠款减少
		// 7.记录服务人员订单取消金额.
		// 8.订单状态改为取消
		if (orderStatus.equals(Constants.ORDER_HOUR_STATUS_7) || orderStatus.equals(Constants.ORDER_HOUR_STATUS_7)) {
			this.cancelOrderForUserRestMoney(orders, orderPrice);
			this.cancelOrderForUserCoupon(orderPrice);
			this.cancelOrderForStaffDispatch(orders);
			
			OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
			searchVo.setOrderId(orderId);
			searchVo.setDispatchStatus((short) 1);
			List<OrderDispatchs> orderDispatchs = orderDispatchService.selectBySearchVo(searchVo);
			
			for (OrderDispatchs item : orderDispatchs) {
				OrgStaffs orgStaff = orgStaffsService.selectByPrimaryKey(item.getStaffId());
				this.cancelOrderForStaffFinance(orders, orderPrice, orgStaff);
			}
			this.cancelOrderForStatus(orders);
		}

		return result;
	}

	// 订单状态改为取消
	private boolean cancelOrderForStatus(Orders order) {
		order.setOrderStatus(Constants.ORDER_STATUS_0);
		order.setUpdateTime(TimeStampUtil.getNowSecond());
		ordersService.updateByPrimaryKeySelective(order);
		return true;
	}

	// 1.退还用户金额, 3.记录交易明细
	private boolean cancelOrderForUserRestMoney(Orders order, OrderPrices orderPrice) {
		
		Long userId = order.getUserId();
		Users user = userService.selectByPrimaryKey(userId);
		
		BigDecimal orderPay = orderPrice.getOrderPay();
		
		BigDecimal orderPayExtDiff = orderPriceExtService.getTotalOrderExtPay(order, (short) 0);
		
		BigDecimal cancelOrderPay = orderPay.add(orderPayExtDiff);
		
		UserDetailSearchVo searchVo = new UserDetailSearchVo();
		searchVo.setUserId(userId);
		searchVo.setOrderId(order.getId());
		searchVo.setOrderType(Constants.USER_DETAIL_ORDER_TYPE_5);
		List<UserDetailPay> list = userDetailPayService.selectBySearchVo(searchVo);
		if (!list.isEmpty()) return false;
		
		// 记录交易明细
		UserDetailPay userDetailPay = userDetailPayService.initUserDetailPay();

		userDetailPay.setUserId(userId);
		userDetailPay.setMobile(user.getMobile());
		userDetailPay.setOrderId(order.getId());
		userDetailPay.setOrderNo(order.getOrderNo());
		userDetailPay.setOrderType(Constants.USER_DETAIL_ORDER_TYPE_5);
		userDetailPay.setPayType(orderPrice.getPayType());
		userDetailPay.setOrderMoney(cancelOrderPay);
		userDetailPay.setOrderPay(cancelOrderPay);
		userDetailPayService.insert(userDetailPay);
		
		
		BigDecimal restMoney = user.getRestMoney();
		BigDecimal add = restMoney.add(cancelOrderPay);
		user.setRestMoney(add);
		user.setUpdateTime(TimeStampUtil.getNowSecond());
		userService.updateByPrimaryKeySelective(user);

		return true;
	}

	// 2.退还用户优惠劵
	private boolean cancelOrderForUserCoupon(OrderPrices orderPrice) {

		Long userCouponId = orderPrice.getCouponId();

		if (userCouponId.equals(0L))
			return true;

		UserCoupons userCoupons = userCouponsService.selectByPrimaryKey(userCouponId);

		if (userCoupons.getIsUsed().equals((short) 1)) {
			userCoupons.setIsUsed((short) 0);
			userCoupons.setUsedTime(0L);
			userCoupons.setOrderNo("");
			userCouponsService.updateByPrimaryKeySelective(userCoupons);
		}
		return true;
	}

	// 4.服务人员已派工状态改为不可用
	private boolean cancelOrderForStaffDispatch(Orders order) {

		Long orderId = order.getId();
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderId(orderId);
		searchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> orderDispatchs = orderDispatchService.selectBySearchVo(searchVo);

		if (orderDispatchs.isEmpty())
			return true;

		for (OrderDispatchs item : orderDispatchs) {
			item.setDispatchStatus((short) 0);
			item.setUpdateTime(TimeStampUtil.getNowSecond());
			orderDispatchService.updateByPrimaryKey(item);
		}
		return true;
	}

	// 5.服务人员收入减少 7.记录服务人员订单取消金额.
	private boolean cancelOrderForStaffFinance(Orders order, OrderPrices orderPrice, OrgStaffs orgStaff) {
		
		Long orderId = order.getId();
		Long staffId = orgStaff.getStaffId();
		BigDecimal totalOrderPay = orderPricesService.getTotalOrderPay(orderPrice);
		BigDecimal orderIncoming = orderPricesService.getTotalOrderIncoming(order, staffId);

		// =======================|订单收入组成，并生成备注

		String remarks = "";
		int staffNum = order.getStaffNums();
		BigDecimal incomingPercent = orderPricesService.getOrderPercent(order, staffId);

		// 1.订单支付金额
		BigDecimal orderPay = orderPrice.getOrderPay();
		orderPay = MathBigDecimalUtil.div(orderPay, new BigDecimal(staffNum));
		orderPay = orderPay.multiply(incomingPercent);
		String orderPayStr = MathBigDecimalUtil.round2(orderPay);
		remarks = "订单收入:" + orderPayStr;
		// 2.订单优惠劵金额
		BigDecimal orderPayCoupon = new BigDecimal(0);
		Long userCouponId = orderPrice.getCouponId();
		if (userCouponId > 0L) {
			UserCoupons userCoupon = userCouponsService.selectByPrimaryKey(userCouponId);
			Long couponId = userCoupon.getCouponId();
			DictCoupons dictCoupon = dictCouponsService.selectByPrimaryKey(couponId);
			orderPayCoupon = dictCoupon.getValue();
			String orderPayCouponStr = MathBigDecimalUtil.round2(orderPayCoupon);
			remarks += " + 订单优惠劵补贴:" + orderPayCouponStr;
		}

		// 3.订单补差价金额
		BigDecimal orderPayExtDiff = orderPriceExtService.getTotalOrderExtPay(order, (short) 0);
		orderPayExtDiff = MathBigDecimalUtil.div(orderPayExtDiff, new BigDecimal(staffNum));
		orderPayExtDiff = orderPayExtDiff.multiply(incomingPercent);
		if (orderPayExtDiff.compareTo(BigDecimal.ZERO) == 1) {
			String orderPayExtDiffStr = MathBigDecimalUtil.round2(orderPayExtDiff);
			remarks += " + 订单补差价收入:" + orderPayExtDiffStr;
		}

		// 4.订单加时金额
		BigDecimal orderPayExtOverWork = orderPriceExtService.getTotalOrderExtPay(order, (short) 1);
		orderPayExtOverWork = MathBigDecimalUtil.div(orderPayExtOverWork, new BigDecimal(staffNum));
		orderPayExtOverWork = orderPayExtOverWork.multiply(incomingPercent);
		if (orderPayExtOverWork.compareTo(BigDecimal.ZERO) == 1) {
			String orderPayExtOverWorkStr = MathBigDecimalUtil.round2(orderPayExtOverWork);
			remarks += " + 订单加时收入:" + orderPayExtOverWorkStr;
		}

		//1. 记录服务人员消费明细
		String orderStatusStr = "取消订单";
		Boolean orderStaffDetailPay = orgStaffDetailPayService.setStaffDetailPay(staffId, orgStaff.getMobile(), Constants.STAFF_DETAIL_ORDER_TYPE_20, orderId, order.getOrderNo(), totalOrderPay, orderIncoming, orderStatusStr, remarks);
		
		
		//2. 扣除用户收入
		OrgStaffFinance orgStaffFinance = orgStaffFinanceService.selectByStaffId(staffId);
		if (orgStaffFinance == null) {
			orgStaffFinance = orgStaffFinanceService.initOrgStaffFinance();
			orgStaffFinance.setStaffId(staffId);
		}
		orgStaffFinance.setMobile(orgStaff.getMobile());
		
		if (orderStaffDetailPay == true) {
			BigDecimal totalIncomingend = orgStaffFinance.getTotalIncoming();
			totalIncomingend.subtract(orderIncoming);
		}
		
		//3. 记录服务人员欠款明细
		BigDecimal totalOrderDept = orderPricesService.getTotalOrderDept(order, staffId);
		if (totalOrderDept.compareTo(BigDecimal.ZERO) == 1) {
			BigDecimal totalDept = orgStaffFinance.getTotalDept();
			
			OrgStaffDetailPaySearchVo orgStaffDetailPaySearchVo = new OrgStaffDetailPaySearchVo();
			orgStaffDetailPaySearchVo.setOrderNo(order.getOrderNo());
			orgStaffDetailPaySearchVo.setStaffId(staffId);
			orgStaffDetailPaySearchVo.setOrderType(Constants.STAFF_DETAIL_DEPT_ORDER_TYPE_1);
			// 判断是否已经存在欠款
			List<OrgStaffDetailDept> orgStaffDetailDepts = orgStaffDetailDeptService.selectBySearchVo(orgStaffDetailPaySearchVo);
			
			if (orgStaffDetailDepts.isEmpty()) {
				OrgStaffDetailDept orgStaffDetailDept = orgStaffDetailDeptService.initOrgStaffDetailDept();
				// 新增欠款明细表 org_staff_detail_dept
				orgStaffDetailDept.setStaffId(staffId);
				orgStaffDetailDept.setMobile(orgStaff.getMobile());
				orgStaffDetailDept.setOrderType(Constants.STAFF_DETAIL_DEPT_ORDER_TYPE_1);
				orgStaffDetailDept.setOrderId(orderId);
				orgStaffDetailDept.setOrderNo(order.getOrderNo());
				orgStaffDetailDept.setOrderMoney(totalOrderPay);
				orgStaffDetailDept.setOrderDept(totalOrderDept);
				orgStaffDetailDept.setOrderStatusStr(orderStatusStr);
				orgStaffDetailDept.setRemarks(remarks);
				orgStaffDetailDeptService.insert(orgStaffDetailDept);
			}
			
			//4. 减去服务人员欠款金额.
			totalDept.subtract(totalOrderDept);
		}
		
		orgStaffFinance.setUpdateTime(TimeStampUtil.getNowSecond());
		if (orgStaffFinance.getId() > 0L) {
			orgStaffFinanceService.updateByPrimaryKeySelective(orgStaffFinance);
		} else {
			orgStaffFinanceService.insert(orgStaffFinance);
		}
		

		return true;
	}

}
