package com.jhj.action.survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.BaseController;
import com.jhj.common.Constants;
import com.jhj.po.model.survey.SurveyOptionRefContent;
import com.jhj.po.model.survey.SurveyQuestion;
import com.jhj.po.model.survey.SurveyQuestionOption;
import com.jhj.service.survey.SurveyOptionRefContentService;
import com.jhj.service.survey.SurveyQuestionOptionService;
import com.jhj.service.survey.SurveyQuestionServcie;
import com.jhj.vo.survey.SurveyOptionRefContentVo;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年12月17日下午6:10:57
 * @Description: 
 *		
 *		选项和 服务内容关联关系	
 *		
 */
@Controller
@RequestMapping(value = "/survey")
public class SurveyOptionRefController extends BaseController {
	
	@Autowired
	private SurveyOptionRefContentService refContentService;
	
	@Autowired
	private SurveyQuestionServcie questionService;
	
	@Autowired
	private SurveyQuestionOptionService optionService;
	
	/*
	 * 跳转到到 设置	
	 * 		选项--服务内容关联关系页面
	 */
	@RequestMapping(value = "ref_content_form",method = RequestMethod.GET)
	public String goToRefContentForm(Model model,
			@RequestParam("id")Long id ,
			@RequestParam("optionNo")String optionNo,
			@RequestParam("qId")Long qId){
		
		
		if(id == null){
			id = 0L;
		}
		
		SurveyQuestion question = questionService.selectByPrimaryKey(qId);
		
		SurveyOptionRefContentVo refContentVo = refContentService.initVo();
		
		refContentVo.setqId(qId);
		refContentVo.setQuestionTitle(question.getTitle());
		
		if(id == 1L){
			
			SurveyOptionRefContent refContent = refContentService.selectOneByQIdAndNo(qId, optionNo);
			
			if(refContent !=null){
				refContentVo.setSelectContent(refContent.getContentId());
			}
		}
		
		refContentVo.setOptionNo(optionNo);
		
		model.addAttribute("refContentVoFormModel", refContentVo);
		
		//标识位，用来确定是修改还是新增
		model.addAttribute("flagId", id);
		
		
		return "survey/optionRefContentForm";
	}
	
	/**
	 * 
	 *  @Title: submitRefContent
	  * @Description: 	
	  * 		提交 选项和 附加内容的 对应关系
	  * 
	  * @param @param flagId			标识位，判断是新增还是修改操作
	  * @param @param qId				题目id
	  * @param @param optionNo			选项序号
	  * @param @param contentIdStr		内容id的字符串拼接
	  * @param @return    设定文件
	  * @return AppResultData<Object>    返回类型
	  * @throws
	 */
	@RequestMapping(value = "/option_ref_content",method = RequestMethod.POST)
	public AppResultData<Object> submitRefContent(
			@RequestParam("flagId")Long flagId,
			@RequestParam("qId")Long qId,
			@RequestParam("optionNo")String optionNo,
			@RequestParam("contentIdStr")String contentIdStr){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		
		SurveyOptionRefContent refContent = refContentService.initRefContent();
		
		if(flagId == 0L){
			//新增
			refContent.setqId(qId);
			refContent.setOptionNo(optionNo);
			
			SurveyQuestionOption option = optionService.selectOneByQIdAndNo(qId, optionNo);
			
			refContent.setOptionId(option.getId().toString());
			
//			String[] split = contentIdStr.split(",");
//			
//			for (int i = 0; i < split.length; i++) {
//				
//				refContent.setContentId(split[i]);
//				
//			}
			
			
			refContent.setContentId(contentIdStr);
			refContentService.insertSelective(refContent);
			
			
	
		}else{
			//修改	
			refContent = refContentService.selectOneByQIdAndNo(qId, optionNo);
			
			refContent.setContentId(contentIdStr);
			
//			String[] split = contentIdStr.split(",");
//			
//			for (int i = 0; i < split.length; i++) {
//				
//				refContent.setContentId(split[i]);
//				
//				refContentService.insertSelective(refContent);
//			}
			
			
			refContentService.updateByPrimaryKeySelective(refContent);
		}
		
		result.setMsg("success");
		return result;
	}
	
	
}
