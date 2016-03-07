package com.jhj.service.survey;

import java.util.List;

import com.jhj.po.model.survey.SurveyOptionRefContent;
import com.jhj.vo.survey.SurveyOptionRefContentVo;


/**
 * 
 *
 * @author :hulj
 * @Date : 2015年12月12日下午4:01:53
 * @Description: 
 *	
 *		选项和附加内容的关联关系
 */
public interface SurveyOptionRefContentService {
	
	int deleteByPrimaryKey(Long id);

    int insertSelective(SurveyOptionRefContent record);

    SurveyOptionRefContent selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SurveyOptionRefContent record);
    
    SurveyOptionRefContent initRefContent();
    
    SurveyOptionRefContentVo initVo();
    
    List<SurveyOptionRefContent>  selectByQId(Long qId);
    
    SurveyOptionRefContent  selectOneByQIdAndNo(Long qId, String optionNo);
    
}
