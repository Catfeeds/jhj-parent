package com.jhj.vo.order.newDispatch;

/**
 *
 * @author :hulj
 * @Date : 2016年5月30日下午3:05:31
 * @Description: 
 *	
 *		具体的事件
 *			
 *		[x点~x点 : 做饭]
 *
 */
public class EventVo {
	
	private String dateDuration;
	
	private String eventName;

	public String getDateDuration() {
		return dateDuration;
	}

	public void setDateDuration(String dateDuration) {
		this.dateDuration = dateDuration;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
}
