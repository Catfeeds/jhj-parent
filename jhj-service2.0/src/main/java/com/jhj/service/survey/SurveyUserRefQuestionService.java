package com.jhj.service.survey;

import com.jhj.po.model.survey.SurveyUserRefQuestion;

public interface SurveyUserRefQuestionService {
	
	int deleteByPrimaryKey(Long id);
	
    int insertSelective(SurveyUserRefQuestion record);
    
    SurveyUserRefQuestion selectByPrimaryKey(Long id);
    
    int updateByPrimaryKeySelective(SurveyUserRefQuestion record);
    
    SurveyUserRefQuestion  initUserRefQ();
    
    
}
