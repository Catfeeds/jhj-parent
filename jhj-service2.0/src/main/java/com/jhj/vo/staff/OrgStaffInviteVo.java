package com.jhj.vo.staff;

import com.jhj.po.model.bs.OrgStaffInvite;
public class OrgStaffInviteVo extends OrgStaffInvite{
	
	private String name;
	
	private String mobile;
	
	private Short settingValueShort;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Short getSettingValueShort() {
		return settingValueShort;
	}

	public void setSettingValueShort(Short settingValueShort) {
		this.settingValueShort = settingValueShort;
	}

	
	
}
