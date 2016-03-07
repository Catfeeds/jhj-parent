package com.jhj.vo.order;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffs;

/**
 *
 * @author :hulj
 * @Date : 2015年8月7日下午2:03:43
 * @Description: 
 * 		助理端--保洁类订单详情，展示页  VO
 * 
 * 		两端 展示内容有 细微差别。。可继承！
 */
public class OrderAmHourViewVo extends OrderHourViewVo {
	
	/*
	 * 派工情况字段
	 */
	
	private String  staffName;	//该订单  阿姨 姓名
	
	private String staffMobile;		//阿姨联系 方式
	
	
	private List<OrgStaffs> staffList;	// 该助理 名下 的  所有 阿姨。。供修改时选择
	
	

	public List<OrgStaffs> getStaffList() {
		return staffList;
	}

	public void setStaffList(List<OrgStaffs> staffList) {
		this.staffList = staffList;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffMobile() {
		return staffMobile;
	}

	public void setStaffMobile(String staffMobile) {
		this.staffMobile = staffMobile;
	}

	
}
