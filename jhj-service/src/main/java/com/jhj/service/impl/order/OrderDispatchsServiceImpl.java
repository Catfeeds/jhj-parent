package com.jhj.service.impl.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.dao.order.OrderDispatchsMapper;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffLeave;
import com.jhj.po.model.bs.OrgStaffSkill;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserPushBind;
import com.jhj.po.model.user.UserTrailReal;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffLeaveService;
import com.jhj.service.bs.OrgStaffSkillService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.order.OrderDispatchAllocateService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserPushBindService;
import com.jhj.service.users.UserTrailRealService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.bs.OrgDispatchPoiVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderDispatchVo;
import com.jhj.vo.order.OrgStaffDispatchVo;
import com.jhj.vo.org.LeaveSearchVo;
import com.jhj.vo.org.OrgSearchVo;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;
import com.jhj.vo.staff.OrgStaffSkillSearchVo;
import com.jhj.vo.staff.StaffSearchVo;
import com.jhj.vo.user.UserPushBindSearchVo;
import com.jhj.vo.user.UserTrailSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.GsonUtil;
import com.meijia.utils.ListUtil;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.PushUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.baidu.BaiduPoiVo;
import com.meijia.utils.baidu.MapPoiUtil;
import com.meijia.utils.vo.AppResultData;

/**
 * 
 * @author lnczx
 *         本类为派工逻辑，按照正常的云店距离，派工逻辑为
 *         前置条件
 *         1. 服务地址
 *         2. 服务时间
 *         3. 服务品类
 *         4. 服务人数
 *         步骤如下：
 * 
 *         1. 根据服务地址经纬度，匹配符合20公里的云店，得到 云店集合A
 * 
 *         2. 根据云店集合A找出所有的员工，得到员工集合B
 * 
 *         1） 审核通过的员工
 *         2） 状态可用的员工
 * 
 *         3. 根据员工集合B找出符合服务品类的员工集合C
 * 
 *         4. 员工集合C，找出服务技能符合的员工，得到员工集合D
 * 
 *         5. 员工集合D，排除在服务时间有派工的员工（注：提前1小时59分钟，服务完成时间后1小时59分钟）。的到员工集合E
 * 
 *         6. 员工集合E，排除在黑名单，得到员工集合F
 * 
 *         7. 员工集合F, 排除请假的员工，得到最终员工集合G。
 * 
 *         8. 根据云店的距离由近到远的优先级进行。
 * 
 *         9. 员工集合找出服务时间日期的订单数，优先选择订单数少的员工，如果相同则随机。
 */
@Service
public class OrderDispatchsServiceImpl implements OrderDispatchsService {

	@Autowired
	private OrderDispatchsMapper orderDispatchsMapper;

	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;

	@Autowired
	private UsersService userService;

	@Autowired
	private UserAddrsService userAddrService;

	@Autowired
	private OrdersService orderService;

	@Autowired
	private OrderPricesService orderPriceService;

	@Autowired
	private OrgStaffSkillService orgStaffSkillService;

	@Autowired
	private OrgsService orgService;

	@Autowired
	private OrgStaffLeaveService orgStaffLeaveService;

	@Autowired
	private OrgStaffsService orgStaffService;

	@Autowired
	private UserTrailRealService trailRealService;

	@Autowired
	private UserPushBindService userPushBindService;

	@Autowired
	private PartnerServiceTypeService partnerServiceTypeService;

	@Autowired
	private OrderDispatchAllocateService orderDispatchAllocateService;

	@Override
	public int deleteByPrimaryKey(Long id) {
		return orderDispatchsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrderDispatchs record) {
		return orderDispatchsMapper.insert(record);
	}

	@Override
	public int insertSelective(OrderDispatchs record) {
		return orderDispatchsMapper.insertSelective(record);
	}

	@Override
	public OrderDispatchs selectByPrimaryKey(Long id) {
		return orderDispatchsMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OrderDispatchs record) {
		return orderDispatchsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrderDispatchs record) {
		return orderDispatchsMapper.updateByPrimaryKey(record);
	}

	@Override
	public OrderDispatchs initOrderDisp() {

		OrderDispatchs dispatchs = new OrderDispatchs();

		dispatchs.setId(0L);
		dispatchs.setUserId(0L);
		dispatchs.setMobile("");
		dispatchs.setOrderId(0L);
		dispatchs.setOrderNo("");
		dispatchs.setServiceDatePre(0L);
		dispatchs.setServiceDate(0L);
		dispatchs.setServiceHours((short) 0);
		dispatchs.setOrgId(0L);
		dispatchs.setParentId(0L);
		dispatchs.setStaffId(0L);
		dispatchs.setStaffName("");
		dispatchs.setStaffMobile("");
		dispatchs.setRemarks("");
		dispatchs.setAmId(0L);
		dispatchs.setDispatchStatus(Constants.ORDER_DIS_ENABLE); // ??派工状态
																	// 默认有效吧？
		dispatchs.setPickAddrName("");
		dispatchs.setPickAddrLat("");
		dispatchs.setPickAddrLng("");
		dispatchs.setPickAddr("");
		dispatchs.setPickDistance(0);
		dispatchs.setUserAddrDistance(0);
		dispatchs.setAddTime(TimeStampUtil.getNow() / 1000);
		dispatchs.setUpdateTime(0L);
		dispatchs.setIsApply((short) 0);
		dispatchs.setApplyTime(0L);
		dispatchs.setAllocate((short) 1);
		dispatchs.setAllocateReason("效率优先");
		return dispatchs;
	}

	@Override
	public List<OrderDispatchs> selectBySearchVo(OrderDispatchSearchVo searchVo) {
		return orderDispatchsMapper.selectBySearchVo(searchVo);
	}

	@Override
	public List<OrderDispatchs> selectByMatchTime(OrderDispatchSearchVo searchVo) {
		return orderDispatchsMapper.selectByMatchTime(searchVo);
	}

	@Override
	public List<Map<String, Object>> totalByYearAndMonth(Map<String, Object> conditions) {
		return orderDispatchsMapper.totalByYearAndMonth(conditions);
	}

	@Override
	public Long totalStaffTodayOrders(Long staffId) {
		return orderDispatchsMapper.totalStaffTodayOrders(staffId);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<HashMap> totalByStaff(OrderDispatchSearchVo searchVo) {
		return orderDispatchsMapper.totalByStaff(searchVo);
	}

	@Override
	public OrderDispatchVo changeToOrderDispatchVo(OrderDispatchs item) {
		OrderDispatchVo vo = new OrderDispatchVo();

		BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);

		// 是否接单状态;
		Short isApply = vo.getIsApply();
		if (isApply.equals((short) 1)) {
			vo.setApplyStatus("是");
			vo.setApplyTimeStr(TimeStampUtil.timeStampToDateStr(vo.getApplyTime() * 1000));
		} else {
			Long now = TimeStampUtil.getNowSecond();
			Long dispatchTime = vo.getAddTime();
			Long lastTime = now - dispatchTime;
			if (lastTime > 60 * 30) {
				vo.setApplyStatus("超");
			} else {
				vo.setApplyStatus("否");
			}
			vo.setApplyTimeStr("");
		}

		return vo;
	}

	/**
	 * 订单类自动派工
	 * 
	 * @param orderId
	 *            订单ID
	 * @param serviceDate
	 *            服务日期
	 * @param serviceHour
	 *            服务时长
	 * @param staffNums
	 *            预约服务人员数 支持多个服务人员.
	 * @param appointStaffIds
	 *            已经指派的员工，需要排除掉
	 */
	@Override
	public List<OrgStaffDispatchVo> autoDispatch(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, int staffNums,
			List<Long> appointStaffIds) {

		List<OrgStaffDispatchVo> dispatchStaffs = new ArrayList<OrgStaffDispatchVo>();

		// 1. 判断是当天还是隔日订单

		
		String today = DateUtil.getToday();

		String serviceDateStr = TimeStampUtil.timeStampToDateStr(serviceDate * 1000, DateUtil.DEFAULT_PATTERN);

		List<OrgStaffDispatchVo> preCanDispatchVos = new ArrayList<OrgStaffDispatchVo>();

		if (serviceDateStr.equals(today)) {
			preCanDispatchVos = orderDispatchAllocateService.manualDispatchToday(addrId, serviceTypeId, serviceDate, serviceHour, 0L, 0L);
		} else {
			preCanDispatchVos = orderDispatchAllocateService.manualDispatchNotToday(addrId, serviceTypeId, serviceDate, serviceHour, 0L, 0L);
		}

		if (preCanDispatchVos.isEmpty())
			return dispatchStaffs;
		
		//指定员工
		if (!appointStaffIds.isEmpty()) {
			for (Long appointStaffId : appointStaffIds) {
				for (int i = 0 ; i < preCanDispatchVos.size(); i++) {
					OrgStaffDispatchVo item = preCanDispatchVos.get(i);
					if (item.getStaffId().equals(appointStaffId)) {
						dispatchStaffs.add(item);
						break;
					}
				}
			}
			
			if (dispatchStaffs.size() == staffNums) return dispatchStaffs;
		}
		
		
		if (preCanDispatchVos.size() < staffNums)
			return dispatchStaffs;
		
		

		// 根据隔日和今日派工依据分别处理
		// 合理分配的集合
		List<OrgStaffDispatchVo> preFirstVos = new ArrayList<OrgStaffDispatchVo>();
		
		// 效率优先的集合
		List<OrgStaffDispatchVo> preSecondVos = new ArrayList<OrgStaffDispatchVo>();
		
		// 10公里之内（以员工位置坐标），员工集合找出服务时间日期的订单数，优先选择订单数少的员工，如果相同则随机
		for (OrgStaffDispatchVo vo : preCanDispatchVos) {
			if (vo.getAllocate() == 0)
				preFirstVos.add(vo);
			if (vo.getAllocate() == 1)
				preSecondVos.add(vo);
		}
		
		if (!preFirstVos.isEmpty()) {
			for (OrgStaffDispatchVo item : preFirstVos) {
				dispatchStaffs.add(item);

				if (dispatchStaffs.size() == staffNums)
					break;
			}
		}

		if (dispatchStaffs.size() < staffNums) {
			for (OrgStaffDispatchVo item : preSecondVos) {
				dispatchStaffs.add(item);
				if (dispatchStaffs.size() == staffNums)
					break;
			}
		}
		
		//如果可派工人数不足，则属于派工不成功.
		if (dispatchStaffs.size() < staffNums) return new ArrayList<OrgStaffDispatchVo>();
		
		return dispatchStaffs;
	}

	/**
	 * 订单类手动派工 ,需要返回List<OrgStaffsNewVo>,
	 * 1. 找出符合派工逻辑的所有门店的员工
	 * 2. 包含门店的距离
	 * 3. 包含员工的距离.
	 */
	@Override
	public List<OrgStaffDispatchVo> manualDispatch(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, Long selectParentId, Long orgId) {

		List<OrgStaffDispatchVo> list = new ArrayList<OrgStaffDispatchVo>();
		
		// 1. 判断是当天还是隔日订单

		String today = DateUtil.getToday();

		String serviceDateStr = TimeStampUtil.timeStampToDateStr(serviceDate * 1000, DateUtil.DEFAULT_PATTERN);

		if (serviceDateStr.equals(today)) {
			list = orderDispatchAllocateService.manualDispatchToday(addrId, serviceTypeId, serviceDate, serviceHour, selectParentId, orgId);
		} else {
			list = orderDispatchAllocateService.manualDispatchNotToday(addrId, serviceTypeId, serviceDate, serviceHour, selectParentId, orgId);
		}		
		
		
		return list;
	}
	
	/**
	 * 订单类手动派工 ,需要返回List<OrgStaffsNewVo>,
	 * 1. 找出符合派工逻辑的所有门店的员工
	 * 2. 包含门店的距离
	 * 3. 包含员工的距离.
	 */
	@Override
	public List<OrgStaffDispatchVo> manualDispatchByOrg(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, Long selectParentId, Long orgId) {

		List<OrgStaffDispatchVo> list = new ArrayList<OrgStaffDispatchVo>();
		
		// 1. 判断是当天还是隔日订单

		String today = DateUtil.getToday();

		String serviceDateStr = TimeStampUtil.timeStampToDateStr(serviceDate * 1000, DateUtil.DEFAULT_PATTERN);

		if (serviceDateStr.equals(today)) {
			list = orderDispatchAllocateService.manualDispatchTodayByOrg(addrId, serviceTypeId, serviceDate, serviceHour, selectParentId, orgId);
		} else {
			list = orderDispatchAllocateService.manualDispatchNotTodayByOrg(addrId, serviceTypeId, serviceDate, serviceHour, selectParentId, orgId);
		}		
		
		
		return list;
	}

	/**
	 * 
	 * @Title: getMatchOrgId
	 * @Description:
	 *               找出符合 指定距离的云店
	 * @throws
	 */
	@Override
	public List<OrgDispatchPoiVo> getMatchOrgs(String fromLat, String fromLng, Long parentId, Long orgId, Boolean needMatchMaxDistance) {

		List<OrgDispatchPoiVo> result = new ArrayList<OrgDispatchPoiVo>();

		// 目标地点：所有云店的集合
		OrgSearchVo searchVo = new OrgSearchVo();

		if (parentId > 0L)
			searchVo.setParentId(parentId);
		if (orgId > 0L)
			searchVo.setOrgId(orgId);
		if (parentId.equals(0L) && orgId.equals(0L)) {
			searchVo.setIsCloud((short) 1);
		}
		searchVo.setOrgStatus((short) 1);
		List<Orgs> orgList = orgService.selectBySearchVo(searchVo);

		if (orgList.isEmpty())
			return result;

		List<BaiduPoiVo> orgAddrList = new ArrayList<BaiduPoiVo>();
		for (Orgs org : orgList) {
			BaiduPoiVo baiduPoiVo = new BaiduPoiVo();

			baiduPoiVo.setLat(org.getPoiLatitude());
			baiduPoiVo.setLng(org.getPoiLongitude());
			baiduPoiVo.setName(org.getOrgName());

			orgAddrList.add(baiduPoiVo);
		}

		Orgs item = null;
		try {
			List<BaiduPoiVo> voList = MapPoiUtil.getMapRouteMatrix(fromLat, fromLng, orgAddrList);

			if (needMatchMaxDistance) {
				voList = MapPoiUtil.getMinDest(voList, Constants.MAX_DISTANCE);
			}
			for (int i = 0; i < orgList.size(); i++) {
				item = orgList.get(i);

				OrgDispatchPoiVo vo = new OrgDispatchPoiVo();
				BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);

				for (BaiduPoiVo baiduPoiVo : voList) {
					if (baiduPoiVo.getLat().equals(item.getPoiLatitude()) && baiduPoiVo.getLng().equals(item.getPoiLongitude())) {

						BeanUtilsExp.copyPropertiesIgnoreNull(baiduPoiVo, vo);
						result.add(vo);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 进行排序，根据距离大小来正序.
		if (result.size() > 0) {
			Collections.sort(result, new Comparator<OrgDispatchPoiVo>() {
				@Override
				public int compare(OrgDispatchPoiVo s1, OrgDispatchPoiVo s2) {
					return Integer.valueOf(s1.getDistanceValue()).compareTo(s2.getDistanceValue());
				}
			});
		}

		return result;
	}

	@Override
	public List<OrgStaffDispatchVo> getStaffDispatch(List<OrgStaffDispatchVo> list, String fromLat, String fromLng) {

		// 计算出员工与目标位置的距离.
		List<BaiduPoiVo> staffPoiList = new ArrayList<BaiduPoiVo>();

		List<Long> staffIds = new ArrayList<Long>();
		for (OrgStaffDispatchVo item : list) {
			if (!staffIds.contains(item.getStaffId()))
				staffIds.add(item.getStaffId());
		}

		UserTrailSearchVo searchVo = new UserTrailSearchVo();
		searchVo.setUserIds(staffIds);
		List<UserTrailReal> trailList = trailRealService.selectBySearchVo(searchVo);
		for (UserTrailReal userTrailReal : trailList) {
			BaiduPoiVo baiduPoiVo = new BaiduPoiVo();
			baiduPoiVo.setLat(userTrailReal.getLat());
			baiduPoiVo.setLng(userTrailReal.getLng());
			baiduPoiVo.setName(userTrailReal.getPoiName());
			staffPoiList.add(baiduPoiVo);
		}

		List<BaiduPoiVo> destList = MapPoiUtil.getMapRouteMatrix(fromLat, fromLng, staffPoiList);

		for (OrgStaffDispatchVo vo : list) {
			vo.setDistanceText("");
			vo.setDurationText("");
			vo.setDistanceValue(0);

			for (UserTrailReal item : trailList) {
				for (BaiduPoiVo baiduPoiVo : destList) {
					if (item.getUserId().equals(vo.getStaffId()) && baiduPoiVo.getLat().equals(item.getLat()) && baiduPoiVo.getLng().equals(item.getLng())) {
						// 合适的服务人员的最新位置 与 服务地址 的 距离、时间等信息
						vo.setDistanceText(baiduPoiVo.getDistanceText());
						vo.setDurationText(baiduPoiVo.getDurationText());
						vo.setDistanceValue(baiduPoiVo.getDistanceValue());
						break;
					}
				}
			}
		}
		return list;
	}

	@Override
	public List<HashMap> getTotalStaffOrders(Long serviceDate, List<Long> staffIds) {
		List<HashMap> totalStaffs = new ArrayList<HashMap>();

		if (staffIds.isEmpty())
			return totalStaffs;

		OrderDispatchSearchVo searchVo6 = new OrderDispatchSearchVo();
		searchVo6.setStaffIds(staffIds);
		searchVo6.setDispatchStatus((short) 1);
		String serviceDateStr = TimeStampUtil.timeStampToDateStr(serviceDate * 1000, "yyyy-MM-dd");
		// 得到服务日期的开始时间戳 00:00:00
		Long startServiceTime = TimeStampUtil.getMillisOfDayFull(DateUtil.getBeginOfDay(serviceDateStr)) / 1000;
		searchVo6.setStartServiceTime(startServiceTime);
		// 得到服务日期的结束时间戳 23:59:59
		Long endServiceTime = TimeStampUtil.getMillisOfDayFull(DateUtil.getEndOfDay(serviceDateStr)) / 1000;
		searchVo6.setEndServiceTime(endServiceTime);

		totalStaffs = this.totalByStaff(searchVo6);

		return totalStaffs;

	}

	@Override
	public boolean doOrderDispatch(Orders order, Long serviceDate, Double serviceHour, Long staffId, int allocate , String allocateReason) {

		Long orderId = order.getId();
		String orderNo = order.getOrderNo();
		Long userId = order.getUserId();
		
		Users u = userService.selectByPrimaryKey(userId);

		OrgStaffs staff = orgStaffService.selectByPrimaryKey(staffId);
		
		OrderDispatchs orderDispatch = this.initOrderDisp(); // 派工状态默认有效 1

		orderDispatch.setUserId(userId);
		orderDispatch.setOrgId(staff.getOrgId());
		orderDispatch.setParentId(staff.getParentOrgId());
		orderDispatch.setMobile(u.getMobile());
		orderDispatch.setOrderId(orderId);
		orderDispatch.setOrderNo(orderNo);

		// 服务开始时间， serviceDate（服务时间）的前一小时， 当前秒值 -3600s
		orderDispatch.setServiceDatePre(serviceDate - Constants.SERVICE_PRE_TIME);
		orderDispatch.setServiceDate(serviceDate);
		orderDispatch.setServiceHours(order.getServiceHour());

		// 更新服务人员与用户地址距离

		Long addrId = order.getAddrId();
		UserAddrs userAddrs = userAddrService.selectByPrimaryKey(addrId);
		String latitude = userAddrs.getLatitude();

		String longitude = userAddrs.getLongitude();

		int distance = this.getLatestDistance(latitude, longitude, staffId);

		orderDispatch.setUserAddrDistance(distance);

		// 工作人员相关
		orderDispatch.setStaffId(staffId);
		orderDispatch.setStaffName(staff.getName());
		orderDispatch.setStaffMobile(staff.getMobile());
		
		orderDispatch.setAllocate((short)allocate);
		orderDispatch.setAllocateReason(allocateReason);
		this.insertSelective(orderDispatch);

		return true;
	}

	/**
	 * 检测用户指派员工，是否满足派工条件
	 * 1. 时间点是否有冲突
	 * 2. 技能是否符合
	 * 3. 距离是否符合.(暂不处理)
	 */
	@Override
	public AppResultData<Object> checAppointDispatch(Long orderId, Long staffId) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());

		// 员工是否可用
		OrgStaffs staff = orgStaffService.selectByPrimaryKey(staffId);
		if (staff == null || staff.getStatus().equals((short) 0)) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg("指定的员工已不可用.");
			return result;
		}

		Orders order = orderService.selectByPrimaryKey(orderId);
		Long serviceTypeId = order.getServiceType();

		// 技能是否可用
		OrgStaffSkillSearchVo searchVo = new OrgStaffSkillSearchVo();

		searchVo.setStaffId(staffId);
		searchVo.setServiceTypeId(serviceTypeId);
		List<OrgStaffSkill> skillList = orgStaffSkillService.selectBySearchVo(searchVo);

		if (skillList.isEmpty()) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg("指定的员工没有此项技能.");
			return result;
		}

		Long serviceDate = order.getServiceDate();
		double serviceHour = order.getServiceHour();
		Long startServiceTime = serviceDate - Constants.SERVICE_PRE_TIME;

		// 注意结束时间也要服务结束后 1:59分钟
		Long endServiceTime = (long) (serviceDate + serviceHour * 3600 + Constants.SERVICE_PRE_TIME);

		OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
		searchVo1.setStaffId(staffId);
		searchVo1.setDispatchStatus((short) 1);
		searchVo1.setStartServiceTime(startServiceTime);
		searchVo1.setEndServiceTime(endServiceTime);
		List<OrderDispatchs> disList = this.selectByMatchTime(searchVo1);

		if (!disList.isEmpty()) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg("指定的员工服务时间有冲突。");
			return result;
		}

		return result;
	}

	/**
	 * 检查是否已经约满了员工
	 * seriviceTypeId 服务ID
	 * serviceDateStr 服务日期（单位:天）
	 * addrId 服务地址
	 */
	@Override
	public List<Map<String, String>> checkDispatchedNotToday(Long serviceTypeId, String serviceDateStr, String lat, String lng, double serviceHours, int staffNums, Short orderType) {

		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();

		for (int i = 0; i < 24; i++) {

			String h = String.valueOf(i);
			if (h.length() == 1)
				h = "0" + h;
			Map<String, String> item1 = new HashMap<String, String>();

			item1.put("service_hour", h + ":00");
			item1.put("total", "0");
			item1.put("total_dispatched", "0");
			item1.put("staffs", "");
			item1.put("is_full", "0");
			datas.add(item1);

			Map<String, String> item2 = new HashMap<String, String>();
			item2.put("service_hour", h + ":30");
			item2.put("total", "0");
			item2.put("total_dispatched", "0");
			item1.put("staffs", "");
			item2.put("is_full", "0");
			datas.add(item2);

		}

		

		List<OrgDispatchPoiVo> orgList = getMatchOrgs(lat, lng, 0L, 0L, true);

		List<Long> orgIds = new ArrayList<Long>();

		for (int i = 0; i < orgList.size(); i++) {
			OrgDispatchPoiVo org = orgList.get(i);
			Long orgId = org.getOrgId();
			orgIds.add(orgId);
//			System.out.println("符合yun'd = " + org.getName() + "--距离 =" + org.getDistanceText());
		}

		if (orgIds.isEmpty()) {
			// 如果匹配不到任何云店，则显示所有都为已约满.
			for (int i = 0; i < datas.size(); i++) {
				Map<String, String> item = datas.get(i);
				item.put("is_full", "1");
				datas.set(i, item);
			}
			return datas;
		}

		List<Long> staffIds = new ArrayList<Long>();
		// 先找出云店下所有的员工,并且具有该项技能的人员.
		StaffSearchVo staffSearchVo = new StaffSearchVo();
		staffSearchVo.setOrgIds(orgIds);
		staffSearchVo.setServiceTypeId(serviceTypeId);
		staffSearchVo.setStatus(1);
		List<OrgStaffs> staffList = orgStaffService.selectBySearchVo(staffSearchVo);
		for (OrgStaffs os : staffList) {
			if (!staffIds.contains(os.getStaffId()))
				staffIds.add(os.getStaffId());
		}
		 System.out.println("总人数:" + staffIds.size());
		// 排除黑名单人员
		OrgStaffFinanceSearchVo searchVo2 = new OrgStaffFinanceSearchVo();
		searchVo2.setIsBlack((short) 1);
		List<OrgStaffFinance> blackList = orgStaffFinanceService.selectBySearchVo(searchVo2);
		for (OrgStaffFinance osf : blackList) {
			if (staffIds.contains(osf.getStaffId())) {
				staffIds.remove(osf.getStaffId());
			}
		}
		
		 System.out.println("排除黑名单后总人数:" + staffIds.size());
		// 排除请假人员
		LeaveSearchVo searchVo3 = new LeaveSearchVo();
		Date leaveDate = DateUtil.parse(serviceDateStr);
		searchVo3.setLeaveDate(leaveDate);
		searchVo3.setLeaveStatus("1");
		List<OrgStaffLeave> leaveList = orgStaffLeaveService.selectBySearchVo(searchVo3);
		if (!leaveList.isEmpty()) {
			for (OrgStaffLeave ol : leaveList) {
				if (staffIds.contains(ol.getStaffId())) {
					staffIds.remove(ol.getStaffId());
				}
			}
		}
		Collections.sort(staffIds);
		System.out.println("排除请假后总人数:" + staffIds.size());
		if (staffIds.isEmpty()) {
			// 如果人数都为0，则显示所有都为已约满.
			for (int i = 0; i < datas.size(); i++) {
				Map<String, String> item = datas.get(i);
				item.put("is_full", "1");
				datas.set(i, item);
			}
			return datas;
		}

		int total = staffIds.size();
		// 找出当天所有的派工订单和人员.
		OrderDispatchSearchVo orderDispatchSearchVo = new OrderDispatchSearchVo();
		Long startServiceTime = TimeStampUtil.getMillisOfDayFull(serviceDateStr + " 00:00:00");
		Long endServiceTime = TimeStampUtil.getMillisOfDayFull(serviceDateStr + " 23:59:59");
		orderDispatchSearchVo.setStartServiceTime(startServiceTime / 1000);
		orderDispatchSearchVo.setEndServiceTime(endServiceTime / 1000);
		orderDispatchSearchVo.setDispatchStatus((short) 1);
		orderDispatchSearchVo.setStaffIds(staffIds);

		double orderServiceHour = 0L;
		
		List<OrderDispatchs> orderDispatchs = this.selectBySearchVo(orderDispatchSearchVo);
		for (OrderDispatchs orderDispatch : orderDispatchs) {
			Long orderId = orderDispatch.getOrderId();
			Orders order = orderService.selectByPrimaryKey(orderId);
			orderServiceHour = order.getServiceHour();
			Long staffId = orderDispatch.getStaffId();
			if (order.getOrderStatus().equals(Constants.ORDER_HOUR_STATUS_0) || order.getOrderStatus().equals(Constants.ORDER_HOUR_STATUS_1)
					|| order.getOrderStatus().equals(Constants.ORDER_HOUR_STATUS_9)) {
				continue;
			}

			// 已 排班的 阿姨, 时间跨度为 
//			Long serviceDate = orderDispatch.getServiceDate();
			Long serviceDate = orderDispatch.getServiceDate() - (long) (120 * 60);

			String serviceDateTmp = TimeStampUtil.timeStampToDateStr(serviceDate * 1000, "m");
			int servcieDateTmpInt = Integer.valueOf(serviceDateTmp);
			if (servcieDateTmpInt > 0 && servcieDateTmpInt < 30) {
				serviceDate = serviceDate - servcieDateTmpInt * 60;
			} else if (servcieDateTmpInt > 30) {
				serviceDate = serviceDate + 30 * 60 - servcieDateTmpInt * 60;
			}

			// 时间跨度为结束时间 + 1小时59分钟被占用
			Double serviceHour = orderDispatch.getServiceHours() + 4;
			// Double serviceHour = orderDispatch.getServiceHours();

			Double stepHour = (double) 0;
			while (stepHour <= serviceHour) {

				String orderServiceDateStr = TimeStampUtil.timeStampToDateStr((long) ((serviceDate + stepHour * 60 * 60) * 1000), "HH:mm");

				for (int i = 0; i < datas.size(); i++) {
					Map<String, String> item = datas.get(i);
					String itemServiceHour = item.get("service_hour");

					if (orderServiceDateStr.equals(itemServiceHour)) {
//						int totalDispatched = Integer.valueOf(item.get("total_dispatched").toString());
//						totalDispatched = totalDispatched + 1;
						
//						item.put("total_dispatched", String.valueOf(totalDispatched));
						String staffs = "";
						if (item.get("staffs") != null) staffs = item.get("staffs").toString();
//						if (staffs.indexOf(staffId.toString() + ",") < 0) {
							staffs+= staffId.toString()+",";
//						}
							
//						if (orderServiceDateStr.equals("09:00")) {
//							 System.out.println("order_id = " + orderId +
//							 "--orderServiceDateStr = " + orderServiceDateStr +
//							 " --itemServiceHour = " + itemServiceHour +
//							 "--staffs =" + staffs);
//						}
						item.put("staffs", staffs);
						datas.set(i, item);
						break;
					}
				}
				stepHour = stepHour + 0.5;
			}
		}

		// 最后再进行循环，把是否约满字段补上.
		for (int i = 0; i < datas.size(); i++) {
			Map<String, String> item = datas.get(i);
			String staffs = "";
			if (item.get("staffs") != null) staffs = item.get("staffs").toString();
			int totalDispatched = 0;
			
			if (!StringUtil.isEmpty(staffs)) {
				String[] arys = StringUtil.convertStrToArray(staffs);
				totalDispatched = arys.length;
			}
			item.put("total_dispatched", String.valueOf(totalDispatched));
			int isFull = 0;
			if (total > 0 && totalDispatched >= 0 && totalDispatched >= total) {
				isFull = 1;
			}
			item.put("total", String.valueOf(total));
			item.put("is_full", String.valueOf(isFull));
			item.put("order_service_hour", String.valueOf(orderServiceHour));
			datas.set(i, item);
		}
		
		//进行判断，自动把不可用的填满约满
		datas = this.autoSetFull(datas, serviceDateStr, serviceHours, staffNums, orderType);
		return datas;
	}
	
	/**
	 * 检查是否已经约满了员工
	 * seriviceTypeId 服务ID
	 * serviceDateStr 服务日期（单位:天）
	 * addrId 服务地址
	 */
	@Override
	public List<Map<String, String>> checkDispatchedToday(Long serviceTypeId, String serviceDateStr, String lat, String lng, double serviceHours, int staffNums, Short orderType) {

		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();

		for (int i = 0; i < 24; i++) {

			String h = String.valueOf(i);
			if (h.length() == 1)
				h = "0" + h;
			Map<String, String> item1 = new HashMap<String, String>();

			item1.put("service_hour", h + ":00");
			item1.put("total", "0");
			item1.put("total_dispatched", "0");
			item1.put("staffs", "");
			item1.put("is_full", "0");
			datas.add(item1);

			Map<String, String> item2 = new HashMap<String, String>();
			item2.put("service_hour", h + ":30");
			item2.put("total", "0");
			item2.put("total_dispatched", "0");
			item1.put("staffs", "");
			item2.put("is_full", "0");
			datas.add(item2);

		}

		
		List<Long> staffIds = new ArrayList<Long>();
		List<Long> orgIds = new ArrayList<Long>();
		// 先找出云店下所有的员工,并且具有该项技能的人员.
		//获得当前预处理员工列表
		StaffSearchVo staffSearchVo = new StaffSearchVo();
		staffSearchVo.setStatus(1);
		staffSearchVo.setServiceTypeId(serviceTypeId);
		staffSearchVo.setIsNotBlack(1);
		List<OrgStaffs> staffList = orgStaffService.selectBySearchVo(staffSearchVo);
		
		for (OrgStaffs staff : staffList) {
			if (!staffIds.contains(staff.getStaffId()))
				staffIds.add(staff.getStaffId());
			
			if (!orgIds.contains(staff.getOrgId())) orgIds.add(staff.getOrgId());
			
		}
		
		 System.out.println("总人数:" + staffIds.size());
		
		// 排除请假人员
		LeaveSearchVo searchVo3 = new LeaveSearchVo();
		Date leaveDate = DateUtil.parse(serviceDateStr);
		searchVo3.setLeaveDate(leaveDate);
		searchVo3.setLeaveStatus("1");
		List<OrgStaffLeave> leaveList = orgStaffLeaveService.selectBySearchVo(searchVo3);
		if (!leaveList.isEmpty()) {
			for (OrgStaffLeave ol : leaveList) {
				if (staffIds.contains(ol.getStaffId())) {
					staffIds.remove(ol.getStaffId());
				}
			}
		}
		 System.out.println("排除请假后总人数:" + staffIds.size());
		if (staffIds.isEmpty()) {
			// 如果人数都为0，则显示所有都为已约满.
			for (int i = 0; i < datas.size(); i++) {
				Map<String, String> item = datas.get(i);
				item.put("is_full", "1");
				datas.set(i, item);
			}
			return datas;
		}
		
		//排除掉当天员工距离在20公里之外的

		List<OrgStaffDispatchVo> prelist = new ArrayList<OrgStaffDispatchVo>();
		//开始计算员工与服务地址的距离
		staffSearchVo = new StaffSearchVo();
		staffSearchVo.setStaffIds(staffIds);
		staffSearchVo.setStatus(1);
		staffList = orgStaffService.selectBySearchVo(staffSearchVo);
		
		for (OrgStaffs item : staffList) {
			OrgStaffDispatchVo vo = orgStaffService.initOrgStaffDispatchVo();
			BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);
			vo.setReason("");
			vo.setDispathStaFlag(1);
			vo.setDispathStaStr("可派工");
			prelist.add(vo);
		}
		
		prelist = this.getStaffDispatch(prelist, lat, lng);
		for (int i = 0; i < prelist.size(); i++) {
			OrgStaffDispatchVo vo = prelist.get(i);
			if (vo.getDistanceValue() > Constants.MAX_DISTANCE) {
				if (staffIds.contains(vo.getStaffId())) {
					staffIds.remove(vo.getStaffId());
				}
			}
		}
		System.out.println("排除距离超出的人员后:" + staffIds.size());
		int total = staffIds.size();
		// 找出当天所有的派工订单和人员.
		OrderDispatchSearchVo orderDispatchSearchVo = new OrderDispatchSearchVo();
		Long startServiceTime = TimeStampUtil.getMillisOfDayFull(serviceDateStr + " 00:00:00");
		Long endServiceTime = TimeStampUtil.getMillisOfDayFull(serviceDateStr + " 23:59:59");
		orderDispatchSearchVo.setStartServiceTime(startServiceTime / 1000);
		orderDispatchSearchVo.setEndServiceTime(endServiceTime / 1000);
		orderDispatchSearchVo.setDispatchStatus((short) 1);
		orderDispatchSearchVo.setStaffIds(staffIds);

		double orderServiceHour = 0L;
		List<OrderDispatchs> orderDispatchs = this.selectBySearchVo(orderDispatchSearchVo);
		for (OrderDispatchs orderDispatch : orderDispatchs) {
			Long orderId = orderDispatch.getOrderId();
			Orders order = orderService.selectByPrimaryKey(orderId);
			orderServiceHour = order.getServiceHour();
			Long staffId = orderDispatch.getStaffId();
			if (order.getOrderStatus().equals(Constants.ORDER_HOUR_STATUS_0) || order.getOrderStatus().equals(Constants.ORDER_HOUR_STATUS_1)
					|| order.getOrderStatus().equals(Constants.ORDER_HOUR_STATUS_9)) {
				continue;
			}

			// 已 排班的 阿姨, 时间跨度为 
//			Long serviceDate = orderDispatch.getServiceDate();
			Long serviceDate = orderDispatch.getServiceDate() - (long) (120 * 60);

			String serviceDateTmp = TimeStampUtil.timeStampToDateStr(serviceDate * 1000, "m");
			int servcieDateTmpInt = Integer.valueOf(serviceDateTmp);
			if (servcieDateTmpInt > 0 && servcieDateTmpInt < 30) {
				serviceDate = serviceDate - servcieDateTmpInt * 60;
			} else if (servcieDateTmpInt > 30) {
				serviceDate = serviceDate + 30 * 60 - servcieDateTmpInt * 60;
			}

			// 时间跨度为结束时间 + 1小时59分钟被占用
			Double serviceHour = orderDispatch.getServiceHours() + 4;
			// Double serviceHour = orderDispatch.getServiceHours();

			Double stepHour = (double) 0;
			while (stepHour < serviceHour) {

				String orderServiceDateStr = TimeStampUtil.timeStampToDateStr((long) ((serviceDate + stepHour * 60 * 60) * 1000), "HH:mm");

				for (int i = 0; i < datas.size(); i++) {
					Map<String, String> item = datas.get(i);
					String itemServiceHour = item.get("service_hour");
					
					if (orderServiceDateStr.equals(itemServiceHour)) {
//						int totalDispatched = Integer.valueOf(item.get("total_dispatched").toString());
//						totalDispatched = totalDispatched + 1;
						// System.out.println("order_id = " + orderId +
						// "--orderServiceDateStr = " + orderServiceDateStr +
						// " --itemServiceHour = " + itemServiceHour +
						// "--totalDispatched =" + totalDispatched);
//						item.put("total_dispatched", String.valueOf(totalDispatched));
						
						String staffs = "";
						if (item.get("staffs") != null) staffs = item.get("staffs").toString();
//						if (staffs.indexOf(staffId.toString() + ",") < 0) {
							staffs+= staffId.toString()+",";
//						}
						item.put("staffs", staffs);
						datas.set(i, item);
						break;
					}
				}
				stepHour = stepHour + 0.5;
			}
		}

		// 最后再进行循环，把是否约满字段补上.
		for (int i = 0; i < datas.size(); i++) {
			Map<String, String> item = datas.get(i);
			String staffs = "";
			if (item.get("staffs") != null) staffs = item.get("staffs").toString();
			int totalDispatched = 0;
			
			if (!StringUtil.isEmpty(staffs)) {
				String[] arys = StringUtil.convertStrToArray(staffs);
				totalDispatched = arys.length;
			}
			item.put("total_dispatched", String.valueOf(totalDispatched));
			int isFull = 0;
			if (total > 0 && totalDispatched >= 0 && totalDispatched >= total) {
				isFull = 1;
			}
			item.put("total", String.valueOf(total));
			item.put("is_full", String.valueOf(isFull));
			item.put("order_service_hour", String.valueOf(orderServiceHour));
			datas.set(i, item);
		}
		
		//进行判断，自动把不可用的填满约满
		datas = this.autoSetFull(datas, serviceDateStr, serviceHours, staffNums, orderType);
		
		return datas;
	}	

	/**
	 * 检查是否已经约满了员工,特定员工
	 * seriviceTypeId 服务ID
	 * serviceDateStr 服务日期（单位:天）
	 * addrId 服务地址
	 */
	@Override
	public List<Map<String, String>> checkDispatchedDayByStaffId(Long serviceTypeId, String serviceDateStr, Long staffId, String lat, String lng, double serviceHours, int staffNums, Short orderType) {

		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();

		// 初始化..........
		for (int i = 0; i < 24; i++) {
			String h = String.valueOf(i);
			if (h.length() == 1)
				h = "0" + h;
			Map<String, String> item1 = new HashMap<String, String>();

			item1.put("service_hour", h + ":00");
			item1.put("total", "0");
			item1.put("total_dispatched", "0");
			item1.put("is_full", "0");
			datas.add(item1);

			Map<String, String> item2 = new HashMap<String, String>();
			item2.put("service_hour", h + ":30");
			item2.put("total", "0");
			item2.put("total_dispatched", "0");
			item2.put("is_full", "0");
			datas.add(item2);
		}

		OrgStaffs staff = orgStaffService.selectByPrimaryKey(staffId);

		Long staffOrgId = staff.getOrgId();

		List<OrgDispatchPoiVo> orgList = getMatchOrgs(lat, lng, 0L, 0L, true);

		List<Long> orgIds = new ArrayList<Long>();

		for (int i = 0; i < orgList.size(); i++) {
			OrgDispatchPoiVo org = orgList.get(i);
			Long orgId = org.getOrgId();
			if (orgId.equals(staffOrgId))
				orgIds.add(orgId);
		}

		if (orgIds.isEmpty()) {
			// 如果匹配不到任何云店，则显示所有都为已约满.
			for (int i = 0; i < datas.size(); i++) {
				Map<String, String> item = datas.get(i);
				item.put("is_full", "1");
				datas.set(i, item);
			}
			return datas;
		}

		List<Long> staffIds = new ArrayList<Long>();
		// 先找出云店下所有的员工,并且具有该项技能的人员.
		StaffSearchVo staffSearchVo = new StaffSearchVo();
		staffSearchVo.setStaffId(staffId);
		staffSearchVo.setOrgIds(orgIds);
		staffSearchVo.setServiceTypeId(serviceTypeId);
		staffSearchVo.setStatus(1);
		List<OrgStaffs> staffList = orgStaffService.selectBySearchVo(staffSearchVo);
		for (OrgStaffs os : staffList) {
			if (!staffIds.contains(os.getStaffId()))
				staffIds.add(os.getStaffId());
		}
		// System.out.println("总人数:" + staffIds.size());
		// 排除黑名单人员
		OrgStaffFinanceSearchVo searchVo2 = new OrgStaffFinanceSearchVo();
		searchVo2.setIsBlack((short) 1);
		searchVo2.setStaffId(staffId);
		List<OrgStaffFinance> blackList = orgStaffFinanceService.selectBySearchVo(searchVo2);
		if (!blackList.isEmpty())
			staffIds = new ArrayList<Long>();
		// System.out.println("排除黑名单后总人数:" + staffIds.size());
		// 排除请假人员
		LeaveSearchVo searchVo3 = new LeaveSearchVo();
		Date leaveDate = DateUtil.parse(serviceDateStr);
		searchVo3.setStaffId(staffId);
		searchVo3.setLeaveDate(leaveDate);
		searchVo3.setLeaveStatus("1");
		List<OrgStaffLeave> leaveList = orgStaffLeaveService.selectBySearchVo(searchVo3);
		if (!leaveList.isEmpty())
			staffIds = new ArrayList<Long>();
		// System.out.println("排除请假后总人数:" + staffIds.size());
		if (staffIds.isEmpty()) {
			// 如果人数都为0，则显示所有都为已约满.
			for (int i = 0; i < datas.size(); i++) {
				Map<String, String> item = datas.get(i);
				item.put("is_full", "1");
				datas.set(i, item);
			}
			return datas;
		}

		int total = staffIds.size();
		// 找出当天所有的派工订单和人员.
		OrderDispatchSearchVo orderDispatchSearchVo = new OrderDispatchSearchVo();
		Long startServiceTime = TimeStampUtil.getMillisOfDayFull(serviceDateStr + " 00:00:00");
		Long endServiceTime = TimeStampUtil.getMillisOfDayFull(serviceDateStr + " 23:59:59");
		orderDispatchSearchVo.setStartServiceTime(startServiceTime / 1000);
		orderDispatchSearchVo.setEndServiceTime(endServiceTime / 1000);
		orderDispatchSearchVo.setDispatchStatus((short) 1);
		orderDispatchSearchVo.setStaffIds(staffIds);

		double orderServiceHour = 0L;
		List<OrderDispatchs> orderDispatchs = this.selectBySearchVo(orderDispatchSearchVo);
		for (OrderDispatchs orderDispatch : orderDispatchs) {
			Long orderId = orderDispatch.getOrderId();
			Orders order = orderService.selectByPrimaryKey(orderId);
			orderServiceHour = order.getServiceHour();

			if (order.getOrderStatus().equals(Constants.ORDER_HOUR_STATUS_0) || order.getOrderStatus().equals(Constants.ORDER_HOUR_STATUS_1)
					|| order.getOrderStatus().equals(Constants.ORDER_HOUR_STATUS_9)) {
				continue;
			}

			// 已 排班的 阿姨, 时间跨度为 服务开始前2小时 - 服务结束时间
			Long serviceDate = orderDispatch.getServiceDate();
//			Long serviceDate = orderDispatch.getServiceDate() - (long) (120 * 60);

			String serviceDateTmp = TimeStampUtil.timeStampToDateStr(serviceDate * 1000, "m");
			int servcieDateTmpInt = Integer.valueOf(serviceDateTmp);
			if (servcieDateTmpInt > 0 && servcieDateTmpInt < 30) {
				serviceDate = serviceDate - servcieDateTmpInt * 60;
			} else if (servcieDateTmpInt > 30) {
				serviceDate = serviceDate + 30 * 60 - servcieDateTmpInt * 60;
			}

			// 时间跨度为结束时间 + 1小时59分钟被占用
			Double serviceHour = orderDispatch.getServiceHours() + 2;
			// Double serviceHour = orderDispatch.getServiceHours();

			Double stepHour = (double) 0;
			while (stepHour < serviceHour) {

				String orderServiceDateStr = TimeStampUtil.timeStampToDateStr((long) ((serviceDate + stepHour * 60 * 60) * 1000), "HH:mm");

				for (int i = 0; i < datas.size(); i++) {
					Map<String, String> item = datas.get(i);
					String itemServiceHour = item.get("service_hour");

					if (orderServiceDateStr.equals(itemServiceHour)) {
						int totalDispatched = Integer.valueOf(item.get("total_dispatched").toString());
						totalDispatched = totalDispatched + 1;
						// System.out.println("order_id = " + orderId +
						// "--orderServiceDateStr = " + orderServiceDateStr +
						// " --itemServiceHour = " + itemServiceHour +
						// "--totalDispatched =" + totalDispatched);
						item.put("total_dispatched", String.valueOf(totalDispatched));
						datas.set(i, item);
						break;
					}
				}
				stepHour = stepHour + 0.5;
			}
		}

		// 最后再进行循环，把是否约满字段补上.
		for (int i = 0; i < datas.size(); i++) {
			Map<String, String> item = datas.get(i);
			int totalDispatched = Integer.valueOf(item.get("total_dispatched").toString());
			int isFull = 0;
			if (total > 0 && totalDispatched >= 0 && totalDispatched >= total) {
				isFull = 1;
			}
			item.put("total", String.valueOf(total));
			item.put("is_full", String.valueOf(isFull));
			item.put("order_service_hour", String.valueOf(orderServiceHour));
			datas.set(i, item);
		}
		
		//进行判断，自动把不可用的填满约满
		datas = this.autoSetFull(datas, serviceDateStr, serviceHours, staffNums, orderType);
		return datas;
	}

	/**
	 * 
	 * @Title: getTheNearestStaff
	 * @Description:
	 *               助理订单 手动派工时,
	 * 
	 *               得到 “可选 服务人员 ” 实时 位置距离 服务地址的 时间 、该服务人员当天的 派工 量
	 * 
	 *               可选服务人员,符合 基本派工条件（距离、黑名单。。）
	 * 
	 * @param fromLat
	 *            用户地址坐标
	 * @param fromLng
	 * @param staIdList
	 *            可用 服务人员 Id
	 * @throws
	 */
	@Override
	public List<OrgStaffDispatchVo> getStaffDistance(String fromLat, String fromLng, List<Long> staIdList) {

		List<OrgStaffDispatchVo> staWithUserList = new ArrayList<OrgStaffDispatchVo>();

		if (staIdList.size() == 0) {
			staIdList.add(0, 0L);
		}

		// 参数中传入服务人员 的 最新 位置 <服务人员：该服务人员的最新位置。。VO>
		UserTrailSearchVo searchVo = new UserTrailSearchVo();
		searchVo.setUserIds(staIdList);
		List<UserTrailReal> list = trailRealService.selectBySearchVo(searchVo);

		List<BaiduPoiVo> orgAddrList = new ArrayList<BaiduPoiVo>();

		for (UserTrailReal userTrailReal : list) {

			BaiduPoiVo baiduPoiVo = new BaiduPoiVo();

			baiduPoiVo.setLat(userTrailReal.getLat());
			baiduPoiVo.setLng(userTrailReal.getLng());
			baiduPoiVo.setName(userTrailReal.getPoiName());

			orgAddrList.add(baiduPoiVo);

		}

		UserTrailReal item = null;

		try {
			List<BaiduPoiVo> destList = MapPoiUtil.getMapRouteMatrix(fromLat, fromLng, orgAddrList);

			Collections.sort(destList, new Comparator<BaiduPoiVo>() {
				@Override
				public int compare(BaiduPoiVo s1, BaiduPoiVo s2) {
					return Integer.valueOf(s1.getDistanceValue()).compareTo(s2.getDistanceValue());
				}
			});

			for (int i = 0; i < list.size(); i++) {
				item = list.get(i);

				Long staffId = item.getUserId();

				// 该 服务人员 当天 的 派单 数量
				Long numTodayOrder = this.totalStaffTodayOrders(staffId);

				// 派工页面 服务人员 相关 信息 VO
				OrgStaffDispatchVo staffsNewVo = orgStaffService.initOrgStaffDispatchVo();

				OrgStaffs staffs = orgStaffService.selectByPrimaryKey(staffId);

				BeanUtilsExp.copyPropertiesIgnoreNull(staffs, staffsNewVo);

				staffsNewVo.setStaffId(item.getUserId());
				staffsNewVo.setTodayOrderNum(numTodayOrder.intValue());

				// 服务人员所在 云店 id
				Long orgId = staffs.getOrgId();

				// 服务人员所在 门店id （该云店 的 上级 门店）
				Long parentOrgId = staffs.getParentOrgId();

				Orgs cloudOrg = orgService.selectByPrimaryKey(orgId);

				if (cloudOrg != null) {
					staffsNewVo.setParentOrgName(cloudOrg.getOrgName());
				}

				Orgs orgs = orgService.selectByPrimaryKey(parentOrgId);

				if (orgs != null) {
					staffsNewVo.setOrgName(orgs.getOrgName());
				}

				// 设置 服务 人员 特色信息, 供派工参考
				for (BaiduPoiVo baiduPoiVo : destList) {
					if (baiduPoiVo.getLat().equals(item.getLat()) && baiduPoiVo.getLng().equals(item.getLng())) {

						// 合适的服务人员的最新位置 与 服务地址 的 距离、时间等信息
						staffsNewVo.setDistanceText(baiduPoiVo.getDistanceText());
						staffsNewVo.setDurationText(baiduPoiVo.getDurationText());
						staffsNewVo.setDistanceValue(baiduPoiVo.getDistanceValue());
					}
				}

				staWithUserList.add(staffsNewVo);
			}

			Collections.sort(staWithUserList, new Comparator<OrgStaffDispatchVo>() {
				@Override
				public int compare(OrgStaffDispatchVo s1, OrgStaffDispatchVo s2) {
					return Integer.valueOf(s1.getDistanceValue()).compareTo(s2.getDistanceValue());
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		return staWithUserList;
	}

	/*
	 * 更新 用户与 “派工后选中”的 服务人员 的 最新距离
	 */
	@Override
	public int getLatestDistance(String userLat, String userLon, Long staffId) {

		List<Long> staIdList = new ArrayList<Long>();
		staIdList.add(staffId);

		List<OrgStaffDispatchVo> list = getStaffDistance(userLat, userLon, staIdList);

		int distance = 0;

		if (list.size() > 0) {
			for (OrgStaffDispatchVo orgStaffsNewVo : list) {
				distance = orgStaffsNewVo.getDistanceValue();
			}
		}

		return distance;
	}

	/**
	 * 派工成功，为员工发送推送消息
	 */
	@Override
	public void pushToStaff(Long staffId, String isShow, String action, Long orderId, String remindTitle, String remindContent) {

		// 1.下单成功，为员工推送消息
		UserPushBind userPushBind = null;
		UserPushBindSearchVo searchVo = new UserPushBindSearchVo();
		searchVo.setUserId(staffId);
		List<UserPushBind> list = userPushBindService.selectBySearchVo(searchVo);
		if (!list.isEmpty())
			userPushBind = list.get(0);

		if (userPushBind != null) {
			String clientId = userPushBind.getClientId();
			PushUtil.getUserStatus(clientId);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("cid", clientId);
			/**
			 * 订单派工透传消息格式：
			 * is_show:是否在通知栏展示 true/false
			 * action:diaptch(点击打开派工信息)，msg(点击打开app)
			 * order_id:订单Id
			 * remind_title:消息栏标题
			 * remind_content:消息展示内容
			 */
			HashMap<String, String> tranParams = new HashMap<String, String>();
			tranParams.put("is_show", isShow); // true=展现,false=不显示
			tranParams.put("action", action);// =dispatch进入派工详情，=msg打开app
			tranParams.put("order_id", orderId + "");
			tranParams.put("remind_title", remindTitle);
			tranParams.put("remind_content", remindContent);

			Orders order = orderService.selectByPrimaryKey(orderId);
			OrderPrices orderPrice = orderPriceService.selectByOrderId(orderId);

			OrderDispatchSearchVo searchVo3 = new OrderDispatchSearchVo();
			searchVo3.setOrderNo(order.getOrderNo());
			searchVo3.setDispatchStatus((short) 1);
			List<OrderDispatchs> orderDispatchs = this.selectBySearchVo(searchVo3);

			OrderDispatchs orderDispatch = null;
			if (!orderDispatchs.isEmpty()) {
				orderDispatch = orderDispatchs.get(0);
			}

			// 服务地址：
			String serviceAddr = "";
			if (order.getAddrId() > 0L) {
				UserAddrs userAddr = userAddrService.selectByPrimaryKey(order.getAddrId());
				serviceAddr = userAddr.getName() + userAddr.getAddr();
			}

			if (order.getOrderType().equals((short) 2)) {
				serviceAddr = orderDispatch.getPickAddrName() + orderDispatch.getPickAddr();
			}

			tranParams.put("service_addr", serviceAddr);

			// 服务时间
			Long serviceDate = order.getServiceDate();

			if (order.getOrderType().equals((short) 2)) {
				serviceDate = order.getAddTime();
			}

			String serviceTime = TimeStampUtil.timeStampToDateStr(serviceDate * 1000, "MM-dd HH:mm");

			tranParams.put("service_time", serviceTime);

			// 服务时长
			String serviceHour = order.getServiceHour() + "小时";
			tranParams.put("service_hour", serviceHour);

			// 服务项目
			String serviceContent = "";
			if (order.getServiceType() > 0L) {
				PartnerServiceType serviceType = partnerServiceTypeService.selectByPrimaryKey(order.getServiceType());
				serviceContent = serviceType.getName();
			}
			tranParams.put("service_content", serviceContent);

			// 服务金额
			String orderMoney = MathBigDecimalUtil.round2(orderPrice.getOrderMoney());
			tranParams.put("order_money", orderMoney);

			// 订单类型
			tranParams.put("order_type", order.getOrderType().toString());

			String jsonParams = GsonUtil.GsonString(tranParams);
			params.put("transmissionContent", jsonParams);
			try {
				PushUtil.AndroidPushToSingle(params);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 智能判断服务日期中间层是否需要约，举例：
	 *   8 - 10：30 可约， 但是11点约满
	 *   则如果可约时间点小于serviceHour, 则把所有的设置为已约满.
	 */
	@Override 
	public List<Map<String, String>> autoSetFull(List<Map<String, String>> datas, String serviceDateStr, double serviceHours, int staffNums, Short orderType) {
		
		
		if (staffNums > 1 && orderType.equals(Constants.ORDER_TYPE_1)) {
			serviceHours = MathBigDecimalUtil.getValueStepHalf(serviceHours, staffNums);
		}
		serviceHours = serviceHours + 2;
		List<Integer> autoSetMapIndex = new ArrayList<Integer>();
		String beginServiceHour = "";
		String endServiceHour = "";
		double checkServiceHour = 0;

		for (int i = 0 ; i < datas.size(); i++) {
			Map<String, String> item = datas.get(i);
			String serviceHour = item.get("service_hour").toString();
			String hourStr = serviceHour.substring(0, 2);
			int hour = Integer.valueOf(hourStr).intValue();
			if (hour < 8) continue;
			String isFull = item.get("is_full").toString();
//			System.out.println("service_hour = " + serviceHour + " --- isFull = " + isFull);
			if (isFull.equals("0")) {
				checkServiceHour+=0.5;
				autoSetMapIndex.add(i);
			}
			
			if (isFull.equals("0") && StringUtil.isEmpty(beginServiceHour))  {
				beginServiceHour = serviceHour;
			} 
			
			if (isFull.equals("1") && StringUtil.isEmpty(endServiceHour)) {
				endServiceHour = serviceHour;
			}
			
			//做相应的判断.
			if (!StringUtil.isEmpty(beginServiceHour) && !StringUtil.isEmpty(endServiceHour)) {
				if (checkServiceHour < serviceHours) {
					for (int j = 0; j < datas.size(); j++) {
						for (int k = 0; k < autoSetMapIndex.size(); k++) {
							if (j == autoSetMapIndex.get(k)) {
								Map<String, String> setitem = datas.get(j);
								setitem.put("is_full", "1");
								datas.set(j, setitem);
							}
						}
					}
					
					beginServiceHour = "";
					endServiceHour = "";
					autoSetMapIndex = new ArrayList<Integer>();
				}
			}	
		}		
		return datas;
	}
}
