package com.jhj.vo.admin;

import java.util.ArrayList;
import java.util.List;

import com.jhj.po.model.admin.AdminAuthority;
import com.jhj.po.model.admin.AdminRole;
import com.meijia.utils.common.extension.ArrayHelper;

public class AdminRoleVo extends AdminRole{

	private  List<AdminAuthority> childList = new ArrayList<AdminAuthority>() ;

	private Long authorityId;
	
	private  Long[]  authorityIds;

	public List<AdminAuthority> getChildList() {
		return childList;
	}

	public void setChildList(List<AdminAuthority> childList) {
		this.childList = childList;
	}

	public Long[] getAuthorityIds() {
		return authorityIds;
	}

	public void setAuthorityIds(Long[] authorityIds) {
		this.authorityIds = authorityIds;
	}

	public String getAuthorityIdsString(){
		return ArrayHelper.LongtoString(authorityIds, ",");
	}

	public Long getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(Long authorityId) {
		this.authorityId = authorityId;
	}
	





}
