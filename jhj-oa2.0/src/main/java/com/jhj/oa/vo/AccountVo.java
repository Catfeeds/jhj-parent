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

	/**
	 * @return the orgId
	 */
	public long getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	
}
