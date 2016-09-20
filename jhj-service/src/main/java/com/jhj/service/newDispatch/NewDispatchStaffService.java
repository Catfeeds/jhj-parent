package com.jhj.service.newDispatch;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.vo.order.OrgStaffsNewVo;
import com.jhj.vo.order.newDispatch.DisStaffWithUserVo;
import com.jhj.vo.order.newDispatch.NewAutoDisStaffVo;

/**
 *
 * @author :hulj
 * @Date : 2016年3月17日上午10:33:26
 * @Description: 
 *		
 *		jhj2.1  用来处理 派工的 service
 *			
 *			可以供 各种流程的 派工使用
 *	
 */
public interface NewDispatchStaffService {
	
	
	/*
	 *  基础保洁类订单 自动 派工, 返回符合基本派工逻辑的  服务人员 集合
	 */
	List<Long>  autoDispatchForBaseOrder(Long orderId,Long serviceDate);
	
	/*
	 *  助理 类  订单 派工, 返回符合基本派工条件的 服务人员集合
	 */
	List<Long>  autoDispatchForAmOrder(String lat,String lon,Long serviceTye);
	
	/*
	 *  运营平台 , 得到 符合 基本 派工 条件 的  服务人员之后，
	 *  
	 *  	列出  服务人员 当天订单数、当前距 服务地址的 距离、到达时间等 信息，供 手动派工 参考
	 */
	List<OrgStaffsNewVo> getTheNearestStaff(String fromLat,String fromLng,List<Long> staIdList);
	
		
	//被选中的 服务人员 与  用户 地址的 距离
	int getLatestDistance(String userLat,String userLon, Long staffId);
	
	/*
	 * 调整基础保洁类订单, 
	 * 		
	 * 	注*： 默认  基础保洁类 订单, 不能修改 服务地址, 可以修改 服务时间
	 * 	 
	  	 故,根据 新的 服务时间, 加载 可用派工
	 * 
	 */
	List<OrgStaffsNewVo> getAbleStaffList(Long orderId,Long serviceDate);
	
	OrgStaffsNewVo initStaffsNew();
		
	/*
	 * 2016年5月13日15:39:19
	 *  
	 * 对于 深度养护 订单, 需要 排除  在 订单 服务时间内, 请假的 员工
	 */
	 List<Long> getLeaveStaffForDeepOrder(Long orderId,Long serviceType);

	List<OrgStaffsNewVo> getAbleStaffListByCloudOrg(Long orderId, Long parentId, Long cloudId);
	
}
