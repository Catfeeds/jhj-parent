package com.jhj.vo.university;

import com.jhj.po.model.university.StudyLearning;

/**
 *
 * @author :hulj
 * @Date : 2015年12月3日下午2:54:27
 * @Description: 
 * 
 * 		后台--叮当大学--培训学习Vo
 *
 */
public class StudyVo extends StudyLearning {
	
	private String name;	//service_type_id对应的名字

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
