package com.jhj.vo.order.newDispatch;

/**
 *
 * @author :hulj
 * @Date : 2016年3月21日下午5:20:17
 * @Description: 
 *		
 *		jhj2.1  运营平台 派工, 可用 服务人员 VO
 */
public class DisStaffWithUserVo {
	
	private Long properStaffId;				//该可选 派工人员 的 id
	
	private String distanceToUser;		//该人员 现在 距 用户 的地址距离
		
	private String timeToUser;			// 该人员现在 到用户 地址 的 时间
	
	private Long todayOrderNum;			// 该人员当天的 派工单量
	
	
	private int distanceValue;		// 距离 数字 ，更新派工表 ，距离 属性时需要
	
	public int getDistanceValue() {
		return distanceValue;
	}

	public void setDistanceValue(int distanceValue) {
		this.distanceValue = distanceValue;
	}


	public Long getProperStaffId() {
		return properStaffId;
	}

	public void setProperStaffId(Long properStaffId) {
		this.properStaffId = properStaffId;
	}

	public String getDistanceToUser() {
		return distanceToUser;
	}

	public void setDistanceToUser(String distanceToUser) {
		this.distanceToUser = distanceToUser;
	}

	public String getTimeToUser() {
		return timeToUser;
	}

	public void setTimeToUser(String timeToUser) {
		this.timeToUser = timeToUser;
	}

	public Long getTodayOrderNum() {
		return todayOrderNum;
	}

	public void setTodayOrderNum(Long todayOrderNum) {
		this.todayOrderNum = todayOrderNum;
	}

}
