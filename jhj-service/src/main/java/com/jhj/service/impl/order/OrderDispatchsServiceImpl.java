package com.jhj.service.impl.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.common.Constants;
import com.jhj.po.dao.bs.OrgStaffLeaveMapper;
import com.jhj.po.dao.order.OrderDispatchsMapper;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffLeave;
import com.jhj.po.model.bs.OrgStaffSkill;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.service.bs.OrgStaffBlackService;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffLeaveService;
import com.jhj.service.bs.OrgStaffSkillService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.vo.bs.OrgDispatchPoiVo;
import com.jhj.vo.dispatch.StaffDispatchVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.org.LeaveSearchVo;
import com.jhj.vo.org.OrgSearchVo;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;
import com.jhj.vo.staff.OrgStaffSkillSearchVo;
import com.jhj.vo.staff.StaffSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.baidu.BaiduPoiVo;
import com.meijia.utils.baidu.MapPoiUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年7月20日下午5:18:49
 * @Description: 
 *
 */
@Service
public class OrderDispatchsServiceImpl implements OrderDispatchsService {
	
	@Autowired
	private OrderDispatchsMapper orderDispatchsMapper;
	
	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;
	
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
		dispatchs.setDispatchStatus(Constants.ORDER_DIS_ENABLE); //??派工状态 默认有效吧？
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
	
	/**
	 *  订单类自动派工 
	 */
	@Override
	public List<Long> autoDispatch(Long orderId,Long serviceDate, Double serviceHour) {
		
		//所有符合 距离 要求的  云店  下  	所有符合 基本派工条件的 员工
		List<Long> staffList = new ArrayList<Long>();
		
		Orders order = orderService.selectByPrimaryKey(orderId);
		
		Long serviceTypeId = order.getServiceType();
		
		Long addrId = order.getAddrId();
		
		UserAddrs addrs = userAddrService.selectByPrimaryKey(addrId);
		
		List<OrgDispatchPoiVo> orgList = getMatchOrgs(addrs.getLatitude(),addrs.getLongitude());
		
		if (orgList.isEmpty()) return staffList;
		
		//所有符合 距离 要求的  云店  下    所有在订单 时间内已经有 派工的员工
		List<Long> dispatchedList = new ArrayList<Long>();
		
		List<Long> staffIds = new ArrayList<Long>();
		
		for (int i = 0; i < orgList.size(); i++) {
			OrgDispatchPoiVo org = orgList.get(i);
			Long orgId = org.getOrgId();
			
			//----1. 找出所有的符合此技能的员工 |
			staffIds = new ArrayList<Long>();
			OrgStaffSkillSearchVo searchVo = new OrgStaffSkillSearchVo();
			searchVo.setOrgId(orgId);
			searchVo.setServiceTypeId(serviceTypeId);
			List<OrgStaffSkill> skillList = orgStaffSkillService.selectBySearchVo(searchVo);

			if (!skillList.isEmpty()) {
				for (OrgStaffSkill s : skillList) {
					if (!staffIds.contains(s.getStaffId())) staffIds.add(s.getStaffId());
				}
			}
			
			if (staffIds.isEmpty()) continue;

			
			
			//---2.服务时间内 已 排班的 阿姨, 时间跨度为 服务开始前2小时 -  服务结束时间
			Long startServiceTime = serviceDate - 3600 * 2;
			Long endServiceTime = (long) (serviceDate + serviceHour * 3600);
			
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
				
			if (staffIds.isEmpty()) continue;
			
			//-----------3.排除掉在黑名单中的服务人员
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
			
			if (staffIds.isEmpty()) continue;
			
			//---------4.排除请假的员工.
			LeaveSearchVo searchVo3 = new LeaveSearchVo();
			
			searchVo3.setOrgId(orgId);
			Long orderServiceDateEnd = (long) (serviceDate + serviceHour * 3600);
		
			//订单服务开始时间
			searchVo3.setLeaveStartTime(serviceDate);
			//订单服务结束时间
			searchVo3.setLeaveEndTime(orderServiceDateEnd);
		
			// 服务时间内  ，同时也在  假期内的 员工
			List<OrgStaffLeave> leaveList = orgStaffLeaveService.selectBySearchVo(searchVo3);
		
			if (!leaveList.isEmpty()) {
				for (OrgStaffLeave ol : leaveList) {
					if (staffIds.contains(ol.getStaffId())) {
						staffIds.remove(ol.getStaffId());
					}
				}
			}
			
			
			//如果该云店有满足员工，则直接跳出循环，返回信息.
			if (!staffIds.isEmpty()) break;
		}
		
//		if (!staffIds.isEmpty()) {
//			StaffSearchVo searchVo5 = new StaffSearchVo();
//			searchVo5.setStaffIds(staffIds);
//			staffList = orgStaffService.selectBySearchVo(searchVo5);
//		}
		
		return staffIds;
	}
	
	/**
	 * 
	 *  @Title: getMatchOrgId
	  * @Description: 
	  * 	找出符合 指定距离的云店
	  * @throws
	 */
	public List<OrgDispatchPoiVo> getMatchOrgs(String fromLat,String fromLng) {
		
		List<OrgDispatchPoiVo> result = new ArrayList<OrgDispatchPoiVo>();
		
		//目标地点：所有云店的集合 
		OrgSearchVo searchVo = new OrgSearchVo();
		searchVo.setIsCloud((short) 1);
		searchVo.setOrgStatus((short) 1);
		List<Orgs> cloudOrgList = orgService.selectBySearchVo(searchVo);
		
		if (cloudOrgList.isEmpty()) return result;
		
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
			
			List<BaiduPoiVo> voList = MapPoiUtil.getMinDest(destList);
			
			for (int i =0; i < cloudOrgList.size(); i++) {
				item = cloudOrgList.get(i);
				
				OrgDispatchPoiVo vo = new OrgDispatchPoiVo();
				BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);
				
				
				for (BaiduPoiVo baiduPoiVo : voList) {
					if (baiduPoiVo.getLat().equals(item.getPoiLatitude()) &&
							baiduPoiVo.getLng().equals(item.getPoiLongitude())) {
						
						BeanUtilsExp.copyPropertiesIgnoreNull(baiduPoiVo, vo);
						result.add(vo);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//进行排序，根据距离大小来正序.
		if(result.size() > 0){
			Collections.sort(result, new Comparator<OrgDispatchPoiVo>() {
				@Override
				public int compare(OrgDispatchPoiVo s1, OrgDispatchPoiVo s2) {
					return Integer.valueOf(s1.getDistanceValue()).compareTo(s2.getDistanceValue());
				}
			});
		}
		
		
		return result;
	}
	
}
