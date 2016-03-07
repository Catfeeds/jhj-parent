package com.jhj.po.model.orderReview;

public class JhjOrderReview {
    private Long id;

    private String staffNo;

    private Long serviceDate;

    private String serviceHour;

    private Short arrriveOnTime;

    private Short cleanReview;

    private Short serviceDetail;

    private Short appearance;

    private Short afterService;

    private Short overAllReview;

    private Long addTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo == null ? null : staffNo.trim();
    }

    public Long getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Long serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getServiceHour() {
        return serviceHour;
    }

    public void setServiceHour(String serviceHour) {
        this.serviceHour = serviceHour == null ? null : serviceHour.trim();
    }

    public Short getArrriveOnTime() {
        return arrriveOnTime;
    }

    public void setArrriveOnTime(Short arrriveOnTime) {
        this.arrriveOnTime = arrriveOnTime;
    }

    public Short getCleanReview() {
        return cleanReview;
    }

    public void setCleanReview(Short cleanReview) {
        this.cleanReview = cleanReview;
    }

    public Short getServiceDetail() {
        return serviceDetail;
    }

    public void setServiceDetail(Short serviceDetail) {
        this.serviceDetail = serviceDetail;
    }

    public Short getAppearance() {
        return appearance;
    }

    public void setAppearance(Short appearance) {
        this.appearance = appearance;
    }

    public Short getAfterService() {
        return afterService;
    }

    public void setAfterService(Short afterService) {
        this.afterService = afterService;
    }

    public Short getOverAllReview() {
        return overAllReview;
    }

    public void setOverAllReview(Short overAllReview) {
        this.overAllReview = overAllReview;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }
}