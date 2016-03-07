package com.jhj.po.model.university;

public class StudyStaffPass {
    private Long id;

    private Long staffId;

    private Long serviceTypeId;

    private Long bankId;

    private Short totalNeed;

    private Short totalRight;

    private Long addTime;

    private Long updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public Short getTotalNeed() {
        return totalNeed;
    }

    public void setTotalNeed(Short totalNeed) {
        this.totalNeed = totalNeed;
    }

    public Short getTotalRight() {
        return totalRight;
    }

    public void setTotalRight(Short totalRight) {
        this.totalRight = totalRight;
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
}