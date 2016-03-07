package com.jhj.action.app.survey;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.Constants;
import com.jhj.service.survey.SurveyContentService;
import com.jhj.service.survey.SurveyOptionRefContentService;
import com.jhj.service.survey.SurveyOptionRefNextQService;
import com.jhj.service.survey.SurveyQuestionOptionService;
import com.jhj.service.survey.SurveyQuestionServcie;
import com.jhj.service.survey.SurveyUserRefQuestionService;
import com.jhj.vo.survey.SurveyContentVo;
import com.jhj.vo.survey.SurveyQuestionVo;
import com.meijia.utils.StringUtil;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年12月22日下午5:27:19
 * @Description: 
 *		
 *		问卷调查--答题相关
 */
@Controller
@RequestMapping(value = "/app/survey")
public class SurveyQuestionController extends BaseController {
	
	@Autowired
	private SurveyQuestionServcie questionService;
	@Autowired
	private SurveyOptionRefNextQService  nextQService;
	
	@Autowired
	private SurveyUserRefQuestionService  userRefQService;
 	@Autowired
	private SurveyQuestionOptionService optionService;
	
 	@Autowired
 	private SurveyOptionRefContentService optionRefContentService;
 	@Autowired
 	private SurveyContentService contentService;
 	
	/**
	 * 
	 *  @Title: loadQuestion
	  * @Description:    加载题目  -->加载第一题和 下一题
	  * @param qId	当前题目的id
	  * 	
	  *    1.如果加载的第一题,则当前题目Id 为0
	  *    2.其他则为  survey_question表中的  qId	
	  * 
	  * @return AppResultData<Object>    返回类型
	  * @throws
	 */
	@RequestMapping(value = "load_question.json",method = RequestMethod.GET)
	public AppResultData<Object> loadQuestion(
			@RequestParam(value = "q_id",required = false,defaultValue="0")Long qId,
			@RequestParam(value = "option_str",required = false,defaultValue = "")String optionStr){
	
		AppResultData<Object> result =  new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		if(qId == null){
			result.setStatus(Constants.ERROR_999);
			result.setMsg("题目不存在");
			return result;
		}
		
		SurveyQuestionVo questionVo = questionService.selectNextQuestion(qId, optionStr);
		
		result.setData(questionVo);
		
		return result;
	}
	
	/**
	 *  提交问卷调查
	 *      参数
	 *      	1>调查时填写的用户信息,确定的  userId	
	 *  		2>每道题及其对应的选项  --> [{"question1":q1,"optionStr":A,B},....]
	 *  	返回	
	 *   		1>选择结果对应的服务	
	 *  	    2>未选择的作为推荐套餐
	 *  		3>全部的赠送套餐
	 * @throws JSONException 
	 */    
		
	@RequestMapping(value = "survey_result.json",method = RequestMethod.POST)
	public AppResultData<Object> submitSurveyResult(
			@RequestParam(value = "survey_user_id",required = true,defaultValue = "0") Long surveyUserId,
			@RequestParam(value="survey_result",required = true,defaultValue="") String surveyResult) throws JSONException{
		
		AppResultData<Object> result =  new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		if(surveyUserId == 0L || StringUtil.isEmpty(surveyResult)){
			
			result.setStatus(Constants.ERROR_999);
			result.setMsg("参数不合法");
			
			return result;
		}
		
		/*
		 * 此时只是一个返回结果,并未在 数据库中 生成任何记录
		 */
		Map<Long, List<SurveyContentVo>> map = questionService.completeResultVo(surveyResult);
		
		result.setData(map);
		
		
		return result;
	}
	
	
}
