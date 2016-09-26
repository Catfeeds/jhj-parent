package com.jhj.vo;

import java.util.List;

public class ServiceAddonSearchVo {
	
	private Long serviceAddonId;
	
	private List<Long> serviceAddonIds;
	
	private Long serviceType;
	
	private List<Long> serviceTypes;

	public Long getServiceAddonId() {
		return serviceAddonId;
	}

	public void setServiceAddonId(Long serviceAddonId) {
		this.serviceAddonId = serviceAddonId;
	}

	public List<Long> getServiceAddonIds() {
		return serviceAddonIds;
	}

	public void setServiceAddonIds(List<Long> serviceAddonIds) {
		this.serviceAddonIds = serviceAddonIds;
	}

	public Long getServiceType() {
		return serviceType;
	}

	public void setServiceType(Long serviceType) {
		this.serviceType = serviceType;
	}

	public List<Long> getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(List<Long> serviceTypes) {
		this.serviceTypes = serviceTypes;
	}
}
