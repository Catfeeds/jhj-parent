package com.jhj.oa.vo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月17日下午12:19:45
 * @Description: 
 *		
 *		日历控件--阿姨排班 ，VO
 */
public class OrderDisVo {
	
	private Long staffId;			// 阿姨 Id
	private String orderNo;			//订单号
	private Long serviceDateStart; //服务开始时间
	private Long serviceDateEnd;	//服务结束时间
	
	private String orderStatusName;	//订单状态名称
	
	private String orderTypeName;	//订单类型名称

	/**
	 * @return the staffId
	 */
	public Long getStaffId() {
		return staffId;
	}

	/**
	 * @param staffId the staffId to set
	 */
	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	
	/**
	 * @return the serviceDateStart
	 */
	public Long getServiceDateStart() {
		return serviceDateStart;
	}

	/**
	 * @param serviceDateStart the serviceDateStart to set
	 */
	public void setServiceDateStart(Long serviceDateStart) {
		this.serviceDateStart = serviceDateStart;
	}

	/**
	 * @return the serviceDateEnd
	 */
	public Long getServiceDateEnd() {
		return serviceDateEnd;
	}

	/**
	 * @param serviceDateEnd the serviceDateEnd to set
	 */
	public void setServiceDateEnd(Long serviceDateEnd) {
		this.serviceDateEnd = serviceDateEnd;
	}

	/**
	 * @return the orderStatusName
	 */
	public String getOrderStatusName() {
		return orderStatusName;
	}

	/**
	 * @param orderStatusName the orderStatusName to set
	 */
	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}

	/**
	 * @return the orderTypeName
	 */
	public String getOrderTypeName() {
		return orderTypeName;
	}

	/**
	 * @param orderTypeName the orderTypeName to set
	 */
	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	
	
}
