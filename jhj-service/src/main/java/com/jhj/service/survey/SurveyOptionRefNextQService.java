package com.jhj.service.survey;

import java.util.List;

import org.json.JSONException;

import com.jhj.po.model.survey.SurveyOptionRefNextQ;

public interface SurveyOptionRefNextQService {
	
	int deleteByPrimaryKey(Long id);

    int insertSelective(SurveyOptionRefNextQ record);

    SurveyOptionRefNextQ selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SurveyOptionRefNextQ record);
    
    List<SurveyOptionRefNextQ> selectByQId(Long qId);
    
    SurveyOptionRefNextQ  initNextQ();
    
    void  completeVo(Long qId,String opStr) throws JSONException;
    
    //根据 选择的题目 和 选择的选项,得到对应的对象,进而得到 下一题的题目 id
    SurveyOptionRefNextQ  selectNextQ(Long qId,String optionStr);
    
}
