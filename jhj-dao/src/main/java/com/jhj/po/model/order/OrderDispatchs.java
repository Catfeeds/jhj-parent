package com.jhj.po.model.order;

public class OrderDispatchs {
    private Long id;

    private Long userId;

    private String mobile;

    private Long orderId;

    private String orderNo;

    private Long serviceDatePre;

    private Long serviceDate;

    private double serviceHours;
    
    private Long orgId;
    
    private Long parentId;

    private Long staffId;

    private String staffName;

    private String staffMobile;

    private String remarks;

    private Long amId;

    private Short dispatchStatus;
    
    private String pickAddrName;
    
    private String pickAddrLat;
    
    private String pickAddrLng;
    
    private String pickAddr;
    
    private int pickDistance;
    
    private int userAddrDistance;

    private Long addTime;

    private Long updateTime;
    
    private Short isApply;
    
    private Long applyTime;
    
    private Short allocate;
    
    private String allocateReason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Long getServiceDatePre() {
        return serviceDatePre;
    }

    public void setServiceDatePre(Long serviceDatePre) {
        this.serviceDatePre = serviceDatePre;
    }

    public Long getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Long serviceDate) {
        this.serviceDate = serviceDate;
    }

    public double getServiceHours() {
        return serviceHours;
    }

    public void setServiceHours(double serviceHours) {
        this.serviceHours = serviceHours;
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
        this.staffName = staffName == null ? null : staffName.trim();
    }

    public String getStaffMobile() {
        return staffMobile;
    }

    public void setStaffMobile(String staffMobile) {
        this.staffMobile = staffMobile == null ? null : staffMobile.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public Long getAmId() {
        return amId;
    }

    public void setAmId(Long amId) {
        this.amId = amId;
    }

    public Short getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(Short dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
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

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getPickAddrName() {
		return pickAddrName;
	}

	public void setPickAddrName(String pickAddrName) {
		this.pickAddrName = pickAddrName;
	}

	public String getPickAddrLat() {
		return pickAddrLat;
	}

	public void setPickAddrLat(String pickAddrLat) {
		this.pickAddrLat = pickAddrLat;
	}

	public String getPickAddrLng() {
		return pickAddrLng;
	}

	public void setPickAddrLng(String pickAddrLng) {
		this.pickAddrLng = pickAddrLng;
	}

	public String getPickAddr() {
		return pickAddr;
	}

	public void setPickAddr(String pickAddr) {
		this.pickAddr = pickAddr;
	}

	public int getPickDistance() {
		return pickDistance;
	}

	public void setPickDistance(int pickDistance) {
		this.pickDistance = pickDistance;
	}

	public int getUserAddrDistance() {
		return userAddrDistance;
	}

	public void setUserAddrDistance(int userAddrDistance) {
		this.userAddrDistance = userAddrDistance;
	}

	public Short getIsApply() {
		return isApply;
	}

	public void setIsApply(Short isApply) {
		this.isApply = isApply;
	}

	public Long getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Long applyTime) {
		this.applyTime = applyTime;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Short getAllocate() {
		return allocate;
	}

	public void setAllocate(Short allocate) {
		this.allocate = allocate;
	}

	public String getAllocateReason() {
		return allocateReason;
	}

	public void setAllocateReason(String allocateReason) {
		this.allocateReason = allocateReason;
	}


	
}