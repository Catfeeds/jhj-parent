package com.jhj.po.model.university;

public class StudyQuestion {
    private Long qId;

    private Long bankId;

    private Long serviceTypeId;

    private String title;

    private String description;

    private Short isMulti;

    private Short isNeed;

    private String answer;

    private Long addTime;

    public Long getqId() {
        return qId;
    }

    public void setqId(Long qId) {
        this.qId = qId;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Short getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(Short isMulti) {
        this.isMulti = isMulti;
    }

    public Short getIsNeed() {
        return isNeed;
    }

    public void setIsNeed(Short isNeed) {
        this.isNeed = isNeed;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }
}