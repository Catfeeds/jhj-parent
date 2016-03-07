package com.jhj.po.dao.survey;

import java.util.List;

import com.jhj.po.model.survey.SurveyUser;

public interface SurveyUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SurveyUser record);

    int insertSelective(SurveyUser record);

    SurveyUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SurveyUser record);

    int updateByPrimaryKey(SurveyUser record);
    
    List<SurveyUser> selectByIdList(List<Long> list); 
}