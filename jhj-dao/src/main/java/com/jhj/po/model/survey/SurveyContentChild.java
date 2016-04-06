package com.jhj.po.model.survey;

import java.math.BigDecimal;

public class SurveyContentChild {
    private Long id;

    private Long contentId;

    private String optionStr;

    private String optionNo;

    private BigDecimal childPrice;

    private Long addTime;

    private Long defaultTimeChild;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getOptionStr() {
        return optionStr;
    }

    public void setOptionStr(String optionStr) {
        this.optionStr = optionStr == null ? null : optionStr.trim();
    }

    public String getOptionNo() {
        return optionNo;
    }

    public void setOptionNo(String optionNo) {
        this.optionNo = optionNo == null ? null : optionNo.trim();
    }

    public BigDecimal getChildPrice() {
        return childPrice;
    }

    public void setChildPrice(BigDecimal childPrice) {
        this.childPrice = childPrice;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Long getDefaultTimeChild() {
        return defaultTimeChild;
    }

    public void setDefaultTimeChild(Long defaultTimeChild) {
        this.defaultTimeChild = defaultTimeChild;
    }
}