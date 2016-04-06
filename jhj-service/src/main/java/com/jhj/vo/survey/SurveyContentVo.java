package com.jhj.vo.survey;

import java.math.BigDecimal;
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
	
	//2016-1-13 14:51:54, 对于 类似于 保洁的 服务, 默认次数为0,需要根据 选项,来确认其 需要展示的次数 
	private  Long  baseContentRealTime;
	
	
	private  List<SurveyContentChild> childList;
	
	
	public Long getBaseContentRealTime() {
		return baseContentRealTime;
	}

	public void setBaseContentRealTime(Long baseContentRealTime) {
		this.baseContentRealTime = baseContentRealTime;
	}

	public List<SurveyContentChild> getChildList() {
		return childList;
	}

	public void setChildList(List<SurveyContentChild> childList) {
		this.childList = childList;
	}

}
