package com.jhj.service.order;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.Users;
import com.jhj.vo.order.OrgStaffsNewVo;

/**
 * @description：订单派工的接口----jhj2.0
 * @author： kerryg
 * @date:2016年1月13日 
 */
public interface DispatchStaffFromOrderService {
	
	//获得符合条件(钟点工)订单服务人员列表--jhj2.0
	public List<OrgStaffsNewVo> getNewBestStaffForHour(Long startTime,Long endTime,Long userAddrId,Long orderId);
	//获得符合条件 (助理 )订单服务人员列表--jhj2.0
	public List<OrgStaffsNewVo> getNewBestStaffForAm(Long startTime,Long endTime,String poiLongitude,String poiLatitude,Long orderId);
	//获得符合条件 (配送)订单服务人员列表--jhj2.0
	public List<OrgStaffsNewVo> getNewBestStaffForDel(Long startTime,Long endTime,Long userAddrId,Long orderId);
	//随机分配服务人员 and 插入日志
	public void disRadomStaff(List<OrgStaffs> orgStaffs,Orders order,Users user);
	//根据staffId分配服务人员 and 插入日志
	public void disStaff(OrgStaffs orgStaffs,Orders order,Users user);
	
	public void disStaff(OrgStaffs orgStaffs,Orders order,Users user,String poiLongitude,String poiLatitude,String pickAddrName,String pickAddr,String userAddrDistance);
	
	public void pushToStaff(Long staffId,String isShow,String action,
		Long orderId,String remindTitle,String remindContent);
	
	
	
}
