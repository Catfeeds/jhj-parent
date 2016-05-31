package com.jhj.vo.order.newDispatch;

import java.util.List;

/**
 *
 * @author :hulj
 * @Date : 2016年5月30日下午3:06:29
 * @Description: 
 *		
 *
 *	 [xx号 : {xx点：做饭}  ]	
 */	
public class TimeEventVo {
	
	//格式 'yyyy-MM-dd'
	private String timeStr;
	
	private List<EventVo> eventList;

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public List<EventVo> getEventList() {
		return eventList;
	}

	public void setEventList(List<EventVo> eventList) {
		this.eventList = eventList;
	}
	
}
