package com.jhj.vo.order;

import java.util.List;

public class OrderDetailVo extends OrderListVo {

	private String orderRatio;
	
	private String telStaff;
	
	private String overWorkStr;
	
	private List<OrderServiceAddonViewVo> serviceAddons;

	public String getOrderRatio() {
		return orderRatio;
	}

	public void setOrderRatio(String orderRatio) {
		this.orderRatio = orderRatio;
	}

	public String getTelStaff() {
		return telStaff;
	}

	public void setTelStaff(String telStaff) {
		this.telStaff = telStaff;
	}

	public List<OrderServiceAddonViewVo> getServiceAddons() {
		return serviceAddons;
	}

	public void setServiceAddons(List<OrderServiceAddonViewVo> serviceAddons) {
		this.serviceAddons = serviceAddons;
	}

	public String getOverWorkStr() {
		return overWorkStr;
	}

	public void setOverWorkStr(String overWorkStr) {
		this.overWorkStr = overWorkStr;
	}
	
	

}
