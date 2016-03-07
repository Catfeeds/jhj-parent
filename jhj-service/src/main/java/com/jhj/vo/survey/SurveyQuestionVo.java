package com.jhj.vo.survey;

import java.util.List;

import com.jhj.po.model.survey.SurveyQuestion;
import com.jhj.po.model.survey.SurveyQuestionOption;

public class SurveyQuestionVo extends SurveyQuestion {
	
	private String bankName;			//题库名称
	
	private List<SurveyQuestionOption> optionList; //选项列表
	
	private String questionTypeName;	//题型名称   单选、多选
	
	private String optionReContent;		//选项关联 的附加内容 名称
	
	private String optionReQName;		//选项关联的下一题的  题目

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public List<SurveyQuestionOption> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<SurveyQuestionOption> optionList) {
		this.optionList = optionList;
	}

	public String getQuestionTypeName() {
		return questionTypeName;
	}

	public void setQuestionTypeName(String questionTypeName) {
		this.questionTypeName = questionTypeName;
	}

	public String getOptionReContent() {
		return optionReContent;
	}

	public void setOptionReContent(String optionReContent) {
		this.optionReContent = optionReContent;
	}

	public String getOptionReQName() {
		return optionReQName;
	}

	public void setOptionReQName(String optionReQName) {
		this.optionReQName = optionReQName;
	}
	
	
}
