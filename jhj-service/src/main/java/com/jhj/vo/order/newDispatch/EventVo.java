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
/**
 * @author hulj
 *
 */
public class EventVo {
	
	private String dateDuration;
	
	private String eventName;
	
	private Long serviceTime;
	
	private String orderNo;
	
	private Short orderType;

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

	public Long getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(Long serviceTime) {
		this.serviceTime = serviceTime;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Short getOrderType() {
		return orderType;
	}

	public void setOrderType(Short orderType) {
		this.orderType = orderType;
	}

	
}
