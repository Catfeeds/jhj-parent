package com.jhj.action.job;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffDetailDept;
import com.jhj.po.model.bs.OrgStaffDetailPay;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.service.bs.OrgStaffDetailDeptService;
import com.jhj.service.bs.OrgStaffDetailPayService;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrdersService;
import com.jhj.utils.OrderUtils;
import com.jhj.vo.order.OrderSearchVo;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/app/job/order")
public class JobOrderController extends BaseController {

	@Autowired
	private OrdersService ordersService;
	
	@Autowired
	private OrderQueryService orderQueryService;

	@Autowired
	private OrgStaffsService orgStaffsService;

	@Autowired
	private OrgStaffDetailPayService orgStaffDetailPayService;

	@Autowired
	private OrderPricesService orderPricesService;

	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;

	@Autowired
	private OrgStaffDetailDeptService orgStaffDetailDeptService;

	/**
	 * 订单超过1个小时未支付,则关闭订单, 就是订单的状态变成 9 ,并且把相应的优惠劵的信息置为空
	 */

	@RequestMapping(value = "check_order_more_60min", method = RequestMethod.GET)
	public AppResultData<Object> OrderMore60min(HttpServletRequest request) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());

		/**
		 * 订单开始服务超过8小时还没点击完成，服务，则需要系统自动变成完成服务
		 */

		// 查找出订单开始服务8小时还没点击完成的订单
		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setOrderStatus((short) 5);
		
		Long now = TimeStampUtil.getNowSecond();
		Long endAddTime = now - 3600 * 8;
		
		searchVo.setEndAddTime(endAddTime);
		
		List<Orders> list = orderQueryService.selectBySearchVo(searchVo);

		for (int i = 0; i < list.size(); i++) {
			// 订单状态设置为0 已关闭
			Orders orders = list.get(i);
			OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(orders
					.getAmId());
			if (orders == null) {
				return result;
			}
			// 改变服务状态为已完成
			orders.setOrderStatus((short) 7);
			ordersService.updateByPrimaryKeySelective(orders);
			OrderPrices orderPrices = orderPricesService.selectByOrderId(orders
					.getId());

			// 新增服务人员交易明细表 org_staff_detail_pay
			OrgStaffDetailPay orgStaffDetailPay = orgStaffDetailPayService
					.initStaffDetailPay();
			// 新增欠款明细表 org_staff_detail_dept
			orgStaffDetailPay.setStaffId(orders.getAmId());
			orgStaffDetailPay.setMobile(orgStaffs.getMobile());
			orgStaffDetailPay.setOrderType(orders.getOrderType());
			orgStaffDetailPay.setOrderId(orders.getId());
			orgStaffDetailPay.setOrderNo(orders.getOrderNo());
			orgStaffDetailPay.setOrderMoney(orderPrices.getOrderMoney());
			orgStaffDetailPay.setOrderPay(orderPrices.getOrderPayBack());
			orgStaffDetailPay.setOrderStatusStr(OrderUtils.getOrderStatusName(
					orders.getOrderType(), orders.getOrderStatus()));
			orgStaffDetailPay.setRemarks(orders.getRemarks());
			orgStaffDetailPayService.insert(orgStaffDetailPay);

			// 更新服务人员财务表
			OrgStaffFinance orgStaffFinance = orgStaffFinanceService.selectByStaffId(orders.getAmId());
			
			if (orgStaffFinance == null) {
				orgStaffFinance = orgStaffFinanceService.initOrgStaffFinance();
				orgStaffFinance.setStaffId(orgStaffs.getStaffId());
			}
			orgStaffFinance.setMobile(orgStaffs.getMobile());
			// 订单金额
			BigDecimal orderMoney = orderPrices.getOrderMoney();
			// 总收入
			BigDecimal totalIncoming = orgStaffFinance.getTotalIncoming();
			// 最终总收入
			BigDecimal totalIncomingend = totalIncoming.add(orderMoney);
			orgStaffFinance.setTotalIncoming(totalIncomingend);
			orgStaffFinance.setUpdateTime(TimeStampUtil.getNowSecond());
			
			if (orgStaffFinance.getId() > 0L) {
				orgStaffFinanceService.updateByPrimaryKeySelective(orgStaffFinance);
			} else {
				orgStaffFinanceService.insert(orgStaffFinance);
			}
			

			if (orderPrices.getPayType().equals((short)6)) {
				OrgStaffDetailDept orgStaffDetailDept = orgStaffDetailDeptService
						.initOrgStaffDetailDept();
				// 新增欠款明细表 org_staff_detail_dept
				orgStaffDetailDept.setStaffId(orders.getAmId());
				orgStaffDetailDept.setMobile(orgStaffs.getMobile());
				orgStaffDetailDept.setOrderType(orders.getOrderType());
				orgStaffDetailDept.setOrderId(orders.getId());
				orgStaffDetailDept.setOrderNo(orders.getOrderNo());
				orgStaffDetailDept.setOrderMoney(orderPrices.getOrderMoney());
				orgStaffDetailDept.setOrderDept(orderPrices.getOrderPayBack());
				orgStaffDetailDept.setOrderStatusStr(OrderUtils
						.getOrderStatusName(orders.getOrderType(),
								orders.getOrderStatus()));
				orgStaffDetailDept.setRemarks(orders.getRemarks());
				orgStaffDetailDeptService.insert(orgStaffDetailDept);
			}
		}
		return result;
	}

}
