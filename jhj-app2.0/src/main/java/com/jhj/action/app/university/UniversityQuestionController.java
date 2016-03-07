package com.jhj.action.app.university;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.Constants;
import com.jhj.po.model.university.StudyBank;
import com.jhj.service.university.StudyBankService;
import com.jhj.service.university.StudyQuestionService;
import com.jhj.vo.university.AppUniversityQuestionVo;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2016年1月9日下午6:24:04
 * @Description: 
 *		
 *		叮当大学--题目相关
 */
@Controller
@RequestMapping(value = "/app/university")
public class UniversityQuestionController extends BaseController {
	
	@Autowired
	private StudyBankService bankService;
	
	@Autowired
	private StudyQuestionService questionService;
	
	/**
	 * 
	 *  @Title: loadQuestionList
	  * @Description: 
	  * 	为 指定 服务类别，加载 随机题目
	  * @param serviceTypeId  服务id		
	  * @return    设定文件
	  * @throws
	 */
	@RequestMapping(value = "load_question_list.json",method = RequestMethod.GET)
	public AppResultData<Object> loadQuestionList(
			@RequestParam("service_type_id")Long serviceTypeId){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		/*
		 *  分析:
		 *  	1. 根据 serviceTypeId,在study_bank表中随机找到 该服务下的一个 bankId
		 * 		2. 根据 bankId, 在 study_question表得到 题目 id的集合
		 */
		
		List<StudyBank> bankList = bankService.selectBankByServiceType(serviceTypeId);
		
		List<Long> bankIdList = new ArrayList<Long>();
		for (StudyBank studyBank : bankList) {
			bankIdList.add(studyBank.getBankId());
		}
		
		//随机重排 list元素顺序
		Collections.shuffle(bankIdList);
		
		Long bankId = bankIdList.get(0);
		
		List<Long> qIdList = questionService.selectByBankId(bankId);
		
		Collections.shuffle(qIdList);
		
//		qIdList.subList(fromIndex, toIndex)
		
		/*
		 *  随机取一定数量的题目返回
		 */
		
		StudyBank studyBank = bankService.selectByPrimaryKey(bankId);
		
		//录入时,设定的 每次 取得题目数
		short randomQNum = studyBank.getRandomQNum();
		
		//实际从 库中得到的 题目总数
		int size = qIdList.size();
		
		//实际返回的 题目 id
		List<Long> realQList = new ArrayList<Long>();
		
		if(size < randomQNum){
			
			//如果 实际并没有 预设的 那么多题目，则返回全部取到的题目
			realQList =  qIdList;
		}else{
			//返回 题库设定的 取得的题目数,short类型，理论上没啥问题
			realQList = qIdList.subList(0, randomQNum);
		}
		
		result.setData(realQList);
		
		return result;
	}
	
	
	/**
	 * 
	 *  @Title: loadOneQuestion
	  * @Description: 
	  * 	根据题目 id, 加载题目详细信息
	  * @param qId
	 */
	@RequestMapping(value = "load_question.json",method = RequestMethod.GET)
	public AppResultData<Object> loadQuestionDetail(
			@RequestParam("q_id")Long qId){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		AppUniversityQuestionVo questionVo = questionService.translateToAppVo(qId);
		
		result.setData(questionVo);
		
		return result;
	}
	
	
}
