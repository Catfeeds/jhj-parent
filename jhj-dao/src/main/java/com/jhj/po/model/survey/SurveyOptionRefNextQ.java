package com.jhj.po.model.survey;

public class SurveyOptionRefNextQ {
    private Long id;

    private Long qId;

    private String optionId;

    private String optionNo;

    private String relatedQId;

    private Long addTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getqId() {
        return qId;
    }

    public void setqId(Long qId) {
        this.qId = qId;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId == null ? null : optionId.trim();
    }

    public String getOptionNo() {
        return optionNo;
    }

    public void setOptionNo(String optionNo) {
        this.optionNo = optionNo == null ? null : optionNo.trim();
    }

    public String getRelatedQId() {
        return relatedQId;
    }

    public void setRelatedQId(String relatedQId) {
        this.relatedQId = relatedQId == null ? null : relatedQId.trim();
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }
}