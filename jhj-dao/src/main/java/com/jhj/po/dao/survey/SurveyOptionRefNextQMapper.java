package com.jhj.po.dao.survey;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jhj.po.model.survey.SurveyOptionRefNextQ;

public interface SurveyOptionRefNextQMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SurveyOptionRefNextQ record);

    int insertSelective(SurveyOptionRefNextQ record);

    SurveyOptionRefNextQ selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SurveyOptionRefNextQ record);

    int updateByPrimaryKey(SurveyOptionRefNextQ record);
    
    List<SurveyOptionRefNextQ> selectByQId(Long qId);
    
    
  //根据 选择的题目 和 选择的选项,得到对应的对象,进而得到 下一题的题目 id
    SurveyOptionRefNextQ  selectNextQ(@Param("qId")Long qId,@Param("optionStr")String optionStr);
    
    
    //新增选项后，需要重置   选项和 下一题的 关联关系时, 先删除之前的关联关系
    void deleteByQId(@Param("qId") Long qId);
    
    
}