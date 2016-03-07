package com.jhj.po.model.survey;

public class SurveyQuestion {
    private Long qId;

    private Long bankId;

    private String title;

    private Short isMulti;

    private Long beforeQId;

    private Long nextQId;

    private Short isFirst;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Short getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(Short isMulti) {
        this.isMulti = isMulti;
    }

    public Long getBeforeQId() {
        return beforeQId;
    }

    public void setBeforeQId(Long beforeQId) {
        this.beforeQId = beforeQId;
    }

    public Long getNextQId() {
        return nextQId;
    }

    public void setNextQId(Long nextQId) {
        this.nextQId = nextQId;
    }

    public Short getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Short isFirst) {
        this.isFirst = isFirst;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }
}