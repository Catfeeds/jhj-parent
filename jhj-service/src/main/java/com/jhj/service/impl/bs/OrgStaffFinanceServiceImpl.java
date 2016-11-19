package com.jhj.service.impl.bs;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.orderReview.JhjSetting;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
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
import com.jhj.service.users.UsersService;
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
		// 得到订单收入比例
		String settingType = OrderUtils.getOrderSettingType(orders.getOrderType());

		settingType += settingLevel;

		
		BigDecimal orderMoney = orderPricesService.getOrderMoney(orderPrices);
		BigDecimal orderPay = orderPricesService.getOrderPay(orderPrices);
		BigDecimal orderIncoming = orderPricesService.getOrderIncoming(orders, staffId);
		orderIncoming = MathBigDecimalUtil.round(orderIncoming, 2);
		
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
				orgStaffDetailDept.setOrderDept(orderPay);
				orgStaffDetailDept.setOrderStatusStr(OrderUtils.getOrderStatusName(orders.getOrderType(), orders.getOrderStatus()));
				orgStaffDetailDept.setRemarks(orders.getRemarks());
				orgStaffDetailDeptService.insert(orgStaffDetailDept);

				// 更新欠款总表
				BigDecimal totalDept = orgStaffFinance.getTotalDept();
				totalDept = totalDept.add(orderPay);
				orgStaffFinance.setTotalDept(totalDept);
				orgStaffFinance.setUpdateTime(TimeStampUtil.getNowSecond());
				this.updateByPrimaryKeySelective(orgStaffFinance);

				BigDecimal maxOrderDept = new BigDecimal(1000);
				JhjSetting jhjSetting = settingService.selectBySettingType("total-dept-blank");
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
	
	//订单补时，服务人员的财务信息操作
	@Override
	public void orderOverWork(Orders orders, OrderPriceExt orderPriceExt, OrgStaffs orgStaffs) {
		Long orderId = orders.getId();
		Long staffId = orgStaffs.getStaffId();
		Short level = orgStaffs.getLevel();
		
		BigDecimal orderPay = orderPriceExt.getOrderPay();
		BigDecimal orderIncoming = orderPriceExtService.getOrderOverWorkIncoming(orders, staffId);
		
		// 服务人员财务表
		OrgStaffFinance orgStaffFinance = this.selectByStaffId(staffId);
		if (orgStaffFinance == null) {
			orgStaffFinance = this.initOrgStaffFinance();
			orgStaffFinance.setStaffId(staffId);
		}
		orgStaffFinance.setMobile(orgStaffs.getMobile());
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
		

		// 新增服务人员交易明细表 org_staff_detail_pay
		OrgStaffDetailPay orgStaffDetailPay = orgStaffDetailPayService.initStaffDetailPay();
		// 新增收入明细表 org_staff_detail_pay
		orgStaffDetailPay.setStaffId(staffId);
		orgStaffDetailPay.setMobile(orgStaffs.getMobile());
		
		// 9 = 订单补时
		orgStaffDetailPay.setOrderType((short)9);
		orgStaffDetailPay.setOrderId(orderId);
		orgStaffDetailPay.setOrderNo(orderPriceExt.getOrderNoExt());
		orgStaffDetailPay.setOrderMoney(orderPay);
		orgStaffDetailPay.setOrderPay(orderIncoming);
		orgStaffDetailPay.setOrderStatusStr("现金支付");
		orgStaffDetailPay.setRemarks(orders.getRemarks());
		orgStaffDetailPayService.insert(orgStaffDetailPay);

		if (orderPriceExt.getPayType().equals((short) 6)) {
			
			OrgStaffDetailPaySearchVo paySearchVo = new OrgStaffDetailPaySearchVo();
			paySearchVo.setStaffId(staffId);
			paySearchVo.setOrderNo(orderPriceExt.getOrderNoExt());
			
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
				orgStaffDetailDept.setOrderMoney(orderPay);
				orgStaffDetailDept.setOrderDept(orderIncoming);
				orgStaffDetailDept.setOrderStatusStr("加时服务");
				orgStaffDetailDept.setRemarks(orders.getRemarks());
				orgStaffDetailDeptService.insert(orgStaffDetailDept);

				// 更新欠款总表
				BigDecimal totalDept = orgStaffFinance.getTotalDept();
				totalDept = totalDept.add(orderPay);
				orgStaffFinance.setTotalDept(totalDept);
				orgStaffFinance.setUpdateTime(TimeStampUtil.getNowSecond());
				this.updateByPrimaryKeySelective(orgStaffFinance);

				BigDecimal maxOrderDept = new BigDecimal(1000);
				JhjSetting jhjSetting = settingService.selectBySettingType("total-dept-blank");
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

	//统计服务人员欠款
	public Map<String, Object> totalMoney(OrgStaffFinanceSearchVo searchVo) {
		return orgStaffFinanceMapper.totalMoney(searchVo);
	}


	//取消派工
	public boolean cancleOrderDone(Orders orders) {
		boolean flg=false;
			
		String orderNo = orders.getOrderNo();
		Long orderId = orders.getId();
		Short orderStatus = orders.getOrderStatus();
		
		if(orderStatus>=Constants.ORDER_STATUS_0 && orderStatus<=Constants.ORDER_STATUS_2) return false;
		
		BigDecimal maxOrderDept= new BigDecimal(0);
		JhjSetting jhjSetting = settingService.selectBySettingType("total-dept-blank");
		if (jhjSetting != null) {
			maxOrderDept = new BigDecimal(jhjSetting.getSettingValue());
		}
		
		//取消订单
		orders.setOrderStatus((short)0);
		orders.setUpdateTime(TimeStampUtil.getNowSecond());
		ordersService.updateByPrimaryKeySelective(orders);
		
		//支付方式
		OrderPrices orderPrice = orderPricesService.selectByOrderNo(orderNo);
		Short payType = orderPrice.getPayType();
		
		//取消派工
		OrderDispatchSearchVo orderDispVo=new OrderDispatchSearchVo();
		orderDispVo.setOrderNo(orderNo);
		orderDispVo.setDispatchStatus((short)1);
		List<OrderDispatchs> orderDispatch = orderDispatchService.selectBySearchVo(orderDispVo);
		BigDecimal orderDept=new BigDecimal(0);
		if(!orderDispatch.isEmpty()){
			for(int i=0,len=orderDispatch.size();i<len;i++){
				OrderDispatchs od = orderDispatch.get(i);
				Long staffId = od.getStaffId();
				od.setDispatchStatus((short)0);
				od.setUpdateTime(TimeStampUtil.getNowSecond());
				orderDispatchService.updateByPrimaryKeySelective(od);
				
				//取消已完成订单
				if(orderStatus==Constants.ORDER_STATUS_7 || orderStatus==Constants.ORDER_STATUS_8){
					//查询欠款
					OrgStaffDetailPaySearchVo staffDetailPayVo=new OrgStaffDetailPaySearchVo();
					staffDetailPayVo.setOrderId(orderId);
					staffDetailPayVo.setOrderNo(orderNo);
					staffDetailPayVo.setStaffId(staffId);
					List<OrgStaffDetailDept> orgStaffDetailDept = orgStaffDetailDeptService.selectBySearchVo(staffDetailPayVo);
					if(!orgStaffDetailDept.isEmpty()){
						OrgStaffDetailDept staffDetailDept = orgStaffDetailDept.get(0);
						orderDept=staffDetailDept.getOrderDept();
						orgStaffDetailDeptService.deleteByPrimaryKey(staffDetailDept.getId());
					}
					
					//查询订单收入金额
					OrgStaffDetailPaySearchVo staffDetailPay = new OrgStaffDetailPaySearchVo();
					staffDetailPay.setOrderId(orderId);
					staffDetailPay.setOrderNo(orderNo);
					staffDetailPay.setStaffId(staffId);
					List<OrgStaffDetailPay> orgStaffDetailPay = orgStaffDetailPayService.selectBySearchVo(staffDetailPay);
					BigDecimal orderPay = orgStaffDetailPay.get(0).getOrderPay();
					orgStaffDetailPayService.deleteByPrimaryKey(orgStaffDetailPay.get(0).getId());
					
					//更新财务表
					OrgStaffFinanceSearchVo staffFubabceVo=new OrgStaffFinanceSearchVo();
					staffFubabceVo.setStaffId(staffId);
					List<OrgStaffFinance> staffFinanceList = orgStaffFinanceMapper.selectBySearchVo(staffFubabceVo);
					OrgStaffFinance orgStaffFinance = staffFinanceList.get(0);
					
					BigDecimal totalIncoming = orgStaffFinance.getTotalIncoming().subtract(orderPay);
					BigDecimal totalDept = orgStaffFinance.getTotalDept();
					BigDecimal subtract = totalDept.subtract(orderDept);
					if(totalDept.compareTo(maxOrderDept)<0){
						orgStaffFinance.setIsBlack((short)0);
					}else{
						OrgStaffBlack staffBlock = orgStaffBlackService.selectByStaffId(staffId);
						if(subtract.compareTo(maxOrderDept)<0){
							orgStaffBlackService.deleteByPrimaryKey(staffBlock.getId());
							orgStaffFinance.setIsBlack((short)0);
						}
					}
					orgStaffFinance.setTotalIncoming(totalIncoming);
					orgStaffFinance.setTotalDept(subtract);
					orgStaffFinance.setUpdateTime(TimeStampUtil.getNowSecond());
					orgStaffFinanceMapper.updateByPrimaryKeySelective(orgStaffFinance);
				}
				//退还余额支付的金额
				if(payType==Constants.PAY_TYPE_0){
					Long userId = orders.getUserId();
					Users user = userService.selectByPrimaryKey(userId);
					BigDecimal restMoney = user.getRestMoney();
					BigDecimal add = restMoney.add(orderPrice.getOrderPay());
					user.setRestMoney(add);
					user.setUpdateTime(TimeStampUtil.getNowSecond());
					userService.updateByPrimaryKeySelective(user);
				}
				
				//退还优惠券
				Long couponId = orderPrice.getCouponId();
				if(couponId>0){
					UserCoupons userCoupons = userCouponsService.selectByPrimaryKey(couponId);
					userCoupons.setIsUsed((short)0);
					userCoupons.setUsedTime(0L);
					userCoupons.setOrderNo("");
					userCouponsService.updateByPrimaryKeySelective(userCoupons);
				}
			}
			flg=true;
		}
		return flg;
	}	

}
