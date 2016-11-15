package com.jhj.vo.order;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderRates;

public class OrderRatesVo extends OrderRates{
	
	private String serviceTypeName;
	
	private String serviceDateStr;
	
	private List<String> orderRateUrl;
	
	private List<OrgStaffs> staffList;
	
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

}
