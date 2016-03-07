package com.jhj.vo.university;

import java.util.List;

import com.jhj.po.model.university.StudyQuestion;
import com.jhj.po.model.university.StudyQuestionOption;

/**
 *
 * @author :hulj
 * @Date : 2016年1月16日下午4:33:31
 * @Description: 
 *	
 *	
 */
public class AppUniversityQuestionVo{

	private StudyQuestion question;
	
	private List<StudyQuestionOption> optionList;

	public StudyQuestion getQuestion() {
		return question;
	}

	public void setQuestion(StudyQuestion question) {
		this.question = question;
	}

	/**
	 * @return the optionList
	 */
	public List<StudyQuestionOption> getOptionList() {
		return optionList;
	}

	/**
	 * @param optionList the optionList to set
	 */
	public void setOptionList(List<StudyQuestionOption> optionList) {
		this.optionList = optionList;
	}

}
