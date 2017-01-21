package com.jhj.vo.order;

import java.util.List;

import com.jhj.po.model.common.Imgs;

public class OrderDetailVo extends OrderListVo {

	private String orderRatio;
	
	private String telStaff;
	
	private String overWorkStr;
	
	private List<OrderServiceAddonViewVo> serviceAddons;
	
	private List<Imgs> orderImgs;
	
	private String orderFromName;

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

	public List<Imgs> getOrderImgs() {
		return orderImgs;
	}

	public void setOrderImgs(List<Imgs> orderImgs) {
		this.orderImgs = orderImgs;
	}

	public String getOrderFromName() {
		return orderFromName;
	}

	public void setOrderFromName(String orderFromName) {
		this.orderFromName = orderFromName;
	}
	
	

}
