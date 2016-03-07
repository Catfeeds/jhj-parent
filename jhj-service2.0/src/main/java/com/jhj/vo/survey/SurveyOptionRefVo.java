package com.jhj.vo.survey;

import java.util.List;
import java.util.Map;

import com.jhj.po.model.survey.SurveyQuestionOption;

/**
 * 
 *
 * @author :hulj
 * @Date : 2015年12月16日下午5:23:48
 * @Description: TODO
 *	
 */
public class SurveyOptionRefVo extends SurveyQuestionOption {
	
	private String  questionTitle;		//当前题目的名称
	
//	private Long defaultQId;				//当前选项
//	
	private List<String> optionNoList;		// 选项序号的结合	
	
	private Long flagId;			//标志位，判断是 新增还是修改
	
	private Map<Long, List<String>> optionMap;	// 下一题与对应选项的  <K,V>
	
	private Long nextQId;			// 表示关联的题目
	
	
	
	public List<String> getOptionNoList() {
		return optionNoList;
	}

	public void setOptionNoList(List<String> optionNoList) {
		this.optionNoList = optionNoList;
	}

	public Long getNextQId() {
		return nextQId;
	}

	public void setNextQId(Long nextQId) {
		this.nextQId = nextQId;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public Long getFlagId() {
		return flagId;
	}

	public void setFlagId(Long flagId) {
		this.flagId = flagId;
	}

	public Map<Long, List<String>> getOptionMap() {
		return optionMap;
	}

	public void setOptionMap(Map<Long, List<String>> optionMap) {
		this.optionMap = optionMap;
	}

	
}
