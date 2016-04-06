package com.jhj.service.impl.survey;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.model.survey.SurveyContent;
import com.jhj.po.model.survey.SurveyUser;
import com.jhj.po.model.survey.SurveyUserRefRecommend;
import com.jhj.service.survey.SurveyContentChildService;
import com.jhj.service.survey.SurveyContentService;
import com.jhj.service.survey.SurveyResultService;
import com.jhj.service.survey.SurveyUserRefRecommendService;
import com.jhj.service.survey.SurveyUserService;
import com.jhj.vo.survey.OaSurveyContentVo;
import com.jhj.vo.survey.OaSurveyResultVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.MathBigDeciamlUtil;
import com.meijia.utils.OneCareUtil;

/*
 * 	运营平台--结果展示
 */
@Service
public class SurveyResultImpl implements SurveyResultService {
	
	@Autowired
	private SurveyUserRefRecommendService recommendService;
	
	@Autowired
	private SurveyUserService surveyUserService;
	
	@Autowired
	private SurveyContentService contentService;
	
	@Autowired
	private SurveyContentChildService contentChildService;
	
	
	@Override
	public OaSurveyResultVo complateVo(SurveyUserRefRecommend recommend) {
		
		OaSurveyResultVo resultVo = initResultVo();
		
		OaSurveyContentVo contentVo = initOaContentVo();
		
		//用户相关信息
		
		Long userId = recommend.getUserId();
		
		SurveyUser surveyUser = surveyUserService.selectByPrimaryKey(userId);
		
		resultVo.setUserName(surveyUser.getUserName());
		resultVo.setUserMobile(surveyUser.getMobile());
		
		//服务内容相关
		
		//1. 无子服务
		
		if(recommend.getContentChildId() == 0L){
			
			Long contentId = recommend.getContentId();
			
			SurveyContent content = contentService.selectByPrimaryKey(contentId);
			
			//无子服务的  服务的  价格（xx元/次）
			BigDecimal contentPrice = content.getPrice();
			
			Long times = recommend.getTimes();
			
			//计费方式  计数期限  0=月 1=年 2=次 3=无（赠送，价钱为0）
			Short measurement = content.getMeasurement();
			
			
			contentVo.setContentName(content.getName());
			contentVo.setContentSumPrice(contentPrice.multiply(new BigDecimal(times)));
			contentVo.setMeasureName(OneCareUtil.getSurveyMeasureMentName(measurement));
			
		}
		
		
		
		
		return null;
	}


	@Override
	public BigDecimal calculatePriceByYear(SurveyContent content) {
		
		
		
		
		return null;
	}

	@Override
	public OaSurveyContentVo initOaContentVo() {
		
		OaSurveyContentVo contentVo = new OaSurveyContentVo();
		
		contentVo.setContentName("");
		contentVo.setMeasureName("");
		contentVo.setContentSumPrice(new BigDecimal(0));
		
		return contentVo;
	}
	
	@Override
	public OaSurveyResultVo initResultVo() {
		
		SurveyUserRefRecommend recommend = recommendService.initRecommend();
		
		OaSurveyResultVo resultVo = new OaSurveyResultVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(recommend, resultVo);
		
		resultVo.setUserName("");
		resultVo.setUserMobile("");
		resultVo.setSumPriceByYear(new BigDecimal(0));
		resultVo.setContentList(new ArrayList<OaSurveyContentVo>());
		
		
		return resultVo;
	}

}
