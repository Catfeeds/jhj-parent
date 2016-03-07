package com.jhj.po.dao.survey;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jhj.po.model.survey.SurveyContent;

public interface SurveyContentMapper {
    int deleteByPrimaryKey(Long contentId);

    int insert(SurveyContent record);

    int insertSelective(SurveyContent record);

    SurveyContent selectByPrimaryKey(Long contentId);

    int updateByPrimaryKeySelective(SurveyContent record);

    int updateByPrimaryKey(SurveyContent record);
    
    List<SurveyContent> selectByListPage();
    
    List<SurveyContent> selectByIds(Long[] ids);
    
    //除了 赠送服务之外的 服务 
    List<SurveyContent>	selectAllContent();
    
    List<SurveyContent> selectByIdList(List<Long> idList);
    
    List<SurveyContent> selectFreeContent();
    
    //不同计费方式的 服务 的 id 的 集合
    List<Long> selectContentIdByMeasurement(@Param("measurement")Short measurement);
    
    //得到 所有的 "单项服务"(不包含子服务和 子服务是 单选的) 的 id
    List<Long> selectSingalContent();
    
    //得到 所有的 子服务是 多选的  服务的 id
    List<Long> selectBoxChildContent();
    
    //得到  子服务是 单选 或 多选的 服务的id
    List<Long> selectByChildType(@Param("childType")Short childType);
    
    //得到 所有 “基础家政” 服务,TODO  此处直接写死了 serviceId
    List<Long> selectBaseContent();
    
    //得到所有 “深度保洁” 服务,TODO 此处直接写死了 serviceId
    List<Long> selectDeepContent();
    
    //得到所有 “助理” 服务,TODO 此处直接写死了 serviceId
    List<Long> selectAmContent();
}