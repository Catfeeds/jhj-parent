package com.jhj.po.model.order;

public class Orders {
    private Long id;

    private Long amId;

    private Long userId;

    private String mobile;

    private Long cityId;

    private Long orgId;

    private Long addrId;
    
    private String orderAddr;

    private String orderNo;

    private Short orderType;

    private Long serviceType;

    private String serviceContent;

    private Long serviceDate;

    private double serviceHour;
    
    private int staffNums;

    private Short orderStatus;

    private Short orderRate;

    private String orderRateContent;

    private Short orderFrom;
    
    private Long orderOpFrom;

    private String remarks;
    
    private String remarksConfirm;

    private Long addTime;

    private Long updateTime;
    
    private Long orderDoneTime;
    
    private String remarksBussinessConfirm;
    
    private Integer periodOrderId;
    
    public String getRemarksBussinessConfirm() {
		return remarksBussinessConfirm;
	}

	public void setRemarksBussinessConfirm(String remarksBussinessConfirm) {
		this.remarksBussinessConfirm = remarksBussinessConfirm;
	}

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

    public double getServiceHour() {
        return serviceHour;
    }

    public void setServiceHour(double serviceHour) {
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
    
    public Long getOrderOpFrom() {
		return orderOpFrom;
	}

	public void setOrderOpFrom(Long orderOpFrom) {
		this.orderOpFrom = orderOpFrom;
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

	public int getStaffNums() {
		return staffNums;
	}

	public void setStaffNums(int staffNums) {
		this.staffNums = staffNums;
	}

	public Long getOrderDoneTime() {
		return orderDoneTime;
	}

	public void setOrderDoneTime(Long orderDoneTime) {
		this.orderDoneTime = orderDoneTime;
	}

	public String getOrderAddr() {
		return orderAddr;
	}

	public void setOrderAddr(String orderAddr) {
		this.orderAddr = orderAddr;
	}

	public Integer getPeriodOrderId() {
		return periodOrderId;
	}

	public void setPeriodOrderId(Integer periodOrderId) {
		this.periodOrderId = periodOrderId;
	}
	

}