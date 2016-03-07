package com.jhj.service.survey;

import java.util.List;
import java.util.Map;





import org.json.JSONException;

import com.jhj.po.model.survey.SurveyContent;
import com.jhj.po.model.survey.SurveyQuestion;
import com.jhj.vo.survey.SurveyContentVo;
import com.jhj.vo.survey.SurveyQuestionVo;

public interface SurveyQuestionServcie {
	
	int deleteByPrimaryKey(Long qId);

    int insertSelective(SurveyQuestion record);

    SurveyQuestion selectByPrimaryKey(Long qId);

    int updateByPrimaryKeySelective(SurveyQuestion record);
    
    SurveyQuestion  initQuestion();
    
    List<SurveyQuestion> selectByListPage();
    
    SurveyQuestionVo  completeVo(SurveyQuestion question);
    
    SurveyQuestionVo  initVo();
    
    //根据 选项的 list 集合，生成该选项对应的 序号 A B C等。。
    Map<String, String>  generateNoForOption(List<String> optionList);
    
    //得到第一题，录入时  isFirst指明了题目的位置
    SurveyQuestion selectFirstQuestion();
    
    //得到下一题
    SurveyQuestionVo selectNextQuestion(Long qId,String optionStr);
    
    /**
     * 处理 答题结果
     * @throws JSONException 
     */
    
    /*
     * 根据 提交问卷时的结果,确定  最终结果页面的内容展示
     * 	
     * 	构造map 返回 -->{<"结果套餐":xxxlist>,<"赠送套餐":xxxlist>,<"推荐套餐":xxxlist>}
     * 
     *  参数：每道题及其对应的选项  --> [{"question1":q1,"optionStr":A,B},....]
     */
    Map<Long, List<SurveyContentVo>> completeResultVo(String surveyResult) throws JSONException;
    
    //根据 部分内容的list,得到该内容及其 子服务内容的 list
    List<SurveyContentVo> getChildVoForContent(List<SurveyContent> contentList);
}
