package com.jhj.po.model.dict;

public class DictAd {
    private Long id;

    private Short No;

    private String imgUrl;

    private String gotoUrl;

    private Short adType;

    private Long addTime;

    private Long updateTime;

    private Short enable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getNo() {
        return No;
    }

    public void setNo(Short No) {
        this.No = No;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public String getGotoUrl() {
        return gotoUrl;
    }

    public void setGotoUrl(String gotoUrl) {
        this.gotoUrl = gotoUrl == null ? null : gotoUrl.trim();
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

	public Short getEnable() {
		return enable;
	}

	public void setEnable(Short enable) {
		this.enable = enable;
	}

	public Short getAdType() {
		return adType;
	}

	public void setAdType(Short adType) {
		this.adType = adType;
	}

}