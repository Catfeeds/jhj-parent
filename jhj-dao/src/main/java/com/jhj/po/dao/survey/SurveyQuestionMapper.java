package com.jhj.po.dao.survey;

import java.util.List;

import com.jhj.po.model.survey.SurveyQuestion;

public interface SurveyQuestionMapper {
    int deleteByPrimaryKey(Long qId);

    int insert(SurveyQuestion record);

    int insertSelective(SurveyQuestion record);

    SurveyQuestion selectByPrimaryKey(Long qId);

    int updateByPrimaryKeySelective(SurveyQuestion record);

    int updateByPrimaryKey(SurveyQuestion record);
    
    List<SurveyQuestion> selectByListPage();
    
    //得到第一题，录入时  isFirst指明了题目的位置
    SurveyQuestion selectFirstQuestion();
    
}