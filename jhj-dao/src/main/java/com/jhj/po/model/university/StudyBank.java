package com.jhj.po.model.university;

public class StudyBank {
    private Long bankId;

    private Long serviceTypeId;

    private String name;

    private Short totalNeed;

    private String description;

    private Long addTime;

    private Short randomQNum;

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Short getTotalNeed() {
        return totalNeed;
    }

    public void setTotalNeed(Short totalNeed) {
        this.totalNeed = totalNeed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Short getRandomQNum() {
        return randomQNum;
    }

    public void setRandomQNum(Short randomQNum) {
        this.randomQNum = randomQNum;
    }
}