package com.jhj.vo.university;

import java.util.List;

import com.jhj.po.model.university.StudyQuestion;
import com.jhj.po.model.university.StudyQuestionOption;

/**
 *
 * @author :hulj
 * @Date : 2015年12月4日下午4:42:34
 * @Description: 
 *		
 *
 *		题目 vo (包含题目选项)
 */
public class StudyQuestionVo extends StudyQuestion {

	private String serviceTypeName;		//服务类别名称
	
	private String bankName;			//题库名称
	
	private Long rightId;				//正确选项的  id
	
	private List<StudyQuestionOption> optionList; //选项列表
	
	private String questionTypeName;	//题型名称   单选、多选
	
	private String isNeedName;			//是否必考的名称    *必考  、 非必考
	
	
	public String getIsNeedName() {
		return isNeedName;
	}

	public void setIsNeedName(String isNeedName) {
		this.isNeedName = isNeedName;
	}

	public String getQuestionTypeName() {
		return questionTypeName;
	}

	public void setQuestionTypeName(String questionTypeName) {
		this.questionTypeName = questionTypeName;
	}

	public Long getRightId() {
		return rightId;
	}

	public void setRightId(Long rightId) {
		this.rightId = rightId;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public List<StudyQuestionOption> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<StudyQuestionOption> optionList) {
		this.optionList = optionList;
	}
	
	
}
