package com.jhj.service.impl.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffCash;
import com.jhj.po.model.bs.OrgStaffDetailDept;
import com.jhj.po.model.bs.OrgStaffDetailPay;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffPayDept;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.service.bs.OrgStaffCashService;
import com.jhj.service.bs.OrgStaffDetailDeptService;
import com.jhj.service.bs.OrgStaffDetailPayService;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffPayDeptService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.job.CleanupStaffService;
import com.jhj.service.order.OrderCardsService;
import com.jhj.service.order.OrderDispatchPriceService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPriceExtService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrderRatesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.service.users.UsersService;
import com.jhj.utils.OrderUtils;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.staff.OrgStaffCashSearchVo;
import com.jhj.vo.staff.OrgStaffIncomingVo;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年8月24日下午4:17:01
 * @Description:
 *               定时任务
 */
@Service
public class CleanupStaffServiceImpl implements CleanupStaffService {

	@Autowired
	private OrdersService orderService;

	@Autowired
	private OrderQueryService orderQueryService;

	@Autowired
	private OrderCardsService orderCardService;

	@Autowired
	private OrderDispatchsService orderDispatchsService;
	
	@Autowired
	private OrderDispatchPriceService orderDispatchPriceService;

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
	private OrgStaffDetailDeptService orgStaffDetailDeptService;

	@Autowired
	private OrderPricesService orderPricesService;

	@Autowired
	private OrgStaffsService orgStaffsService;

	@Autowired
	private PartnerServiceTypeService partnerServiceTypeService;
	
	@Autowired
	private OrderPriceExtService orderPriceExtService;
	
	@Autowired
	private OrgStaffDetailPayService orgStaffDetailPayService;
	
	@Autowired
	private OrgStaffPayDeptService orgStaffPayDeptService;
	
	@Autowired
	private OrgStaffCashService orgStaffCashService;



	/*
	 * 处理用户订单记录,已完成的订单。
	 * 1.生成员工消费明细表
	 * 2.如果有欠款，则记录
	 */
	@Override
	public void rebuildOrder() {
		OrderSearchVo searchVo = new OrderSearchVo();
		List<Short> orderTypes = new ArrayList<Short>();
		orderTypes.add((short) 0);
		orderTypes.add((short) 1);
		
		List<Short> orderStatusList = new ArrayList<Short>();
		orderStatusList.add((short) 7);
		orderStatusList.add((short) 8);
		searchVo.setOrderStatusList(orderStatusList);
		searchVo.setOrderByProperty(" id asc");
		
		searchVo.setStartAddTime(1476892800L);
		
		List<Orders> list = orderQueryService.selectBySearchVo(searchVo);
		
		for (int i = 0; i < list.size(); i++) {
			Orders order = list.get(i);
			Long orderId = order.getId();
			//找出所有派工人员，循环进行写入
			OrderDispatchSearchVo orderDispatchSearchVo = new OrderDispatchSearchVo();
			orderDispatchSearchVo.setOrderId(orderId);
			orderDispatchSearchVo.setDispatchStatus((short) 1);
			List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(orderDispatchSearchVo);
			for (OrderDispatchs od : orderDispatchs) {
				Long staffId = od.getStaffId();
				OrgStaffs orgStaff = orgStaffsService.selectByPrimaryKey(staffId);
				OrgStaffIncomingVo vo = orgStaffFinanceService.getStaffInComingDetail(orgStaff, order, od);
				
				//1.写入消费明细
				String orderStatusStr = OrderUtils.getOrderStatusName(order.getOrderType(), order.getOrderStatus());
				Boolean orderStaffDetailPay = orgStaffDetailPayService.setStaffDetailPay(staffId, orgStaff.getMobile(), Constants.STAFF_DETAIL_ORDER_TYPE_0, orderId,
						order.getOrderNo(), vo.getTotalOrderPay(), vo.getTotalOrderIncoming(), orderStatusStr, vo.getRemarks(), order.getUpdateTime());
				
				//2.处理服务人员的财务表
				OrgStaffFinance orgStaffFinance = orgStaffFinanceService.selectByStaffId(staffId);
				if (orgStaffFinance == null) {
					orgStaffFinance = orgStaffFinanceService.initOrgStaffFinance();
					orgStaffFinance.setStaffId(staffId);
				}
				orgStaffFinance.setMobile(orgStaff.getMobile());
				
				// 总收入
				BigDecimal totalIncoming = orgStaffFinance.getTotalIncoming();
				// 最终总收入
				BigDecimal totalIncomingend = totalIncoming.add(vo.getTotalOrderIncoming());
				orgStaffFinance.setTotalIncoming(totalIncomingend);
				orgStaffFinance.setUpdateTime(TimeStampUtil.getNowSecond());
				// 欠款
				BigDecimal totalOrderDept = vo.getTotalOrderDept();
				BigDecimal totalDept = orgStaffFinance.getTotalDept();
				totalDept = totalDept.add(totalOrderDept);
				orgStaffFinance.setTotalDept(totalDept);
				orgStaffFinanceService.updateByPrimaryKeySelective(orgStaffFinance);
				
				if (totalOrderDept.compareTo(BigDecimal.ZERO) == 1) {
					//写入欠款明细表
					OrgStaffDetailDept orgStaffDetailDept = orgStaffDetailDeptService.initOrgStaffDetailDept();
					// 新增欠款明细表 org_staff_detail_dept
					orgStaffDetailDept.setStaffId(staffId);
					orgStaffDetailDept.setMobile(orgStaff.getMobile());
					orgStaffDetailDept.setOrderType(order.getOrderType());
					orgStaffDetailDept.setOrderId(orderId);
					orgStaffDetailDept.setOrderNo(order.getOrderNo());
					orgStaffDetailDept.setOrderMoney(vo.getTotalOrderPay());
					orgStaffDetailDept.setOrderDept(totalOrderDept);
					orgStaffDetailDept.setOrderStatusStr(OrderUtils.getOrderStatusName(order.getOrderType(), order.getOrderStatus()));
					orgStaffDetailDept.setRemarks(order.getRemarks());
					orgStaffDetailDept.setAddTime(order.getUpdateTime());
					orgStaffDetailDeptService.insert(orgStaffDetailDept);
				}
				
				//3.写入服务人员订单收入明细表 
				orderDispatchPriceService.doOrderDispatchPrice(order, od);
			}
		}
	}
	
	/*
	 * 处理员工还款记录，
	 * 1.生成员工消费明细表
	 * 2.减去欠款总额.
	 */
	@Override
	public void rebuildStaffPayDep() {
		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setOrderStatus((short) 2);
		List<OrgStaffPayDept> list = orgStaffPayDeptService.selectBySearchVo(searchVo);
		
		for (OrgStaffPayDept os : list) {
			Long staffId = os.getStaffId();
			OrgStaffs orgStaff = orgStaffService.selectByPrimaryKey(staffId);
			OrgStaffFinance orgStaffFinance = orgStaffFinanceService.selectByStaffId(staffId);

			BigDecimal totalDept = orgStaffFinance.getTotalDept();
			//更新服务人财务表
			totalDept = totalDept.subtract(os.getOrderMoney());
			orgStaffFinance.setTotalDept(totalDept);
			orgStaffFinance.setUpdateTime(TimeStampUtil.getNowSecond());
			orgStaffFinanceService.updateByPrimaryKey(orgStaffFinance);
			
			// 操作服务人员财务明细表 org_staff_detail_pay，插入一条 order_type = 4 = 还款订单 的记录
			OrgStaffDetailPay orgStaffDetailPay = orgStaffDetailPayService.initStaffDetailPay();
			orgStaffDetailPay.setStaffId(staffId);
			orgStaffDetailPay.setMobile(orgStaff.getMobile());
			orgStaffDetailPay.setOrderType((short) 4);
			orgStaffDetailPay.setOrderId(os.getOrderId());
			orgStaffDetailPay.setOrderNo(os.getOrderNo());
			orgStaffDetailPay.setOrderMoney(os.getOrderMoney());
			orgStaffDetailPay.setOrderPay(os.getOrderMoney());
			orgStaffDetailPay.setOrderStatusStr("完成支付");
			orgStaffDetailPay.setAddTime(os.getAddTime());
			orgStaffDetailPayService.insert(orgStaffDetailPay);
		}
	}	
	
	/*
	 * 处理员工提现记录
	 * 1.生成员工消费明细表
	 * 2.增加总提现金额
	 */
	@Override
	public void rebuildStaffCash() {
		OrgStaffCashSearchVo searchVo = new OrgStaffCashSearchVo();
		searchVo.setOrderStatus((short) 3);
		List<OrgStaffCash> list = orgStaffCashService.selectBySearchVo(searchVo);
		
		for (OrgStaffCash os : list) {
			Long staffId = os.getStaffId();
			OrgStaffs orgStaff = orgStaffService.selectByPrimaryKey(staffId);
			OrgStaffFinance orgStaffFinance = orgStaffFinanceService.selectByStaffId(staffId);

			BigDecimal totalCash = orgStaffFinance.getTotalCash();
			//更新服务人财务表
			totalCash = totalCash.add(os.getOrderMoney());
			orgStaffFinance.setTotalCash(totalCash);
			orgStaffFinance.setUpdateTime(TimeStampUtil.getNowSecond());
			orgStaffFinanceService.updateByPrimaryKey(orgStaffFinance);
			
			// 操作服务人员财务明细表 org_staff_detail_pay，插入一条 order_type = 5 提现 的记录
			OrgStaffDetailPay orgStaffDetailPay = orgStaffDetailPayService.initStaffDetailPay();
			orgStaffDetailPay.setStaffId(staffId);
			orgStaffDetailPay.setMobile(orgStaff.getMobile());
			orgStaffDetailPay.setOrderType(Constants.ORDER_TYPE_5);
			orgStaffDetailPay.setOrderId(os.getOrderId());
			orgStaffDetailPay.setOrderNo(os.getOrderNo());
			orgStaffDetailPay.setOrderMoney(os.getOrderMoney());
			orgStaffDetailPay.setOrderPay(os.getOrderMoney());
			orgStaffDetailPay.setOrderStatusStr("提现已打款");
			orgStaffDetailPay.setAddTime(os.getUpdateTime());
			orgStaffDetailPayService.insert(orgStaffDetailPay);
		}
	}	
}
