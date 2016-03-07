package com.jhj.po.dao.survey;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jhj.po.model.survey.SurveyUserRefRecommend;

public interface SurveyUserRefRecommendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SurveyUserRefRecommend record);

    int insertSelective(SurveyUserRefRecommend record);

    SurveyUserRefRecommend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SurveyUserRefRecommend record);

    int updateByPrimaryKey(SurveyUserRefRecommend record);
    
    List<Long> selectUserIdByListPage();
    
    List<List<SurveyUserRefRecommend>> selectByResultNo(List<String> flagList);
    
    List<SurveyUserRefRecommend> selectByUserId(Long userId);
    
    //根据用户id 和 contentId 得到 , 该用户选择的 该 单项服务（子服务不是多选） 的 次数
    SurveyUserRefRecommend selectByUserIdAndContentId(@Param("userId")Long userId,@Param("contentId") Long contentId);
    
    //根据用户 Id 删除 记录
    void deleteByUserId(@Param("userId")Long userId);
}

