package com.jhj.po.model.order;

public class Orders {
    private Long id;

    private Long amId;

    private Long userId;

    private String mobile;

    private Long cityId;

    private Long orgId;

    private Long addrId;

    private String orderNo;

    private Short orderType;

    private Long serviceType;

    private String serviceContent;

    private Long serviceDate;

    private Short serviceHour;

    private Short orderStatus;

    private Short orderRate;

    private String orderRateContent;

    private Short orderFrom;

    private String remarks;
    
    private String remarksConfirm;

    private Long addTime;

    private Long updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmId() {
        return amId;
    }

    public void setAmId(Long amId) {
        this.amId = amId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getAddrId() {
        return addrId;
    }

    public void setAddrId(Long addrId) {
        this.addrId = addrId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Short getOrderType() {
        return orderType;
    }

    public void setOrderType(Short orderType) {
        this.orderType = orderType;
    }

    public Long getServiceType() {
        return serviceType;
    }

    public void setServiceType(Long serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceContent() {
        return serviceContent;
    }

    public void setServiceContent(String serviceContent) {
        this.serviceContent = serviceContent == null ? null : serviceContent.trim();
    }

    public Long getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Long serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Short getServiceHour() {
        return serviceHour;
    }

    public void setServiceHour(Short serviceHour) {
        this.serviceHour = serviceHour;
    }

    public Short getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Short orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Short getOrderRate() {
        return orderRate;
    }

    public void setOrderRate(Short orderRate) {
        this.orderRate = orderRate;
    }

    public String getOrderRateContent() {
        return orderRateContent;
    }

    public void setOrderRateContent(String orderRateContent) {
        this.orderRateContent = orderRateContent == null ? null : orderRateContent.trim();
    }

    public Short getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(Short orderFrom) {
        this.orderFrom = orderFrom;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public String getRemarksConfirm() {
		return remarksConfirm;
	}

	public void setRemarksConfirm(String remarksConfirm) {
		this.remarksConfirm = remarksConfirm;
	}

	public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addTime == null) ? 0 : addTime.hashCode());
		result = prime * result + ((addrId == null) ? 0 : addrId.hashCode());
		result = prime * result + ((amId == null) ? 0 : amId.hashCode());
		result = prime * result + ((cityId == null) ? 0 : cityId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + ((orderFrom == null) ? 0 : orderFrom.hashCode());
		result = prime * result + ((orderNo == null) ? 0 : orderNo.hashCode());
		result = prime * result + ((orderRate == null) ? 0 : orderRate.hashCode());
		result = prime * result + ((orderRateContent == null) ? 0 : orderRateContent.hashCode());
		result = prime * result + ((orderStatus == null) ? 0 : orderStatus.hashCode());
		result = prime * result + ((orderType == null) ? 0 : orderType.hashCode());
		result = prime * result + ((orgId == null) ? 0 : orgId.hashCode());
		result = prime * result + ((remarks == null) ? 0 : remarks.hashCode());
		result = prime * result + ((remarksConfirm == null) ? 0 : remarksConfirm.hashCode());
		result = prime * result + ((serviceContent == null) ? 0 : serviceContent.hashCode());
		result = prime * result + ((serviceDate == null) ? 0 : serviceDate.hashCode());
		result = prime * result + ((serviceHour == null) ? 0 : serviceHour.hashCode());
		result = prime * result + ((serviceType == null) ? 0 : serviceType.hashCode());
		result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Orders other = (Orders) obj;
		if (addTime == null) {
			if (other.addTime != null)
				return false;
		} else if (!addTime.equals(other.addTime))
			return false;
		if (addrId == null) {
			if (other.addrId != null)
				return false;
		} else if (!addrId.equals(other.addrId))
			return false;
		if (amId == null) {
			if (other.amId != null)
				return false;
		} else if (!amId.equals(other.amId))
			return false;
		if (cityId == null) {
			if (other.cityId != null)
				return false;
		} else if (!cityId.equals(other.cityId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (orderFrom == null) {
			if (other.orderFrom != null)
				return false;
		} else if (!orderFrom.equals(other.orderFrom))
			return false;
		if (orderNo == null) {
			if (other.orderNo != null)
				return false;
		} else if (!orderNo.equals(other.orderNo))
			return false;
		if (orderRate == null) {
			if (other.orderRate != null)
				return false;
		} else if (!orderRate.equals(other.orderRate))
			return false;
		if (orderRateContent == null) {
			if (other.orderRateContent != null)
				return false;
		} else if (!orderRateContent.equals(other.orderRateContent))
			return false;
		if (orderStatus == null) {
			if (other.orderStatus != null)
				return false;
		} else if (!orderStatus.equals(other.orderStatus))
			return false;
		if (orderType == null) {
			if (other.orderType != null)
				return false;
		} else if (!orderType.equals(other.orderType))
			return false;
		if (orgId == null) {
			if (other.orgId != null)
				return false;
		} else if (!orgId.equals(other.orgId))
			return false;
		if (remarks == null) {
			if (other.remarks != null)
				return false;
		} else if (!remarks.equals(other.remarks))
			return false;
		if (remarksConfirm == null) {
			if (other.remarksConfirm != null)
				return false;
		} else if (!remarksConfirm.equals(other.remarksConfirm))
			return false;
		if (serviceContent == null) {
			if (other.serviceContent != null)
				return false;
		} else if (!serviceContent.equals(other.serviceContent))
			return false;
		if (serviceDate == null) {
			if (other.serviceDate != null)
				return false;
		} else if (!serviceDate.equals(other.serviceDate))
			return false;
		if (serviceHour == null) {
			if (other.serviceHour != null)
				return false;
		} else if (!serviceHour.equals(other.serviceHour))
			return false;
		if (serviceType == null) {
			if (other.serviceType != null)
				return false;
		} else if (!serviceType.equals(other.serviceType))
			return false;
		if (updateTime == null) {
			if (other.updateTime != null)
				return false;
		} else if (!updateTime.equals(other.updateTime))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
    
    
}