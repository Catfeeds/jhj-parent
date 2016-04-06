package com.jhj.service.survey;

import java.util.List;

import com.jhj.po.model.survey.SurveyContent;
import com.jhj.vo.survey.SurveyContentVo;
import com.jhj.vo.survey.SurveyServiceContentVo;

public interface SurveyContentService {
	
	int deleteByPrimaryKey(Long contentId);

    int insertSelective(SurveyContent record);

    SurveyContent selectByPrimaryKey(Long contentId);

    int updateByPrimaryKeySelective(SurveyContent record);
    
    List<SurveyContent> selectByListPage();
    
    SurveyContent initContent();
    
    SurveyServiceContentVo completeVo(SurveyContent content);
    
    List<SurveyContent>	selectAllContent();
    
    List<SurveyContent> selectByIds(Long[] ids);
    
    SurveyContentVo initContentVo();
    
    //不同计费方式的 服务 的 id 的 集合
    List<Long> selectContentIdByMeasurement(short mearsurement);
    
    List<SurveyContent> selectByIdList(List<Long> list);
    
    //得到 所有的 "单项服务"(不包含子服务和 子服务是 单选的) 的 id
    List<Long> selectSingalContent();
    
    //得到 所有的 子服务是 多选的  服务的 id
    List<Long> selectBoxChildContent();
    
    //得到  子服务是 单选 或 多选的 服务的id
    List<Long> selectByChildType(short childType);
    
    //得到 所有 “基础家政” 服务,TODO  此处直接写死了 serviceId
    List<Long> selectBaseContent();
    
    //得到所有 “深度保洁” 服务,TODO 此处直接写死了 serviceId
    List<Long> selectDeepContent();
    
    //得到所有 “助理” 服务,TODO 此处直接写死了 serviceId
    List<Long> selectAmContent();
    
    //默认次数由选项决定的 服务 2016-1-13 15:08:48
    List<Long> selectSetDefaultTime();
}
