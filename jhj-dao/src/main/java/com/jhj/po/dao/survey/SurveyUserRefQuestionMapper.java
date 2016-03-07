package com.jhj.po.dao.survey;

import com.jhj.po.model.survey.SurveyUserRefQuestion;

public interface SurveyUserRefQuestionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SurveyUserRefQuestion record);

    int insertSelective(SurveyUserRefQuestion record);

    SurveyUserRefQuestion selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SurveyUserRefQuestion record);

    int updateByPrimaryKey(SurveyUserRefQuestion record);
}