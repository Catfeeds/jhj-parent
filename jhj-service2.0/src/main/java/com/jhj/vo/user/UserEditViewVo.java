package com.jhj.vo.user;

import java.util.List;

import com.jhj.po.model.bs.Tags;
import com.jhj.po.model.user.Users;

public class UserEditViewVo extends Users{

	
	
	private List<Tags> list;
	
	private String tagIds;
	//标签名称
	
	private String addrName;

	private List<Long> tagList;

	public List<Tags> getList() {
		return list;
	}

	public void setList(List<Tags> list) {
		this.list = list;
	}

	public String getAddrName() {
		return addrName;
	}

	public void setAddrName(String addrName) {
		this.addrName = addrName;
	}

	public String getTagIds() {
		return tagIds;
	}

	public void setTagIds(String tagIds) {
		this.tagIds = tagIds;
	}

	public List<Long> getTagList() {
		return tagList;
	}

	public void setTagList(List<Long> tagList) {
		this.tagList = tagList;
	}
	
}
