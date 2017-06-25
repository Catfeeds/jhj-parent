package com.jhj.vo.staff;

import java.math.BigDecimal;


public class OrgStaffIncomingVo {
	
	private Long parentId;
	
	private String parentOrgName;
	
	private Long orgId;
	
	private String orgName;
	
	private Long staffId;
	
	private String staffName;
	
	private String staffMobile;
	
	private Long orderId;
	
	private String orderNo;
	
	private String orderFromStr;
	
	private String addTimeStr;
	
	private String orderTypeName;
	
	private String serviceDateStr;
	
	private Double serviceHour;
	
	private int staffNum;
	
	private String addr;
	
	private String userMobile;
	
	private Short isVip;
		
	private String isVipStr;
	
	private String payTypeName;
	
	private BigDecimal totalOrderMoney;
	
	private BigDecimal totalOrderPay;
	
	private BigDecimal orderMoney;
	
	private BigDecimal orderPay;
	
	private String couponName;
	
	private BigDecimal orderIncoming;
	
	private BigDecimal orderPayCoupon;
	
	private BigDecimal orderPayCouponIncoming;
	
	private BigDecimal orderPayExtDiff;
	
	private BigDecimal orderPayExtDiffIncoming;
	
	private BigDecimal orderPayExtOverWork;
	
	private BigDecimal orderPayExtOverWorkIncoming;
	
	private BigDecimal totalOrderIncoming;
	
	private BigDecimal totalOrderDept;
	
	private BigDecimal incomingPercent;
	
	private String remarks;
	
	private String groupCode;
	
	private String validateCodeName;
	
	private String orderDonetimeStr;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
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
		this.staffName = staffName;
	}

	public String getStaffMobile() {
		return staffMobile;
	}

	public void setStaffMobile(String staffMobile) {
		this.staffMobile = staffMobile;
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
		this.orderNo = orderNo;
	}

	public String getAddTimeStr() {
		return addTimeStr;
	}

	public void setAddTimeStr(String addTimeStr) {
		this.addTimeStr = addTimeStr;
	}

	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}

	public String getServiceDateStr() {
		return serviceDateStr;
	}

	public void setServiceDateStr(String serviceDateStr) {
		this.serviceDateStr = serviceDateStr;
	}

	public Double getServiceHour() {
		return serviceHour;
	}

	public void setServiceHour(Double serviceHour) {
		this.serviceHour = serviceHour;
	}

	public int getStaffNum() {
		return staffNum;
	}

	public void setStaffNum(int staffNum) {
		this.staffNum = staffNum;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getIsVipStr() {
		return isVipStr;
	}

	public void setIsVipStr(String isVipStr) {
		this.isVipStr = isVipStr;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public BigDecimal getOrderIncoming() {
		return orderIncoming;
	}

	public void setOrderIncoming(BigDecimal orderIncoming) {
		this.orderIncoming = orderIncoming;
	}

	public BigDecimal getOrderPayCoupon() {
		return orderPayCoupon;
	}

	public void setOrderPayCoupon(BigDecimal orderPayCoupon) {
		this.orderPayCoupon = orderPayCoupon;
	}

	public BigDecimal getOrderPayExtDiff() {
		return orderPayExtDiff;
	}

	public void setOrderPayExtDiff(BigDecimal orderPayExtDiff) {
		this.orderPayExtDiff = orderPayExtDiff;
	}

	public BigDecimal getOrderPayExtOverWork() {
		return orderPayExtOverWork;
	}

	public void setOrderPayExtOverWork(BigDecimal orderPayExtOverWork) {
		this.orderPayExtOverWork = orderPayExtOverWork;
	}

	public BigDecimal getTotalOrderIncoming() {
		return totalOrderIncoming;
	}

	public void setTotalOrderIncoming(BigDecimal totalOrderIncoming) {
		this.totalOrderIncoming = totalOrderIncoming;
	}

	public BigDecimal getTotalOrderDept() {
		return totalOrderDept;
	}

	public void setTotalOrderDept(BigDecimal totalOrderDept) {
		this.totalOrderDept = totalOrderDept;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public BigDecimal getTotalOrderMoney() {
		return totalOrderMoney;
	}

	public void setTotalOrderMoney(BigDecimal totalOrderMoney) {
		this.totalOrderMoney = totalOrderMoney;
	}

	public BigDecimal getOrderPayExtDiffIncoming() {
		return orderPayExtDiffIncoming;
	}

	public void setOrderPayExtDiffIncoming(BigDecimal orderPayExtDiffIncoming) {
		this.orderPayExtDiffIncoming = orderPayExtDiffIncoming;
	}

	public BigDecimal getOrderPayExtOverWorkIncoming() {
		return orderPayExtOverWorkIncoming;
	}

	public void setOrderPayExtOverWorkIncoming(BigDecimal orderPayExtOverWorkIncoming) {
		this.orderPayExtOverWorkIncoming = orderPayExtOverWorkIncoming;
	}

	public Short getIsVip() {
		return isVip;
	}

	public void setIsVip(Short isVip) {
		this.isVip = isVip;
	}

	public BigDecimal getTotalOrderPay() {
		return totalOrderPay;
	}

	public void setTotalOrderPay(BigDecimal totalOrderPay) {
		this.totalOrderPay = totalOrderPay;
	}

	public BigDecimal getIncomingPercent() {
		return incomingPercent;
	}

	public void setIncomingPercent(BigDecimal incomingPercent) {
		this.incomingPercent = incomingPercent;
	}

	public BigDecimal getOrderPayCouponIncoming() {
		return orderPayCouponIncoming;
	}

	public void setOrderPayCouponIncoming(BigDecimal orderPayCouponIncoming) {
		this.orderPayCouponIncoming = orderPayCouponIncoming;
	}

	public BigDecimal getOrderPay() {
		return orderPay;
	}

	public void setOrderPay(BigDecimal orderPay) {
		this.orderPay = orderPay;
	}

	public String getOrderFromStr() {
		return orderFromStr;
	}

	public void setOrderFromStr(String orderFromStr) {
		this.orderFromStr = orderFromStr;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getValidateCodeName() {
		return validateCodeName;
	}

	public void setValidateCodeName(String validateCodeName) {
		this.validateCodeName = validateCodeName;
	}

	public String getOrderDonetimeStr() {
		return orderDonetimeStr;
	}

	public void setOrderDonetimeStr(String orderDonetimeStr) {
		this.orderDonetimeStr = orderDonetimeStr;
	}
	
	
}
