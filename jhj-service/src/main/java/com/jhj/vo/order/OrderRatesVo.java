package com.jhj.vo.order;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderRates;

public class OrderRatesVo extends OrderRates {

	private String serviceTypeName;

	private String serviceDateStr;

	private Long serviceDate;

	private List<String> orderRateUrl;

	private List<OrgStaffs> staffList;

	// 用户手机号
	private String usermobile;

	// 用户地址
	private String userAddr;

	private String staffMobile;

	private String staffName;

	private String orgName;

	private String parentOrgName;

	private Short orderType;

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public String getServiceDateStr() {
		return serviceDateStr;
	}

	public void setServiceDateStr(String serviceDateStr) {
		this.serviceDateStr = serviceDateStr;
	}

	public Long getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Long serviceDate) {
		this.serviceDate = serviceDate;
	}

	public List<String> getOrderRateUrl() {
		return orderRateUrl;
	}

	public void setOrderRateUrl(List<String> orderRateUrl) {
		this.orderRateUrl = orderRateUrl;
	}

	public List<OrgStaffs> getStaffList() {
		return staffList;
	}

	public void setStaffList(List<OrgStaffs> staffList) {
		this.staffList = staffList;
	}

	public String getUsermobile() {
		return usermobile;
	}

	public void setUsermobile(String usermobile) {
		this.usermobile = usermobile;
	}

	public String getUserAddr() {
		return userAddr;
	}

	public void setUserAddr(String userAddr) {
		this.userAddr = userAddr;
	}

	public String getStaffMobile() {
		return staffMobile;
	}

	public void setStaffMobile(String staffMobile) {
		this.staffMobile = staffMobile;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public Short getOrderType() {
		return orderType;
	}

	public void setOrderType(Short orderType) {
		this.orderType = orderType;
	}

}
