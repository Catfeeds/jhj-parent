package com.jhj.action.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.BaseController;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.po.model.order.Orders;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.newDispatch.NewDispatchStaffService;
import com.jhj.service.order.DispatchStaffFromOrderService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPayService;
import com.jhj.service.order.OrderServiceAddonsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserPushBindService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.order.OrderServiceAddonViewVo;
import com.jhj.vo.order.OrgStaffsNewVo;
import com.jhj.vo.staff.StaffSearchVo;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2016年3月21日下午2:48:09
 * @Description:
 *
 *               jhj2.1 -- 运营平台，手动派工
 */
@Controller
@RequestMapping(value = "/new_dispatch")
public class OrderDispatchController extends BaseController {

	@Autowired
	private NewDispatchStaffService newDisService;

	@Autowired
	private OrderDispatchsService orderDispatchsService;

	@Autowired
	private UsersService userService;

	@Autowired
	private UserAddrsService userAddrService;

	@Autowired
	private OrdersService orderSevice;

	@Autowired
	private OrgStaffsService orgStaffsService;

	@Autowired
	private UserPushBindService bindService;

	@Autowired
	private OrderPayService orderPayService;

	@Autowired
	private DispatchStaffFromOrderService dispatchStaffFromOrderService;

	@Autowired
	private PartnerServiceTypeService partnerService;
	
	@Autowired
	private OrderServiceAddonsService orderServiceAddonsService;

	/**
	 * 
	 * @Title: loadProperStaffListForBase
	 * @Description:
	 *               钟点工订单调整时, 根据 改变后的 服务时间, 加载 可用派工人员列表
	 * 
	 * @param @param model
	 * @param @param orderId
	 * @param @param newServiceDate
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "load_staff_by_change_service_date.json", method = RequestMethod.GET)
	public List<OrgStaffsNewVo> loadProperStaffListForBase(HttpServletRequest request, Model model, @RequestParam("orderId") Long orderId,
			@RequestParam("newServiceDate") Long newServiceDate) {

		Orders orders = orderSevice.selectByPrimaryKey(orderId);

		Short orderStatus = orders.getOrderStatus();

		List<OrgStaffsNewVo> list = new ArrayList<OrgStaffsNewVo>();
		
		// 对于 钟点工订单, 只有订单状态为 "已支付" 或 "已派工",可以进行 调整派工
		if (orderStatus != Constants.ORDER_HOUR_STATUS_2 && orderStatus != Constants.ORDER_HOUR_STATUS_3) {
			return list;
		}
		
		Long serviceDate = orders.getServiceDate();
		
		if (newServiceDate != null) serviceDate = newServiceDate;
		
		double serviceHour = (double)orders.getServiceHour();
		
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);
		
		list = orderDispatchsService.manualDispatch(orderId, serviceDate, serviceHour, sessionOrgId);
		
//		list = newDisService.getAbleStaffList(orderId, newServiceDate);
			
		return list;
	}

	/*
	 * 钟点工-- 提交修改结果
	 */
	@RequestMapping(value = "save_order_hour.json", method = RequestMethod.POST)
	public AppResultData<Object> submitManuBaseOrder(Model model, 
			@RequestParam("orderId") Long orderId, 
			@RequestParam("selectStaffIds") String selectStaffIds,
			@RequestParam("newServiceDate") String newServiceDate, 
			@RequestParam("distanceValue") int distanceValue) {

		AppResultData<Object> resultData = new AppResultData<Object>(Constants.SUCCESS_0, "", "");

		if (newServiceDate.length() == 16)
			newServiceDate += ":00";

		Orders order = orderSevice.selectByPrimaryKey(orderId);
		Short orderStatus = order.getOrderStatus();
		// 对于 钟点工订单, 只有订单状态为 "已支付" 或 "已派工",可以进行 调整派工
		if (orderStatus != Constants.ORDER_HOUR_STATUS_2 && orderStatus != Constants.ORDER_HOUR_STATUS_3) {
			resultData.setStatus(Constants.ERROR_999);
			resultData.setMsg("只有已支付或已派工的订单能进行派工,返回列表页");
			return resultData;
		}
		
		Long serviceDateTime = TimeStampUtil.getMillisOfDayFull(newServiceDate) / 1000;
		Long oldServiceDateTime = order.getServiceDate();
		
		String[] staffIdsAry = StringUtil.convertStrToArray(selectStaffIds);
		//本次派工人员，可为多个.
		List<Long> staffIds = new ArrayList<Long>();
		//本地派工人员与已经派工过的人员比较，只保留新的派工人员.
		List<Long> newDispathStaffIds = new ArrayList<Long>();
		
		//已经派工的人员，遗留的信息
		List<Long> oldDispatchStaffIds = new ArrayList<Long>();
		
		for (int i = 0; i < staffIdsAry.length; i++) {
			String tmpStaffIdStr = staffIdsAry[i];
			if (StringUtil.isEmpty(tmpStaffIdStr)) continue;
			Long tmpStaffId = Long.valueOf(tmpStaffIdStr);
			staffIds.add(tmpStaffId);
			newDispathStaffIds.add(tmpStaffId);
		}
		
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderNo(order.getOrderNo());
		searchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> disList = orderDispatchsService.selectBySearchVo(searchVo);
		
		
		//去掉已有的派工人员，只保留新增的派工人员.
		if (!disList.isEmpty()) {
			for (OrderDispatchs d : disList) {
				if (staffIds.contains(d.getStaffId())) {
					newDispathStaffIds.remove(d.getStaffId());
					oldDispatchStaffIds.add(d.getStaffId());
				}
			}
		}
		
		//派工时间相同，不更换派工人员的情况，不做任何操作
		if (oldServiceDateTime.equals(serviceDateTime) && newDispathStaffIds.size() == 0) {
			resultData.setStatus(Constants.ERROR_999);
			resultData.setMsg("派工人员和服务时间都无变化，不做操作.");
			return resultData;
		}
		
		StaffSearchVo staffSearchVo = new StaffSearchVo();
		staffSearchVo.setStaffIds(staffIds);
		List<OrgStaffs> staffs = orgStaffsService.selectBySearchVo(staffSearchVo);
		
		
		// 发送通知
		String beginTimeStr = TimeStampUtil.timeStampToDateStr(serviceDateTime * 1000, "MM月-dd日HH:mm");
		String endTimeStr = TimeStampUtil.timeStampToDateStr((long) ((serviceDateTime + order.getServiceHour() * 3600) * 1000), "HH:mm");
		String timeStr = beginTimeStr + "-" + endTimeStr;
		
		// 用户收到派工通知---发送短信
		String[] smsContent = new String[] { timeStr };
		
		
		//处理只更换派工时间，不更换派工人员的情况.
		if (!oldServiceDateTime.equals(serviceDateTime) && newDispathStaffIds.size() == 0) {
			
			//先检测在新的服务时间，原派工服务人员时间是否有冲突.
			for (OrderDispatchs op : disList) {
				// ---2.服务时间内 已 排班的 阿姨, 时间跨度为 服务开始前1:59分钟 - 服务结束时间
				Long startServiceTime = serviceDateTime - Constants.SERVICE_PRE_TIME;
				
				// 注意结束时间也要服务结束后 1:59分钟
				Long endServiceTime = (long) (serviceDateTime + order.getServiceHour() * 3600 + Constants.SERVICE_PRE_TIME);
				
				OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
				searchVo1.setStaffId(op.getStaffId());
				searchVo1.setDispatchStatus((short) 1);
				searchVo1.setStartServiceTime(startServiceTime);
				searchVo1.setEndServiceTime(endServiceTime);
				List<OrderDispatchs> ooDisList = orderDispatchsService.selectByMatchTime(searchVo1);
				
				for (OrderDispatchs nop : ooDisList) {
					
					if (nop.getOrderId().equals(orderId)) continue;
					
					resultData.setStatus(Constants.ERROR_999);
					resultData.setMsg(nop.getStaffName() + "在" + newServiceDate + "已有其他派工，不能修改服务时间，请进行换人操作.");
					return resultData;
				}
				
			}
			
			
			// 更新订单表
			order.setServiceDate(serviceDateTime);
			order.setUpdateTime(TimeStampUtil.getNowSecond());
			orderSevice.updateByPrimaryKeySelective(order);
			
			//将服务人员的派工时间更新为新的派工时间.
			for (OrderDispatchs op : disList) {
				op.setServiceDate(serviceDateTime);
				op.setServiceDatePre(serviceDateTime - Constants.SERVICE_PRE_TIME);
				op.setUpdateTime(TimeStampUtil.getNowSecond());
				orderDispatchsService.updateByPrimaryKey(op);
				
				//发送短信
				SmsUtil.SendSms(op.getStaffMobile(), "114590", smsContent);
			}
			return resultData;
		}
		
		OrderDispatchs oldDispatchs = null;
		List<String> oldStaffMobiles = new ArrayList<String>();
		//将替换的派工人员状态置为 0
		if (!disList.isEmpty()) {
			for (OrderDispatchs d : disList) {
				if (!staffIds.contains(d.getStaffId())) {
					d.setDispatchStatus((short) 0);
					d.setUpdateTime(TimeStampUtil.getNowSecond());
					orderDispatchsService.updateByPrimaryKey(d);
					oldStaffMobiles.add(d.getStaffMobile());
				} 
			}
			oldDispatchs = disList.get(0);
		}
		
		Double serviceHour = (double) order.getServiceHour();
		// 进行派工
		for (Long staffId : newDispathStaffIds) {
			Boolean doOrderDispatch = orderDispatchsService.doOrderDispatch(order, serviceDateTime, serviceHour, staffId);
		}

		// 更新订单表
		order.setStaffNums(staffIds.size());
		order.setServiceDate(serviceDateTime);
		order.setUpdateTime(TimeStampUtil.getNowSecond());
		// 更新为 已派工
		order.setOrderStatus(Constants.ORDER_HOUR_STATUS_3);
		orderSevice.updateByPrimaryKeySelective(order);


		// 2)派工成功，为服务人员发送推送消息---推送消息
		for (OrgStaffs item : staffs) {
			dispatchStaffFromOrderService.pushToStaff(item.getStaffId(), "true", "dispatch", orderId, OneCareUtil.getJhjOrderTypeName(order.getOrderType()),
					Constants.ALERT_STAFF_MSG);
	
			SmsUtil.SendSms(item.getMobile(), "114590", smsContent);
		}

		// 给原来派工人员发送短信提醒，派工订单被取消
		for (String oldStaffMobile : oldStaffMobiles) {
			SmsUtil.SendSms(oldStaffMobile, Constants.MESSAGE_ORDER_CANCLE, new String[] { beginTimeStr,
					partnerService.selectByPrimaryKey(order.getServiceType()).getName() });
		}
		return resultData;
	}
	
	/*
	 * 深度服务-- 提交修改结果
	 */
	@RequestMapping(value = "save_order_exp.json", method = RequestMethod.POST)
	public AppResultData<Object> saveExpOrder(Model model, 
			@RequestParam("orderId") Long orderId, 
			@RequestParam("selectStaffIds") String selectStaffIds,
			@RequestParam("newServiceDate") String newServiceDate) {

		AppResultData<Object> resultData = new AppResultData<Object>(Constants.SUCCESS_0, "", "");

		if (newServiceDate.length() == 16)
			newServiceDate += ":00";

		Orders order = orderSevice.selectByPrimaryKey(orderId);
		Short orderStatus = order.getOrderStatus();
		// 对于 钟点工订单, 只有订单状态为 "已支付" 或 "已派工",可以进行 调整派工
		if (orderStatus != Constants.ORDER_HOUR_STATUS_2 && orderStatus != Constants.ORDER_HOUR_STATUS_3) {
			resultData.setStatus(Constants.ERROR_999);
			resultData.setMsg("只有已支付或已派工的订单能进行派工,返回列表页");
			return resultData;
		}
		
		Long serviceDateTime = TimeStampUtil.getMillisOfDayFull(newServiceDate) / 1000;
		Long oldServiceDateTime = order.getServiceDate();
		

		
		String[] staffIdsAry = StringUtil.convertStrToArray(selectStaffIds);
		//本次派工人员，可为多个.
		List<Long> staffIds = new ArrayList<Long>();
		//本地派工人员与已经派工过的人员比较，只保留新的派工人员.
		List<Long> newDispathStaffIds = new ArrayList<Long>();
		
		//已经派工的人员，遗留的信息
		List<Long> oldDispatchStaffIds = new ArrayList<Long>();
		
		for (int i = 0; i < staffIdsAry.length; i++) {
			String tmpStaffIdStr = staffIdsAry[i];
			if (StringUtil.isEmpty(tmpStaffIdStr)) continue;
			Long tmpStaffId = Long.valueOf(tmpStaffIdStr);
			staffIds.add(tmpStaffId);
			newDispathStaffIds.add(tmpStaffId);
		}
		
		
		
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderNo(order.getOrderNo());
		searchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> disList = orderDispatchsService.selectBySearchVo(searchVo);
		
		
		//去掉已有的派工人员，只保留新增的派工人员.
		if (!disList.isEmpty()) {
			for (OrderDispatchs d : disList) {
				if (staffIds.contains(d.getStaffId())) {
					newDispathStaffIds.remove(d.getStaffId());
					oldDispatchStaffIds.add(d.getStaffId());
				}
			}
		}
			
		//派工时间相同，不更换派工人员的情况，不做任何操作
		if (oldServiceDateTime.equals(serviceDateTime) && newDispathStaffIds.size() == 0) {
			resultData.setStatus(Constants.ERROR_999);
			resultData.setMsg("派工人员和服务时间都无变化，不做操作.");
			return resultData;
		}
		
		StaffSearchVo staffSearchVo = new StaffSearchVo();
		staffSearchVo.setStaffIds(staffIds);
		List<OrgStaffs> staffs = orgStaffsService.selectBySearchVo(staffSearchVo);

		// 发送通知
		String beginTimeStr = TimeStampUtil.timeStampToDateStr(order.getServiceDate() * 1000, "MM月-dd日HH:mm");
		String endTimeStr = TimeStampUtil.timeStampToDateStr((long) ((order.getServiceDate() + order.getServiceHour() * 3600) * 1000), "HH:mm");
		String timeStr = beginTimeStr + "-" + endTimeStr;
		String[] smsContent = new String[] { timeStr };
		
		
		//处理只更换派工时间，不更换派工人员的情况.
		if (!oldServiceDateTime.equals(serviceDateTime) && newDispathStaffIds.size() == 0) {
			
			//判断已派工的服务人员，在新的服务时间内，是否有时间冲突，如果有则返回错误信息.
			Long startServiceTime = serviceDateTime - Constants.SERVICE_PRE_TIME;
			
			// 注意结束时间也要服务结束后 1:59分钟
			Long endServiceTime = (long) (serviceDateTime + order.getServiceHour() * 3600 + Constants.SERVICE_PRE_TIME);
			
			OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
			searchVo1.setStaffIds(oldDispatchStaffIds);
			searchVo1.setDispatchStatus((short) 1);
			searchVo1.setStartServiceTime(startServiceTime);
			searchVo1.setEndServiceTime(endServiceTime);
			List<OrderDispatchs> ooDisList = orderDispatchsService.selectByMatchTime(searchVo1);
			
			for (OrderDispatchs nop : ooDisList) {
				
				if (nop.getOrderId().equals(orderId)) continue;
				
				resultData.setStatus(Constants.ERROR_999);
				resultData.setMsg(nop.getStaffName() + "在" + newServiceDate + "已有其他派工，不能修改服务时间，请进行换人操作.");
				return resultData;
			}
			
			// 更新订单表
			
			order.setServiceDate(serviceDateTime);
			order.setUpdateTime(TimeStampUtil.getNowSecond());
			orderSevice.updateByPrimaryKeySelective(order);
			
			//更新派工人员的
			if (!disList.isEmpty()) {
				for (OrderDispatchs d : disList) {
					d.setServiceDate(serviceDateTime);
					d.setServiceDatePre(serviceDateTime - Constants.SERVICE_PRE_TIME);
					d.setUpdateTime(TimeStampUtil.getNowSecond());
					orderDispatchsService.updateByPrimaryKey(d);
					
					SmsUtil.SendSms(d.getStaffMobile(), "114590", smsContent);
				}
			}
			
			return resultData;
			
		}
		
		OrderDispatchs oldDispatchs = null;
		List<String> oldStaffMobiles = new ArrayList<String>();
		//将替换的派工人员状态置为 0
		if (!disList.isEmpty()) {
			for (OrderDispatchs d : disList) {
				if (!staffIds.contains(d.getStaffId())) {
					d.setDispatchStatus((short) 0);
					d.setUpdateTime(TimeStampUtil.getNowSecond());
					orderDispatchsService.updateByPrimaryKey(d);
					oldStaffMobiles.add(d.getStaffMobile());
				} 
			}
			oldDispatchs = disList.get(0);
		}
		
		
		
		//计算派工时间
		Double serviceHour = (double) order.getServiceHour();
				
		// 进行派工，兼容一人和多人
		for (Long staffId : newDispathStaffIds) {
			Boolean doOrderDispatch = orderDispatchsService.doOrderDispatch(order, serviceDateTime, serviceHour, staffId);
		}
		
		//如果为多人派工，需要对服务时间进行平均. 考虑到调整派工，人数变化的情况，所以要整体的更新.
		//平均值频度为30分钟，比如 2.1 小时则为 2.5小时，  2.6小时则为3小时.
		if (staffIds.size() > 1) {
			int totalStaffs = staffIds.size();
			serviceHour = MathBigDecimalUtil.getValueStepHalf(serviceHour, totalStaffs);

			disList = orderDispatchsService.selectBySearchVo(searchVo);
			
			for (OrderDispatchs d : disList) {
				d.setServiceDatePre(serviceDateTime - Constants.SERVICE_PRE_TIME);
				d.setServiceDate(serviceDateTime);
				d.setServiceHours(serviceHour);
				d.setUpdateTime(TimeStampUtil.getNowSecond());
				orderDispatchsService.updateByPrimaryKey(d);
			}
		}

		// 更新订单表
		order.setStaffNums(staffIds.size());
		order.setServiceDate(serviceDateTime);
		order.setUpdateTime(TimeStampUtil.getNowSecond());
		// 更新为 已派工
		order.setServiceHour(serviceHour);
		order.setOrderStatus(Constants.ORDER_HOUR_STATUS_3);
		orderSevice.updateByPrimaryKeySelective(order);

		

		/*
		 * 派工 短信通知
		 */
		// 用户收到派工通知---发送短信
		

		// 2)派工成功，为服务人员发送推送消息---推送消息
		
		
		for (OrgStaffs item : staffs) {
			dispatchStaffFromOrderService.pushToStaff(item.getStaffId(), "true", "dispatch", orderId, OneCareUtil.getJhjOrderTypeName(order.getOrderType()),
					Constants.ALERT_STAFF_MSG);
	
			SmsUtil.SendSms(item.getMobile(), "114590", smsContent);
		}

		// 给原来派工人员发送短信提醒，派工订单被取消
		for (String oldStaffMobile : oldStaffMobiles) {
			SmsUtil.SendSms(oldStaffMobile, Constants.MESSAGE_ORDER_CANCLE, new String[] { beginTimeStr,
					partnerService.selectByPrimaryKey(order.getServiceType()).getName() });
		}
		return resultData;
	}

	/**
	 * 
	 * @Title: loadProperStaffListForAm
	 * @Description:
	 *               助理类订单, 手动派工时,根据条件 加载 可用派工人员
	 * 
	 *               对于 此类订单, 服务时间 不可变。。但是 地址和 服务人员可以改变
	 * 
	 @param rderId
	 * @param fromLat
	 * @param fromLng
	 * @return List<OrgStaffsNewVo> 返回类型
	 * @throws
	 */
	@RequestMapping(value = "load_staff_for_am_order.json", method = RequestMethod.POST)
	public List<OrgStaffsNewVo> loadProperStaffListForAm(Model model, @RequestParam("orderId") Long orderId, @RequestParam("fromLat") String fromLat,
			@RequestParam("fromLng") String fromLng) {

		Orders orders = orderSevice.selectByPrimaryKey(orderId);

		List<Long> staIdList = newDisService.autoDispatchForAmOrder(fromLat, fromLng, orders.getServiceType());

		// 请假的员工(服务类型 属于 深度养护 类别下 )
		List<Long> leaveStaffIdList = newDisService.getLeaveStaffForDeepOrder(orderId, orders.getServiceType());

		// 排除请假的员工
		staIdList.removeAll(leaveStaffIdList);

		List<OrgStaffsNewVo> list = new ArrayList<OrgStaffsNewVo>();

		Short orderStatus = orders.getOrderStatus();

		// 对于 助理 类 订单，只有在 '已预约','已派工' 两种状态 可以 修改 派工人员
		if (orderStatus == Constants.ORDER_AM_STATUS_2 || orderStatus == Constants.ORDER_AM_STATUS_3 || orderStatus == Constants.ORDER_AM_STATUS_4) {
			list = newDisService.getTheNearestStaff(fromLat, fromLng, staIdList);

			OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
			searchVo1.setDispatchStatus((short) 1);
			searchVo1.setStartServiceTime(orders.getServiceDate());
			searchVo1.setStartServiceHourTime((long) (orders.getServiceDate() + orders.getServiceHour() * 3600));
			
			for (OrgStaffsNewVo orgStaffsNewVo : list) {

				searchVo1.setStaffId(orgStaffsNewVo.getStaffId());

				// 对应当前订单的日期。。该员工是否 有 派工单
				List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(searchVo1);

				int disNum = 0;
				if (!orderDispatchs.isEmpty()) disNum = orderDispatchs.size();

				if (disNum > 0) {
					orgStaffsNewVo.setDispathStaStr("不可派工");
					orgStaffsNewVo.setDispathStaFlag(0);
				} else {
					orgStaffsNewVo.setDispathStaStr("可派工");
					orgStaffsNewVo.setDispathStaFlag(1);
				}
			}

		}
		return list;
	}

	/**
	 * 
	 * @Title: submitManuAmOrderResult
	 * @Description:
	 *               提交 助理 订单 手动派工结果
	 * @param orderId
	 *
	 * @param fromLat
	 *            用户的地址 经纬度
	 * @param fromLng
	 * 
	 * @param staffId
	 *            被选中的服务人员
	 * @param distance
	 *            被选中服务人员,距离用户地址的 距离 数字,(更新派工表字段使用)
	 * 
	 * @param userAddrName
	 *            与用户沟通后确定的服务地址
	 * @throws
	 */
	@RequestMapping(value = "/submit_manu_am_order_result.json", method = RequestMethod.POST)
	public AppResultData<Object> submitManuAmOrderResult(@RequestParam("orderId") Long orderId, @RequestParam("fromLat") String fromLat,
			@RequestParam("fromLng") String fromLng, @RequestParam("selectStaffId") Long staffId, @RequestParam("userAddrName") String userAddrName,
			@RequestParam("distance") int distance) {

		AppResultData<Object> resultData = new AppResultData<Object>(Constants.SUCCESS_0, "", "");

		Orders order = orderSevice.selectByPrimaryKey(orderId);

		// 修改的 只是 有效派工的订单
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderNo(order.getOrderNo());
		searchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> list = orderDispatchsService.selectBySearchVo(searchVo);

		OrderDispatchs dispatchs = orderDispatchsService.initOrderDisp();

		// 如果可以查出记录。表示未 修改 派工, 否则是 手动新增派工
		if (list.size() <= 0) {

			// 如果没有派工记录。。表示是 新增派工

			dispatchs.setUserId(order.getUserId());
			dispatchs.setMobile(order.getMobile());
			dispatchs.setOrderId(orderId);
			dispatchs.setOrderNo(order.getOrderNo());
			dispatchs.setServiceDate(order.getServiceDate());
			dispatchs.setServiceDatePre(order.getServiceDate() - 3600);
			dispatchs.setRemarks(order.getRemarks());
			// 默认3小时服务时长
			dispatchs.setServiceHours((short) 3);

		} else {

			// 有派工记录。表示 是 修改 派工
			dispatchs = list.get(0);
		}

		dispatchs.setPickAddrName(userAddrName);
		dispatchs.setPickAddrLat(fromLat);
		dispatchs.setPickAddrLng(fromLng);

		// 助理派工表。。。这里两个距离 : 距用户/取货地址 距离 设置 为同一个值了、、
		dispatchs.setPickDistance(distance);
		dispatchs.setUserAddrDistance(distance);

		OrgStaffs staffs = orgStaffsService.selectByPrimaryKey(staffId);

		dispatchs.setOrgId(staffs.getOrgId());
		dispatchs.setStaffId(staffId);
		dispatchs.setStaffMobile(staffs.getMobile());
		dispatchs.setStaffName(staffs.getName());
		dispatchs.setDispatchStatus(Constants.ORDER_DIS_ENABLE);

		Short orderStatus = order.getOrderStatus();

		// 对于 助理 类 订单，只有在 '已预约','已派工' 两种状态 可以 修改 派工人员

		if (orderStatus == Constants.ORDER_AM_STATUS_3) {

			// 如果订单状态是 已预约。 手动增加派工

			// 插入 派工表
			orderDispatchsService.insertSelective(dispatchs);

			// 更新 order_status 为 已派工
			order.setOrderStatus(Constants.ORDER_AM_STATUS_4);
			order.setUpdateTime(TimeStampUtil.getNowSecond());

			/*
			 * 更新助理订单 门店 为 派工人员 所在的 云店
			 */
			OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(staffId);

			order.setOrgId(orgStaffs.getOrgId());

			orderSevice.updateByPrimaryKeySelective(order);
		}

		if (orderStatus == Constants.ORDER_AM_STATUS_4) {
			// 已派工。 修改派工表
			dispatchs.setUpdateTime(TimeStampUtil.getNowSecond());
			orderDispatchsService.updateByPrimaryKeySelective(dispatchs);
		}

		String beginTimeStr = TimeStampUtil.timeStampToDateStr(order.getServiceDate() * 1000, "MM月-dd日HH:mm");
		String endTimeStr = TimeStampUtil.timeStampToDateStr((long) ((order.getServiceDate() + order.getServiceHour() * 3600) * 1000), "HH:mm");
		String timeStr = beginTimeStr + "-" + endTimeStr;

		// 1) 用户收到派工通知---发送短信
		String[] contentForUser = new String[] { timeStr };

		// 2)派工成功，为服务人员发送推送消息---推送消息
		dispatchStaffFromOrderService.pushToStaff(staffs.getStaffId(), "true", "dispatch", orderId, OneCareUtil.getJhjOrderTypeName(order.getOrderType()),
				Constants.ALERT_STAFF_MSG);

		SmsUtil.SendSms(staffs.getMobile(), "114590", contentForUser);

		return resultData;
	}

	/**
	 * 
	 * @Title: loadProperStaffListForBaseByCloudOrg
	 * @Description:
	 *               根据 云店 动态加载 该 云店下 可用的 派工
	 * @param orderId
	 * @param cloudOrgId
	 * @throws
	 */
	@RequestMapping(value = "load_staff_by_change_cloud_org.json", method = RequestMethod.GET)
	public List<OrgStaffsNewVo> loadProperStaffListForBaseByCloudOrg(Model model, 
			@RequestParam("orderId") Long orderId, @RequestParam("parentId") Long parentId,
			@RequestParam("orgId") Long orgId,
			@RequestParam("newServiceDate") Long newServiceDate
			) {

		Orders orders = orderSevice.selectByPrimaryKey(orderId);

		Short orderStatus = orders.getOrderStatus();

		List<OrgStaffsNewVo> list = new ArrayList<OrgStaffsNewVo>();

		// 对于 钟点工订单, 只有订单状态为 "已支付" 或 "已派工",可以进行 调整派工
		if (orderStatus != Constants.ORDER_HOUR_STATUS_2 && orderStatus != Constants.ORDER_HOUR_STATUS_3) {
			return list;
		}
		Long serviceDate = orders.getServiceDate();
		
		if (newServiceDate != null) serviceDate = newServiceDate;
		
		double serviceHour = (double)orders.getServiceHour();
		list = orderDispatchsService.manualDispatchByOrg(orderId, serviceDate, serviceHour, parentId, orgId);
//		list = newDisService.getAbleStaffListByCloudOrg(orderId, parentId, orgId);

	

		return list;
	}

}
