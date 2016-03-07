package com.jhj.oa.vo.account;

import org.hibernate.validator.constraints.NotEmpty;

public class AdminRoleEditVo {
	private Integer id;
	@NotEmpty(message="{name.not.empty}")
	private String name;

	public void setId(Integer id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}

	public Integer getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
}
