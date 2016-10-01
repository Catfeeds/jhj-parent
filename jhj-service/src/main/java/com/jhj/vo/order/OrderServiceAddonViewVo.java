package com.jhj.vo.order;

import com.jhj.po.model.order.OrderServiceAddons;

/**
 *
 * @author :hulj
 * @Date : 2015年8月5日上午10:53:35
 * @Description: 
 * 		钟点工 -- 订单详情  VO
 *
 */
public class OrderServiceAddonViewVo extends OrderServiceAddons {
	
	private String serviceAddonName;
	
	private Double serviceHour;

	public String getServiceAddonName() {
		return serviceAddonName;
	}

	public void setServiceAddonName(String serviceAddonName) {
		this.serviceAddonName = serviceAddonName;
	}

	public Double getServiceHour() {
		return serviceHour;
	}

	public void setServiceHour(Double serviceHour) {
		this.serviceHour = serviceHour;
	}
		
}
