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

import com.github.pagehelper.PageHelper;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.dao.bs.OrgStaffLeaveMapper;
import com.jhj.po.dao.order.OrderDispatchsMapper;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffLeave;
import com.jhj.po.model.bs.OrgStaffSkill;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserTrailReal;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffBlackService;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffLeaveService;
import com.jhj.service.bs.OrgStaffSkillService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.newDispatch.NewDispatchStaffService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserTrailRealService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.bs.OrgDispatchPoiVo;
import com.jhj.vo.dispatch.StaffDispatchVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderDispatchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.order.OrgStaffsNewVo;
import com.jhj.vo.org.LeaveSearchVo;
import com.jhj.vo.org.OrgSearchVo;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;
import com.jhj.vo.staff.OrgStaffSkillSearchVo;
import com.jhj.vo.staff.StaffSearchVo;
import com.jhj.vo.user.UserTrailSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.baidu.BaiduPoiVo;
import com.meijia.utils.baidu.MapPoiUtil;
import com.meijia.utils.vo.AppResultData;

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
	private NewDispatchStaffService newDisStaService;

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
		
		//是否接单状态;
		Short isApply = vo.getIsApply();
		if (isApply.equals((short)1)) {
			vo.setApplyStatus("是");
			vo.setApplyTimeStr(TimeStampUtil.timeStampToDateStr(vo.getApplyTime() * 1000));
		} else {
			Long now = TimeStampUtil.getNowSecond();
			Long dispatchTime = vo.getAddTime();
			Long lastTime = now - dispatchTime;
			if ( lastTime > 60 * 30) {
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
	 * @param orderId  订单ID
	 * @param serviceDate 服务日期
	 * @param serviceHour 服务时长
	 * @param staffNums   预约服务人员数 支持多个服务人员.
	 * @param appointStaffIds  已经指派的员工，需要排除掉
	 */
	@Override
	public List<Long> autoDispatch(Long orderId, Long serviceDate, Double serviceHour, int staffNums, List<Long> appointStaffIds) {

		List<Long> dispatchStaffIds = new ArrayList<Long>();

		Orders order = orderService.selectByPrimaryKey(orderId);

		Long serviceTypeId = order.getServiceType();

		Long addrId = order.getAddrId();

		UserAddrs addrs = userAddrService.selectByPrimaryKey(addrId);

		List<OrgDispatchPoiVo> orgList = getMatchOrgs(addrs.getLatitude(), addrs.getLongitude(), 0L, 0L, true);

		if (orgList.isEmpty())
			return dispatchStaffIds;

		List<Long> staffIds = new ArrayList<Long>();
		List<Long> canDispatchStaffIds = new ArrayList<Long>();

		for (int i = 0; i < orgList.size(); i++) {
			OrgDispatchPoiVo org = orgList.get(i);
			Long orgId = org.getOrgId();
			
			//先找出云店下所有的员工
			StaffSearchVo staffSearchVo = new StaffSearchVo();
			staffSearchVo.setOrgId(orgId);
			staffSearchVo.setStatus(1);
			List<OrgStaffs> staffList = orgStaffService.selectBySearchVo(staffSearchVo);
			
			if (staffList.isEmpty()) continue;
			
			List<Long> staffIdAlls = new ArrayList<Long>();
			for (OrgStaffs s : staffList) {
				if (!staffIdAlls.contains(s.getStaffId())) staffIdAlls.add(s.getStaffId());
			}

			// ----1. 找出所有的符合此技能的员工 |
			staffIds = new ArrayList<Long>();
			OrgStaffSkillSearchVo searchVo = new OrgStaffSkillSearchVo();
			searchVo.setOrgId(orgId);
			searchVo.setStaffIds(staffIdAlls);
			searchVo.setServiceTypeId(serviceTypeId);
			List<OrgStaffSkill> skillList = orgStaffSkillService.selectBySearchVo(searchVo);
			
			if (!skillList.isEmpty()) {
				for (OrgStaffSkill s : skillList) {
					if (!staffIds.contains(s.getStaffId()))
						staffIds.add(s.getStaffId());
				}
			}

			if (staffIds.isEmpty())
				continue;

			// ---2.服务时间内 已 排班的 阿姨, 时间跨度为 服务开始前1:59分钟 - 服务结束时间
			Long startServiceTime = serviceDate - Constants.SERVICE_PRE_TIME;
			
			// 注意结束时间也要服务结束后 1:59分钟
			Long endServiceTime = (long) (serviceDate + serviceHour * 3600 + Constants.SERVICE_PRE_TIME);

			OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
			searchVo1.setOrgId(orgId);
			searchVo1.setDispatchStatus((short) 1);
			searchVo1.setStartServiceTime(startServiceTime);
			searchVo1.setEndServiceTime(endServiceTime);
			List<OrderDispatchs> disList = this.selectByMatchTime(searchVo1);

			for (OrderDispatchs orderDispatch : disList) {
				if (staffIds.contains(orderDispatch.getStaffId())) {
					staffIds.remove(orderDispatch.getStaffId());
				}
			}

			if (staffIds.isEmpty())
				continue;

			// ---3.排除掉在黑名单中的服务人员
			OrgStaffFinanceSearchVo searchVo2 = new OrgStaffFinanceSearchVo();
			searchVo2.setStaffIds(staffIds);
			searchVo2.setIsBlack((short) 1);
			List<OrgStaffFinance> blackList = orgStaffFinanceService.selectBySearchVo(searchVo2);

			if (!blackList.isEmpty()) {
				for (OrgStaffFinance os : blackList) {
					if (staffIds.contains(os.getStaffId())) {
						staffIds.remove(os.getStaffId());
					}
				}
			}

			if (staffIds.isEmpty())
				continue;

			// ---4.排除请假的员工.
			LeaveSearchVo searchVo3 = new LeaveSearchVo();

			searchVo3.setStaffIds(staffIdAlls);

			String serviceDateStr = TimeStampUtil.timeStampToDateStr(serviceDate * 1000, "yyyy-MM-dd"); 
			Date leaveDate = DateUtil.parse(serviceDateStr);
			searchVo3.setLeaveDate(leaveDate);
			searchVo3.setLeaveStatus("1");

			// 服务时间内 ，同时也在 假期内的 员工
			List<OrgStaffLeave> leaveList = orgStaffLeaveService.selectBySearchVo(searchVo3);

			if (!leaveList.isEmpty()) {
				for (OrgStaffLeave ol : leaveList) {
					if (staffIds.contains(ol.getStaffId())) {
						staffIds.remove(ol.getStaffId());
					}
				}
			}

			
			if (staffIds.isEmpty()) continue;
			
			//排除掉已经指定的员工
			if (!appointStaffIds.isEmpty()) {
				for (Long asId : appointStaffIds) {
					staffIds.remove(asId);
				}
			}
			
			// 如果该云店有满足员工，则加入canDispatchStaffIds ,如果canDispatchStaffIds >= staffNums 则直接跳出循环.
			for (Long sid : staffIds) {
				canDispatchStaffIds.add(sid);
			}
		
			int canDispatchStaffIdsSize = 0;
			if (!canDispatchStaffIds.isEmpty()) canDispatchStaffIdsSize = canDispatchStaffIds.size();
			if (canDispatchStaffIdsSize >= staffNums) break;
		}
		
		if (canDispatchStaffIds.isEmpty()) return dispatchStaffIds;
		

		if (canDispatchStaffIds.size() == staffNums) {
			dispatchStaffIds = canDispatchStaffIds;
			return dispatchStaffIds;
		}

		// ---5.找出已匹配的员工列表，并统计当天的订单数，优先指派订单数少的员工，如果订单数相同，则随机
	

		List<OrgStaffsNewVo> list = new ArrayList<OrgStaffsNewVo>();
		StaffSearchVo searchVo5 = new StaffSearchVo();
		searchVo5.setStaffIds(canDispatchStaffIds);
		searchVo5.setStatus(1);
		List<OrgStaffs> staffList = orgStaffService.selectBySearchVo(searchVo5);

		// 员工服务日期当天的订单数
		List<HashMap> totalStaffs = this.getTotalStaffs(serviceDate, canDispatchStaffIds);

		for (OrgStaffs item : staffList) {
			OrgStaffsNewVo vo = this.initStaffsNew();
			BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);
			vo.setDispathStaFlag(1);
			for (HashMap totalItem : totalStaffs) {
				
				if (totalItem.get("staff_id") == null) continue;
				if (totalItem.get("total") == null) continue;
				Long staffId = (Long) totalItem.get("staff_id");
				int total = Integer.valueOf(totalItem.get("total").toString());

				if (staffId.equals(vo.getStaffId())) {
					vo.setTodayOrderNum(total);
				}
			}

			list.add(vo);
		}

		// 进行排序，根据距离大小来正序.
		if (list.size() > 0) {
			Collections.sort(list, new Comparator<OrgStaffsNewVo>() {
				@Override
				public int compare(OrgStaffsNewVo s1, OrgStaffsNewVo s2) {
					return Integer.valueOf(s1.getTodayOrderNum()).compareTo(s2.getTodayOrderNum());
				}
			});

			for (int i = 0; i < staffNums; i++) {
				if(list.size()>0){
					OrgStaffsNewVo orgStaffsNewVo = list.get(i);
					if(orgStaffsNewVo!=null){
						dispatchStaffIds.add(orgStaffsNewVo.getStaffId());
					}
				}
			}
		}

		return dispatchStaffIds;
	}

	/**
	 * 订单类手动派工 ,需要返回List<OrgStaffsNewVo>,
	 * 1. 找出符合派工逻辑的所有门店的员工
	 * 2. 包含门店的距离
	 * 3. 包含员工的距离.
	 */
	@Override
	public List<OrgStaffsNewVo> manualDispatch(Long orderId, Long serviceDate, Double serviceHour, Long sessionOrgId) {

		List<OrgStaffsNewVo> list = new ArrayList<OrgStaffsNewVo>();

		Orders order = orderService.selectByPrimaryKey(orderId);

		Long serviceTypeId = order.getServiceType();

		Long addrId = order.getAddrId();
		UserAddrs addrs = userAddrService.selectByPrimaryKey(addrId);
		String fromLat = addrs.getLatitude();
		String fromLng = addrs.getLongitude();

		List<OrgDispatchPoiVo> orgList = getMatchOrgs(fromLat, fromLng, 0L, 0L, true);

		if (orgList.isEmpty())
			return list;

		List<Long> staffIds = new ArrayList<Long>();

		for (int i = 0; i < orgList.size(); i++) {
			OrgDispatchPoiVo org = orgList.get(i);
			Long orgId = org.getOrgId();
			if (sessionOrgId > 0L) {
				if (!org.getParentId().equals(sessionOrgId)) continue;
			}
			
			//先找出云店下所有的员工
			StaffSearchVo staffSearchVo = new StaffSearchVo();
			staffSearchVo.setOrgId(orgId);
			staffSearchVo.setStatus(1);
			List<OrgStaffs> staffList = orgStaffService.selectBySearchVo(staffSearchVo);
			
			if (staffList.isEmpty()) continue;
			
			List<Long> staffIdAlls = new ArrayList<Long>();
			for (OrgStaffs s : staffList) {
				if (!staffIdAlls.contains(s.getStaffId())) staffIdAlls.add(s.getStaffId());
			}
			
			
			
			// ----1. 找出所有的符合此技能的员工 |
			OrgStaffSkillSearchVo searchVo = new OrgStaffSkillSearchVo();
			searchVo.setOrgId(orgId);
			searchVo.setStaffIds(staffIdAlls);
			searchVo.setServiceTypeId(serviceTypeId);
			List<OrgStaffSkill> skillList = orgStaffSkillService.selectBySearchVo(searchVo);

			if (!skillList.isEmpty()) {
				for (OrgStaffSkill s : skillList) {
					if (!staffIds.contains(s.getStaffId()))
						staffIds.add(s.getStaffId());
				}
			}

			if (staffIds.isEmpty())
				continue;

			// ---2.服务时间内 已 排班的 阿姨, 时间跨度为 服务开始前1:59分钟 - 服务结束时间
			Long startServiceTime = serviceDate - Constants.SERVICE_PRE_TIME;
			
			// 注意结束时间也要服务结束后 1:59分钟
			Long endServiceTime = (long) (serviceDate + serviceHour * 3600 + Constants.SERVICE_PRE_TIME);
			
			OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
			searchVo1.setOrgId(orgId);
			searchVo1.setDispatchStatus((short) 1);
			searchVo1.setStartServiceTime(startServiceTime);
			searchVo1.setEndServiceTime(endServiceTime);
			List<OrderDispatchs> disList = this.selectByMatchTime(searchVo1);

			for (OrderDispatchs orderDispatch : disList) {
				if (staffIds.contains(orderDispatch.getStaffId())) {
					staffIds.remove(orderDispatch.getStaffId());
				}
			}

			if (staffIds.isEmpty())
				continue;

		}

		// ---3.排除掉在黑名单中的服务人员
		OrgStaffFinanceSearchVo searchVo2 = new OrgStaffFinanceSearchVo();
		searchVo2.setStaffIds(staffIds);
		searchVo2.setIsBlack((short) 1);
		List<OrgStaffFinance> blackList = orgStaffFinanceService.selectBySearchVo(searchVo2);

		if (!blackList.isEmpty()) {
			for (OrgStaffFinance os : blackList) {
				if (staffIds.contains(os.getStaffId())) {
					staffIds.remove(os.getStaffId());
				}
			}
		}

		if (staffIds.isEmpty())
			return list;

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

		// ---5.找出已匹配的员工列表，并统计当天的订单数，优先指派订单数少的员工，如果订单数相同，则随机

		StaffSearchVo searchVo5 = new StaffSearchVo();
		searchVo5.setStaffIds(staffIds);
		searchVo5.setStatus(1);
		List<OrgStaffs> staffList = orgStaffService.selectBySearchVo(searchVo5);

		// 员工服务日期的订单数
		List<HashMap> totalStaffs = this.getTotalStaffs(serviceDate, staffIds);

		// 门店名称
		OrgSearchVo orgSearchVo = new OrgSearchVo();
		orgSearchVo.setIsParent(1);
		List<Orgs> orgParents = orgService.selectBySearchVo(orgSearchVo);

		for (OrgStaffs item : staffList) {
			OrgStaffsNewVo vo = this.initStaffsNew();
			BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);
			vo.setReason("");
			vo.setDispathStaFlag(1);
			vo.setDispathStaStr("可派工");

			// 门店名称
			for (Orgs o : orgParents) {
				if (o.getOrgId().equals(vo.getParentOrgId())) {
					vo.setStaffOrgName(o.getOrgName());
					break;
				}
			}

			// 门店距离
			for (OrgDispatchPoiVo poiVo : orgList) {
				if (vo.getOrgId().equals(poiVo.getOrgId())) {
					vo.setStaffCloudOrgName(poiVo.getOrgName());
					vo.setOrgDistanceValue(poiVo.getDistanceValue());
					vo.setOrgDistanceText(poiVo.getDistanceText());
					break;
				}
			}

			for (HashMap totalItem : totalStaffs) {
				if (totalItem.get("staff_id") == null) continue;
				if (totalItem.get("total") == null) continue;
				Long staffId = (Long) totalItem.get("staff_id");
				int total = Integer.valueOf(totalItem.get("total").toString());

				if (staffId.equals(vo.getStaffId())) {
					vo.setTodayOrderNum(total);
				}
			}

			list.add(vo);
		}

		// 员工距离
		list = this.getStaffDispatch(list, fromLat, fromLng);

		// 进行排序，根据距离大小来正序.
		if (list.size() > 0) {
			Collections.sort(list, new Comparator<OrgStaffsNewVo>() {
				@Override
				public int compare(OrgStaffsNewVo s1, OrgStaffsNewVo s2) {
					return Integer.valueOf(s1.getDistanceValue()).compareTo(s2.getDistanceValue());
				}
			});
		}
		return list;
	}

	/**
	 * 订单类手动派工 ,需要返回List<OrgStaffsNewVo>,
	 * 1. 找出符合派工逻辑的针对门店或云店的所有员工（包括不可派工的员工）;
	 * 2. 包含门店的距离
	 * 3. 包含员工的距离.
	 */
	@Override
	public List<OrgStaffsNewVo> manualDispatchByOrg(Long orderId, Long serviceDate, Double serviceHour, Long parentId, Long orgId) {

		Orders order = orderService.selectByPrimaryKey(orderId);

		Long serviceTypeId = order.getServiceType();

		Long addrId = order.getAddrId();
		UserAddrs addrs = userAddrService.selectByPrimaryKey(addrId);
		String fromLat = addrs.getLatitude();
		String fromLng = addrs.getLongitude();

		List<OrgStaffsNewVo> list = new ArrayList<OrgStaffsNewVo>();

		StaffSearchVo staffSearchVo = new StaffSearchVo();
		staffSearchVo.setStatus(1);

		if (parentId > 0L)
			staffSearchVo.setParentId(parentId);
		if (orgId > 0L)
			staffSearchVo.setOrgId(orgId);
		List<OrgStaffs> staffList = orgStaffService.selectBySearchVo(staffSearchVo);

		List<Long> staffIds = new ArrayList<Long>();
		List<Long> noSkillstaffIds = new ArrayList<Long>();
		for (OrgStaffs staff : staffList) {
			OrgStaffsNewVo vo = this.initStaffsNew();
			BeanUtilsExp.copyPropertiesIgnoreNull(staff, vo);
			vo.setDispathStaFlag(1);
			vo.setDispathStaStr("可派工");
			list.add(vo);
			if (!staffIds.contains(staff.getStaffId()))
				staffIds.add(staff.getStaffId());
			if (!noSkillstaffIds.contains(staff.getStaffId()))
				noSkillstaffIds.add(staff.getStaffId());
		}

		// 1. 找出云店距离目标位置集合,判断如果超出20Km，则写上原因, 距离超出.
		List<OrgDispatchPoiVo> orgList = getMatchOrgs(fromLat, fromLng, parentId, orgId, false);
		if (orgList.isEmpty())
			return list;

		List<Long> overMaxDistanceOrgIds = new ArrayList<Long>();
		for (int i = 0; i < orgList.size(); i++) {
			OrgDispatchPoiVo poiVo = orgList.get(i);
			if (poiVo.getDistanceValue() > Constants.MAX_DISTANCE) {
				overMaxDistanceOrgIds.add(poiVo.getOrgId());
			}
		}

		for (Long maxOrgId : overMaxDistanceOrgIds) {
			for (OrgStaffsNewVo vo : list) {
				if (vo.getOrgId().equals(maxOrgId)) {
					vo.setDispathStaFlag(0);
					vo.setDispathStaStr("不可派工");
					vo.setReason(ConstantMsg.NOT_DISPATCH_OVER_MAX_DISTANCE);
				}
			}
		}

		// 1. 找出技能不符合的员工.
		OrgStaffSkillSearchVo searchVo = new OrgStaffSkillSearchVo();
		if (parentId > 0L)
			searchVo.setParentId(parentId);
		if (orgId > 0L)
			searchVo.setOrgId(orgId);
		searchVo.setServiceTypeId(serviceTypeId);
		List<OrgStaffSkill> skillList = orgStaffSkillService.selectBySearchVo(searchVo);

		List<Long> skillStaffIds = new ArrayList<Long>();
		for (OrgStaffSkill item : skillList) {
			if (!skillStaffIds.contains(item.getStaffId()))
				skillStaffIds.add(item.getStaffId());
		}

		// 找出有技能和没技能的差集

		noSkillstaffIds.removeAll(skillStaffIds);

		for (OrgStaffsNewVo vo : list) {
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

		// ---2.服务时间内 已 排班的 阿姨, 时间跨度为 服务开始前1:59分钟 - 服务结束时间
		Long startServiceTime = serviceDate - Constants.SERVICE_PRE_TIME;
		
		// 注意结束时间也要服务结束后 1:59分钟
		Long endServiceTime = (long) (serviceDate + serviceHour * 3600 + Constants.SERVICE_PRE_TIME);

		OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
		if (parentId > 0L)
			searchVo1.setParentId(parentId);
		if (orgId > 0L)
			searchVo1.setOrgId(orgId);
		searchVo1.setDispatchStatus((short) 1);
		searchVo1.setStartServiceTime(startServiceTime);
		searchVo1.setEndServiceTime(endServiceTime);
		List<OrderDispatchs> disList = this.selectByMatchTime(searchVo1);
		List<Long> haveDispatchStaffIds = new ArrayList<Long>();
		for (OrderDispatchs item : disList) {
			if (!haveDispatchStaffIds.contains(item.getStaffId()))
				haveDispatchStaffIds.add(item.getStaffId());
		}

		for (OrgStaffsNewVo vo : list) {
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

		// ---3.排除掉在黑名单中的服务人员
		OrgStaffFinanceSearchVo searchVo2 = new OrgStaffFinanceSearchVo();
		searchVo2.setStaffIds(staffIds);
		searchVo2.setIsBlack((short) 1);
		List<OrgStaffFinance> blackList = orgStaffFinanceService.selectBySearchVo(searchVo2);

		for (OrgStaffsNewVo vo : list) {
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

		// ---4.排除请假的员工.
		LeaveSearchVo searchVo3 = new LeaveSearchVo();


		String serviceDateStr = TimeStampUtil.timeStampToDateStr(serviceDate * 1000, "yyyy-MM-dd"); 
		Date leaveDate = DateUtil.parse(serviceDateStr);
		searchVo3.setLeaveDate(leaveDate);
		searchVo3.setLeaveStatus("1");
		
		List<OrgStaffLeave> leaveList = orgStaffLeaveService.selectBySearchVo(searchVo3);

		for (OrgStaffsNewVo vo : list) {
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

		// 门店名称
		OrgSearchVo orgSearchVo = new OrgSearchVo();
		orgSearchVo.setIsParent(1);
		List<Orgs> orgParents = orgService.selectBySearchVo(orgSearchVo);

		// 员工服务日期的订单数
		List<HashMap> totalStaffs = this.getTotalStaffs(serviceDate, staffIds);

		for (OrgStaffsNewVo vo : list) {

			// 门店名称
			for (Orgs o : orgParents) {
				if (o.getOrgId().equals(vo.getParentOrgId())) {
					vo.setStaffOrgName(o.getOrgName());
					break;
				}
			}

			// 门店距离
			for (OrgDispatchPoiVo poiVo : orgList) {
				if (vo.getOrgId().equals(poiVo.getOrgId())) {
					vo.setStaffCloudOrgName(poiVo.getOrgName());
					vo.setOrgDistanceValue(poiVo.getDistanceValue());
					vo.setOrgDistanceText(poiVo.getDistanceText());
					break;
				}
			}

			// 员工服务日期的订单数
			for (HashMap totalItem : totalStaffs) {
				if (totalItem.get("staff_id") == null) continue;
				if (totalItem.get("total") == null) continue;
				Long staffId = (Long) totalItem.get("staff_id");
				int total = Integer.valueOf(totalItem.get("total").toString());

				if (staffId.equals(vo.getStaffId())) {
					vo.setTodayOrderNum(total);
				}
			}

		}

		// 员工距离
		list = this.getStaffDispatch(list, fromLat, fromLng);

		// 进行排序，根据距离大小来正序.
		if (list.size() > 0) {
			Collections.sort(list, new Comparator<OrgStaffsNewVo>() {
				@Override
				public int compare(OrgStaffsNewVo s1, OrgStaffsNewVo s2) {
					return Integer.valueOf(s1.getDistanceValue()).compareTo(s2.getDistanceValue());
				}
			});
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
	public List<OrgStaffsNewVo> getStaffDispatch(List<OrgStaffsNewVo> list, String fromLat, String fromLng) {

		// 计算出员工与目标位置的距离.
		List<BaiduPoiVo> staffPoiList = new ArrayList<BaiduPoiVo>();

		List<Long> staffIds = new ArrayList<Long>();
		for (OrgStaffsNewVo item : list) {
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

		for (OrgStaffsNewVo vo : list) {
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

	private List<HashMap> getTotalStaffs(Long serviceDate, List<Long> staffIds) {
		List<HashMap> totalStaffs = new ArrayList<HashMap>();

		if (staffIds.isEmpty())
			return totalStaffs;

		OrderDispatchSearchVo searchVo6 = new OrderDispatchSearchVo();
		searchVo6.setStaffIds(staffIds);

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
	public OrgStaffsNewVo initStaffsNew() {

		OrgStaffs staffs = orgStaffService.initOrgStaffs();

		OrgStaffsNewVo newVo = new OrgStaffsNewVo();

		BeanUtilsExp.copyPropertiesIgnoreNull(staffs, newVo);

		newVo.setLat("");
		newVo.setLat("");
		newVo.setDistanceValue(0);
		newVo.setDistanceText("");
		newVo.setDurationValue(0);
		newVo.setDurationText("");
		newVo.setOrgDistanceText("");
		newVo.setOrgDistanceValue(0);
		newVo.setTodayOrderNum(0);

		newVo.setStaffOrgName("");
		newVo.setStaffCloudOrgName("");
		newVo.setDispathStaFlag(0);
		newVo.setDispathStaStr("");
		newVo.setReason("");

		return newVo;
	}

	@Override
	public boolean doOrderDispatch(Orders order, Long serviceDate, Double serviceHour, Long staffId) {
		
		Long orderId = order.getId();
		String orderNo = order.getOrderNo();
		Long userId = order.getUserId();
		
		Users u = userService.selectByPrimaryKey(userId);
		
		OrgStaffs staff = orgStaffService.selectByPrimaryKey(staffId);
		
		OrderDispatchs orderDispatch = this.initOrderDisp(); //派工状态默认有效  1
		
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
		
		//更新服务人员与用户地址距离
				
		Long addrId = order.getAddrId();
		
		UserAddrs userAddrs = userAddrService.selectByPrimaryKey(addrId);
		
		String latitude = userAddrs.getLatitude();
		
		String longitude = userAddrs.getLongitude();
		
		int distance = newDisStaService.getLatestDistance(latitude, longitude, staffId);
		
		orderDispatch.setUserAddrDistance(distance);
		
		//工作人员相关
		orderDispatch.setStaffId(staff.getStaffId());
		orderDispatch.setStaffName(staff.getName());
		orderDispatch.setStaffMobile(staff.getMobile());
		
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
		
		//员工是否可用
		OrgStaffs staff = orgStaffService.selectByPrimaryKey(staffId);
		if (staff == null || staff.getStatus().equals((short)0) ){
			result.setStatus(Constants.ERROR_999);
			result.setMsg("指定的员工已不可用.");
			return result;
		}
		
		Orders order = orderService.selectByPrimaryKey(orderId);
		Long serviceTypeId = order.getServiceType();
		
		//技能是否可用
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

}
