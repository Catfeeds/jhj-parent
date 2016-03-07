package com.jhj.vo;
/**
 * @description：
 * @author： kerryg
 * @date:2015年9月7日 
 */
public class SocialsSearchVo {
	
	 private String title;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title !=null ? title.trim():new String();
	}
}
