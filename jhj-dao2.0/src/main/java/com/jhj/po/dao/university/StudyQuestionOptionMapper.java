package com.jhj.po.dao.university;

import java.util.List;

import com.jhj.po.model.university.StudyQuestionOption;

public interface StudyQuestionOptionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StudyQuestionOption record);

    int insertSelective(StudyQuestionOption record);

    StudyQuestionOption selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StudyQuestionOption record);

    int updateByPrimaryKey(StudyQuestionOption record);
    
    //通过题目找选项
    List<StudyQuestionOption> selectByQId(Long qId);
    
    //批量删除
    void  deleteByIdList(List<Long> optionIdList);
    
}