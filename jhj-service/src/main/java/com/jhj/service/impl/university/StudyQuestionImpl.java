package com.jhj.service.impl.university;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.university.PartnerServiceTypeMapper;
import com.jhj.po.dao.university.StudyBankMapper;
import com.jhj.po.dao.university.StudyQuestionMapper;
import com.jhj.po.dao.university.StudyQuestionOptionMapper;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.university.StudyBank;
import com.jhj.po.model.university.StudyQuestion;
import com.jhj.po.model.university.StudyQuestionOption;
import com.jhj.service.university.StudyQuestionService;
import com.jhj.vo.university.AppUniversityQuestionVo;
import com.jhj.vo.university.QuestionSearchVo;
import com.jhj.vo.university.StudyQuestionVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年12月4日下午5:34:07
 * @Description: 
 *	
 *		平台--大学--题目管理
 */
@Service
public class StudyQuestionImpl implements StudyQuestionService {

	@Autowired
	private StudyQuestionMapper questionMapper;
	
	@Autowired
	private StudyBankMapper bankMapper;
	
	@Autowired
	private PartnerServiceTypeMapper partServiceMapper;
	
	@Autowired
	private StudyQuestionOptionMapper optionMapper;
	
	
	@Override
	public int deleteByPrimaryKey(Long qId) {
		return questionMapper.deleteByPrimaryKey(qId);
	}

	@Override
	public int insertSelective(StudyQuestion record) {
		return questionMapper.insertSelective(record);
	}

	@Override
	public StudyQuestion selectByPrimaryKey(Long qId) {
		return questionMapper.selectByPrimaryKey(qId);
	}

	@Override
	public int updateByPrimaryKeySelective(StudyQuestion record) {
		return questionMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<StudyQuestion> selectByListPage(QuestionSearchVo searchVo) {
		return questionMapper.selectByListPage(searchVo);
	}

	@Override
	public StudyQuestionVo completeVo(StudyQuestion question) {
		
		//服务类别id
		Long serviceTypeId = question.getServiceTypeId();
		
		//题库id
		Long bankId = question.getBankId();
		
		//题目id
		Long qId = question.getqId();
		
		StudyQuestionVo questionVo = initQuestionVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(question, questionVo);
		
		if(qId != null && qId !=0L){
			
			StudyBank studyBank = bankMapper.selectByPrimaryKey(bankId);
			questionVo.setBankName(studyBank.getName());
			
			PartnerServiceType partnerServiceType = partServiceMapper.selectByPrimaryKey(serviceTypeId);
			
			if(partnerServiceType != null){
				questionVo.setServiceTypeName(partnerServiceType.getName());
			}
			
			
			List<StudyQuestionOption> list = optionMapper.selectByQId(qId);
			questionVo.setOptionList(list);
			
			//题型名称   单选、多选
			if(questionVo.getIsMulti() == (short)0){
				questionVo.setQuestionTypeName("单选题");
			}else{
				questionVo.setQuestionTypeName("多选题");
			}
			
			//是否必考的名称
			if(questionVo.getIsNeed() == (short)0){
				questionVo.setIsNeedName("非必考");
			}else{
				questionVo.setIsNeedName("*必考");
			}
		}
		
		return questionVo;
	}

	@Override
	public StudyQuestion initStudyQuestion() {
		
		StudyQuestion question = new StudyQuestion();
		
		question.setqId(0L);
		
		question.setBankId(0L);
		question.setServiceTypeId(0L);
		question.setTitle("");			//题干
		question.setDescription("");	//题目备注
		question.setIsMulti((short)0);	//是否多选 0 = 单选 1 = 多选
		question.setIsNeed((short)0);	//是否必考 0 = 否 1 = 是	
		question.setAnswer("");
		question.setAddTime(TimeStampUtil.getNowSecond());
		
		return question;
	}

	@Override
	public StudyQuestionVo initQuestionVo() {
		
		StudyQuestion question = initStudyQuestion();
		
		StudyQuestionVo questionVo = new StudyQuestionVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(question, questionVo);
		
		questionVo.setBankName("");
		questionVo.setServiceTypeName("");
		questionVo.setOptionList(new ArrayList<StudyQuestionOption>());
		
		questionVo.setRightId(0L);
		questionVo.setQuestionTypeName("");
		
		return questionVo;
	}
	
	/*
	 *  根据前台传递的  选项集合 ,生成对应的 map
	 *  
	 *  	ex:   	list(option1,option2....)---> map{(A:option1),(B:option2)....}
	 *  			
	 */
	@Override
	public Map<String, String> generateNoForOption(List<String> optionList) {
		
		// 序号： 选项  键值对
		Map<String, String> map = new HashMap<String, String>();
		
		//序号集合
		List<String> noList = OneCareUtil.generateNoForOptions();
		
		for (int i = 0; i < optionList.size(); i++) {
			for (int j = 0; j < noList.size(); j++) {
				
				if(i == j){
					map.put(noList.get(j), optionList.get(i));
				}
			}
		}
		
		System.out.println(map);
		
		return map;
	}

	
	/*
	 * 得到正确选项 对应的 序号, 作为 study_option 表的 answer 字段
	 * 			
	 * 		分析: 选项都是 从 字母list中得到 序号, 则只需 从字母表找 指定 索引即可
	 * 		
	 */
	@Override
	public List<String> getRightOptionNos(List<String> rightOptionList) {
		
		//正确选项 序号 集合
		List<String> rightList = new ArrayList<String>();
		
		
		//序号集合
		List<String> noList = OneCareUtil.generateNoForOptions();
		
		for (int i = 0; i < rightOptionList.size(); i++) {
			for (int j = 0; j < noList.size(); j++) {
				if(Integer.valueOf(rightOptionList.get(i)) == j){
					rightList.add(noList.get(j));
				}
			}
		}
		
		System.out.println(rightList);
		
		return rightList;
	}
	
	
	@Override
	public List<Long> selectByBankId(Long bankId) {
		return questionMapper.selectByBankId(bankId);
	}

	
	/**
	 * 根据 题目id，得到该题目详情
	 */
	@Override
	public AppUniversityQuestionVo translateToAppVo(Long qId) {
		
		StudyQuestion studyQuestion = questionMapper.selectByPrimaryKey(qId);
		
		List<StudyQuestionOption> list = optionMapper.selectByQId(qId);
		
		
		AppUniversityQuestionVo questionVo = new AppUniversityQuestionVo();
		
		questionVo.setQuestion(studyQuestion);
		questionVo.setOptionList(list);
		
		return questionVo;
	}
}
