package com.jhj.service.impl.order;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffLeave;
import com.jhj.po.model.bs.OrgStaffSkill;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffLeaveService;
import com.jhj.service.bs.OrgStaffSkillService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.order.OrderDispatchAllocateService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserTrailRealService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.bs.OrgDispatchPoiVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrgStaffDispatchVo;
import com.jhj.vo.org.LeaveSearchVo;
import com.jhj.vo.org.OrgSearchVo;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;
import com.jhj.vo.staff.OrgStaffSkillSearchVo;
import com.jhj.vo.staff.StaffSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.ListUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

/**
 * 
 * @author lnczx
 * 本类为派工逻辑，按照正常的云店距离，派工逻辑为
	前置条件
	  1.   服务地址
	  2.   服务时间
	  3.   服务品类
	步骤如下：
	日期区分：隔日订单 
		1.  根据服务地址经纬度，匹配符合20公里的云店，得到 云店集合A
		2.  根据云店集合A找出所有的员工，得到员工集合B
		    1） 审核通过的员工
		    2） 状态可用的员工
		3.  根据员工集合B找出符合服务品类的员工集合C
		4.  员工集合D，排除在服务时间有派工的员工（注：提前2小时）。的到员工集合E
		5.  员工集合E，排除在黑名单，得到员工集合F
		6.  员工集合F,  排除请假的员工，得到员工集合G。
		7.  10公里之内（以云店位置坐标），优先选择前一日没有订单的员工，如果相同则随机
		8. 根据云店的距离由近到远的优先级进行。
		9.员工集合找出服务时间日期的订单数，优先选择订单数少的员工，如果相同则随机。（调整订单后不计订单数量，按照派单成功实际接单数计算）

	日期区分：当日订单 
		1.  根据服务地址经纬度，匹配符合位置坐标在20公里内的员工，得到 云店集合A
		2.  根据云店集合A找出所有的员工，得到员工集合B
		    1） 审核通过的员工
		    2） 状态可用的员工
		3.  根据员工集合B找出符合服务品类的员工集合C
		4.  员工集合C，找出最匹配的员工服务技能，得到员工集合D
		5.  员工集合D，排除在服务时间有派工的员工（注：提前2小时）。的到员工集合E
		6.  员工集合E，排除在黑名单，得到员工集合F
		7.  员工集合F,  排除请假的员工，得到员工集合G。
		8.  10公里之内（以员工位置坐标），员工集合找出服务时间日期的订单数，优先选择订单数少的员工，如果相同则随机。（调整订单后不计订单数量，按照派单成功实际接单数计算）
		9. 超过10公里，根据员工的位置坐标距离由近到远的优先级进行。
		10. 订单数量相同，优先找订单金额数值低的员工。
	
	1---7-10      效率优先  
	8           合理分配（显示）推荐原因
 */
@Service
public class OrderDispatchAllocateServiceImpl implements OrderDispatchAllocateService {

	@Autowired
	private OrderDispatchsService orderDispatchService;

	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;
	
	@Autowired
	private UsersService userService;	

	@Autowired
	private UserAddrsService userAddrService;

	@Autowired
	private OrdersService orderService;

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

	/**
	 * 订单类自动派工 , 隔日订单
	 * @param addrId   			地址ID
	 * @param servcieTypeId 	服务类型ID
	 * @param serviceDate 		服务日期
	 * @param serviceHour 		服务时长
	 * @param selectParentId	选择门店
	 * @param selectOrgId		选择云店	
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<OrgStaffDispatchVo> manualDispatchNotToday(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, Long selectParentId, Long selectOrgId) {

		List<OrgStaffDispatchVo> list = new ArrayList<OrgStaffDispatchVo>();
		UserAddrs addrs = userAddrService.selectByPrimaryKey(addrId);
		String fromLat = addrs.getLatitude();
		String fromLng = addrs.getLongitude();

		List<OrgDispatchPoiVo> orgList = orderDispatchService.getMatchOrgs(fromLat, fromLng, 0L, 0L, true);

		if (orgList.isEmpty())
			return list;

		List<Long> staffIds = new ArrayList<Long>();

		for (int i = 0; i < orgList.size(); i++) {
			OrgDispatchPoiVo org = orgList.get(i);
			Long orgId = org.getOrgId();
			if (selectParentId > 0L) {
				if (!org.getParentId().equals(selectParentId)) continue;
			}
			
			if (selectOrgId > 0L) {
				if (!orgId.equals(selectOrgId)) continue;
			}
			
			//先找出云店下所有的员工, 有技能，不在黑名单
			StaffSearchVo staffSearchVo = new StaffSearchVo();
			staffSearchVo.setOrgId(orgId);
			staffSearchVo.setStatus(1);
			staffSearchVo.setServiceTypeId(serviceTypeId);
			staffSearchVo.setIsNotBlack(1);
			List<OrgStaffs> staffList = orgStaffService.selectBySearchVo(staffSearchVo);
			
			if (staffList.isEmpty()) continue;
			
			for (OrgStaffs s : staffList) {
				if (!staffIds.contains(s.getStaffId())) staffIds.add(s.getStaffId());
			}
			
			if (staffIds.isEmpty()) continue;

		}

		// ---4. 在订单服务时间内请假的员工.
		LeaveSearchVo leaveSearchVo = new LeaveSearchVo();
		String serviceDateStr = TimeStampUtil.timeStampToDateStr(serviceDate * 1000, "yyyy-MM-dd"); 
		Date leaveDate = DateUtil.parse(serviceDateStr);
		leaveSearchVo.setLeaveDate(leaveDate);
		leaveSearchVo.setLeaveStatus("1");
		
		// 服务时间内 ，同时也在 假期内的 员工
		List<OrgStaffLeave> leaveList = orgStaffLeaveService.selectBySearchVo(leaveSearchVo);

		if (!leaveList.isEmpty()) {
			for (OrgStaffLeave ol : leaveList) {
				if (staffIds.contains(ol.getStaffId())) {
					staffIds.remove(ol.getStaffId());
				}
			}
		}

		if (staffIds.isEmpty())
			return list;
		
		
		// ---2.服务时间内 已 排班的 阿姨, 时间跨度为 服务开始前1:59分钟 - 服务结束时间
		Long startServiceTime = serviceDate - Constants.SERVICE_PRE_TIME;
		
		// 注意结束时间也要服务结束后 1:59分钟
		Long endServiceTime = (long) (serviceDate + serviceHour * 3600 + Constants.SERVICE_PRE_TIME);
		
		OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
		searchVo1.setDispatchStatus((short) 1);
		searchVo1.setStartServiceTime(startServiceTime);
		searchVo1.setEndServiceTime(endServiceTime);
		List<OrderDispatchs> disList = orderDispatchService.selectByMatchTime(searchVo1);

		for (OrderDispatchs orderDispatch : disList) {
			if (staffIds.contains(orderDispatch.getStaffId())) {
				staffIds.remove(orderDispatch.getStaffId());
			}
		}

		if (staffIds.isEmpty()) return list;
		

		// ---5.找出已匹配的员工列表，并统计当天的订单数，优先指派订单数少的员工，如果订单数相同，则随机
		StaffSearchVo searchVo5 = new StaffSearchVo();
		searchVo5.setStaffIds(staffIds);
		searchVo5.setStatus(1);
		List<OrgStaffs> staffList = orgStaffService.selectBySearchVo(searchVo5);

		// 员工服务日期的订单数
		List<HashMap> totalStaffOrders = orderDispatchService.getTotalStaffOrders(serviceDate, staffIds);

		// -- 6.找出已匹配的员工列表，并统计前一天的订单数. 优先指派订单数为0的员工.
		Date serviceDateObj = DateUtil.parse(serviceDateStr);
		String preServiceDateStr = DateUtil.addDay(serviceDateObj, -1, Calendar.DATE, DateUtil.DEFAULT_PATTERN);
		Long preServiceDate = TimeStampUtil.getMillisOfDay(preServiceDateStr) / 1000; 
		List<HashMap> preTotalStaffOrders = orderDispatchService.getTotalStaffOrders(preServiceDate, staffIds);
		
		// 门店名称
		OrgSearchVo orgSearchVo = new OrgSearchVo();
		orgSearchVo.setIsParent(1);
		List<Orgs> orgParents = orgService.selectBySearchVo(orgSearchVo);

		for (OrgStaffs item : staffList) {
			OrgStaffDispatchVo vo = orgStaffService.initOrgStaffDispatchVo();
			BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);
			vo.setReason("");
			vo.setDispathStaFlag(1);
			vo.setDispathStaStr("可派工");

			// 门店名称
			for (Orgs o : orgParents) {
				if (o.getOrgId().equals(vo.getParentOrgId())) {
					vo.setParentOrgName(o.getOrgName());
					break;
				}
			}

			// 门店距离
			for (OrgDispatchPoiVo poiVo : orgList) {
				if (vo.getOrgId().equals(poiVo.getOrgId())) {
					vo.setOrgName(poiVo.getOrgName());
					vo.setOrgDistanceValue(poiVo.getDistanceValue());
					vo.setOrgDistanceText(poiVo.getDistanceText());
					break;
				}
			}
			
			//当天有派工的人员
			for (HashMap totalItem : totalStaffOrders) {
				if (totalItem.get("staff_id") == null) continue;
				if (totalItem.get("total") == null) continue;
				Long staffId = (Long) totalItem.get("staff_id");
				int total = Integer.valueOf(totalItem.get("total").toString());

				if (staffId.equals(vo.getStaffId())) {
					vo.setTodayOrderNum(total);
				}
			}
			
			//隔天有派工的人员
			for (HashMap totalItem : preTotalStaffOrders) {
				if (totalItem.get("staff_id") == null) continue;
				if (totalItem.get("total") == null) continue;
				Long staffId = (Long) totalItem.get("staff_id");
				int total = Integer.valueOf(totalItem.get("total").toString());

				if (staffId.equals(vo.getStaffId())) {
					vo.setPreDayOrderNum(total);
				}
			}

			list.add(vo);
		}

		// 员工距离
		list = orderDispatchService.getStaffDispatch(list, fromLat, fromLng);
		
		//派工依据. 0 = 合理分配  1 = 效率优先
		for (int i = 0; i < list.size(); i++) {
			OrgStaffDispatchVo item = list.get(i);
			String allocateReason = "效率优先";
			int allocate = 1;
			int orgDistanceValue = item.getOrgDistanceValue();
			int preDayOrderNum = item.getPreDayOrderNum();
			if (orgDistanceValue <= 10000 && preDayOrderNum == 0) {
				allocateReason = "合理分配";
				allocate = 0;
			}
			item.setAllocateReason(allocateReason);
			item.setAllocate(allocate);
			list.set(i, item);
		}
		
		// 进行排序，根据云店距离大小, 分配依据，员工距离 正序.
		if (list.size() > 0) {
			String[] sortNameArr = {"orgDistanceValue", "allocate", "distanceValue"};
			boolean[] typeArr = {true, true, true};
			ListUtil.sort(list, sortNameArr, typeArr);
		}
		
		return list;
	}
	
	/**
	 * 订单类手动派工 , 当日订单
	 * @param addrId   			地址ID
	 * @param servcieTypeId 	服务类型ID
	 * @param serviceDate 		服务日期
	 * @param serviceHour 		服务时长
	 * @param selectParentId	选择门店
	 * @param selectOrgId		选择云店	
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<OrgStaffDispatchVo> manualDispatchToday(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, Long selectParentId, Long selectOrgId) {

		List<OrgStaffDispatchVo> list = new ArrayList<OrgStaffDispatchVo>();
		UserAddrs addrs = userAddrService.selectByPrimaryKey(addrId);
		String fromLat = addrs.getLatitude();
		String fromLng = addrs.getLongitude();
		
		//获得当前预处理员工列表
		StaffSearchVo staffSearchVo = new StaffSearchVo();
		if (selectParentId > 0L) staffSearchVo.setParentId(selectParentId);
		if (selectOrgId > 0L) staffSearchVo.setParentId(selectOrgId);
		staffSearchVo.setStatus(1);
		staffSearchVo.setServiceTypeId(serviceTypeId);
		staffSearchVo.setIsNotBlack(1);
		List<OrgStaffs> staffList = orgStaffService.selectBySearchVo(staffSearchVo);
		
		List<Long> staffIds = new ArrayList<Long>();
		List<Long> orgIds = new ArrayList<Long>();
		for (OrgStaffs staff : staffList) {
			if (!staffIds.contains(staff.getStaffId()))
				staffIds.add(staff.getStaffId());
			
			if (!orgIds.contains(staff.getOrgId())) orgIds.add(staff.getOrgId());
			
		}
		
		// ---在订单服务时间内请假的员工.
		LeaveSearchVo leaveSearchVo = new LeaveSearchVo();
		String serviceDateStr = TimeStampUtil.timeStampToDateStr(serviceDate * 1000, "yyyy-MM-dd"); 
		Date leaveDate = DateUtil.parse(serviceDateStr);
		leaveSearchVo.setLeaveDate(leaveDate);
		leaveSearchVo.setLeaveStatus("1");
		
		// 服务时间内 ，同时也在 假期内的 员工
		List<OrgStaffLeave> leaveList = orgStaffLeaveService.selectBySearchVo(leaveSearchVo);

		if (!leaveList.isEmpty()) {
			for (OrgStaffLeave ol : leaveList) {
				if (staffIds.contains(ol.getStaffId())) {
					staffIds.remove(ol.getStaffId());
				}
			}
		}

		if (staffIds.isEmpty()) return list;
		
		// ---2.服务时间内 已 排班的 阿姨, 时间跨度为 服务开始前1:59分钟 - 服务结束时间
		Long startServiceTime = serviceDate - Constants.SERVICE_PRE_TIME;
		
		// 注意结束时间也要服务结束后 1:59分钟
		Long endServiceTime = (long) (serviceDate + serviceHour * 3600 + Constants.SERVICE_PRE_TIME);
		
		OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
		searchVo1.setDispatchStatus((short) 1);
		searchVo1.setStartServiceTime(startServiceTime);
		searchVo1.setEndServiceTime(endServiceTime);
		List<OrderDispatchs> disList = orderDispatchService.selectByMatchTime(searchVo1);

		for (OrderDispatchs orderDispatch : disList) {
			if (staffIds.contains(orderDispatch.getStaffId())) {
				staffIds.remove(orderDispatch.getStaffId());
			}
		}

		if (staffIds.isEmpty()) return list;
		
		// 员工服务日期的订单数
		List<HashMap> totalStaffOrders = orderDispatchService.getTotalStaffOrders(serviceDate, staffIds);
		
		Date serviceDateObj = DateUtil.parse(serviceDateStr);
		String preServiceDateStr = DateUtil.addDay(serviceDateObj, -1, Calendar.DATE, DateUtil.DEFAULT_PATTERN);
		Long preServiceDate = TimeStampUtil.getMillisOfDay(preServiceDateStr) / 1000; 
		List<HashMap> preTotalStaffOrders = orderDispatchService.getTotalStaffOrders(preServiceDate, staffIds);
		
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
		
		prelist = orderDispatchService.getStaffDispatch(prelist, fromLat, fromLng);
		
		// 门店名称
		OrgSearchVo orgSearchVo = new OrgSearchVo();
		orgSearchVo.setIsParent(1);
		List<Orgs> orgParents = orgService.selectBySearchVo(orgSearchVo);
		
		//匹配距离的门店
		List<OrgDispatchPoiVo> orgList = orderDispatchService.getMatchOrgs(fromLat, fromLng, 0L, 0L, true);
		
		//当日订单最小值.
		int minTotalOrders = 0;
		
		for (int i = 0; i < prelist.size(); i++) {
			OrgStaffDispatchVo vo = prelist.get(i);
			if (vo.getDistanceValue() > Constants.MAX_DISTANCE) continue;
			
			// 门店名称
			for (Orgs o : orgParents) {
				if (o.getOrgId().equals(vo.getParentOrgId())) {
					vo.setParentOrgName(o.getOrgName());
					break;
				}
			}

			// 门店距离
			for (OrgDispatchPoiVo poiVo : orgList) {
				if (vo.getOrgId().equals(poiVo.getOrgId())) {
					vo.setOrgName(poiVo.getOrgName());
					vo.setOrgDistanceValue(poiVo.getDistanceValue());
					vo.setOrgDistanceText(poiVo.getDistanceText());
					break;
				}
			}
			
			//当天有派工的人员
			for (HashMap totalItem : totalStaffOrders) {
				if (totalItem.get("staff_id") == null) continue;
				if (totalItem.get("total") == null) continue;
				Long staffId = (Long) totalItem.get("staff_id");
				int total = Integer.valueOf(totalItem.get("total").toString());

				if (staffId.equals(vo.getStaffId())) {
					vo.setTodayOrderNum(total);
				}
			}
			
			//隔天有派工的人员
			for (HashMap totalItem : preTotalStaffOrders) {
				if (totalItem.get("staff_id") == null) continue;
				if (totalItem.get("total") == null) continue;
				Long staffId = (Long) totalItem.get("staff_id");
				int total = Integer.valueOf(totalItem.get("total").toString());

				if (staffId.equals(vo.getStaffId())) {
					vo.setPreDayOrderNum(total);
				}
			}
			
			if (i == 0) {
				minTotalOrders = vo.getTodayOrderNum();
			} else {
				if (vo.getTodayOrderNum() < minTotalOrders) {
					minTotalOrders = vo.getTodayOrderNum();
				}
			}
			
			list.add(vo);
		}
		
		//派工依据. 0 = 合理分配  1 = 效率优先
		for (int i = 0; i < list.size(); i++) {
			OrgStaffDispatchVo item = list.get(i);
			String allocateReason = "效率优先";
			int allocate = 1;
			int distanceValue = item.getDistanceValue();
			int total = item.getTodayOrderNum();
			if (distanceValue <= 10000 && total == minTotalOrders) {
				allocateReason = "合理分配";
				allocate = 0;
			}
			item.setAllocateReason(allocateReason);
			item.setAllocate(allocate);
			list.set(i, item);
		}
		
		
		if (list.size() > 0) {
			String[] sortNameArr = {"allocate", "todayOrderNum", "distanceValue"};
			boolean[] typeArr = {true, true, true};
			ListUtil.sort(list, sortNameArr, typeArr);
		}
		
		return list;
	}
	
	/**
	 * 订单类手动派工 , 当日订单
	 * @param addrId   			地址ID
	 * @param servcieTypeId 	服务类型ID
	 * @param serviceDate 		服务日期
	 * @param serviceHour 		服务时长
	 * @param selectParentId	选择门店
	 * @param selectOrgId		选择云店	
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<OrgStaffDispatchVo> manualDispatchTodayByOrg(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, Long selectParentId, Long selectOrgId) {

		List<OrgStaffDispatchVo> list = this.manualDispatchByOrg(addrId, serviceTypeId, serviceDate, serviceHour, selectParentId, selectOrgId);
		//当日订单最小值.
		int minTotalOrders = 0;
		
		for (int i = 0; i < list.size(); i++) {
			OrgStaffDispatchVo vo = list.get(i);
			if (i == 0) {
				minTotalOrders = vo.getTodayOrderNum();
			} else {
				if (vo.getTodayOrderNum() < minTotalOrders) {
					minTotalOrders = vo.getTodayOrderNum();
				}
			}
		}
		
		//派工依据. 0 = 合理分配  1 = 效率优先
		for (int i = 0; i < list.size(); i++) {
			OrgStaffDispatchVo item = list.get(i);
			String allocateReason = "效率优先";
			int allocate = 1;
			int distanceValue = item.getDistanceValue();
			int total = item.getTodayOrderNum();
			if (distanceValue <= 10000 && total == minTotalOrders) {
				allocateReason = "合理分配";
				allocate = 0;
			}
			item.setAllocateReason(allocateReason);
			item.setAllocate(allocate);
			list.set(i, item);
		}
		
		
		if (list.size() > 0) {
			String[] sortNameArr = {"dispathStaFlag", "allocate", "todayOrderNum", "distanceValue"};
			boolean[] typeArr = {false, true, true, true};
			ListUtil.sort(list, sortNameArr, typeArr);
		}
		
		return list;
	}
	
	/**
	 * 订单类手动派工 , 当日订单
	 * @param addrId   			地址ID
	 * @param servcieTypeId 	服务类型ID
	 * @param serviceDate 		服务日期
	 * @param serviceHour 		服务时长
	 * @param selectParentId	选择门店
	 * @param selectOrgId		选择云店	
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<OrgStaffDispatchVo> manualDispatchNotTodayByOrg(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, Long selectParentId, Long selectOrgId) {

		List<OrgStaffDispatchVo> list = this.manualDispatchByOrg(addrId, serviceTypeId, serviceDate, serviceHour, selectParentId, selectOrgId);
		//派工依据. 0 = 合理分配  1 = 效率优先
		for (int i = 0; i < list.size(); i++) {
			OrgStaffDispatchVo item = list.get(i);
			String allocateReason = "效率优先";
			int allocate = 1;
			int orgDistanceValue = item.getOrgDistanceValue();
			int preDayOrderNum = item.getPreDayOrderNum();
			if (orgDistanceValue <= 10000 && preDayOrderNum == 0) {
				allocateReason = "合理分配";
				allocate = 0;
			}
			item.setAllocateReason(allocateReason);
			item.setAllocate(allocate);
			list.set(i, item);
		}
		
		// 进行排序，根据云店距离大小, 分配依据，员工距离 正序.
		if (list.size() > 0) {
			String[] sortNameArr = {"dispathStaFlag", "orgDistanceValue", "allocate", "distanceValue"};
			boolean[] typeArr = {false, true, true, true};
			ListUtil.sort(list, sortNameArr, typeArr);
		}
		
		return list;
	}	
	
	/**
	 * 订单类手动派工 , 当日订单
	 * @param addrId   			地址ID
	 * @param servcieTypeId 	服务类型ID
	 * @param serviceDate 		服务日期
	 * @param serviceHour 		服务时长
	 * @param selectParentId	选择门店
	 * @param selectOrgId		选择云店	
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<OrgStaffDispatchVo> manualDispatchByOrg(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, Long selectParentId, Long selectOrgId) {

		List<OrgStaffDispatchVo> list = new ArrayList<OrgStaffDispatchVo>();
		UserAddrs addrs = userAddrService.selectByPrimaryKey(addrId);
		String fromLat = addrs.getLatitude();
		String fromLng = addrs.getLongitude();
		
		//获得当前预处理员工列表
		StaffSearchVo staffSearchVo = new StaffSearchVo();
		if (selectParentId > 0L) staffSearchVo.setParentId(selectParentId);
		if (selectOrgId > 0L) staffSearchVo.setParentId(selectOrgId);
		staffSearchVo.setStatus(1);
		List<OrgStaffs> staffList = orgStaffService.selectBySearchVo(staffSearchVo);
		
		List<Long> staffIds = new ArrayList<Long>();
		List<Long> noSkillstaffIds = new ArrayList<Long>();
		List<Long> orgIds = new ArrayList<Long>();
		for (OrgStaffs staff : staffList) {
			if (!staffIds.contains(staff.getStaffId()))
				staffIds.add(staff.getStaffId());
			
			if (!noSkillstaffIds.contains(staff.getStaffId()))
				noSkillstaffIds.add(staff.getStaffId());
			
			if (!orgIds.contains(staff.getOrgId())) orgIds.add(staff.getOrgId());
			
			OrgStaffDispatchVo vo = orgStaffService.initOrgStaffDispatchVo();
			BeanUtilsExp.copyPropertiesIgnoreNull(staff, vo);
			vo.setReason("");
			vo.setDispathStaFlag(1);
			vo.setDispathStaStr("可派工");
			list.add(vo);
			
		}
		
		// 1. 找出技能不符合的员工.
		OrgStaffSkillSearchVo searchVo = new OrgStaffSkillSearchVo();
		if (selectParentId > 0L) searchVo.setParentId(selectParentId);
		if (selectOrgId > 0L) searchVo.setOrgId(selectOrgId);
		searchVo.setServiceTypeId(serviceTypeId);
		List<OrgStaffSkill> skillList = orgStaffSkillService.selectBySearchVo(searchVo);

		List<Long> skillStaffIds = new ArrayList<Long>();
		for (OrgStaffSkill item : skillList) {
			if (!skillStaffIds.contains(item.getStaffId()))
				skillStaffIds.add(item.getStaffId());
		}
		
		noSkillstaffIds.removeAll(skillStaffIds);

		for (OrgStaffDispatchVo vo : list) {
			for (Long item : noSkillstaffIds) {
				if (vo.getStaffId().equals(item)) {
					vo.setDispathStaFlag(0);
					vo.setDispathStaStr("不可派工");
					if(vo.getReason()!=null){
						vo.setReason(vo.getReason() + ";" + ConstantMsg.NOT_DISPATCH_NOT_SKILL);
					}else{
						vo.setReason(ConstantMsg.NOT_DISPATCH_NOT_SKILL);
					}
					
				}
			}
		}
		
		// ---在订单服务时间内请假的员工.
		LeaveSearchVo leaveSearchVo = new LeaveSearchVo();
		String serviceDateStr = TimeStampUtil.timeStampToDateStr(serviceDate * 1000, "yyyy-MM-dd"); 
		Date leaveDate = DateUtil.parse(serviceDateStr);
		leaveSearchVo.setLeaveDate(leaveDate);
		leaveSearchVo.setLeaveStatus("1");
		
		// 服务时间内 ，同时也在 假期内的 员工
		List<OrgStaffLeave> leaveList = orgStaffLeaveService.selectBySearchVo(leaveSearchVo);

		for (OrgStaffDispatchVo vo : list) {
			for (OrgStaffLeave os : leaveList) {
				if (vo.getStaffId().equals(os.getStaffId())) {
					vo.setDispathStaFlag(0);
					vo.setDispathStaStr("不可派工");
					if(vo.getReason()!=null){
						vo.setReason(vo.getReason() + ";" + ConstantMsg.NOT_DISPATCH_LEAVE);
					}else{
						vo.setReason(ConstantMsg.NOT_DISPATCH_LEAVE);
					}
				}
			}
		}
		
		// ---在黑名单中的服务人员
		OrgStaffFinanceSearchVo searchVo2 = new OrgStaffFinanceSearchVo();
		searchVo2.setStaffIds(staffIds);
		searchVo2.setIsBlack((short) 1);
		List<OrgStaffFinance> blackList = orgStaffFinanceService.selectBySearchVo(searchVo2);

		for (OrgStaffDispatchVo vo : list) {
			for (OrgStaffFinance os : blackList) {
				if (vo.getStaffId().equals(os.getStaffId())) {
					vo.setDispathStaFlag(0);
					vo.setDispathStaStr("不可派工");
					if(vo.getReason()!=null){
						vo.setReason(vo.getReason() + ";" + ConstantMsg.NOT_DISPATCH_BLACK_LIST);
					}else{
						vo.setReason(ConstantMsg.NOT_DISPATCH_BLACK_LIST);
					}
				}
			}
		}


		// ---2.服务时间内 已 排班的 阿姨, 时间跨度为 服务开始前1:59分钟 - 服务结束时间
		Long startServiceTime = serviceDate - Constants.SERVICE_PRE_TIME;
		
		// 注意结束时间也要服务结束后 1:59分钟
		Long endServiceTime = (long) (serviceDate + serviceHour * 3600 + Constants.SERVICE_PRE_TIME);
		
		OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
		if (selectParentId > 0L) searchVo1.setParentId(selectParentId);
		if (selectOrgId > 0L) searchVo1.setParentId(selectOrgId);
		searchVo1.setDispatchStatus((short) 1);
		searchVo1.setStartServiceTime(startServiceTime);
		searchVo1.setEndServiceTime(endServiceTime);
		List<OrderDispatchs> disList = orderDispatchService.selectByMatchTime(searchVo1);

		List<Long> haveDispatchStaffIds = new ArrayList<Long>();
		for (OrderDispatchs orderDispatch : disList) {
			if (!haveDispatchStaffIds.contains(orderDispatch.getStaffId()))
				haveDispatchStaffIds.add(orderDispatch.getStaffId());
		}
		
		
		for (OrgStaffDispatchVo vo : list) {
			for (Long item : haveDispatchStaffIds) {
				if (vo.getStaffId().equals(item)) {
					vo.setDispathStaFlag(0);
					vo.setDispathStaStr("不可派工");
					if(vo.getReason()!=null){
						vo.setReason(vo.getReason() + ";" + ConstantMsg.NOT_DISPATCH_SERVICE_DATE_CONFLIT);
					}else{
						vo.setReason(ConstantMsg.NOT_DISPATCH_SERVICE_DATE_CONFLIT);
					}
				}
			}
		}
		
		// 员工服务日期的订单数
		List<HashMap> totalStaffOrders = orderDispatchService.getTotalStaffOrders(serviceDate, staffIds);
		
		Date serviceDateObj = DateUtil.parse(serviceDateStr);
		String preServiceDateStr = DateUtil.addDay(serviceDateObj, -1, Calendar.DATE, DateUtil.DEFAULT_PATTERN);
		Long preServiceDate = TimeStampUtil.getMillisOfDay(preServiceDateStr) / 1000; 
		List<HashMap> preTotalStaffOrders = orderDispatchService.getTotalStaffOrders(preServiceDate, staffIds);
		
		list = orderDispatchService.getStaffDispatch(list, fromLat, fromLng);
		
		//开始计算员工与服务地址的距离			
		// 门店名称
		OrgSearchVo orgSearchVo = new OrgSearchVo();
		orgSearchVo.setIsParent(1);
		List<Orgs> orgParents = orgService.selectBySearchVo(orgSearchVo);
		
	
		
		//匹配距离的门店
		List<OrgDispatchPoiVo> orgList = orderDispatchService.getMatchOrgs(fromLat, fromLng, 0L, 0L, false);
		
		for (int i = 0; i < list.size(); i++) {
			OrgStaffDispatchVo vo = list.get(i);

			// 门店名称
			for (Orgs o : orgParents) {
				if (o.getOrgId().equals(vo.getParentOrgId())) {
					vo.setParentOrgName(o.getOrgName());
					break;
				}
			}
			
			// 门店距离
			for (OrgDispatchPoiVo poiVo : orgList) {
				if (vo.getOrgId().equals(poiVo.getOrgId())) {
					vo.setOrgName(poiVo.getOrgName());
					vo.setOrgDistanceValue(poiVo.getDistanceValue());
					vo.setOrgDistanceText(poiVo.getDistanceText());
					
					if (vo.getOrgDistanceValue() > Constants.MAX_DISTANCE) {
						vo.setDispathStaFlag(0);
						vo.setDispathStaStr("不可派工");
						if(vo.getReason()!=null){
							vo.setReason(vo.getReason() + ";" + ConstantMsg.NOT_DISPATCH_SERVICE_DATE_CONFLIT);
						}else{
							vo.setReason(ConstantMsg.NOT_DISPATCH_SERVICE_DATE_CONFLIT);
						}
					}
					
					break;
				}
			}

			//当天有派工的人员
			for (HashMap totalItem : totalStaffOrders) {
				if (totalItem.get("staff_id") == null) continue;
				if (totalItem.get("total") == null) continue;
				Long staffId = (Long) totalItem.get("staff_id");
				int total = Integer.valueOf(totalItem.get("total").toString());

				if (staffId.equals(vo.getStaffId())) {
					vo.setTodayOrderNum(total);
				}
			}
			
			//隔天有派工的人员
			for (HashMap totalItem : preTotalStaffOrders) {
				if (totalItem.get("staff_id") == null) continue;
				if (totalItem.get("total") == null) continue;
				Long staffId = (Long) totalItem.get("staff_id");
				int total = Integer.valueOf(totalItem.get("total").toString());

				if (staffId.equals(vo.getStaffId())) {
					vo.setPreDayOrderNum(total);
				}
			}
			
		}
		return list;
	}

}
