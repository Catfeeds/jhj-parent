package com.jhj.po.model.survey;

public class SurveyUserRefRecommend {
    private Long id;

    private Long userId;

    private Long contentId;

    private Long addTime;

    private String resultNo;

    private Long times;

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

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
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

    public Long getContentChildId() {
        return contentChildId;
    }

    public void setContentChildId(Long contentChildId) {
        this.contentChildId = contentChildId;
    }
}