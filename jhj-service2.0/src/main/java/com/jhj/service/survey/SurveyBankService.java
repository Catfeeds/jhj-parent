package com.jhj.service.survey;

import java.util.List;

import com.jhj.po.model.survey.SurveyBank;

public interface SurveyBankService {
	
	int deleteByPrimaryKey(Long bankId);

    int insertSelective(SurveyBank record);

    SurveyBank selectByPrimaryKey(Long bankId);

    int updateByPrimaryKeySelective(SurveyBank record);

    List<SurveyBank> selectByListPage();
    
    SurveyBank  initBank();
	
}
