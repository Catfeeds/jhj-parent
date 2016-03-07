package com.jhj.po.dao.survey;

import java.util.List;

import com.jhj.po.model.survey.SurveyBank;

public interface SurveyBankMapper {
    int deleteByPrimaryKey(Long bankId);

    int insert(SurveyBank record);

    int insertSelective(SurveyBank record);

    SurveyBank selectByPrimaryKey(Long bankId);

    int updateByPrimaryKeySelective(SurveyBank record);

    int updateByPrimaryKey(SurveyBank record);
    
    List<SurveyBank> selectByListPage();
    
}