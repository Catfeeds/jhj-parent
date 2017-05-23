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
import com.jhj.oa.auth.AccountAuth;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.po.model.admin.AdminAccount;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderLogService;
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
import com.jhj.vo.order.OrgStaffDispatchVo;
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
	private PartnerServiceTypeService partnerService;

	@Autowired
	private OrderServiceAddonsService orderServiceAddonsService;

	@Autowired
	private OrderLogService orderLogService;

	
	/**
	 * 
	 * @Title: loadAutoDispatch
	 * @Description: 自动派工，返回一个可用服务人员ID.
	 *              
	 * 
	 * @param @param model
	 * @param @param orderId
	 * @param @param newServiceDate
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "load_auto_dispatch.json", method = RequestMethod.GET)
	public List<OrgStaffDispatchVo> loadAutoDispatch(
			@RequestParam("addrId") Long addrId,
			@RequestParam("serviceTypeId") Long serviceTypeId,
			@RequestParam("serviceDate") Long serviceDate,
			@RequestParam("serviceHour") double serviceHour,
			@RequestParam("staffNums") int staffNums
			) {
		
		List<OrgStaffDispatchVo> list = new ArrayList<OrgStaffDispatchVo>();
		
		PartnerServiceType serviceType = partnerService.selectByPrimaryKey(serviceTypeId);

		if (serviceType == null)
			return list;

		if (serviceType.getIsAuto().equals((short) 0))
			return list;
		
		
		list = orderDispatchsService.autoDispatch(addrId, serviceTypeId, serviceDate, serviceHour, staffNums, new ArrayList<Long>());

		return list;
	}
	
	
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
	public List<OrgStaffDispatchVo> loadProperStaffListForBase(HttpServletRequest request, 
			Model model, 
			@RequestParam("addrId") Long addrId,
			@RequestParam("serviceTypeId") Long serviceTypeId,
			@RequestParam("orderStatus") Short orderStatus,
			@RequestParam("serviceDate") Long serviceDate,
			@RequestParam("serviceHour") double serviceHour
			) {

		List<OrgStaffDispatchVo> list = new ArrayList<OrgStaffDispatchVo>();

		// 对于 钟点工订单, 只有订单状态为 "已支付" 或 "已派工",可以进行 调整派工
		if (orderStatus != Constants.ORDER_HOUR_STATUS_2 && orderStatus != Constants.ORDER_HOUR_STATUS_3) {
			return list;
		}
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);
		Long orgId = 0L;
		list = orderDispatchsService.manualDispatch(addrId, serviceTypeId, serviceDate, serviceHour, sessionOrgId, orgId);
		return list;
	}

	/*
	 * 钟点工-- 提交修改结果
	 */
	@RequestMapping(value = "save_order_hour.json", method = RequestMethod.POST)
	public AppResultData<Object> submitManuBaseOrder(Model model, @RequestParam("orderId") Long orderId, @RequestParam("selectStaffIds") String selectStaffIds,
			@RequestParam("newServiceDate") String newServiceDate, @RequestParam("distanceValue") int distanceValue, HttpServletRequest request) {

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
		// 本次派工人员，可为多个.
		List<Long> staffIds = new ArrayList<Long>();
		// 本地派工人员与已经派工过的人员比较，只保留新的派工人员.
		List<Long> newDispathStaffIds = new ArrayList<Long>();

		// 已经派工的人员，遗留的信息
		List<Long> oldDispatchStaffIds = new ArrayList<Long>();

		for (int i = 0; i < staffIdsAry.length; i++) {
			String tmpStaffIdStr = staffIdsAry[i];
			if (StringUtil.isEmpty(tmpStaffIdStr))
				continue;
			Long tmpStaffId = Long.valueOf(tmpStaffIdStr);
			staffIds.add(tmpStaffId);
			newDispathStaffIds.add(tmpStaffId);
		}

		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderNo(order.getOrderNo());
		searchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> disList = orderDispatchsService.selectBySearchVo(searchVo);

		// 去掉已有的派工人员，只保留新增的派工人员.
		if (!disList.isEmpty()) {
			for (OrderDispatchs d : disList) {
				if (staffIds.contains(d.getStaffId())) {
					newDispathStaffIds.remove(d.getStaffId());
					oldDispatchStaffIds.add(d.getStaffId());
				}
			}
		}

		// 派工时间相同，不更换派工人员的情况，不做任何操作
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

		AccountAuth sessionAccountAuth = AuthHelper.getSessionAccountAuth(request);

		// 处理只更换派工时间，不更换派工人员的情况.
		if (!oldServiceDateTime.equals(serviceDateTime) && newDispathStaffIds.size() == 0) {

			// 先检测在新的服务时间，原派工服务人员时间是否有冲突.
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

					if (nop.getOrderId().equals(orderId))
						continue;

					resultData.setStatus(Constants.ERROR_999);
					resultData.setMsg(nop.getStaffName() + "在" + newServiceDate + "已有其他派工，不能修改服务时间，请进行换人操作.");
					return resultData;
				}
			}

			// 更新订单表
			order.setServiceDate(serviceDateTime);
			order.setUpdateTime(TimeStampUtil.getNowSecond());
			orderSevice.updateByPrimaryKeySelective(order);

			// 将服务人员的派工时间更新为新的派工时间.
			for (OrderDispatchs op : disList) {
				op.setServiceDate(serviceDateTime);
				op.setServiceDatePre(serviceDateTime - Constants.SERVICE_PRE_TIME);
				op.setUpdateTime(TimeStampUtil.getNowSecond());
				orderDispatchsService.updateByPrimaryKey(op);

				// 发送短信
				SmsUtil.SendSms(op.getStaffMobile(), "114590", smsContent);
			}

			OrderLog orderLog = orderLogService.initOrderLog(order);
			orderLog.setUserType((short) 2);
			orderLog.setUserId(sessionAccountAuth.getId());
			orderLog.setUserName(sessionAccountAuth.getUsername());
			orderLog.setAction(Constants.ORDER_ACTION_UPDATE_SERVICE_DATETIME);
			orderLogService.insert(orderLog);

			return resultData;
		}

		OrderDispatchs oldDispatchs = null;
		List<String> oldStaffMobiles = new ArrayList<String>();
		// 将替换的派工人员状态置为 0
		if (!disList.isEmpty()) {
			for (OrderDispatchs d : disList) {
				if (!staffIds.contains(d.getStaffId())) {
					d.setServiceDate(serviceDateTime);
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
			int allocate = 0;
			String allocateReason = "合理分配";
			Boolean doOrderDispatch = orderDispatchsService.doOrderDispatch(order, serviceDateTime, serviceHour, staffId, allocate, allocateReason);
		}

		// 更新订单表
		order.setStaffNums(staffIds.size());
		order.setServiceDate(serviceDateTime);
		order.setUpdateTime(TimeStampUtil.getNowSecond());
		// 更新为 已派工
		order.setOrderStatus(Constants.ORDER_HOUR_STATUS_3);
		int updateByPrimaryKeySelective = orderSevice.updateByPrimaryKeySelective(order);
		if (updateByPrimaryKeySelective > 0) {
			OrderLog orderLog = orderLogService.initOrderLog(order);
			AccountAuth accountAuth = AuthHelper.getSessionAccountAuth(request);
			orderLog.setUserType((short) 2);
			orderLog.setUserId(accountAuth.getId());
			orderLog.setUserName(accountAuth.getUsername());
			orderLog.setAction(Constants.ORDER_ACTION_UPDATE_DISPATCHS_STAFF);
			orderLogService.insert(orderLog);
		}

		// 2)派工成功，为服务人员发送推送消息---推送消息
		for (OrgStaffs item : staffs) {
			orderDispatchsService.pushToStaff(item.getStaffId(), "true", "dispatch", orderId, OneCareUtil.getJhjOrderTypeName(order.getOrderType()),
					Constants.ALERT_STAFF_MSG);

			SmsUtil.SendSms(item.getMobile(), "114590", smsContent);
		}

		// 给原来派工人员发送短信提醒，派工订单被取消
		for (String oldStaffMobile : oldStaffMobiles) {
			SmsUtil.SendSms(oldStaffMobile, Constants.MESSAGE_ORDER_CANCLE,
					new String[] { beginTimeStr, partnerService.selectByPrimaryKey(order.getServiceType()).getName() });
		}

		return resultData;
	}

	/*
	 * 深度服务-- 提交修改结果
	 */
	@RequestMapping(value = "save_order_exp.json", method = RequestMethod.POST)
	public AppResultData<Object> saveExpOrder(Model model, @RequestParam("orderId") Long orderId, @RequestParam("selectStaffIds") String selectStaffIds,
			@RequestParam("newServiceDate") String newServiceDate, HttpServletRequest request) {

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
		// 本次派工人员，可为多个.
		List<Long> staffIds = new ArrayList<Long>();
		// 本地派工人员与已经派工过的人员比较，只保留新的派工人员.
		List<Long> newDispathStaffIds = new ArrayList<Long>();

		// 已经派工的人员，遗留的信息
		List<Long> oldDispatchStaffIds = new ArrayList<Long>();

		for (int i = 0; i < staffIdsAry.length; i++) {
			String tmpStaffIdStr = staffIdsAry[i];
			if (StringUtil.isEmpty(tmpStaffIdStr))
				continue;
			Long tmpStaffId = Long.valueOf(tmpStaffIdStr);
			staffIds.add(tmpStaffId);
			newDispathStaffIds.add(tmpStaffId);
		}

		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderNo(order.getOrderNo());
		searchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> disList = orderDispatchsService.selectBySearchVo(searchVo);

		// 去掉已有的派工人员，只保留新增的派工人员.
		if (!disList.isEmpty()) {
			for (OrderDispatchs d : disList) {
				if (staffIds.contains(d.getStaffId())) {
					newDispathStaffIds.remove(d.getStaffId());
					oldDispatchStaffIds.add(d.getStaffId());
				}
			}
		}

		// 派工时间相同，不更换派工人员的情况，不做任何操作
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

		AccountAuth sessionAccountAuth = AuthHelper.getSessionAccountAuth(request);

		// 处理只更换派工时间，不更换派工人员的情况.
		if (!oldServiceDateTime.equals(serviceDateTime) && newDispathStaffIds.size() == 0) {

			// 判断已派工的服务人员，在新的服务时间内，是否有时间冲突，如果有则返回错误信息.
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

				if (nop.getOrderId().equals(orderId))
					continue;

				resultData.setStatus(Constants.ERROR_999);
				resultData.setMsg(nop.getStaffName() + "在" + newServiceDate + "已有其他派工，不能修改服务时间，请进行换人操作.");
				return resultData;
			}

			// 更新订单表

			order.setServiceDate(serviceDateTime);
			order.setUpdateTime(TimeStampUtil.getNowSecond());
			orderSevice.updateByPrimaryKeySelective(order);

			// 更新派工人员的
			if (!disList.isEmpty()) {
				for (OrderDispatchs d : disList) {
					d.setServiceDate(serviceDateTime);
					d.setServiceDatePre(serviceDateTime - Constants.SERVICE_PRE_TIME);
					d.setUpdateTime(TimeStampUtil.getNowSecond());
					orderDispatchsService.updateByPrimaryKey(d);

					SmsUtil.SendSms(d.getStaffMobile(), "114590", smsContent);
				}
			}
			OrderLog orderLog = orderLogService.initOrderLog(order);
			orderLog.setUserId(sessionAccountAuth.getId());
			orderLog.setUserName(sessionAccountAuth.getUsername());
			orderLog.setUserType((short) 2);
			orderLog.setAction(Constants.ORDER_ACTION_UPDATE_SERVICE_DATETIME);
			orderLogService.insert(orderLog);
			return resultData;

		}

		OrderDispatchs oldDispatchs = null;
		List<String> oldStaffMobiles = new ArrayList<String>();
		// 将替换的派工人员状态置为 0
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

		// 计算派工时间
		Double serviceHour = (double) order.getServiceHour();

		// 进行派工，兼容一人和多人
		for (Long staffId : newDispathStaffIds) {
			int allocate = 0;
			String allocateReason = "合理分配";
			Boolean doOrderDispatch = orderDispatchsService.doOrderDispatch(order, serviceDateTime, serviceHour, staffId, allocate, allocateReason);
		}

		// 如果为多人派工，需要对服务时间进行平均. 考虑到调整派工，人数变化的情况，所以要整体的更新.
		// 平均值频度为30分钟，比如 2.1 小时则为 2.5小时， 2.6小时则为3小时.
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
		int updateByPrimaryKeySelective = orderSevice.updateByPrimaryKeySelective(order);

		if (updateByPrimaryKeySelective > 0) {
			OrderLog orderLog = orderLogService.initOrderLog(order);
			orderLog.setUserId(sessionAccountAuth.getId());
			orderLog.setUserName(sessionAccountAuth.getUsername());
			orderLog.setUserType((short) 2);
			orderLog.setAction(Constants.ORDER_ACTION_UPDATE_DISPATCHS_STAFF);
			orderLogService.insert(orderLog);
		}

		/*
		 * 派工 短信通知
		 */
		// 用户收到派工通知---发送短信

		// 2)派工成功，为服务人员发送推送消息---推送消息

		for (OrgStaffs item : staffs) {
			orderDispatchsService.pushToStaff(item.getStaffId(), "true", "dispatch", orderId, OneCareUtil.getJhjOrderTypeName(order.getOrderType()),
					Constants.ALERT_STAFF_MSG);

			SmsUtil.SendSms(item.getMobile(), "114590", smsContent);
		}

		// 给原来派工人员发送短信提醒，派工订单被取消
		for (String oldStaffMobile : oldStaffMobiles) {
			SmsUtil.SendSms(oldStaffMobile, Constants.MESSAGE_ORDER_CANCLE,
					new String[] { beginTimeStr, partnerService.selectByPrimaryKey(order.getServiceType()).getName() });
		}
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
	public List<OrgStaffDispatchVo> loadProperStaffListForBaseByCloudOrg(Model model, 
			@RequestParam("parentId") Long parentId, 
			@RequestParam("orgId") Long orgId, 
			@RequestParam("addrId") Long addrId,
			@RequestParam("serviceTypeId") Long serviceTypeId,
			@RequestParam("orderStatus") Short orderStatus,
			@RequestParam("serviceDate") Long serviceDate,
			@RequestParam("serviceHour") double serviceHour) {

		List<OrgStaffDispatchVo> list = new ArrayList<OrgStaffDispatchVo>();

		// 对于 钟点工订单, 只有订单状态为 "已支付" 或 "已派工",可以进行 调整派工
		if (orderStatus != Constants.ORDER_HOUR_STATUS_2 && orderStatus != Constants.ORDER_HOUR_STATUS_3) {
			return list;
		}
		list = orderDispatchsService.manualDispatch(addrId, serviceTypeId, serviceDate, serviceHour, parentId, orgId);
		return list;
	}

}
