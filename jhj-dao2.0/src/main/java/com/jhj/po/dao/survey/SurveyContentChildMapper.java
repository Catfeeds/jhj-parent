package com.jhj.po.dao.survey;

import java.util.List;

import com.jhj.po.model.survey.SurveyContentChild;

public interface SurveyContentChildMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SurveyContentChild record);

    int insertSelective(SurveyContentChild record);

    SurveyContentChild selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SurveyContentChild record);

    int updateByPrimaryKey(SurveyContentChild record);
    
    //根据服务内容id, 得到所有子服务选项
    List<SurveyContentChild> selectByContentId(Long contentId);
    
    //批量删除
    void  deleteByContentId(Long contentId);
}