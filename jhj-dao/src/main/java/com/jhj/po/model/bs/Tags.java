package com.jhj.po.model.bs;

public class Tags {
    private Long tagId;

    private String tagName;

    private Short tagType;

    private Long addTime;

    private Short isEnable;

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName == null ? null : tagName.trim();
    }

    public Short getTagType() {
        return tagType;
    }

    public void setTagType(Short tagType) {
        this.tagType = tagType;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Short getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Short isEnable) {
        this.isEnable = isEnable;
    }
}