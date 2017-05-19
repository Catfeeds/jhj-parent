package com.jhj.vo.order.newDispatch;

import java.util.ArrayList;
import java.util.List;

import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.vo.order.OrgStaffDispatchVo;

/**
 *
 * @author :hulj
 * @Date : 2016年3月21日下午3:05:12
 * @Description: 
 *		
 *	 jhj2.1 自动派工    VO
 */
public class NewAutoDisStaffVo extends OrderDispatchs{
	
	
	private List<OrgStaffDispatchVo> staffList = new ArrayList<OrgStaffDispatchVo>();

	public List<OrgStaffDispatchVo> getStaffList() {
		return staffList;
	}

	public void setStaffList(List<OrgStaffDispatchVo> staffList) {
		this.staffList = staffList;
	}

	
}
