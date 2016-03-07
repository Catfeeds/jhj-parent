package com.jhj.oa.vo;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffs;

/**
 *
 * @author :hulj
 * @Date : 2015年9月9日上午10:47:55
 * @Description: 
 *		为用户调整助理， 
 */
public class AmVo {
	
	private List<OrgStaffs> amList;	//当前门店下的 助理列表
	
	private List<Long> userIdList;  //当前助理的 所有用户
	
	private Long oldAmId;			//当前助理id
	
	private String amName;			//当前助理名称
	
	
	
	/**
	 * @return the amName
	 */
	public String getAmName() {
		return amName;
	}

	/**
	 * @param amName the amName to set
	 */
	public void setAmName(String amName) {
		this.amName = amName;
	}

	/**
	 * @return the oldAmId
	 */
	public Long getOldAmId() {
		return oldAmId;
	}

	/**
	 * @param oldAmId the oldAmId to set
	 */
	public void setOldAmId(Long oldAmId) {
		this.oldAmId = oldAmId;
	}

	/**
	 * @return the userIdList
	 */
	public List<Long> getUserIdList() {
		return userIdList;
	}

	/**
	 * @param userIdList the userIdList to set
	 */
	public void setUserIdList(List<Long> userIdList) {
		this.userIdList = userIdList;
	}

	/**
	 * @return the amList
	 */
	public List<OrgStaffs> getAmList() {
		return amList;
	}

	/**
	 * @param amList the amList to set
	 */
	public void setAmList(List<OrgStaffs> amList) {
		this.amList = amList;
	}
	
	
	
}
