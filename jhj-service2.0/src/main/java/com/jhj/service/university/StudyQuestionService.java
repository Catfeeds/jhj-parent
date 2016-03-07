package com.jhj.service.university;

import java.util.List;
import java.util.Map;

import com.jhj.po.model.university.StudyQuestion;
import com.jhj.vo.university.AppUniversityQuestionVo;
import com.jhj.vo.university.QuestionSearchVo;
import com.jhj.vo.university.StudyQuestionVo;

/**
 *
 * @author :hulj
 * @Date : 2015年12月4日下午4:29:07
 * @Description: 
 *	
 */
public interface StudyQuestionService {
	
	int deleteByPrimaryKey(Long qId);

    int insertSelective(StudyQuestion record);

    StudyQuestion selectByPrimaryKey(Long qId);

    int updateByPrimaryKeySelective(StudyQuestion record);
    
    StudyQuestion  initStudyQuestion();
    
    StudyQuestionVo initQuestionVo();
    
    //平台--大学--题目管理
    List<StudyQuestion> selectByListPage(QuestionSearchVo searchVo);
    
    StudyQuestionVo	 completeVo(StudyQuestion question);
    
    //根据 选项的 list 集合，生成该选项对应的 序号 A B C等。。
    Map<String, String>  generateNoForOption(List<String> optionList);
    
    //根据 正确选项的 索引的集合, 得到 正确选项对应的 序号的 集合
    List<String> getRightOptionNos(List<String> rightOptionList);
    
    //根据bankId，得到题库下的所有题目 id
    List<Long> selectByBankId(Long bankId);
    
    //app 加载题目 转为 VO
    AppUniversityQuestionVo translateToAppVo(Long qId);
    
}
