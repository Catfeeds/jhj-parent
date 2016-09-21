package com.jhj.oa.vo;

import com.jhj.po.model.admin.AdminAccount;

/**
 *
 * @author :hulj
 * @Date : 2015年9月11日下午6:48:46
 * @Description: 
 *
 */
public class AccountVo extends AdminAccount{

	private long orgId;

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

}
