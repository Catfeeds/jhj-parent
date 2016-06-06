package com.jhj.vo.dispatch;

/**
 *
 * @author :hulj
 * @Date : 2016年5月30日下午12:09:15
 * @Description: 
 *	
 *		门店工作人员 派工列表vo
 *
 *		-- 保存 从 dispatch 表中取出的 记录
 */
public class StaffDispatchVo {
	
	//格式 "yyyy-MM-dd"
	private String serviceDateStr;
	
	private Long staffId;
	
	private String staffName;
	
	private Long orderId;
	
	private String orderNo;
	
	public String getServiceDateStr() {
		return serviceDateStr;
	}

	public void setServiceDateStr(String serviceDateStr) {
		this.serviceDateStr = serviceDateStr;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
}
