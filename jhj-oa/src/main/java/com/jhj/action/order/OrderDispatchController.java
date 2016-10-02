package com.jhj.action.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.BaseController;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.newDispatch.NewDispatchStaffService;
import com.jhj.service.order.DispatchStaffFromOrderService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPayService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserPushBindService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.order.OrgStaffsNewVo;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.SmsUtil;
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
	private OrgStaffsService staffService;

	@Autowired
	private UserPushBindService bindService;

	@Autowired
	private OrderPayService orderPayService;

	@Autowired
	private DispatchStaffFromOrderService dispatchStaffFromOrderService;

	@Autowired
	private PartnerServiceTypeService partnerService;

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
	public List<OrgStaffsNewVo> loadProperStaffListForBase(Model model, @RequestParam("orderId") Long orderId,
			@RequestParam("newServiceDate") Long newServiceDate) {

		Orders orders = orderSevice.selectByPrimaryKey(orderId);

		Short orderStatus = orders.getOrderStatus();

		List<OrgStaffsNewVo> list = new ArrayList<OrgStaffsNewVo>();
		
		// 对于 钟点工订单, 只有订单状态为 "已支付" 或 "已派工",可以进行 调整派工
		if (orderStatus != Constants.ORDER_HOUR_STATUS_2 && orderStatus != Constants.ORDER_HOUR_STATUS_3) {
			return list;
		}
		
		Long serviceDate = orders.getServiceDate();
		double serviceHour = (double)orders.getServiceHour();
		list = orderDispatchsService.manualDispatch(orderId, serviceDate, serviceHour);
		
//		list = newDisService.getAbleStaffList(orderId, newServiceDate);
			
		return list;
	}

	/*
	 * 钟点工-- 提交修改结果
	 */
	@RequestMapping(value = "submit_manu_base_order.json", method = RequestMethod.POST)
	public AppResultData<Object> submitManuBaseOrder(Model model, 
			@RequestParam("orderId") Long orderId, 
			@RequestParam("selectStaffId") Long selectStaffId,
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

		if (selectStaffId == 0) {
			resultData.setStatus(Constants.ERROR_999);
			resultData.setMsg("没有选择派工人员,返回列表页");
			return resultData;
		}

		OrgStaffs staffs = staffService.selectByPrimaryKey(selectStaffId);

		if (staffs == null) {
			resultData.setStatus(Constants.ERROR_999);
			resultData.setMsg("无效的派工人员,返回列表页");
			return resultData;
		}

		Long serviceDateTime = TimeStampUtil.getMillisOfDayFull(newServiceDate) / 1000;
		OrderDispatchs oldDispatchs = null;
		String oldStaffMobile = "";
		// 查询 出 某个 order_No 对应的 有效的 派工记录。。如果有，则需要将派工状态设置为0，并新增新的派工信息
		
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderNo(order.getOrderNo());
		searchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> disList = orderDispatchsService.selectBySearchVo(searchVo);
		
		boolean isChangeDispatch = false;
		if (disList.size() > 0) {
			oldDispatchs = disList.get(0);
			isChangeDispatch = true;
			oldStaffMobile = oldDispatchs.getStaffMobile();
		}
		
		Double serviceHour = (double) order.getServiceHour();
		// 进行派工
		Boolean doOrderDispatch = orderDispatchsService.doOrderDispatch(order, serviceHour, selectStaffId, isChangeDispatch);

		if (doOrderDispatch == false) {
			resultData.setStatus(Constants.ERROR_999);
			resultData.setMsg("派工失败");
			return resultData;
		}

		// 更新订单表
		order.setServiceDate(serviceDateTime);
		order.setUpdateTime(TimeStampUtil.getNowSecond());
		// 更新为 已派工
		order.setOrderStatus(Constants.ORDER_HOUR_STATUS_3);
		orderSevice.updateByPrimaryKeySelective(order);

		// 发送通知
		String beginTimeStr = TimeStampUtil.timeStampToDateStr(order.getServiceDate() * 1000, "MM月-dd日HH:mm");
		String endTimeStr = TimeStampUtil.timeStampToDateStr((order.getServiceDate() + order.getServiceHour() * 3600) * 1000, "HH:mm");
		String timeStr = beginTimeStr + "-" + endTimeStr;

		/*
		 * 派工 短信通知
		 */
		// 用户收到派工通知---发送短信
		String[] smsContent = new String[] { timeStr };

		// 2)派工成功，为服务人员发送推送消息---推送消息
		dispatchStaffFromOrderService.pushToStaff(staffs.getStaffId(), "true", "dispatch", orderId, OneCareUtil.getJhjOrderTypeName(order.getOrderType()),
				Constants.ALERT_STAFF_MSG);

		SmsUtil.SendSms(staffs.getMobile(), "114590", smsContent);

		// 给原来派工人员发送短信提醒，派工订单被取消
		SmsUtil.SendSms(oldStaffMobile, Constants.MESSAGE_ORDER_CANCLE, new String[] { beginTimeStr,
				partnerService.selectByPrimaryKey(order.getServiceType()).getName() });

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
			searchVo1.setStartServiceHourTime(orders.getServiceDate() + orders.getServiceHour() * 3600);
			
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

		OrgStaffs staffs = staffService.selectByPrimaryKey(staffId);

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
			OrgStaffs orgStaffs = staffService.selectByPrimaryKey(staffId);

			order.setOrgId(orgStaffs.getOrgId());

			orderSevice.updateByPrimaryKeySelective(order);
		}

		if (orderStatus == Constants.ORDER_AM_STATUS_4) {
			// 已派工。 修改派工表
			dispatchs.setUpdateTime(TimeStampUtil.getNowSecond());
			orderDispatchsService.updateByPrimaryKeySelective(dispatchs);
		}

		String beginTimeStr = TimeStampUtil.timeStampToDateStr(order.getServiceDate() * 1000, "MM月-dd日HH:mm");
		String endTimeStr = TimeStampUtil.timeStampToDateStr((order.getServiceDate() + order.getServiceHour() * 3600) * 1000, "HH:mm");
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
			@RequestParam("orgId") Long orgId) {

		Orders orders = orderSevice.selectByPrimaryKey(orderId);

		Short orderStatus = orders.getOrderStatus();

		List<OrgStaffsNewVo> list = new ArrayList<OrgStaffsNewVo>();

		// 对于 钟点工订单, 只有订单状态为 "已支付" 或 "已派工",可以进行 调整派工
		if (orderStatus != Constants.ORDER_HOUR_STATUS_2 && orderStatus != Constants.ORDER_HOUR_STATUS_3) {
			return list;
		}
		Long serviceDate = orders.getServiceDate();
		double serviceHour = (double)orders.getServiceHour();
		list = orderDispatchsService.manualDispatchByOrg(orderId, serviceDate, serviceHour, parentId, orgId);
//		list = newDisService.getAbleStaffListByCloudOrg(orderId, parentId, orgId);

	

		return list;
	}

}
