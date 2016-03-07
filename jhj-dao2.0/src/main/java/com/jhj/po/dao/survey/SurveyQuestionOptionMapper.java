package com.jhj.po.dao.survey;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jhj.po.model.survey.SurveyQuestionOption;

public interface SurveyQuestionOptionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SurveyQuestionOption record);

    int insertSelective(SurveyQuestionOption record);

    SurveyQuestionOption selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SurveyQuestionOption record);

    int updateByPrimaryKey(SurveyQuestionOption record);
    
    List<SurveyQuestionOption>  selectByQId(Long qId);
    
    //批量删除
    void  deleteByIdList(List<Long> optionIdList);
    
    //根据 题目 和 序号 得到 选项id
    List<SurveyQuestionOption> selectByQIdAndNo(Map<String, Object> map);
    
    SurveyQuestionOption  selectOneByQIdAndNo(@Param("qId") Long qId, @Param("optionNo") String optionNo);
}