package com.jhj.service.university;

import java.util.List;

import com.jhj.po.model.university.StudyQuestionOption;

/**
 *
 * @author :hulj
 * @Date : 2015年12月8日上午11:44:19
 * @Description: 
 *		
 *		选项 service
 *	
 */
public interface StudyQuestionOptionService {

    int deleteByPrimaryKey(Long id);

    int insertSelective(StudyQuestionOption record);

    StudyQuestionOption selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StudyQuestionOption record);
	
    //通过题目找选项
    List<StudyQuestionOption> selectByQId(Long qId);
    
    StudyQuestionOption  initQuestionOption();
    
    //批量删除
    void  deleteByIdList(List<Long> optionIdList);
    
}
