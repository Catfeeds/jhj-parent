package com.jhj.service.survey;

import java.util.List;

import com.jhj.po.model.survey.SurveyUser;

public interface SurveyUserService {
	
	int deleteByPrimaryKey(Long id);

    int insertSelective(SurveyUser record);

    SurveyUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SurveyUser record);
    
    SurveyUser initUser();
    
    List<SurveyUser> selectByIdList(List<Long> useIdList);
}
