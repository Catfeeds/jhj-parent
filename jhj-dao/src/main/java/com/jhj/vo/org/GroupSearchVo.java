package com.jhj.vo.org;

/**
 *
 * @author :hulj
 * @Date : 2016年3月8日下午6:42:49
 * @Description: 
 *		
 *		云店管理--搜索条件
 */
public class GroupSearchVo {
	
	private long parentId;
	
	private Short orgStatus;

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public Short getOrgStatus() {
		return orgStatus;
	}

	public void setOrgStatus(Short orgStatus) {
		this.orgStatus = orgStatus;
	}
	
}
