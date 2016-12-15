package com.jhj.service.impl.bs;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.dao.bs.OrgStaffFinanceMapper;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.bs.OrgStaffBlack;
import com.jhj.po.model.bs.OrgStaffDetailDept;
import com.jhj.po.model.bs.OrgStaffDetailPay;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.orderReview.JhjSetting;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.UserDetailPay;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.OrgStaffBlackService;
import com.jhj.service.bs.OrgStaffDetailDeptService;
import com.jhj.service.bs.OrgStaffDetailPayService;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPriceExtService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.orderReview.SettingService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.service.users.UsersService;
import com.jhj.utils.OrderUtils;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.staff.OrgStaffDetailPaySearchVo;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年7月6日下午2:44:25
 * @Description:
 *
 */
@Service
public class OrgStaffFinanceServiceImpl implements OrgStaffFinanceService {

	@Autowired
	private OrgStaffFinanceMapper orgStaffFinanceMapper;

	@Autowired
	private OrderPricesService orderPricesService;

	@Autowired
	private OrderPriceExtService orderPriceExtService;

	@Autowired
	private OrgStaffDetailDeptService orgStaffDetailDeptService;

	@Autowired
	private OrgStaffDetailPayService orgStaffDetailPayService;

	@Autowired
	private SettingService settingService;

	@Autowired
	private OrgStaffBlackService orgStaffBlackService;

	@Autowired
	private OrdersService ordersService;

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

	@Override
	public int deleteByPrimaryKey(Long id) {

		return orgStaffFinanceMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Long insert(OrgStaffFinance record) {

		return orgStaffFinanceMapper.insert(record);
	}

	@Override
	public Long insertSelective(OrgStaffFinance record) {

		return orgStaffFinanceMapper.insertSelective(record);
	}

	@Override
	public OrgStaffFinance selectByPrimaryKey(Long orderId) {

		return orgStaffFinanceMapper.selectByPrimaryKey(orderId);
	}

	@Override
	public int updateByPrimaryKeySelective(OrgStaffFinance record) {

		return orgStaffFinanceMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrgStaffFinance record) {

		return orgStaffFinanceMapper.updateByPrimaryKey(record);
	}

	@Override
	public OrgStaffFinance initOrgStaffFinance() {

		OrgStaffFinance record = new OrgStaffFinance();

		record.setId(0L);
		record.setStaffId(0L);
		record.setMobile("");
		record.setTotalIncoming(new BigDecimal(0));
		record.setTotalDept(new BigDecimal(0));
		record.setTotalCash(new BigDecimal(0));
		record.setRestMoney(new BigDecimal(0));
		record.setIsBlack((short) 0);
		record.setAddTime(TimeStampUtil.getNowSecond());
		record.setUpdateTime(TimeStampUtil.getNowSecond());

		return record;
	}

	@Override
	public OrgStaffFinance selectByStaffId(Long userId) {

		return orgStaffFinanceMapper.selectByStaffId(userId);
	}

	@Override
	public PageInfo selectByListPage(OrgStaffFinanceSearchVo searchVo, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<OrgStaffFinance> list = orgStaffFinanceMapper.selectByListPage(searchVo);
		PageInfo result = new PageInfo(list);
		return result;
	}

	@Override
	public List<OrgStaffFinance> selectBySearchVo(OrgStaffFinanceSearchVo searchVo) {
		return orgStaffFinanceMapper.selectBySearchVo(searchVo);
	}
	
	/**
	 * 完成服务的操作
	 * 1. 记录消费明细
	 * 2. 如果有欠款，记录明细
	 * 3. 如果有加时，则需要记录加时的欠款
	 * 4. 服务人员总收入增加
	 * 5. 服务人员总欠款增加
	 */
	@Override
	public void orderDone(Orders orders, OrderPrices orderPrices, OrgStaffs orgStaffs) {
		Long orderId = orders.getId();
		Long staffId = orgStaffs.getStaffId();

		BigDecimal totalOrderPay = orderPricesService.getTotalOrderPay(orderPrices);
		BigDecimal orderIncoming = orderPricesService.getTotalOrderIncoming(orders, staffId);
		
		
		//=======================|订单收入组成，并生成备注
		
		String remarks = "";
		int staffNum = orders.getStaffNums();
		BigDecimal incomingPercent = orderPricesService.getOrderPercent(orders, staffId);
		
		//1.订单支付金额
		BigDecimal orderPay = orderPrices.getOrderPay();
		orderPay = MathBigDecimalUtil.div(orderPay, new BigDecimal(staffNum));
		orderPay = orderPay.multiply(incomingPercent);
		String orderPayStr = MathBigDecimalUtil.round2(orderPay);
		remarks = "订单收入:" + orderPayStr;
		//2.订单优惠劵金额
		BigDecimal orderPayCoupon = new BigDecimal(0);
		Long userCouponId = orderPrices.getCouponId();
		if (userCouponId > 0L) {
			UserCoupons userCoupon = userCouponsService.selectByPrimaryKey(userCouponId);
			Long couponId = userCoupon.getCouponId();
			DictCoupons dictCoupon = dictCouponsService.selectByPrimaryKey(couponId);
			orderPayCoupon = dictCoupon.getValue();
			String orderPayCouponStr = MathBigDecimalUtil.round2(orderPayCoupon);
			remarks+= " + 订单优惠劵补贴:" + orderPayCouponStr;
		}
		

		//3.订单补差价金额
		BigDecimal orderPayExtDiff = orderPriceExtService.getTotalOrderExtPay(orders, (short) 0);
		orderPayExtDiff = MathBigDecimalUtil.div(orderPayExtDiff, new BigDecimal(staffNum));
		orderPayExtDiff = orderPayExtDiff.multiply(incomingPercent);
		if (orderPayExtDiff.compareTo(BigDecimal.ZERO) == 1) {
			String orderPayExtDiffStr = MathBigDecimalUtil.round2(orderPayExtDiff);
			remarks+= " + 订单补差价收入:" + orderPayExtDiffStr;
		}
		
		//4.订单加时金额
		BigDecimal orderPayExtOverWork = orderPriceExtService.getTotalOrderExtPay(orders, (short) 1);
		orderPayExtOverWork = MathBigDecimalUtil.div(orderPayExtOverWork, new BigDecimal(staffNum));
		orderPayExtOverWork = orderPayExtOverWork.multiply(incomingPercent);
		if (orderPayExtOverWork.compareTo(BigDecimal.ZERO) == 1) {
			String orderPayExtOverWorkStr = MathBigDecimalUtil.round2(orderPayExtOverWork);
			remarks+= " + 订单加时收入:" + orderPayExtOverWorkStr;
		}

		// 服务人员财务表
		OrgStaffFinance orgStaffFinance = this.selectByStaffId(staffId);
		if (orgStaffFinance == null) {
			orgStaffFinance = this.initOrgStaffFinance();
			orgStaffFinance.setStaffId(staffId);
		}
		orgStaffFinance.setMobile(orgStaffs.getMobile());

		// 新增服务人员交易明细表 org_staff_detail_pay
		String orderStatusStr = OrderUtils.getOrderStatusName(orders.getOrderType(), orders.getOrderStatus());
		Boolean orderStaffDetailPay = orgStaffDetailPayService.setStaffDetailPay(staffId, orgStaffs.getMobile(), Constants.STAFF_DETAIL_ORDER_TYPE_0, orderId, orders.getOrderNo(), totalOrderPay, orderIncoming, orderStatusStr, remarks);
		
		if (orderStaffDetailPay == true) {
			// 总收入
			BigDecimal totalIncoming = orgStaffFinance.getTotalIncoming();
			// 最终总收入
			BigDecimal totalIncomingend = totalIncoming.add(orderIncoming);
			orgStaffFinance.setTotalIncoming(totalIncomingend);
			orgStaffFinance.setUpdateTime(TimeStampUtil.getNowSecond());

			if (orgStaffFinance.getId() > 0L) {
				this.updateByPrimaryKeySelective(orgStaffFinance);
			} else {
				this.insert(orgStaffFinance);
			}
		}

		if (orderPrices.getPayType().equals((short) 6)) {
			OrgStaffDetailPaySearchVo orgStaffDetailPaySearchVo = new OrgStaffDetailPaySearchVo();
			orgStaffDetailPaySearchVo.setOrderNo(orders.getOrderNo());
			orgStaffDetailPaySearchVo.setStaffId(staffId);
			orgStaffDetailPaySearchVo.setOrderType(Constants.STAFF_DETAIL_DEPT_ORDER_TYPE_0);
			// 判断是否已经存在欠款
			List<OrgStaffDetailDept> orgStaffDetailDepts = orgStaffDetailDeptService.selectBySearchVo(orgStaffDetailPaySearchVo);
			
			if (orgStaffDetailDepts.isEmpty()) {
				
				BigDecimal totalOrderDept = orderPricesService.getTotalOrderDept(orders, staffId);
				
				OrgStaffDetailDept orgStaffDetailDept = orgStaffDetailDeptService.initOrgStaffDetailDept();
				// 新增欠款明细表 org_staff_detail_dept
				orgStaffDetailDept.setStaffId(staffId);
				orgStaffDetailDept.setMobile(orgStaffs.getMobile());
				orgStaffDetailDept.setOrderType(orders.getOrderType());
				orgStaffDetailDept.setOrderId(orderId);
				orgStaffDetailDept.setOrderNo(orders.getOrderNo());
				orgStaffDetailDept.setOrderMoney(totalOrderPay);
				orgStaffDetailDept.setOrderDept(totalOrderDept);
				orgStaffDetailDept.setOrderStatusStr(OrderUtils.getOrderStatusName(orders.getOrderType(), orders.getOrderStatus()));
				orgStaffDetailDept.setRemarks(orders.getRemarks());
				orgStaffDetailDeptService.insert(orgStaffDetailDept);

				// 更新欠款总表
				BigDecimal totalDept = orgStaffFinance.getTotalDept();
				totalDept = totalDept.add(orderPay);
				orgStaffFinance.setTotalDept(totalDept);
				orgStaffFinance.setUpdateTime(TimeStampUtil.getNowSecond());
				this.updateByPrimaryKeySelective(orgStaffFinance);
				
				
				orgStaffBlackService.checkStaffBlank(orgStaffFinance);
			}
		} else {
			// 判断是否订单支付方式不是现金支付，但是有订单加时，则这个也需要增加
			OrderSearchVo osearchVo = new OrderSearchVo();
			osearchVo.setOrderId(orderId);
			osearchVo.setOrderExtType((short) 1);
			List<OrderPriceExt> list = orderPriceExtService.selectBySearchVo(osearchVo);
			if (!list.isEmpty()) {
				for (OrderPriceExt ope : list) {
					this.orderOverWork(orders, ope, orgStaffs);
				}
			}
		}
	}

	// 订单补时，服务人员的财务信息操作，仅做判断更新欠款，是否需要加入黑名单
	@Override
	public void orderOverWork(Orders orders, OrderPriceExt orderPriceExt, OrgStaffs orgStaffs) {
		Long orderId = orders.getId();
		Long staffId = orgStaffs.getStaffId();

		Short orderExtType = 1;
		BigDecimal orderPay = orderPriceExtService.getTotalOrderExtPay(orders, orderExtType);

		// 服务人员财务表
		OrgStaffFinance orgStaffFinance = this.selectByStaffId(staffId);
		if (orgStaffFinance == null) {
			orgStaffFinance = this.initOrgStaffFinance();
			orgStaffFinance.setStaffId(staffId);
		}
		orgStaffFinance.setMobile(orgStaffs.getMobile());

		if (orderPriceExt.getPayType().equals((short) 6)) {

			OrgStaffDetailPaySearchVo paySearchVo = new OrgStaffDetailPaySearchVo();
			paySearchVo.setStaffId(staffId);
			paySearchVo.setOrderNo(orderPriceExt.getOrderNoExt());
			paySearchVo.setOrderType(Constants.STAFF_DETAIL_DEPT_ORDER_TYPE_0);
			// 判断是否已经存在欠款
			List<OrgStaffDetailDept> orgStaffDetailDepts = orgStaffDetailDeptService.selectBySearchVo(paySearchVo);

			if (orgStaffDetailDepts.isEmpty()) {
				OrgStaffDetailDept orgStaffDetailDept = orgStaffDetailDeptService.initOrgStaffDetailDept();
				// 新增欠款明细表 org_staff_detail_dept
				orgStaffDetailDept.setStaffId(staffId);
				orgStaffDetailDept.setMobile(orgStaffs.getMobile());
				orgStaffDetailDept.setOrderType(Constants.STAFF_DETAIL_DEPT_ORDER_TYPE_0);
				orgStaffDetailDept.setOrderId(orderId);
				orgStaffDetailDept.setOrderNo(orderPriceExt.getOrderNoExt());
				orgStaffDetailDept.setOrderMoney(orderPay);
				orgStaffDetailDept.setOrderDept(orderPay);
				orgStaffDetailDept.setOrderStatusStr("加时服务");
				orgStaffDetailDept.setRemarks(orders.getRemarks());
				orgStaffDetailDeptService.insert(orgStaffDetailDept);

				// 更新欠款总表
				BigDecimal totalDept = orgStaffFinance.getTotalDept();
				totalDept = totalDept.add(orderPay);
				orgStaffFinance.setTotalDept(totalDept);
				orgStaffFinance.setUpdateTime(TimeStampUtil.getNowSecond());
				this.updateByPrimaryKeySelective(orgStaffFinance);
				
				orgStaffBlackService.checkStaffBlank(orgStaffFinance);
			}
		}
	}

	// 统计服务人员欠款
	public Map<String, Object> totalMoney(OrgStaffFinanceSearchVo searchVo) {
		return orgStaffFinanceMapper.totalMoney(searchVo);
	}


}
