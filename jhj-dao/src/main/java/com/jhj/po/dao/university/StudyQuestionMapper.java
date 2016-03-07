package com.jhj.po.dao.university;

import java.util.List;

import com.jhj.po.model.university.StudyQuestion;
import com.jhj.vo.university.QuestionSearchVo;

public interface StudyQuestionMapper {
    int deleteByPrimaryKey(Long qId);

    int insert(StudyQuestion record);

    int insertSelective(StudyQuestion record);

    StudyQuestion selectByPrimaryKey(Long qId);

    int updateByPrimaryKeySelective(StudyQuestion record);

    int updateByPrimaryKey(StudyQuestion record);
    
    
    List<StudyQuestion> selectByListPage(QuestionSearchVo searchVo);
    
    //根据bankId，得到题库下的所有题目 id
    List<Long> selectByBankId(Long bankId);
    
    
}