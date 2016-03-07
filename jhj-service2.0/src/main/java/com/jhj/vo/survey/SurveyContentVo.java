package com.jhj.vo.survey;

import java.util.List;

import com.jhj.po.model.survey.SurveyContent;
import com.jhj.po.model.survey.SurveyContentChild;

/**
 * 
 *
 * @author :hulj
 * @Date : 2015年12月24日下午4:13:02
 *		
 *		提交问卷调查后的返回  vo
 *			综合处理可能包含  子服务的 content
 *
 */
public class SurveyContentVo extends SurveyContent {
	
	
	
	private  List<SurveyContentChild> childList;
	

	public List<SurveyContentChild> getChildList() {
		return childList;
	}

	public void setChildList(List<SurveyContentChild> childList) {
		this.childList = childList;
	}

}
