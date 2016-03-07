package com.jhj.service.survey;

import java.math.BigDecimal;

import com.jhj.po.model.survey.SurveyContent;
import com.jhj.po.model.survey.SurveyUserRefRecommend;
import com.jhj.vo.survey.OaSurveyContentVo;
import com.jhj.vo.survey.OaSurveyResultVo;


/*
 * 问卷调查结果 --service,  运营 平台用
 */
public interface SurveyResultService {
	
	//页面 展示 vo，包含服务内容、用户信息等。
	OaSurveyResultVo	initResultVo();
	//页面展示 服务内容Vo
	OaSurveyContentVo initOaContentVo();
	
	OaSurveyResultVo	complateVo(SurveyUserRefRecommend recommend);
	
	BigDecimal calculatePriceByYear(SurveyContent content);
	
	
}
