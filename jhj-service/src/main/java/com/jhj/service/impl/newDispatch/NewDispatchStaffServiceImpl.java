package com.jhj.service.impl.newDispatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.dao.bs.OrgStaffLeaveMapper;
import com.jhj.po.model.bs.OrgStaffLeave;
import com.jhj.po.model.bs.OrgStaffSkill;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserTrailReal;
import com.jhj.service.bs.OrgStaffBlackService;
import com.jhj.service.bs.OrgStaffLeaveService;
import com.jhj.service.bs.OrgStaffSkillService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.newDispatch.NewDispatchStaffService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderHourAddService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserTrailRealService;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrgStaffsNewVo;
import com.jhj.vo.org.LeaveSearchVo;
import com.jhj.vo.org.OrgSearchVo;
import com.jhj.vo.staff.OrgStaffSkillSearchVo;
import com.jhj.vo.staff.StaffSearchVo;
import com.jhj.vo.user.UserTrailSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.baidu.BaiduPoiVo;
import com.meijia.utils.baidu.MapPoiUtil;

/**
 *
 * @author :hulj
 * @Date : 2016年3月17日上午10:59:58
 * @Description: 
 *		
 *		派工逻辑 的 实现 service 
 */
@Service
public class NewDispatchStaffServiceImpl implements NewDispatchStaffService {
	
	
	@Autowired
	private UserAddrsService userAddrService;
	
	@Autowired
	private OrgsService orgService;
	
	@Autowired
	private OrdersService orderService;
	
	@Autowired
	private OrderDispatchsService orderDispatchsService;
	
	@Autowired
	private OrgStaffsService staffService;
	
	@Autowired
	private OrgStaffBlackService blackService;
	
	@Autowired
	private UserTrailRealService trailRealService;
	
	@Autowired
	private OrderHourAddService orderHourAddService;
	
	@Autowired
	private OrgStaffLeaveService orgStaffLeaveService;
	
	@Autowired
	private PartnerServiceTypeService partService;
	
	@Autowired
	private OrgStaffSkillService orgStaffSkillService;
	
	/*
	 *  助理类订单,手动派工，返回基础派工 逻辑 支持的  员工 id 集合
	 * 
	 */
	@Override
	public List<Long> autoDispatchForAmOrder(String lat,String lon,Long serviceTypeId) {
		
		
		//返回  所有云店的 所有可用 服务人员
		List<Long> properStaIdList = new ArrayList<Long>();
		
		//符合时间、距离的 云店
		List<Orgs> orgList = getMatchOrgId(lat, lon);
			
		for (int i = 0; i < orgList.size(); i++) {
			
			List<Long> staffIdList = new ArrayList<Long>();
			
			Orgs org = orgList.get(i);
			
			Long orgId = org.getOrgId();
			// 根据 服务类型 和 门店id, 找到  符合 服务要求 的 服务人员
			
			OrgStaffSkillSearchVo searchVo = new OrgStaffSkillSearchVo();
			searchVo.setOrgId(orgId);
			searchVo.setServiceTypeId(serviceTypeId);
			List<OrgStaffSkill> skillList = orgStaffSkillService.selectBySearchVo(searchVo);
			
			if (!skillList.isEmpty()) {
				for (OrgStaffSkill s : skillList) {
					if (!staffIdList.contains(s.getStaffId())) {
						staffIdList.add(s.getStaffId());
					}
				}
			}

			//黑名单
			List<Long> blackIdList = blackService.selectAllBadRateStaffId();
			
			//最终 符合条件的 服务人员
			staffIdList.removeAll(blackIdList);
			
			properStaIdList.addAll(staffIdList);
		}
			
		
		return properStaIdList;
	}
	
	/**
	 *  基础保洁类 订单的 派工, 
	 * 
	 */
	@Override
	public List<Long> autoDispatchForBaseOrder(Long orderId,Long serviceDate) {
		
		Orders order = orderService.selectByPrimaryKey(orderId);
		
		Long addrId = order.getAddrId();
		
		UserAddrs addrs = userAddrService.selectByPrimaryKey(addrId);
		
		List<Orgs> orgList = getMatchOrgId(addrs.getLatitude(),addrs.getLongitude());
		
		Long serviceTypeId = order.getServiceType();
		
		//所有符合 距离 要求的  云店  下    所有在订单 时间内已经有 派工的员工
		List<Long> dispatchIdList = new ArrayList<Long>();
		
		//所有符合 距离 要求的  云店  下  	所有符合 基本派工条件的 员工
		List<Long> staffIdList = new ArrayList<Long>();
		
		if(orgList.size() > 0){
			for (int i = 0; i < orgList.size(); i++) {
				Orgs org = orgList.get(i);
				Long orgId = org.getOrgId();
				
				List<Long> staffIds = new ArrayList<Long>();
				// 根据 服务类型 和 门店id, 找到  '该云店下 '  符合   '服务类型' 要求 的 服务人员
				OrgStaffSkillSearchVo searchVo = new OrgStaffSkillSearchVo();
				searchVo.setOrgId(orgId);
				searchVo.setServiceTypeId(serviceTypeId);
				List<OrgStaffSkill> skillList = orgStaffSkillService.selectBySearchVo(searchVo);
				
				
				if (!skillList.isEmpty()) {
					for (OrgStaffSkill s : skillList) {
						if (!staffIds.contains(s.getStaffId())) {
							staffIds.add(s.getStaffId());
						}
					}
				}

				//服务时间内 已 排班的 阿姨
				
				/*
				 *  2016年5月16日14:39:37  预留 2小时， 将 服务 开始 时间  提前 2小时，用作预留 时间
				 */
				OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
				searchVo1.setOrgId(orgId);
				searchVo1.setDispatchStatus((short) 1);
				searchVo1.setStartServiceTime(serviceDate-3600*2);
				searchVo1.setEndServiceTime(serviceDate+order.getServiceHour()*3600);
				List<OrderDispatchs> disList = orderDispatchsService.selectByMatchTime(searchVo1);
				
				for (OrderDispatchs orderDispatchs : disList) {
					dispatchIdList.add(orderDispatchs.getStaffId());
				}
				
				staffIdList.addAll(staffIds);
			}
		}
		
		//黑名单
		List<Long> blackIdList = blackService.selectAllBadRateStaffId();
		
		//最终 符合基本 派工 条件的 服务人员
		staffIdList.removeAll(blackIdList);
		staffIdList.removeAll(dispatchIdList);
		
		
		/*
		 *  2016年3月31日18:36:21  对于 厨娘烧饭有 附加服务。 还需过滤 标签匹配
		 *  
		 */
		
		//匹配技能标签		TODO  匹配时，有个   清洁用品。写死了  id
		staffIdList = orderHourAddService.getMatchTagStaffIds(orderId,staffIdList);
		
		
		/* 
		 * 2016年5月13日14:42:20
		 *	 	排除请假的 员工 （在假期内）
		 *	
		 *	对于 基础 保洁类 订单， 排除 请假时间 和 订单 服务时间 有交集  的员工
		 *	
		 */
		
		LeaveSearchVo searchVo = new LeaveSearchVo();
		
		Short serviceHour = order.getServiceHour();
		
		Long orderServiceDateEnd = serviceDate + serviceHour*3600;
		
		//订单服务开始时间
		searchVo.setServiceStartTime(serviceDate);
		//订单服务结束时间
		searchVo.setServiceEndTime(orderServiceDateEnd);
		
		// 服务时间内  ，同时也在  假期内的 员工
		List<OrgStaffLeave> leaveList = orgStaffLeaveService.selectBySearchVo(searchVo);
		
		List<Long> leaveStaffIdList = new ArrayList<Long>();
		
		for (OrgStaffLeave orgStaffLeave : leaveList) {
			leaveStaffIdList.add(orgStaffLeave.getStaffId());
		}
		
		//排除 假期内员工
		staffIdList.removeAll(leaveStaffIdList);
		
		return staffIdList;
	}
	
	
	/**
	 * 
	 *  @Title: getMatchOrgId
	  * @Description: 
	  * 	对于 助理、基础保洁的 手动派工，参数是  “实时” 选择的 用户 坐标
	  * 	
	  *     对于 保洁的自动派工, 参数是 用户的 addrId
	  *     
	  *     综合：传递 坐标
	  *     
	  * @return List<Orgs>   符合 10km、 1h 范围的 云店 
	  * @throws
	 */
	public List<Orgs> getMatchOrgId(String fromLat,String fromLng) {
		
		List<Orgs> matchOrgList = new ArrayList<Orgs>();
		
		//目标地点：所有云店的集合 
		OrgSearchVo searchVo = new OrgSearchVo();
		searchVo.setIsCloud((short) 1);
		searchVo.setOrgStatus((short) 1);
		List<Orgs> cloudOrgList = orgService.selectBySearchVo(searchVo);
		
		if (cloudOrgList.isEmpty()) return matchOrgList;
		
		List<BaiduPoiVo> orgAddrList = new ArrayList<BaiduPoiVo>();
		for (Orgs org : cloudOrgList) {
			BaiduPoiVo baiduPoiVo = new BaiduPoiVo();
			
			baiduPoiVo.setLat(org.getPoiLatitude());
			baiduPoiVo.setLng(org.getPoiLongitude());
			baiduPoiVo.setName(org.getOrgName());
			
			orgAddrList.add(baiduPoiVo);
		}
		
		Orgs item = null;
		try {
			List<BaiduPoiVo> destList = MapPoiUtil.getMapRouteMatrix(fromLat, fromLng, orgAddrList);
			/**
			 * 
			 *  2016年6月1日17:46:42 
			 *  
			 *   临时决定调换成  20Km , 3小时
			 */
			
			List<BaiduPoiVo> voList = MapPoiUtil.getMinDest(destList, Constants.maxDistance);
			
			for (int i =0; i < cloudOrgList.size(); i++) {
				item = cloudOrgList.get(i);
				
				for (BaiduPoiVo baiduPoiVo : voList) {
					if (baiduPoiVo.getLat().equals(item.getPoiLatitude()) &&
							baiduPoiVo.getLng().equals(item.getPoiLongitude())) {
						matchOrgList.add(item);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return matchOrgList;
	}
	
	/**
	 * 
	 *  @Title: getTheNearestStaff
	  * @Description: 
	  * 		助理订单 手动派工时, 
	  * 			
	  * 		得到 “可选  服务人员 ” 实时 位置距离 服务地址的 时间 、该服务人员当天的 派工 量	
	  * 
	  * 		可选服务人员,符合 基本派工条件（距离、黑名单。。）
	  * 
	  * @param fromLat		用户地址坐标
	  * @param fromLng
	  * @param staIdList    可用 服务人员 Id
	  * @throws
	 */
	@Override
	public  List<OrgStaffsNewVo> getTheNearestStaff(String fromLat,String fromLng,List<Long> staIdList){
		
		List<OrgStaffsNewVo> staWithUserList = new ArrayList<OrgStaffsNewVo>();
		
		if(staIdList.size() == 0){
			staIdList.add(0, 0L);
		}
		
		// 参数中传入服务人员     的 最新 位置   <服务人员：该服务人员的最新位置。。VO>
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
			
			for (int i =0; i < list.size(); i++) {
				item = list.get(i);
				
				/**
				 *  2016年3月24日14:37:20  
				 *  
				 * 	 可能会有  该 服务人员 不存在  位置信息的情况,
				 * 		
				 * 	但是 需要 显示 该 服务人员 可用！！
				 */
				Long staffId = item.getUserId();
				
				// 该 服务人员 当天 的 派单 数量
				int numTodayOrder = orderDispatchsService.totalStaffTodayOrders(staffId);
				
				//派工页面 服务人员 相关 信息 VO
				OrgStaffsNewVo staffsNewVo = initStaffsNew();
				
				OrgStaffs staffs = staffService.selectByPrimaryKey(staffId);
				
				BeanUtilsExp.copyPropertiesIgnoreNull(staffs, staffsNewVo);
				
				staffsNewVo.setStaffId(item.getUserId());
				staffsNewVo.setTodayOrderNum(numTodayOrder);
				
				//服务人员所在 云店 id
				Long orgId = staffs.getOrgId();
				
				//服务人员所在 门店id （该云店 的 上级 门店）
				Long parentOrgId = staffs.getParentOrgId();
				
				Orgs cloudOrg = orgService.selectByPrimaryKey(orgId);
				
				if(cloudOrg !=null){
					staffsNewVo.setStaffCloudOrgName(cloudOrg.getOrgName());
				}
				
				Orgs orgs = orgService.selectByPrimaryKey(parentOrgId);
				
				if(orgs !=null){
					staffsNewVo.setStaffOrgName(orgs.getOrgName());
				}
				
				// 设置 服务 人员 特色信息, 供派工参考
				for (BaiduPoiVo baiduPoiVo : destList) {
					if (baiduPoiVo.getLat().equals(item.getLat()) &&
							baiduPoiVo.getLng().equals(item.getLng())) {
						
						// 合适的服务人员的最新位置    与  服务地址 的 距离、时间等信息
						staffsNewVo.setDistanceText(baiduPoiVo.getDistanceText());
						staffsNewVo.setDurationText(baiduPoiVo.getDurationText());
						staffsNewVo.setDistanceValue(baiduPoiVo.getDistanceValue());
					}
				}
				
				staWithUserList.add(staffsNewVo);
			}
			
			Collections.sort(staWithUserList, new Comparator<OrgStaffsNewVo>() {
			    @Override
				public int compare(OrgStaffsNewVo s1, OrgStaffsNewVo s2) {
			        return Integer.valueOf(s1.getDistanceValue()).compareTo(s2.getDistanceValue());
			    }
			}); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return staWithUserList;
	}
	
	
	
	/*
	 *  更新  用户与  “派工后选中”的 服务人员 的  最新距离
	 */
	@Override
	public int getLatestDistance(String userLat,String userLon, Long staffId){
		
		List<Long> staIdList = new ArrayList<Long>();
		staIdList.add(staffId);
		
		List<OrgStaffsNewVo> list = getTheNearestStaff(userLat, userLon, staIdList);
		
		int distance = 0;
		
		if(list.size() > 0 ){
			for (OrgStaffsNewVo orgStaffsNewVo : list) {
				distance = orgStaffsNewVo.getDistanceValue();
			}
		}
		
		return distance;
	}
	
	
	
	/*
	 * 调整基础保洁类订单, 
	 * 		
	 * 	注*： 默认  基础保洁类 订单, 不能修改 服务地址, 可以修改 服务时间
	 * 	 
	  	 故,根据 新的 服务时间, 加载 可用派工
	 * 
	 */
	@Override
	public List<OrgStaffsNewVo> getAbleStaffList(Long orderId, Long serviceDate) {
		
		Orders orders = orderService.selectByPrimaryKey(orderId);
		
		Long addrId = orders.getAddrId();
		
		UserAddrs userAddr = userAddrService.selectByPrimaryKey(addrId);
		
		String latitude = userAddr.getLatitude();
		String longitude = userAddr.getLongitude();
		
//		List<OrderDispatchs> list = orderDispatchsService.selectEnableStaffNow(orders.getOrgId(), serviceDate, serviceDate+3*3600);
		
		//符合基本派工逻辑的  员工
		List<Long> staIdList = autoDispatchForBaseOrder(orderId, serviceDate);
		
		//转换为 VO	
		List<OrgStaffsNewVo> list = getTheNearestStaff(latitude, longitude, staIdList);
		
		return list;
	}
	
	@Override
	public OrgStaffsNewVo initStaffsNew(){
		
		OrgStaffs staffs = staffService.initOrgStaffs();
		
		OrgStaffsNewVo newVo = new OrgStaffsNewVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(staffs, newVo);
		
		newVo.setLat("");
		newVo.setLat("");
		newVo.setDistanceValue(0);
		newVo.setDistanceText("");
		newVo.setDurationValue(0);
		newVo.setDurationText("");
		newVo.setTodayOrderNum(0);
		
		newVo.setStaffOrgName("");
		newVo.setStaffCloudOrgName("");
		newVo.setDispathStaFlag(0);
		newVo.setDispathStaStr("");
		newVo.setReason("");
		
		return newVo;
	}
	
	
	/*
	 * 2016年5月4日17:08:50
	 * 
	 * 对于 修改 服务时间也 没有 可用派工的  钟点工订单，
	 * 
	 */
	@Override
	public List<OrgStaffsNewVo> getAbleStaffListByCloudOrg(Long orderId, Long parentId, Long cloudId) {
		
		StaffSearchVo searchVo = new StaffSearchVo();
		searchVo.setParentId(parentId);
		
		if (cloudId > 0L) searchVo.setOrgId(cloudId);
		
		List<OrgStaffs> staffList = staffService.selectBySearchVo(searchVo);
		
		List<Long> staffIdList = new ArrayList<Long>();
		
		for (OrgStaffs orgStaffs : staffList) {
			
			staffIdList.add(orgStaffs.getStaffId());
		}
		
		Orders orders = orderService.selectByPrimaryKey(orderId);
		
		Long addrId = orders.getAddrId();
		
		UserAddrs userAddr = userAddrService.selectByPrimaryKey(addrId);
		
		String latitude = userAddr.getLatitude();
		String longitude = userAddr.getLongitude();
		
		
		/*   根据云店 加载 可用派工时，
		 * 
		 *   不再 走 自动派工逻辑。而是  直接  加载 对应云店的工作人员 派工情况即可
		 */
		
		//转换为 VO	
	    List<OrgStaffsNewVo> list = getTheNearestStaff(latitude, longitude, staffIdList);
		
		return list;
	}
	
	
	
	@Override
	public List<Long> getLeaveStaffForDeepOrder(Long orderId, Long serviceType) {
		
		Orders order = orderService.selectByPrimaryKey(orderId);
		
		// 请假的 服务人员
		List<Long> leaveStaffIdList = new ArrayList<Long>();
		
		PartnerServiceType partner = partService.selectByPrimaryKey(serviceType);
		Long parentId = partner.getParentId();
		
		// 如果当前 助理类 订单 的 服务类型 属于  深度养护类的  子服务，，需要排除请假周期内的 员工
 		if(parentId == 26){
			
 			LeaveSearchVo searchVo = new LeaveSearchVo();
 			
 			Short serviceHour = order.getServiceHour();
 			
 			Long serviceDate = order.getServiceDate();
 			
 			Long orderServiceDateEnd = serviceDate + serviceHour*3600;
 			
 			//订单服务开始时间
 			searchVo.setServiceStartTime(serviceDate);
 			//订单服务结束时间
 			searchVo.setServiceEndTime(orderServiceDateEnd);
 			
 			// 服务时间内  ，同时也在  假期内的 员工
 			List<OrgStaffLeave> leaveList = orgStaffLeaveService.selectBySearchVo(searchVo);
 			
 			for (OrgStaffLeave orgStaffLeave : leaveList) {
 				leaveStaffIdList.add(orgStaffLeave.getStaffId());
 			}
		}
		
		return leaveStaffIdList;
	}
	
}
