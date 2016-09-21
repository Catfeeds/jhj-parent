package com.jhj.vo.bs;

import java.io.Serializable;
import java.util.List;

import com.jhj.base.model.models.ChainEntity;

/**
 *
 * @author :hulj
 * @Date : 2016年3月10日上午11:49:02
 * @Description: 
 *	
 *		服务类别树形结构 Vo
 */
public class NewPartnerServiceVo extends ChainEntity<Integer, NewPartnerServiceVo> implements Serializable {
	
	private static final long serialVersionUID = -7973799371813452144L;
	
	private String url;
	private String matchUrl;
	private String itemIcon;
	private Long parentId;
	private List<NewPartnerServiceVo> childList;
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the matchUrl
	 */
	public String getMatchUrl() {
		return matchUrl;
	}
	/**
	 * @param matchUrl the matchUrl to set
	 */
	public void setMatchUrl(String matchUrl) {
		this.matchUrl = matchUrl;
	}
	/**
	 * @return the itemIcon
	 */
	public String getItemIcon() {
		return itemIcon;
	}
	/**
	 * @param itemIcon the itemIcon to set
	 */
	public void setItemIcon(String itemIcon) {
		this.itemIcon = itemIcon;
	}
	/**
	 * @return the parentId
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * @return the childList
	 */
	public List<NewPartnerServiceVo> getChildList() {
		return childList;
	}
	/**
	 * @param childList the childList to set
	 */
	public void setChildList(List<NewPartnerServiceVo> childList) {
		this.childList = childList;
	}
	
	
}
