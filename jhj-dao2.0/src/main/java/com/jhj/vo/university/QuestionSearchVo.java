package com.jhj.vo.university;

/**
 *
 * @author :hulj
 * @Date : 2015年12月4日下午5:11:26
 * @Description: 
 *		
 *		题目管理   searchVo
 */
public class QuestionSearchVo {
	
	private Long serviceTypeId;	//服务类别
	
	private Long bankId;		//题库,需要和服务类别构成联动
	
	private Short isNeed;		//是否必考

	
	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public Short getIsNeed() {
		return isNeed;
	}

	public void setIsNeed(Short isNeed) {
		this.isNeed = isNeed;
	}
	
}
