package com.jhj.vo.bs;

import com.jhj.po.model.bs.OrgStaffs;

/**
 *
 * @author :hulj
 * @Date : 2016年3月10日下午5:59:44
 * @Description: 
 *		
 *		jhj2.1服务人员 管理 列表 页Vo	
 */
public class NewStaffListVo extends OrgStaffs {
	
	private String nativePlace;
	
	private String orgName;		//云店名称
	
	private String parentOrgName;	//门店名称
	
	private int isAuthIdCard;  //是否实名认证 0 = 否 1 = 是.

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public int getIsAuthIdCard() {
		return isAuthIdCard;
	}

	public void setIsAuthIdCard(int isAuthIdCard) {
		this.isAuthIdCard = isAuthIdCard;
	}
	
}
