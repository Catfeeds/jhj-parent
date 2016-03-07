package com.jhj.vo.admin;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jhj.base.model.models.ChainEntity;


public class AdminAuthorityVo extends ChainEntity<Integer, AdminAuthorityVo> implements Serializable{


	private String url;
	private String matchUrl;
	private String itemIcon;
	private Long parentId;
	private List<AdminAuthorityVo> childList = new ArrayList<AdminAuthorityVo>();


	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMatchUrl() {
		return matchUrl;
	}
	public void setMatchUrl(String matchUrl) {
		this.matchUrl = matchUrl;
	}
	public String getItemIcon() {
		return itemIcon;
	}
	public void setItemIcon(String itemIcon) {
		this.itemIcon = itemIcon;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public List<AdminAuthorityVo> getChildList() {
		return childList;
	}
	public void setChildList(List<AdminAuthorityVo> childList) {
		this.childList = childList;
	}



}
