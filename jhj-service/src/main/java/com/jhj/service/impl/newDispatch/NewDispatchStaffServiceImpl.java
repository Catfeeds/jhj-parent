package com.jhj.service.impl.newDispatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserTrailReal;
import com.jhj.service.bs.OrgStaffBlackService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.newDispatch.NewDispatchStaffService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserTrailRealService;
import com.jhj.vo.order.OrgStaffsNewVo;
import com.jhj.vo.order.newDispatch.DisStaffWithUserVo;
import com.jhj.vo.order.newDispatch.NewAutoDisStaffVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.baidu.BaiduMapUtil;
import com.meijia.utils.baidu.BaiduPoiVo;

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
	private OrderDispatchsService dispatchService;
	
	@Autowired
	private OrgStaffsService staffService;
	
	@Autowired
	private OrgStaffBlackService blackService;
	
	@Autowired
	private UserTrailRealService trailRealService;
	
	/*
	 *  助理类订单,手动派工，返回基础派工 逻辑 支持的  员工 id 集合
	 * 
	 */
	@Override
	public List<Long> autoDispatchForAmOrder(String lat,String lon,Long serviceTye) {
		
		
		//返回  所有云店的 所有可用 服务人员
		List<Long> properStaIdList = new ArrayList<Long>();
		
		//符合时间、距离的 云店
		List<Orgs> orgList = getMatchOrgId(lat, lon);
			
		for (int i = 0; i < orgList.size(); i++) {
			
			List<Long> staffIdList = new ArrayList<Long>();
			
			Orgs org = orgList.get(i);
			
			Long orgId = org.getOrgId();
			// 根据 服务类型 和 门店id, 找到  符合 服务要求 的 服务人员
			staffIdList = staffService.getProperStaffByOrgAndServiceType(orgId, serviceTye);
			
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
		
		Orders order = orderService.selectbyOrderId(orderId);
		
		Long addrId = order.getAddrId();
		
		UserAddrs addrs = userAddrService.selectByPrimaryKey(addrId);
		
		List<Orgs> orgList = getMatchOrgId(addrs.getLatitude(),addrs.getLongitude());
		
		Long serviceType = order.getServiceType();
		
		//所有符合 距离 要求的  云店  下    所有在订单 时间内已经有 派工的员工
		List<Long> dispatchIdList = new ArrayList<Long>();
		
		//所有符合 距离 要求的  云店  下  	所有符合 基本派工条件的 员工
		List<Long> staffIdList = new ArrayList<Long>();
		
		if(orgList.size() > 0){
			for (int i = 0; i < orgList.size(); i++) {
				Orgs org = orgList.get(i);
				Long orgId = org.getOrgId();
				
				// 根据 服务类型 和 门店id, 找到  '该云店下 '  符合   '服务类型' 要求 的 服务人员
				List<Long> staffIds = staffService.getProperStaffByOrgAndServiceType(orgId, serviceType);
				
				//服务时间内 已 排班的 阿姨
				List<OrderDispatchs> disList = dispatchService.selectEnableStaffNow(orgId, serviceDate, serviceDate+order.getServiceHour()*3600);
				
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
//		staffList = staffService.selectByids(staffIdList);
		
		
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
		List<Orgs> cloudOrgList = orgService.selectCloudOrgs();
		
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
			List<BaiduPoiVo> destList = BaiduMapUtil.getMapRouteMatrix(fromLat, fromLng, orgAddrList);
			List<BaiduPoiVo> voList = BaiduMapUtil.getMinDest(destList, 10000, 3600);
			
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
		List<UserTrailReal> list = trailRealService.selectLatestPositionForUser(staIdList);
		
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
			List<BaiduPoiVo> destList = BaiduMapUtil.getMapRouteMatrix(fromLat, fromLng, orgAddrList);
			
			Collections.sort(destList, new Comparator<BaiduPoiVo>() {
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
				Long numTodayOrder = dispatchService.getTodayOrderNumForTheSta(staffId);
				
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
		
		Orders orders = orderService.selectbyOrderId(orderId);
		
		Long addrId = orders.getAddrId();
		
		UserAddrs userAddr = userAddrService.selectByPrimaryKey(addrId);
		
		String latitude = userAddr.getLatitude();
		String longitude = userAddr.getLongitude();
		
//		List<OrderDispatchs> list = dispatchService.selectEnableStaffNow(orders.getOrgId(), serviceDate, serviceDate+3*3600);
		
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
		newVo.setTodayOrderNum(0L);
		
		newVo.setStaffOrgName("");
		newVo.setStaffCloudOrgName("");
		
		return newVo;
	}
}
