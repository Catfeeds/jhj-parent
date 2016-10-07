package com.jhj.service.impl.bs;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.common.Constants;
import com.jhj.po.dao.bs.OrgStaffFinanceMapper;
import com.jhj.po.model.bs.OrgStaffBlack;
import com.jhj.po.model.bs.OrgStaffDetailDept;
import com.jhj.po.model.bs.OrgStaffDetailPay;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.orderReview.JhjSetting;
import com.jhj.service.bs.OrgStaffBlackService;
import com.jhj.service.bs.OrgStaffDetailDeptService;
import com.jhj.service.bs.OrgStaffDetailPayService;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.orderReview.SettingService;
import com.jhj.utils.OrderUtils;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.staff.OrgStaffDetailPaySearchVo;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.TimeStampUtil;

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

	@Override
	public void orderDone(Orders orders, OrderPrices orderPrices, OrgStaffs orgStaffs) {
		Long orderId = orders.getId();
		Long staffId = orgStaffs.getStaffId();
		Short level = orgStaffs.getLevel();
		String settingLevel = "-level-" + level.toString();
		// 得到订单收入
		String settingType = "";
		if (orders.getOrderType() == 0) {
			// 钟点功能收入比例 hour-ratio
			settingType = "hour-ratio";
		}
		
		if (orders.getOrderType() == 1) {
			// 钟点功能收入比例 hour-ratio
			settingType = "deep-ratio";
		}
		
		if (orders.getOrderType() == 2) {
			// 助理服务收入比例 am-ratio
			settingType = "am-ratio";
		}

		if (orders.getOrderType() == 3) {
			// 配送服务收入比例 am-ratio
			settingType = "dis-ratio";
		}

		settingType += settingLevel;

		BigDecimal orderIncoming = new BigDecimal(0);
		BigDecimal orderMoney = orderPrices.getOrderMoney();
		
		//如果为深度养护，需要判断是否为多人派工，订单金额需要平均
		if (orders.getOrderType().equals(Constants.ORDER_TYPE_1)) {
			OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
			searchVo1.setOrderId(orders.getId());
			searchVo1.setDispatchStatus((short) 1);
			List<OrderDispatchs> orderDispatchs = orderDispatchService.selectBySearchVo(searchVo1);
			
			if (orderDispatchs.size() > 1) {
				orderMoney = MathBigDecimalUtil.div(orderMoney, new BigDecimal(orderDispatchs.size()));
			}
		}
		
		
		JhjSetting jhjSetting = settingService.selectBySettingType(settingType);
		if (jhjSetting != null) {
			BigDecimal settingValue = new BigDecimal(jhjSetting.getSettingValue());
			orderIncoming = orderMoney.multiply(settingValue);
			orderIncoming = MathBigDecimalUtil.round(orderIncoming, 2);
		}

		// 服务人员财务表
		OrgStaffFinance orgStaffFinance = this.selectByStaffId(staffId);
		if (orgStaffFinance == null) {
			orgStaffFinance = this.initOrgStaffFinance();
			orgStaffFinance.setStaffId(staffId);
		}
		orgStaffFinance.setMobile(orgStaffs.getMobile());

		// 新增服务人员交易明细表 org_staff_detail_pay

		// 先判断是否已经存在，如果存在则跳过.
		OrgStaffDetailPaySearchVo paySearchVo = new OrgStaffDetailPaySearchVo();
		paySearchVo.setStaffId(staffId);
		paySearchVo.setOrderId(orderId);
		List<OrgStaffDetailPay> orgStaffDetailPays = orgStaffDetailPayService.selectBySearchVo(paySearchVo);

		if (orgStaffDetailPays.isEmpty()) {
			OrgStaffDetailPay orgStaffDetailPay = orgStaffDetailPayService.initStaffDetailPay();
			// 新增收入明细表 org_staff_detail_pay
			orgStaffDetailPay.setStaffId(staffId);
			orgStaffDetailPay.setMobile(orgStaffs.getMobile());
			orgStaffDetailPay.setOrderType(orders.getOrderType());
			orgStaffDetailPay.setOrderId(orderId);
			orgStaffDetailPay.setOrderNo(orders.getOrderNo());
			orgStaffDetailPay.setOrderMoney(orderMoney);
			orgStaffDetailPay.setOrderPay(orderIncoming);
			orgStaffDetailPay.setOrderStatusStr(OrderUtils.getOrderStatusName(orders.getOrderType(), orders.getOrderStatus()));
			orgStaffDetailPay.setRemarks(orders.getRemarks());
			orgStaffDetailPayService.insert(orgStaffDetailPay);

			// 订单金额

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

			// 判断是否已经存在欠款
			List<OrgStaffDetailDept> orgStaffDetailDepts = orgStaffDetailDeptService.selectBySearchVo(paySearchVo);

			if (orgStaffDetailDepts.isEmpty()) {
				OrgStaffDetailDept orgStaffDetailDept = orgStaffDetailDeptService.initOrgStaffDetailDept();
				// 新增欠款明细表 org_staff_detail_dept
				orgStaffDetailDept.setStaffId(staffId);
				orgStaffDetailDept.setMobile(orgStaffs.getMobile());
				orgStaffDetailDept.setOrderType(orders.getOrderType());
				orgStaffDetailDept.setOrderId(orderId);
				orgStaffDetailDept.setOrderNo(orders.getOrderNo());
				orgStaffDetailDept.setOrderMoney(orderMoney);
				orgStaffDetailDept.setOrderDept(orderMoney);
				orgStaffDetailDept.setOrderStatusStr(OrderUtils.getOrderStatusName(orders.getOrderType(), orders.getOrderStatus()));
				orgStaffDetailDept.setRemarks(orders.getRemarks());
				orgStaffDetailDeptService.insert(orgStaffDetailDept);

				// 更新欠款总表
				BigDecimal totalDept = orgStaffFinance.getTotalDept();
				totalDept = totalDept.add(orderMoney);
				orgStaffFinance.setTotalDept(totalDept);
				orgStaffFinance.setUpdateTime(TimeStampUtil.getNowSecond());
				this.updateByPrimaryKeySelective(orgStaffFinance);

				BigDecimal maxOrderDept = new BigDecimal(1000);
				jhjSetting = settingService.selectBySettingType("total-dept-blank");
				if (jhjSetting != null) {
					maxOrderDept = new BigDecimal(jhjSetting.getSettingValue());
				}

				if (totalDept.compareTo(maxOrderDept) >= 0) {
					OrgStaffBlack orgStaffBlack = orgStaffBlackService.initOrgStaffBlack();
					orgStaffBlack.setStaffId(orgStaffDetailDept.getStaffId());
					orgStaffBlack.setMobile(orgStaffDetailDept.getMobile());
					orgStaffBlackService.insertSelective(orgStaffBlack);

					// 设置黑名单标识.
					orgStaffFinance.setIsBlack((short) 1);
					orgStaffFinance.setUpdateTime(TimeStampUtil.getNowSecond());
					this.updateByPrimaryKey(orgStaffFinance);

					// 欠款大于1000给服务人员发送加入黑名单的短信通知
					ordersService.userJoinBlackSuccessTodo(orgStaffs.getMobile());
				}
			}
		}
	}

}
