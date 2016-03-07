package com.jhj.po.model.bs;

public class Gifts {
    private Long giftId;

    private String name;

    private Short rangeMonth;

    private Long addTime;

    private Long updateTime;


    public Long getGiftId() {
		return giftId;
	}

	public void setGiftId(Long giftId) {
		this.giftId = giftId;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Short getRangeMonth() {
        return rangeMonth;
    }

    public void setRangeMonth(Short rangeMonth) {
        this.rangeMonth = rangeMonth;
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