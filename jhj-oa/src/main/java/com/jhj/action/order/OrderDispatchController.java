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
import com.jhj.service.order.OrderExpCleanService;
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
	
	@Autowired
	private OrderExpCleanService orderExpCleanService;

	
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
	 * 调整派工接口
	 * 兼容几种情况
	 * 1. 未派工的进行派工
	 * 2. 只修改派工时间.
	 * 3. 派工时间不变，调整派工人员
	 * 4. 派工时间变化，派工人员变化.
	 */
	@RequestMapping(value = "save_order_dispatch.json", method = RequestMethod.POST)
	public AppResultData<Object> saveExpOrder(Model model, 
			@RequestParam("orderType") Short orderType, 
			@RequestParam("orderId") Long orderId, 
			@RequestParam("selectStaffIds") String selectStaffIds,
			@RequestParam("newServiceDate") String newServiceDate, HttpServletRequest request) {

		AppResultData<Object> resultData = new AppResultData<Object>(Constants.SUCCESS_0, "", "");

		if (newServiceDate.length() == 16) newServiceDate += ":00";
				
		Orders order = orderSevice.selectByPrimaryKey(orderId);
		Short orderStatus = order.getOrderStatus();
		// 只有订单状态为 "已支付" 或 "已派工",可以进行 调整派工
		if (orderStatus != Constants.ORDER_HOUR_STATUS_2 && orderStatus != Constants.ORDER_HOUR_STATUS_3) {
			resultData.setStatus(Constants.ERROR_999);
			resultData.setMsg("只有已支付或已派工的订单能进行派工,返回列表页");
			return resultData;
		}
		
		//新的派工时间
		Long serviceDateTime = TimeStampUtil.getMillisOfDayFull(newServiceDate) / 1000;
		
		//老的派工时间
		Long oldServiceDateTime = order.getServiceDate();

		String[] staffIdsAry = StringUtil.convertStrToArray(selectStaffIds);
		// 新的派工人员
		List<Long> staffIds = new ArrayList<Long>();
		// 新的派工人员与已经派工过的人员比较，只保留新的派工人员.
		List<Long> newDispathStaffIds = new ArrayList<Long>();
		// 已经派工的人员
		List<Long> oldDispatchStaffIds = new ArrayList<Long>();
		int newStaffNum = staffIdsAry.length;
		int oldStaffNum = 0;

		for (int i = 0; i < staffIdsAry.length; i++) {
			String tmpStaffIdStr = staffIdsAry[i];
			if (StringUtil.isEmpty(tmpStaffIdStr))
				continue;
			Long tmpStaffId = Long.valueOf(tmpStaffIdStr);
			staffIds.add(tmpStaffId);
			newDispathStaffIds.add(tmpStaffId);
		}
		newStaffNum = staffIdsAry.length;

		OrderDispatchSearchVo orderDispatchSearchVo = new OrderDispatchSearchVo();
		orderDispatchSearchVo.setOrderNo(order.getOrderNo());
		orderDispatchSearchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> disList = orderDispatchsService.selectBySearchVo(orderDispatchSearchVo);
		
		// 去掉已有的派工人员，只保留新增的派工人员.
		if (!disList.isEmpty()) {
			for (OrderDispatchs d : disList) {
				oldStaffNum = disList.size();
				if (staffIds.contains(d.getStaffId())) {
					newDispathStaffIds.remove(d.getStaffId());
					oldDispatchStaffIds.add(d.getStaffId());
				}
			}
		}
		
		if (orderType.equals(Constants.ORDER_TYPE_0) && newStaffNum != oldStaffNum) {
			resultData.setStatus(Constants.ERROR_999);
			resultData.setMsg("基础保洁订单不能修改人数，因为涉及到价格的变更.");
			return resultData;
		}

		// 派工时间相同，不更换派工人员的情况，不做任何操作
		if (oldServiceDateTime.equals(serviceDateTime) && newStaffNum == oldStaffNum && newDispathStaffIds.size() == 0) {
			resultData.setStatus(Constants.ERROR_999);
			resultData.setMsg("派工人员和服务时间都无变化，不做操作.");
			return resultData;
		}
		
		List<Long> dispatchStaffIds = new ArrayList<Long>();
		String orderLogRemarks = "";
		//处理几种情况
		//1. 如果为未派工，则 = staffIds;
		if (order.getOrderStatus().equals(Constants.ORDER_HOUR_STATUS_2)) {
			dispatchStaffIds = staffIds;
			orderLogRemarks = Constants.ORDER_ACTION_DISPATCHS;
		} else {
			//2. 只修改派工时间.
			if (!oldServiceDateTime.equals(serviceDateTime) && newDispathStaffIds.size() == 0) {
				dispatchStaffIds = staffIds;
				orderLogRemarks = Constants.ORDER_ACTION_UPDATE_SERVICE_DATETIME;
			}
			//3. 派工时间不变，调整派工人员
			if (oldServiceDateTime.equals(serviceDateTime) && newDispathStaffIds.size() > 0) {
				dispatchStaffIds = oldDispatchStaffIds;
				orderLogRemarks = Constants.ORDER_ACTION_UPDATE_DISPATCHS_STAFF;
			}
			//4. 派工时间变化，派工人员变化.
			if (!oldServiceDateTime.equals(serviceDateTime) && newDispathStaffIds.size() > 0 ) {
				dispatchStaffIds = staffIds;
				orderLogRemarks = Constants.ORDER_ACTION_UPDATE_SERVICE_DATETIME + ";" + Constants.ORDER_ACTION_UPDATE_DISPATCHS_STAFF;
			}
		}
		
		// 计算派工时间
		Double serviceHour = (double) order.getServiceHour();
				
		// 如果为多人派工，需要对服务时间进行平均. 考虑到调整派工，人数变化的情况，所以要整体的更新.
		// 平均值频度为30分钟，比如 2.1 小时则为 2.5小时， 2.6小时则为3小时.
		if (orderType.equals(Constants.ORDER_TYPE_1) && staffIds.size() > 1 && newDispathStaffIds.size() > 0) {
			int totalStaffs = staffIds.size();
			// 派工人数会变化，所以需要重新计算订单小时数.
			List<OrderServiceAddons> orderAddons = orderServiceAddonsService.selectByOrderId(orderId);
			if (!orderAddons.isEmpty()) {
				serviceHour = orderExpCleanService.mathOrderServiceHour(orderAddons);
			} else {
				serviceHour = order.getServiceHour() * oldStaffNum;
			}
			serviceHour = MathBigDecimalUtil.getValueStepHalf(serviceHour, totalStaffs);
		}

		//检测是否有时间冲突的情况
		Long startServiceTime = serviceDateTime;
		// 注意结束时间也要服务结束后 1:59分钟
		Long endServiceTime = (long) (serviceDateTime + serviceHour * 3600 + Constants.SERVICE_PRE_TIME);

		OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
		searchVo1.setStaffIds(dispatchStaffIds);
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
				
		StaffSearchVo staffSearchVo = new StaffSearchVo();
		staffSearchVo.setStaffIds(staffIds);
		List<OrgStaffs> staffs = orgStaffsService.selectBySearchVo(staffSearchVo);

		// 发送通知
		String beginTimeStr = TimeStampUtil.timeStampToDateStr(order.getServiceDate() * 1000, "MM月-dd日HH:mm");
		String endTimeStr = TimeStampUtil.timeStampToDateStr((long) ((order.getServiceDate() + order.getServiceHour() * 3600) * 1000), "HH:mm");
		String timeStr = beginTimeStr + "-" + endTimeStr;
		String[] smsContent = new String[] { timeStr };

		AccountAuth sessionAccountAuth = AuthHelper.getSessionAccountAuth(request);

		// 更新订单表信息
		
		if (order.getOrderStatus().equals(Constants.ORDER_HOUR_STATUS_2)) {
			order.setOrderStatus(Constants.ORDER_HOUR_STATUS_3);
		}
		
		order.setServiceDate(serviceDateTime);
		order.setServiceHour(serviceHour);
		order.setStaffNums(staffIds.size());
		order.setUpdateTime(TimeStampUtil.getNowSecond());
		orderSevice.updateByPrimaryKeySelective(order);
		
		// 更新订单日志.
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLog.setUserId(sessionAccountAuth.getId());
		orderLog.setUserName(sessionAccountAuth.getUsername());
		orderLog.setUserType((short) 2);
		orderLog.setAction(orderLogRemarks);
		orderLogService.insert(orderLog);
		
		List<String> oldStaffMobiles = new ArrayList<String>();
		//进行派工， 判断是否需要将老派工变为不可用.
		if (newDispathStaffIds.size() > 0) {
			for (OrderDispatchs d : disList) {
				if (!staffIds.contains(d.getStaffId())) {
					d.setDispatchStatus((short) 0);
					d.setUpdateTime(TimeStampUtil.getNowSecond());
					orderDispatchsService.updateByPrimaryKey(d);
					oldStaffMobiles.add(d.getStaffMobile());
				}
			}
		}
		
		orderDispatchSearchVo = new OrderDispatchSearchVo();
		orderDispatchSearchVo.setOrderNo(order.getOrderNo());
		orderDispatchSearchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> newDisList = orderDispatchsService.selectBySearchVo(orderDispatchSearchVo);
		
		// 进行派工，兼容一人和多人
		for (Long staffId : staffIds) {
			Boolean willDispatch = true;
			for (OrderDispatchs d : newDisList) {
				if (staffId.equals(d.getStaffId())) {
					willDispatch = false;
					break;
				}
			}
			
			if (!willDispatch) continue;
			
			int allocate = 0;
			String allocateReason = "合理分配";
			Boolean doOrderDispatch = orderDispatchsService.doOrderDispatch(order, serviceDateTime, serviceHour, staffId, allocate, allocateReason);
			
			OrgStaffs orgStaff = orgStaffsService.selectByPrimaryKey(staffId);
			orderDispatchsService.pushToStaff(orgStaff.getStaffId(), "true", "dispatch", orderId, OneCareUtil.getJhjOrderTypeName(order.getOrderType()),
					Constants.ALERT_STAFF_MSG);

			SmsUtil.SendSms(orgStaff.getMobile(), "114590", smsContent);
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
		
		
		list = orderDispatchsService.manualDispatchByOrg(addrId, serviceTypeId, serviceDate, serviceHour, parentId, orgId);
		return list;
	}

}
