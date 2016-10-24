package com.jhj.vo.staff;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffCash;

/**
 *
 * @author :hulj
 * @Date : 2015年8月12日上午11:15:19
 * @Description: 
 *		运营平台--订单管理VO
 */
public class OrgStaffCashSearchVo {
	
    private Long staffId;

    private String mobile;
    
    private Short orderStatus;
    
    private List<Short> orderStatusList;

	//员工名称
	private String staffName;


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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Short getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Short orderStatus) {
		this.orderStatus = orderStatus;
	}

	public List<Short> getOrderStatusList() {
		return orderStatusList;
	}

	public void setOrderStatusList(List<Short> orderStatusList) {
		this.orderStatusList = orderStatusList;
	}

	
}
