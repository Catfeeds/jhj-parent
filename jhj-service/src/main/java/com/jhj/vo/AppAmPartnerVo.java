package com.jhj.vo;

import java.util.List;

/**
 *
 * @author :hulj
 * @Date : 2016年4月13日下午5:00:35
 * @Description: 
 *
 */
public class AppAmPartnerVo {
	
	/*
	 * 助理类订单  二级页面。 顶部 图片 地址
	 *  
	 *  对于  助理类订单，由于公用 一个 二级页面。因此 需要 此字段 
	 * 
	 */
	private String topImgUrl;	
	
	private List<PartnerServiceTypeVo> list;
	
	public List<PartnerServiceTypeVo> getList() {
		return list;
	}

	public void setList(List<PartnerServiceTypeVo> list) {
		this.list = list;
	}

	public String getTopImgUrl() {
		return topImgUrl;
	}

	public void setTopImgUrl(String topImgUrl) {
		this.topImgUrl = topImgUrl;
	}
	
}
