package com.jhj.vo.staff;

import com.jhj.po.model.bs.OrgStaffCash;

/**
 *
 * @author :hulj
 * @Date : 2015年8月12日上午11:15:19
 * @Description: 
 *		运营平台--订单管理VO
 */
public class OrgStaffCashSearchVo  extends OrgStaffCash{
	

	private String addTimeStr;

	private String updateTimeStr;
	
	//员工名称
	private String staffName;


	public String getAddTimeStr() {
		return addTimeStr;
	}

	public void setAddTimeStr(String addTimeStr) {
		this.addTimeStr = addTimeStr;
	}

	public String getUpdateTimeStr() {
		return updateTimeStr;
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	
}
