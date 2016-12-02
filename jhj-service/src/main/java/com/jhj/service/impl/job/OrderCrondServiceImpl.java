package com.jhj.service.impl.job;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jhj.common.Constants;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.OrderRates;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.job.OrderCrondService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrderRatesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.JsonOrderRateItemVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年8月24日下午4:17:01
 * @Description:
 *               定时任务
 */
@Service
public class OrderCrondServiceImpl implements OrderCrondService {

	@Autowired
	private OrdersService orderService;
	
	@Autowired
	private OrderQueryService orderQueryService;

	@Autowired
	private OrderDispatchsService orderDispatchsService;

	@Autowired
	private UsersService userService;

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
	 * 1.钟点工-- 服务开始前两小时 通知 -- 接收方： 用户
	 */
	@Override
	public void noticeBeforeService() {

		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setOrderType(Constants.ORDER_TYPE_0);
		searchVo.setOrderStatus(Constants.ORDER_HOUR_STATUS_3);

		Long now = TimeStampUtil.getNowMin();
		Long twoHourBefore = now - 2 * 3600;
		searchVo.setStartServiceTime(twoHourBefore);
		searchVo.setEndServiceTime(twoHourBefore);

		List<Orders> list = orderQueryService.selectBySearchVo(searchVo);

		for (Orders order : list) {

			// xx月 xx 日 xx:xx~ xx:xx
			String beginTimeStr = TimeStampUtil.timeStampToDateStr(order.getServiceDate() * 1000, "MM月-dd日HH:mm");
			String endTimeStr = TimeStampUtil.timeStampToDateStr((long)(order.getServiceDate() + order.getServiceHour() * 3600) * 1000, "HH:mm");
			String timeStr = beginTimeStr + "-" + endTimeStr;
			
			Long serviceTypeId = order.getServiceType();
			PartnerServiceType serviceType = partnerServiceTypeService.selectByPrimaryKey(serviceTypeId);
			String serviceTypeName = "";
			if (serviceType != null) {
				serviceTypeName = serviceType.getName();
			}
			// 家政师 xx 　在开始前２小时之内换人???

			Long orderId = order.getId();
			OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
			searchVo1.setOrderId(orderId);
			searchVo1.setDispatchStatus((short) 1);
			List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(searchVo1);
			
			if (orderDispatchs.isEmpty()) continue;
			
			String staffName = "";
			for (OrderDispatchs item : orderDispatchs) {
				String tmpStaffName = item.getStaffName();
				if (staffName.indexOf(tmpStaffName + ",") < 0) staffName+= tmpStaffName + ",";
			}
			
			if (!StringUtil.isEmpty(staffName) && staffName.substring(staffName.length()-1).equals(",")) {
				staffName = staffName.substring(0, staffName.length()-1);
			}


			// 用户手机号
			Long userId = order.getUserId();

			Users user = userService.selectByPrimaryKey(userId);

			String[] tempStr = new String[] { timeStr, serviceTypeName, staffName };

			SmsUtil.SendSms(user.getMobile(), "114844", tempStr);

			// SmsUtil.SendSms("13521193653", "29156", tempStr);
		}

	}

	/*
	 * 2.服务开始时间内，将订单状态改为服务中
	 */
	@Override
	public void updateDuringService() {

		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setOrderStatus(Constants.ORDER_STATUS_4);

		Long now = TimeStampUtil.getNowSecond();
		searchVo.setEndAddTime(now);
		searchVo.setStartServiceHourTime(now);

		List<Orders> list = orderQueryService.selectBySearchVo(searchVo);

		for (Orders orders : list) {
			orders.setOrderStatus(Constants.ORDER_STATUS_5);
			orderService.updateByPrimaryKeySelective(orders);
		}
	}

	/*
	 * 3.服务结束时间后，如果订单状态还是ORDER_STATUS_5 ，则改为待评价状态
	 */
	@Override
	public void updateAfterService() {

		OrderSearchVo searchVo = new OrderSearchVo();
		
		List<Short> orderStatusList = new ArrayList<Short>();
		orderStatusList.add(Constants.ORDER_STATUS_3);
		orderStatusList.add(Constants.ORDER_STATUS_5);
		
		searchVo.setOrderStatusList(orderStatusList);
		
		//订单完成服务时间10个小时之后
		Long now = TimeStampUtil.getNowSecond();
		searchVo.setEndServiceHourTime(now);
		
		//避免老数据，所以从 2016-10-21 00：00：00开始  = 1476979200
		searchVo.setStartServiceTime(1476979200L);

		List<Orders> list = orderQueryService.selectBySearchVo(searchVo);

		for (Orders orders : list) {
			
			Long serviceDate = orders.getServiceDate();
			double serviceHour = orders.getServiceHour();
			
			Long endServiceDate = serviceDate + (long)serviceHour * 3600;
			
			Long afterTenHourServiceDate = endServiceDate + 10 * 3600;
			System.out.println("end = " + afterTenHourServiceDate.toString() + "--- now = " + now.toString());
			if (afterTenHourServiceDate > now) continue;
			
			orders.setOrderStatus(Constants.ORDER_STATUS_7);
			orders.setUpdateTime(TimeStampUtil.getNowSecond());
			orderService.updateByPrimaryKeySelective(orders);

			// 更新服务人员的财务信息，包括财务总表，财务明细，欠款明细，是否加入黑名单
			Long orderId = orders.getId();
			OrderPrices orderPrices = orderPricesService.selectByOrderId(orderId);

			String orderNo = orders.getOrderNo();
			// 更新orderdispatchs的更新时间
			OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
			searchVo1.setOrderNo(orderNo);
			searchVo1.setDispatchStatus((short) 1);
			List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(searchVo1);

			for (OrderDispatchs item : orderDispatchs) {
				Long staffId = item.getStaffId();

				OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(staffId);
				orgStaffFinanceService.orderDone(orders, orderPrices, orgStaffs);
			}

		}
	}

	/*
	 * 4.在钟点工状态为待支付状态 ORDER_STATUS_3，超过1个小时未支付的订单，将订单状态改为已关闭.
	 */
	@Override
	public void updateOverTimeNotPay() {

		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setOrderType(Constants.ORDER_TYPE_0);
		searchVo.setOrderStatus(Constants.ORDER_STATUS_1);

		Long now = TimeStampUtil.getNowMin();

		searchVo.setStartAddTime(now + 1800);

		List<Orders> list = orderQueryService.selectBySearchVo(searchVo);

		for (Orders orders : list) {
			orders.setOrderStatus(Constants.ORDER_STATUS_9);
			orders.setUpdateTime(TimeStampUtil.getNowSecond());
			orderService.updateByPrimaryKeySelective(orders);
		}
	}

	/*
	 * 6.深度养护订单开始服务后 24小时，订单状态变为 已完成
	 * 
	 * 没有 已完成 状态，暂时 改为 已评价，，但不用生成评价记录
	 */
	@Override
	public void amOrderStatusOverOneDay() {

		
		OrderSearchVo searchVo = new OrderSearchVo();
		
		searchVo.setOrderType(Constants.ORDER_TYPE_2);
		searchVo.setOrderStatus(Constants.ORDER_STATUS_5);

		Long now = TimeStampUtil.getNowMin();
		
		searchVo.setEndUpdateTime(now - 24*3600);
		
		List<Orders> list = orderQueryService.selectBySearchVo(searchVo);
		
		for (Orders orders : list) {
			orders.setOrderStatus(Constants.ORDER_STATUS_7);
			orders.setUpdateTime(TimeStampUtil.getNowSecond());
			orderService.updateByPrimaryKeySelective(orders);
		}

	}
}
