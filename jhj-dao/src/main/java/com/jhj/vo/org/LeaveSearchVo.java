package com.jhj.vo.org;


/**
 *
 * @author :hulj
 * @Date : 2016年5月12日上午11:47:28
 * @Description: 
 *
 */
public class LeaveSearchVo {
	
	private Long cloudOrgId;
	
	private Long parentOrgId;
	
	private String addTimeStr;		// 请假时间 ： 如，周三 请 周五的假， 此字段为  周三
	
	private Long addStartTime;
	private Long addEndTime;
	
	private String leaveTimeStr;	// 假期时间： 周三请 周五的假，此字段为 周五
	private Long leaveTimeStamp;	// 假期时间的时间戳，数据库查询用
	
					
	private Long staffId;	//员工手机号对应的员工id, 已 保证是 在 同一个 门店 ！
	private String staffMobile;

	private Short leaveDuration;	//假期时间段 标识
	
	private Long orderServiceDateStart;	// 派工时，订单服务时间 （开始）的时间戳
	
	private Long orderServiceDateEnd;	// 派工时，订单服务时间 （结束）的时间戳
	
	
	// 服务人员排班表里，开始时间(和 leaveDate 比较),确定某个时间下,服务人员的请假状态
	private Long dispatchDateStartStr;	
	
	//格式为     日期格式不能直接比较。转换为时间戳
	private Long dispatchDateEndStr;
	
	
	

	public Long getDispatchDateStartStr() {
		return dispatchDateStartStr;
	}

	public void setDispatchDateStartStr(Long dispatchDateStartStr) {
		this.dispatchDateStartStr = dispatchDateStartStr;
	}

	public Long getDispatchDateEndStr() {
		return dispatchDateEndStr;
	}

	public void setDispatchDateEndStr(Long dispatchDateEndStr) {
		this.dispatchDateEndStr = dispatchDateEndStr;
	}

	public Long getParentOrgId() {
		return parentOrgId;
	}

	public void setParentOrgId(Long parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	public Long getOrderServiceDateStart() {
		return orderServiceDateStart;
	}

	public void setOrderServiceDateStart(Long orderServiceDateStart) {
		this.orderServiceDateStart = orderServiceDateStart;
	}

	public Long getOrderServiceDateEnd() {
		return orderServiceDateEnd;
	}

	public void setOrderServiceDateEnd(Long orderServiceDateEnd) {
		this.orderServiceDateEnd = orderServiceDateEnd;
	}

	public Long getLeaveTimeStamp() {
		return leaveTimeStamp;
	}

	public void setLeaveTimeStamp(Long leaveTimeStamp) {
		this.leaveTimeStamp = leaveTimeStamp;
	}

	public String getStaffMobile() {
		return staffMobile;
	}

	public void setStaffMobile(String staffMobile) {
		this.staffMobile = staffMobile;
	}

	public String getLeaveTimeStr() {
		return leaveTimeStr;
	}

	public Long getStaffId() {
		return staffId;
	}
	
	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
	public void setLeaveTimeStr(String leaveTimeStr) {
		this.leaveTimeStr = leaveTimeStr;
	}

	public Short getLeaveDuration() {
		return leaveDuration;
	}

	public void setLeaveDuration(Short leaveDuration) {
		this.leaveDuration = leaveDuration;
	}

	public Long getAddStartTime() {
		return addStartTime;
	}

	public void setAddStartTime(Long addStartTime) {
		this.addStartTime = addStartTime;
	}

	public Long getAddEndTime() {
		return addEndTime;
	}

	public void setAddEndTime(Long addEndTime) {
		this.addEndTime = addEndTime;
	}

	

	public String getAddTimeStr() {
		return addTimeStr;
	}

	public void setAddTimeStr(String addTimeStr) {
		this.addTimeStr = addTimeStr;
	}

	public Long getCloudOrgId() {
		return cloudOrgId;
	}

	public void setCloudOrgId(Long cloudOrgId) {
		this.cloudOrgId = cloudOrgId;
	}
	
}
