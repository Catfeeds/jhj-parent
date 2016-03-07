package com.jhj.service.survey;

import java.util.List;
import java.util.Map;

import com.jhj.po.model.survey.SurveyUserRefRecommend;
import com.jhj.vo.survey.OaSurveyUserResultVo;
import com.jhj.vo.survey.SurveyResultPriceVo;

public interface SurveyUserRefRecommendService {
	
	int deleteByPrimaryKey(Long id);

    int insertSelective(SurveyUserRefRecommend record);

    SurveyUserRefRecommend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SurveyUserRefRecommend record);
	
    SurveyUserRefRecommend  initRecommend();
    
    List<Long> selectByListPage();
    
    List<List<SurveyUserRefRecommend>> selectByResultNo(List<String> flagList);
    
    List<SurveyUserRefRecommend> selectByUserId(Long userId);
    
    OaSurveyUserResultVo initOaResultVo();
    
    
    //根据用户id 和 contentId 得到 , 该用户选择的 该 单项服务（子服务不是多选） 的 次数
    SurveyUserRefRecommend selectByUserIdAndContentId(Long userId,Long contentId);
    
    Map<Short, SurveyResultPriceVo> getResultPrice(Long userId);
    
    //删除某用户的 所有选择结果
    void deleteByUserId(Long userId);
}
