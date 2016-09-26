package com.jhj.vo;

import java.util.List;

import com.jhj.po.model.bs.Tags;

/**
 *
 * @author :hulj
 * @Date : 2015年7月15日下午6:32:04
 * @Description: TODO
 *
 */
public class TagSearchVo extends Tags {
	
	private Long tagId;
	
	private List<Long> tagIds;
	
	private String tagName;
	
	private Short tagType;

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public List<Long> getTagIds() {
		return tagIds;
	}

	public void setTagIds(List<Long> tagIds) {
		this.tagIds = tagIds;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Short getTagType() {
		return tagType;
	}

	public void setTagType(Short tagType) {
		this.tagType = tagType;
	}
}
