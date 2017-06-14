package com.jhj.vo.dict;

public class CooperativeBusinessSearchVo {
	
	private Long id;

    private String businessName;

    private String appName;

    private String businessLoginName;

    private String businessPassWord;

    private Short enable;
    
    private String broker;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getBusinessLoginName() {
		return businessLoginName;
	}

	public void setBusinessLoginName(String businessLoginName) {
		this.businessLoginName = businessLoginName;
	}

	public String getBusinessPassWord() {
		return businessPassWord;
	}

	public void setBusinessPassWord(String businessPassWord) {
		this.businessPassWord = businessPassWord;
	}

	public Short getEnable() {
		return enable;
	}

	public void setEnable(Short enable) {
		this.enable = enable;
	}

	public String getBroker() {
		return broker;
	}

	public void setBroker(String broker) {
		this.broker = broker;
	}

}
