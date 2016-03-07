package com.jhj.service.survey;

import java.util.List;
import java.util.Map;

import com.jhj.po.model.survey.SurveyQuestionOption;
import com.jhj.vo.survey.SurveyOptionRefVo;

public interface SurveyQuestionOptionService {
	
	int deleteByPrimaryKey(Long id);

    int insertSelective(SurveyQuestionOption record);

    SurveyQuestionOption selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SurveyQuestionOption record);

    List<SurveyQuestionOption>  selectByQId(Long qId);
	
    void deleteByIdList(List<Long> optionIdList);
    	
    SurveyQuestionOption  initOption();
    
    SurveyOptionRefVo initOptionRefVo();
    
    //根据题目和序号，得到该选项的id
    List<SurveyQuestionOption> selectByQIdAndNo(Map<String, Object> map);
    
    SurveyQuestionOption  selectOneByQIdAndNo(Long qId, String optionNo);
    
}	
