package com.jhj.vo.university;

import com.jhj.po.model.university.PartnerServiceType;

/**
 *
 * @author :hulj
 * @Date : 2016年1月14日下午3:36:08
 * @Description:
 *	
 *		平台--服务类别列表，列表页vo
 */
public class OaPartnerServiceTypeVo extends PartnerServiceType {
	
	private String parentName;

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
}
