package com.jhj.action.survey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.po.model.survey.SurveyOptionRefNextQ;
import com.jhj.po.model.survey.SurveyQuestion;
import com.jhj.po.model.survey.SurveyQuestionOption;
import com.jhj.service.survey.SurveyOptionRefNextQService;
import com.jhj.service.survey.SurveyQuestionOptionService;
import com.jhj.service.survey.SurveyQuestionServcie;
import com.jhj.vo.survey.SurveyOptionRefVo;
import com.jhj.vo.survey.SurveyQuestionVo;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年12月11日下午3:53:17
 * @Description: 
 * 		题目
 */
@Controller
@RequestMapping(value = "/survey")
public class SurveyQuestionController extends BaseController {
	
	
	@Autowired
	private SurveyQuestionServcie questionService;
	
	@Autowired
	private SurveyQuestionOptionService optionService;
	
	@Autowired
	private SurveyOptionRefNextQService nextQService;
	
	@RequestMapping(value  ="question_list",method = RequestMethod.GET)
	public String contentList(HttpServletRequest request,Model model){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		List<SurveyQuestion> list = questionService.selectByListPage();
		
		SurveyQuestion question = null;
		
		for (int i = 0; i < list.size(); i++) {
			question = list.get(i);
			
			SurveyQuestionVo completeVo = questionService.completeVo(question);
			
			list.set(i, completeVo);
		}
		
		PageInfo result = new PageInfo(list);	
		
		model.addAttribute("questionListModel", result);
		
		return "survey/questionList";
		
	}
	
	/*
	 * 跳转到 form页
	 */
	@RequestMapping(value = "question_form",method = RequestMethod.GET)
	public String contentForm(HttpServletRequest request, Model model,
					@RequestParam("id") Long id){
								
		if(id == null){
			id = 0L;
		}
		
		SurveyQuestionVo questionVo = questionService.initVo();
		
		if( id !=null && id >0L){
			
			SurveyQuestion surveyQuestion = questionService.selectByPrimaryKey(id);
			
			questionVo = questionService.completeVo(surveyQuestion);
		}
		
		model.addAttribute("questionVoFormModel", questionVo);
		
		return "survey/questionForm";
	}
	
	
	/**
	 * 
	 *  @Title: submitQuestionForm
	  * @Description: 
	  * 		
	  * 	录入题目 及其  选项	
	  * 
	  * 	1. 生成题目
	  * 	2. 生成 选项，及其次数
	  * 		
	  * @param id			页面标识。新增还是修改
	  * @param bankId		题库Id
	  * @param title			题干
	  * @param isMulti		是否多选    0= 单选  1=多选
	  * @param isFirst		题目位置	0 = 第一题    1=位于中间   2=最后一题	
	  * @param beforeQId		上一题的 题目 id
	  * @param optionArray	
	  * 				
	  * 			json数组  	[{"选项内容"：xx,"选项默认的次数"：xx}, ...]
	  * 		
	  * 			“选项默认的次数” 主要针对 
	  * 				如   "保洁每周几次？"，选项 A 1-2次  B 3-4次		
	  * 			 为A/B 默认次数 为 2 、4.. 供答题结束后的默认次数 展示		
	  * 
	  * @throws JSONException    设定文件
	  * @return AppResultData<Object>    返回类型
	 */
	@RequestMapping(value = "question_form",method = RequestMethod.POST)
	public AppResultData<Object> submitQuestionForm(HttpServletRequest request, 
			@RequestParam("qId") Long id,
			@RequestParam(value = "bankId",required = false,defaultValue = "0") Long bankId,
			@RequestParam("title") String title,
			@RequestParam("isMulti") Short isMulti,
			@RequestParam("isFirst") Short isFirst,
			@RequestParam(value= "beforeQId",required = false, defaultValue = "0") Long beforeQId,
			@RequestParam("optionArray") String optionArray) throws JSONException{
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		SurveyQuestion question = questionService.initQuestion();
		// 1. 录入题目
		if(id == 0L){
			/*
			 * 添加 ： 1 考题  2 考题选项  
			 */
			
			//考题
			
			question.setBankId(bankId);
			question.setTitle(title);
			question.setIsMulti(isMulti); 
			question.setIsFirst(isFirst);
			
			questionService.insertSelective(question);
		}else{
			
			/*
			 * 修改 : 
			 * 	  对于选项, 可能会有 删减之类的操作， 决定，直接删除原来的 选项记录， 插入新的选项	，实现修改		
			 * 
			 */
			question = questionService.selectByPrimaryKey(id);
			
			// 分析： 题目修改时, 不应该修改  题库 和 服务类别, 修改的只是 该题目的 相关属性
			
			question.setTitle(title);
			question.setIsMulti(isMulti);
			question.setIsFirst(isFirst);
			
			questionService.updateByPrimaryKeySelective(question);
			//选项修改
			List<SurveyQuestionOption> list = optionService.selectByQId(question.getqId());
			
			List<Long> optionIdList = new ArrayList<Long>();
			
			for (SurveyQuestionOption option : list) {
				optionIdList.add(option.getId());
			}
			
			//先删除,再插入
			optionService.deleteByIdList(optionIdList);
		}
		
		//得到当前题目在数据库中的 id
		Long qId = question.getqId();
		
		//设置上一题的  “下一题” 为当前题目
		SurveyQuestion surveyQuestion = questionService.selectByPrimaryKey(beforeQId);
		
		if(surveyQuestion != null){
			
			question.setBeforeQId(beforeQId);
			surveyQuestion.setNextQId(qId);
			
			questionService.updateByPrimaryKeySelective(question);
			questionService.updateByPrimaryKeySelective(surveyQuestion);
		}
		
		//2. 录入选项
		SurveyQuestionOption option = optionService.initOption();
		
		option.setqId(question.getqId());
		option.setBankId(bankId);
		
		
		JSONArray jsonArray = new JSONArray(optionArray);
		
		List<String> optionStrList = new ArrayList<String>();
		
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String optionStr = jsonObject.getString("optionStr");
			String defaultTime = jsonObject.getString("defaultTime");
			
			option.setTitle(optionStr);
			option.setDefaultTimeOption(Long.valueOf(defaultTime));
			
			optionService.insertSelective(option);
			
			optionStrList.add(optionStr);
		}
		
		//所有选项、序号的键值对--> {(A:选项1)，(B:选项2)，(C:选项3)....}
		Map<String, String> map = questionService.generateNoForOption(optionStrList);
		
		/*
		 *  得到 插入数据库 却 没有 序号的 记录 
		 */
		List<SurveyQuestionOption> optionList = optionService.selectByQId(qId);
		
		for (Map.Entry<String, String> entry : map.entrySet()) {
			
			String optionStr = entry.getValue();
			for (SurveyQuestionOption surveyQuestionOption : optionList) {
				
				String title2 = surveyQuestionOption.getTitle();
				
				//如果 选项内容   相同, 则设置 选项序号
				if(title2.equals(optionStr)){
					surveyQuestionOption.setNo(entry.getKey());
					
					optionService.updateByPrimaryKeySelective(surveyQuestionOption);
				}
			}
		}
		
		result.setMsg("success");
		
		return result;
	}
	
	
	/*
	 *  跳转到  设置选项的 关联内容，页面
	 * 	
	 * 		根据题目 id, 跳转到设置选项和下一题的关联关系页面 		
	 */
	@RequestMapping(value = "option_relate",method = RequestMethod.GET)
	public String setQuestionRelate(Model model,
			@RequestParam("id")Long id,
			@RequestParam("qId")Long qId){
		
		//id 为 标识符, id=0 表示为 新增设置 下一题    id=1 表示为  修改 选项和 下一题的关联关系
		
		List<SurveyQuestionOption> list = optionService.selectByQId(qId);
		
		//该题目的选项序号的集合
		List<String> noList  = new ArrayList<String>();
		
		for (SurveyQuestionOption option : list) {
			noList.add(option.getNo());
		}
		
		SurveyOptionRefVo optionRefVo = optionService.initOptionRefVo();
		
		
		Map<Long, List<String>>	map = new HashMap<Long, List<String>>();
		
		map.put(0L, noList);
		
		// 选项和题目的 对应关系 -->  下一题id : A、B、C。。。。
		optionRefVo.setOptionMap(map);
		
		//标志位
		optionRefVo.setFlagId(id);
		
		//所有选项
		optionRefVo.setOptionNoList(noList);
		
		//当前题目的id
		optionRefVo.setqId(qId);
		//题目名称
		SurveyQuestion question = questionService.selectByPrimaryKey(qId);
		optionRefVo.setQuestionTitle(question.getTitle());
		
		//修改的话 TODO 
		if(id == 1L){
			
			
		}
		
//		List<SurveyOptionRefNextQ> byQId = nextQService.selectByQId(qId);
		
		model.addAttribute("optionRefQFormModel", optionRefVo);
		
		return "survey/optionRefQForm";
	}
	
	
	/**
	  * 
	  *  @Title: submitQuestionRelate
	  * @Description: 
	  * 			勾选确定 选项集合 和 对应下一题的  对应关系
	  * @param @param qId				当前题目Id			
	  * 
	  * @param @param optionRefArray	选项集合和对应下一题的字符串结果--> 
	  * 						此处，选项集合 前台 可以传 字符串拼接值，或者 数组,都挺麻烦~~								
	  * @param @return			
	  * @param @throws JSONException    设定文件
	  * @return AppResultData<String>    返回类型
	  * @throws
	 */
	@RequestMapping(value = "option_relate",method = RequestMethod.POST)
	public AppResultData<String>  submitQuestionRelate(
			@RequestParam(value ="qId",required = true,defaultValue="0")Long qId,
			@RequestParam(value = "optionRefArray",required =false,defaultValue = "") String optionRefArray) throws JSONException{
		
		AppResultData<String> result = new AppResultData<String>(Constants.SUCCESS_0, "", "");
		
		if(optionRefArray == ""){
			optionRefArray = "[]";
		}
		
		nextQService.completeVo(qId, optionRefArray);
		
		result.setMsg("success");
		
		return result;
	}
	
	
	
}
