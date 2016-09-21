package com.jhj.vo.admin;

import com.jhj.po.model.admin.AdminAccount;

public class AdminAccountVo extends AdminAccount {

	private String roleName;
	
	private Long orgId;
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

}
