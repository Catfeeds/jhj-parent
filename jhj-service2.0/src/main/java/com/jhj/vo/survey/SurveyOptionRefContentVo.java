package com.jhj.vo.survey;

import java.util.List;

import com.jhj.po.model.survey.SurveyContent;
import com.jhj.po.model.survey.SurveyOptionRefContent;

public class SurveyOptionRefContentVo extends SurveyOptionRefContent {
	
	private String questionTitle;	//当前题目
	
	private List<SurveyContent> contentList;		//所有的服务内容的 id 
	
//	private List<Long> selectContentList;	//选中的服务内容id
	
	private String selectContent;		
	

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}


	public List<SurveyContent> getContentList() {
		return contentList;
	}

	public void setContentList(List<SurveyContent> contentList) {
		this.contentList = contentList;
	}

	public String getSelectContent() {
		return selectContent;
	}

	public void setSelectContent(String selectContent) {
		this.selectContent = selectContent;
	}
	
	
	
//	public List<Long> getSelectContentList() {
//		return selectContentList;
//	}
//
//	public void setSelectContentList(List<Long> selectContentList) {
//		this.selectContentList = selectContentList;
//	}


	
	
}
