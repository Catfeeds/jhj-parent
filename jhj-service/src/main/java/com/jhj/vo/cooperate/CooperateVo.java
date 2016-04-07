package com.jhj.vo.cooperate;

import com.jhj.po.model.cooperate.CooperativeBusiness;

/**
 *
 * @author :hulj
 * @Date : 2016年4月7日下午2:17:49
 * @Description: 
 *		
 *	  表单页 vo
 */
public class CooperateVo extends CooperativeBusiness {
	
	private String confirmPassWord;

	public String getConfirmPassWord() {
		return confirmPassWord;
	}

	public void setConfirmPassWord(String confirmPassWord) {
		this.confirmPassWord = confirmPassWord;
	}
	
}
