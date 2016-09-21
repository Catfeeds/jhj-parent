package com.jhj.po.dao.survey;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jhj.po.model.survey.SurveyOptionRefContent;

public interface SurveyOptionRefContentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SurveyOptionRefContent record);

    int insertSelective(SurveyOptionRefContent record);

    SurveyOptionRefContent selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SurveyOptionRefContent record);

    int updateByPrimaryKey(SurveyOptionRefContent record);
    
    List<SurveyOptionRefContent>  selectByQId(Long qId);
    
    SurveyOptionRefContent  selectOneByQIdAndNo(@Param("qId")Long qId, @Param("optionNo")String optionNo);
    
}