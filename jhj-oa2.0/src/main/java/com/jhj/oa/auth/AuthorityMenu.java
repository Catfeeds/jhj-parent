package com.jhj.oa.auth;

import java.util.List;

public class AuthorityMenu {

	private Long id;
	private String name;
	private String itemIcon;
	private String url;
	private List<AuthorityMenu> childrens;

	public AuthorityMenu(Long id, String name, String itemIcon, String url){
		this.id=id;
		this.name=name;
		this.itemIcon=itemIcon;
		this.url=url;
	}

	public AuthorityMenu(Long id, String name,  String itemIcon, String url, List<AuthorityMenu> childrens){
		this.id=id;
		this.name=name;
		this.itemIcon=itemIcon;
		this.url=url;
		this.childrens=childrens;
	}

	public void setId(Long id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setItemIcon(String itemIcon){
		this.itemIcon=itemIcon;
	}
	public void setUrl(String url){
		this.url=url;
	}
	public void setChildrens(List<AuthorityMenu> childrens){
		this.childrens=childrens;
	}

	public Long getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public String getItemIcon(){
		return this.itemIcon;
	}
	public String getUrl(){
		return this.url;
	}
	public List<AuthorityMenu> getChildrens(){
		return this.childrens;
	}

}