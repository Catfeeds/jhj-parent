package com.jhj.service.survey;

import java.util.List;

import com.jhj.po.model.survey.SurveyContentChild;

public interface SurveyContentChildService {
	
	int deleteByPrimaryKey(Long id);

    int insertSelective(SurveyContentChild record);

    SurveyContentChild selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SurveyContentChild record);

    SurveyContentChild  initContentChild();
    
    List<SurveyContentChild> selectByContentId(Long contentId);
    
    //批量删除
    void  deleteByContentId(Long contentId);
    
    
    
}
