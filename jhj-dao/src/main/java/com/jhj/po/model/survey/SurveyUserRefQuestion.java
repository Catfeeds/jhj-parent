package com.jhj.po.model.survey;

public class SurveyUserRefQuestion {
    private Long id;

    private Long userId;

    private Long qId;

    private Long optionId;

    private Long contentId;

    private String resultNo;

    private Long times;

    private Long addTime;

    private Long contentChildId;

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

    public Long getqId() {
        return qId;
    }

    public void setqId(Long qId) {
        this.qId = qId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getResultNo() {
        return resultNo;
    }

    public void setResultNo(String resultNo) {
        this.resultNo = resultNo == null ? null : resultNo.trim();
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Long getContentChildId() {
        return contentChildId;
    }

    public void setContentChildId(Long contentChildId) {
        this.contentChildId = contentChildId;
    }
}