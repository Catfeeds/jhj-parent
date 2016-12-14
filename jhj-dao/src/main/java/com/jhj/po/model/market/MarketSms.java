package com.jhj.po.model.market;

import java.util.List;

public class MarketSms {
    private Integer marketSmsId;

    private Integer smsTempId;

    private String userGroupType;
    
    private List<String> userGroupTypeList;

    private Integer totalSend;

    private Integer totalSended;

    private Integer totalFail;

    private Long addTime;

    private Long updateTime;

    public Integer getMarketSmsId() {
        return marketSmsId;
    }

    public void setMarketSmsId(Integer marketSmsId) {
        this.marketSmsId = marketSmsId;
    }

    public Integer getSmsTempId() {
        return smsTempId;
    }

    public void setSmsTempId(Integer smsTempId) {
        this.smsTempId = smsTempId;
    }

    public String getUserGroupType() {
		return userGroupType;
	}

	public void setUserGroupType(String userGroupType) {
		this.userGroupType = userGroupType;
	}

	public List<String> getUserGroupTypeList() {
		return userGroupTypeList;
	}

	public void setUserGroupTypeList(List<String> userGroupTypeList) {
		this.userGroupTypeList = userGroupTypeList;
	}

	public Integer getTotalSend() {
        return totalSend;
    }

    public void setTotalSend(Integer totalSend) {
        this.totalSend = totalSend;
    }

    public Integer getTotalSended() {
        return totalSended;
    }

    public void setTotalSended(Integer totalSended) {
        this.totalSended = totalSended;
    }

    public Integer getTotalFail() {
        return totalFail;
    }

    public void setTotalFail(Integer totalFail) {
        this.totalFail = totalFail;
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