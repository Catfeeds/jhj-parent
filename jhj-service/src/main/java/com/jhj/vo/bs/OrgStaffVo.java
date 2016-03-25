package com.jhj.vo.bs;

import java.math.BigDecimal;
import java.util.List;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Tags;
import com.jhj.po.model.user.Users;
import com.jhj.vo.bs.staffAuth.StaffAuthVo;




/**
 *
 * @author :hulj
 * @Date : 2015年7月9日上午11:20:21
 * @Description: 
 *
 */
public class OrgStaffVo extends OrgStaffs  {
	
	
	private String amName;

	
	
	private List<Tags> tagList;
	
	private String tagNames;
	//接收被点击的 标签的 id
	private String tagId;
	
	
	private String tagIds;
	
	private String hukou;
	
	private BigDecimal amSumMoney;	//助理截止到现在 的 所有已支付订单 的 流水和
	
	
	/*
	 *  助理人员管理--助理名下所有 用户
	 */

	private List<Users> userList; // 助理名下所有用户
	
	private List<OrgStaffs> nowOrgAmList;	//当前门店下 的 所有助理
	
	
	//2016年1月22日18:44:36 服务人员 身份认证 Vo
	private List<StaffAuthVo> authList;
	
	//2016年1月22日18:44:46 已经通过认证的 项目 id，从 org_staff_auth表获得
	private String authIds;
	
	//2016年3月9日15:35:20  云店名称 （二级店。）
	private String orgName;
	
	//2016年3月9日15:35:52  一级门店名称
	private String parentOrgName;
	
	public String getParentOrgName() {
		return parentOrgName;
	}
	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}
	public String getAuthIds() {
		return authIds;
	}
	public void setAuthIds(String authIds) {
		this.authIds = authIds;
	}
	public List<StaffAuthVo> getAuthList() {
		return authList;
	}
	public void setAuthList(List<StaffAuthVo> authList) {
		this.authList = authList;
	}
	public List<OrgStaffs> getNowOrgAmList() {
		return nowOrgAmList;
	}
	public void setNowOrgAmList(List<OrgStaffs> nowOrgAmList) {
		this.nowOrgAmList = nowOrgAmList;
	}
	public BigDecimal getAmSumMoney() {
		return amSumMoney;
	}
	public void setAmSumMoney(BigDecimal amSumMoney) {
		this.amSumMoney = amSumMoney;
	}
	
	public List<Users> getUserList() {
		return userList;
	}
	public void setUserList(List<Users> userList) {
		this.userList = userList;
	}
	public String getAmName() {
		return amName;
	}
	public void setAmName(String amName) {
		this.amName = amName;
	}
	/**
	 * @return the tagId
	 */
	public String getTagId() {
		return tagId;
	}
	/**
	 * @param tagId the tagId to set
	 */
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	/**
	 * @return the tagNames
	 */
	public String getTagNames() {
		return tagNames;
	}
	/**
	 * @param tagNames the tagNames to set
	 */
	public void setTagNames(String tagNames) {
		this.tagNames = tagNames;
	}
	

	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * @return the tagList
	 */
	public List<Tags> getTagList() {
		return tagList;
	}

	/**
	 * @param tagList the tagList to set
	 */
	public void setTagList(List<Tags> tagList) {
		this.tagList = tagList;
	}

	public String getTagIds() {
		return tagIds;
	}

	public void setTagIds(String tagIds) {
		this.tagIds = tagIds;
	}

	public String getHukou() {
		return hukou;
	}

	public void setHukou(String hukou) {
		this.hukou = hukou;
	}

	



}
