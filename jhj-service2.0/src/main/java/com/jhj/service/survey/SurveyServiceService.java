package com.jhj.service.survey;

import java.util.List;

import com.jhj.po.model.survey.SurveyService;


/**
 * 
 *
 * @author :hulj
 * @Date : 2015年12月12日下午4:01:53
 * @Description: 
 *	
 *		服务大类（服务类别：基础家政、深度保洁.....）
 */
public interface SurveyServiceService {
	
	int deleteByPrimaryKey(Long serviceId);

    int insertSelective(SurveyService record);

    SurveyService selectByPrimaryKey(Long serviceId);

    int updateByPrimaryKeySelective(SurveyService record);

    List<SurveyService> selectByListPage();
	
    SurveyService initService();
}
