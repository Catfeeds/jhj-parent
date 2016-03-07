package com.jhj.po.dao.survey;

import java.util.List;

import com.jhj.po.model.survey.SurveyService;

public interface SurveyServiceMapper {
    int deleteByPrimaryKey(Long serviceId);

    int insert(SurveyService record);

    int insertSelective(SurveyService record);

    SurveyService selectByPrimaryKey(Long serviceId);

    int updateByPrimaryKeySelective(SurveyService record);

    int updateByPrimaryKey(SurveyService record);
    
    List<SurveyService> selectByListPage();
}