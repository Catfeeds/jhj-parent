package com.jhj.service.impl.university;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.model.university.StudyBank;
import com.jhj.po.model.university.StudyQuestion;
import com.jhj.po.model.university.StudyStaffPass;
import com.jhj.po.model.university.StudyStaffTest;
import com.jhj.service.university.StudyBankService;
import com.jhj.service.university.StudyQuestionService;
import com.jhj.service.university.StudyStaffPassService;
import com.jhj.service.university.StudyStaffTestService;
import com.jhj.service.university.UniversityResultService;
import com.jhj.vo.university.AppUniversityResultVo;

/**
 *
 * @author :hulj
 * @Date : 2016年1月18日下午5:54:15
 * @Description: 
 *
 */
@Service
public class UniversityResultImpl implements UniversityResultService {
	
	@Autowired
	private StudyStaffTestService testService;
	
	@Autowired
	private StudyStaffPassService passService;
	
	@Autowired
	private StudyQuestionService questionService;
	
	@Autowired
	private StudyBankService bankService;
	
	@Override
	public AppUniversityResultVo insertRecord(Long staffId, Long serviceTypeId, String universityResult) throws JSONException {
		
		
		JSONArray jsonArray = new JSONArray(universityResult);
		
		//返回结果
		AppUniversityResultVo resultVo = new AppUniversityResultVo();
		
		short rightNum = 0;
		
		Long bankId = 0L;
		
		//1. 解析json，向 study_staff_test表插入记录
		for (int i = 0; i < jsonArray.length(); i++) {
			
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			
			String questionId = jsonObject.getString("questionId");
			String optionNo = jsonObject.getString("optionNo");
			
			StudyStaffTest staffTest = testService.initStaffTest();	
			
			staffTest.setStaffId(staffId);
			staffTest.setqId(Long.valueOf(questionId));
			staffTest.setTestAnswer(optionNo);
			
			//bankId 通过 题目 id 查到
			StudyQuestion question = questionService.selectByPrimaryKey(Long.valueOf(questionId));
			
			if(bankId == 0L){
				bankId = question.getBankId();
			}
			
			staffTest.setBankId(bankId);
			
			String rightAnswer = question.getAnswer();
			
			if(optionNo.equals(rightAnswer)){
				//结果正确
				staffTest.setIsRight(Constants.UNIVERSITY_QUESTION_SUCCESS);
				
				rightNum ++;
			}else{
				staffTest.setIsRight(Constants.UNIVERSITY_QUESTION_FAIL);
			}
			
			//1. 先在生成 study_staff_test 答题记录
			testService.insertSelective(staffTest);
		}
		
		
		resultVo.setRightNum(rightNum);
		
		StudyBank studyBank = bankService.selectByPrimaryKey(bankId);
		
		short totalNeed = studyBank.getTotalNeed();
		
		resultVo.setNeedNum(totalNeed);
		System.out.println(rightNum);
		if(rightNum < totalNeed){
			resultVo.setResultFlag(Constants.UNIVERSITY_STAFF_PASS_STATUS_FAIL);
		}else{
			resultVo.setResultFlag(Constants.UNIVERSITY_STAFF_PASS_STATUS_SUCCESS);
			
			//如果通过，需要在 study_staff_pass中生成记录
			
			StudyStaffPass staffPass = passService.initStaffPass();
			
			staffPass.setStaffId(staffId);
			staffPass.setServiceTypeId(serviceTypeId);
			staffPass.setBankId(bankId);
			staffPass.setTotalNeed(totalNeed);
			staffPass.setTotalRight(rightNum);
			
			passService.insertSelective(staffPass);
		}
		
		return resultVo;
	}

}
