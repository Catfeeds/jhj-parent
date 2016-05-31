package com.jhj.vo.order.newDispatch;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;


/**
 *
 * @author :hulj
 * @Date : 2016年5月30日下午12:43:59
 * @Description: 
 *		排班 和 请假 vo
 *	  
 *		{ 某人  : [xx号 : {xx点：做饭},{xx点 : 保洁}  ]	 }	
 *
 */
public class OaStaffDisAndLeaveVo {
	
	//员工姓名
	private String staffName;
	
	//员工id
	private Long staffId;
	
	// 日期 和 事件的 对应 关系  {xx月xx号: [x点~x点 : 做饭] }
	private List<TimeEventVo> timeEventList;

	public List<TimeEventVo> getTimeEventList() {
		return timeEventList;
	}

	public void setTimeEventList(List<TimeEventVo> timeEventList) {
		this.timeEventList = timeEventList;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
	
}
