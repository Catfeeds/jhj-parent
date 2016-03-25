package com.jhj.action.university;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.jhj.po.model.university.StudyQuestion;
import com.jhj.po.model.university.StudyQuestionOption;
import com.jhj.service.university.StudyQuestionOptionService;
import com.jhj.service.university.StudyQuestionService;
import com.jhj.vo.university.QuestionSearchVo;
import com.jhj.vo.university.StudyQuestionVo;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年12月4日下午4:13:54
 * @Description: 
 *			
 *		平台--叮当大学--题目管理--
 *						这里和 题目选项 综合在 一个 菜单下
 */
@Controller
@RequestMapping(value = "/university")
public class StudyQuestionController extends BaseController {

	@Autowired
	private StudyQuestionService questionService;
	@Autowired
	private StudyQuestionOptionService optionService;
	
	
	@RequestMapping(value = "question_list",method = RequestMethod.GET)
	public String getQuestionList(Model model, HttpServletRequest request,
			QuestionSearchVo searchVo){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		if(searchVo == null){
			searchVo = new QuestionSearchVo();
		}
		
		List<StudyQuestion> list = questionService.selectByListPage(searchVo);
		
		StudyQuestion question = null;
		
		for (int i = 0; i < list.size(); i++) {
			question = list.get(i);
			
			StudyQuestionVo completeVo = questionService.completeVo(question);
			
			list.set(i, completeVo);
		}
		
		PageInfo result = new PageInfo(list);	
		
		model.addAttribute("questionListVoModel", result);
		model.addAttribute("questionSearchVoModel", searchVo);
		
		return "university/questionList";
	}
	
	/*
	 * 跳转到form页
	 */
	@RequestMapping(value = "question_form",method = RequestMethod.GET)
	public String questionForm(HttpServletRequest request, Model model,
					@RequestParam("id") Long id){
								
		if(id == null){
			id = 0L;
		}
		StudyQuestionVo questionVo = questionService.initQuestionVo();
		
		if( id !=null && id >0L){
			StudyQuestion studyQuestion = questionService.selectByPrimaryKey(id);
			questionVo = questionService.completeVo(studyQuestion);
		}
		
		model.addAttribute("questionVoFormModel", questionVo);
		
		return "university/questionForm";
	}
	
	/**
	 * 
	 *  @Title: submitQuestionForm
	 *  
	  * @Description: 考题管理 form表单提交
	  * 		
	  * 			1. 生成考题
	  * 			2. 生成考题对应的选项
	  * 
	  * @param  id   			题目id,隐藏域 id,判断 是添加还是修改页面
	  * @param  request
	  * @param  serviceTypeId	服务类别Id  -->非必选，修改时可根据 题目 id得到该参数
	  * @param  bankId			题库Id	  -->非必选，修改时可根据 题目 id得到该参数	
	  * @param  title			题干
	  * @param  description		题目描述
	  * @param  isNeed			是否必考   0否   1是
	  * 
	  * @param  optionArray		所添加的选项的 集合, 
	  * 									前台封装的数组，这里要用 List接收，
	  * 												还需要结合 ajax 的 traditional: true 参数
	  * 
	  * @param  rightOption     正确答案 在 List 里的 索引位置
	  * 							
	  * 						此处直接设置为  list,应对可能出现的 多选	
	  * 
	  * @throws
	 */
	@RequestMapping(value = "question_form",method = RequestMethod.POST)
	public AppResultData<Object> submitQuestionForm(HttpServletRequest request, 
			@RequestParam("qId") Long id,
			@RequestParam(value = "serviceTypeId",required = false,defaultValue = "0") Long serviceTypeId,
			@RequestParam(value = "bankId",required = false,defaultValue = "0") Long bankId,
			@RequestParam("title") String title,
			@RequestParam(value = "description",required = false,defaultValue = "") String description,
			@RequestParam("isNeed") Short isNeed,
			@RequestParam("optionArray") List<String> optionArray,
			@RequestParam("rightOption") List<String> rightOption){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0,"","");
		
		//正确答案的 序号 --> A,B.....
		List<String> rightOptionNos = questionService.getRightOptionNos(rightOption);
		// apache 工具类 :  list<String> --> String
		String join = StringUtils.join(rightOptionNos, ",");
		
		//所有选项、序号的键值对--> {(A:选项1)，(B:选项2)，(C:选项3)....}
		Map<String, String> map = questionService.generateNoForOption(optionArray);
		
		
		StudyQuestion question = questionService.initStudyQuestion();
		
		if(id == 0L){
			/*
			 * 添加 ： 1 考题  2 考题选项
			 */
			
			//考题
			
			question.setBankId(bankId);
			question.setServiceTypeId(serviceTypeId);
			question.setTitle(title);
			question.setDescription(description);
//			question.setIsMulti(());  默认值，都是单选
			question.setIsNeed(isNeed);
			
			question.setAnswer(join);
			
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
			question.setDescription(description);
			question.setIsNeed(isNeed);
			question.setAnswer(join);
			
			questionService.updateByPrimaryKeySelective(question);
			
			
			//选项修改
			List<StudyQuestionOption> list = optionService.selectByQId(question.getqId());
			
			List<Long> optionIdList = new ArrayList<Long>();
			
			for (StudyQuestionOption studyQuestionOption : list) {
				optionIdList.add(studyQuestionOption.getId());
			}
			
			//先删除,再插入
			optionService.deleteByIdList(optionIdList);
			
		}
		
		
		//考题选项
		StudyQuestionOption option = optionService.initQuestionOption();
		
		/**
		 *  获取新增对象,插入后的主键id,有两种方法。。。
		 */
		option.setqId(question.getqId());
		option.setBankId(bankId);
		option.setServiceTypeId(serviceTypeId);
		
		
		for (Map.Entry<String, String> entry : map.entrySet()) {
				
			option.setNo(entry.getKey());
			option.setTitle(entry.getValue());	
			
			optionService.insertSelective(option);
		}
		
		
		return result;
	}
	
	
}
