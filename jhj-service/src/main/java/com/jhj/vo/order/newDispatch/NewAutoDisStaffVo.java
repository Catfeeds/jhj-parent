package com.jhj.vo.order.newDispatch;

import java.util.ArrayList;
import java.util.List;

import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.vo.order.OrgStaffsNewVo;

/**
 *
 * @author :hulj
 * @Date : 2016年3月21日下午3:05:12
 * @Description: 
 *		
 *	 jhj2.1 自动派工    VO
 */
public class NewAutoDisStaffVo extends OrderDispatchs{
	
	
	private List<OrgStaffsNewVo> staffList = new ArrayList<OrgStaffsNewVo>();

	public List<OrgStaffsNewVo> getStaffList() {
		return staffList;
	}

	public void setStaffList(List<OrgStaffsNewVo> staffList) {
		this.staffList = staffList;
	}

	
}
