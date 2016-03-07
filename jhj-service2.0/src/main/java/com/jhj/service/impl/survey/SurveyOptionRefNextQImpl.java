package com.jhj.service.impl.survey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.survey.SurveyOptionRefNextQMapper;
import com.jhj.po.model.survey.SurveyOptionRefNextQ;
import com.jhj.po.model.survey.SurveyQuestion;
import com.jhj.po.model.survey.SurveyQuestionOption;
import com.jhj.service.survey.SurveyOptionRefNextQService;
import com.jhj.service.survey.SurveyQuestionOptionService;
import com.jhj.service.survey.SurveyQuestionServcie;
import com.meijia.utils.TimeStampUtil;

@Service
public class SurveyOptionRefNextQImpl implements SurveyOptionRefNextQService {
	
	@Autowired
	private SurveyOptionRefNextQMapper nextMapper;
	@Autowired
	private SurveyQuestionOptionService optionService;
	@Autowired
	private SurveyQuestionServcie questionService;
	
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return nextMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(SurveyOptionRefNextQ record) {
		return nextMapper.insertSelective(record);
	}

	@Override
	public SurveyOptionRefNextQ selectByPrimaryKey(Long id) {
		return nextMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SurveyOptionRefNextQ record) {
		return nextMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<SurveyOptionRefNextQ> selectByQId(Long qId) {
		return nextMapper.selectByQId(qId);
	}

	@Override
	public SurveyOptionRefNextQ initNextQ() {
		
		SurveyOptionRefNextQ nextQ = new SurveyOptionRefNextQ();
		
		nextQ.setId(0L);
		nextQ.setOptionId("");
		nextQ.setOptionNo("");
		nextQ.setqId(0L);
		nextQ.setRelatedQId("");
		nextQ.setAddTime(TimeStampUtil.getNowSecond());
		
		return nextQ;
	}
	
	
	/**
	 * 设置	当前题目的选项 和  关联的题目 的关联关系
	 * 				
	 * 
	 * 
	 *  
	 */
	
	@Override
	public void completeVo(Long qId,String optionRefArray) throws JSONException {
		
		//2016年1月7日18:21:01  对于后期 新增 选项时,  先之前的  选项和 题目的 关联关系
		nextMapper.deleteByQId(qId);
		
		
		
		//选项所属的对象
		SurveyQuestion nowQuestion = questionService.selectByPrimaryKey(qId);
		
		
		SurveyOptionRefNextQ nextQ = initNextQ();
		
		//解析参数。。插入表中
		JSONArray array = new JSONArray(optionRefArray);
		
//		System.out.println(array.length());
		for (int i = 0; i < array.length(); i++) {
			
			JSONObject jsonObject = array.getJSONObject(i);
			
			String nextQId = jsonObject.getString("nextQId");
			
			/*
			 * 	此处勾选的选项以 字符串拼接的形式传递，
			 * 	若是数组,可以直接使用  getJSONArray("xxx"),然后遍历
			 */
			String opStr = jsonObject.getString("opStr");
			
			String[] opArray = opStr.split(",");
			
			List<String> noList  = new ArrayList<String>();
			for (int j = 0; j < opArray.length; j++) {
				
				noList.add(opArray[j]);
				
			}
			
			
			
			//mybatis 查询条件
			Map<String, Object> map  = new HashMap<String, Object>();
			
			map.put("qId", qId);
			map.put("myList", noList);
			
			List<SurveyQuestionOption> optionList = optionService.selectByQIdAndNo(map);
			
			//选项id的集合
			List<String> optionIdList = new ArrayList<String>();
			for (SurveyQuestionOption surveyQuestionOption : optionList) {
				
				optionIdList.add(surveyQuestionOption.getId().toString());
				
			}
			
			//id字符串
			String join = StringUtils.join(optionIdList, ",");
			
			nextQ.setqId(qId);
			nextQ.setOptionId(join);
			nextQ.setOptionNo(opStr);
			nextQ.setRelatedQId(nextQId);
			insertSelective(nextQ);
			
			
			/**
			 * 2015-12-25 19:07:31 
			 * 			如果当前绑定的  下一题,在录入时,已经有了默认的上一题,
			 * 			
			 * 		修改该对象的  next_q_id 为 选项 所属题目的  next_q_id
			 * 
			 *   解决：根据选项跳转题目后，再选择，无法找到下一题的问题
			 * 		
			 */
			
			//选项绑定的下一题  对象
			SurveyQuestion nextQuestion = questionService.selectByPrimaryKey(Long.valueOf(nextQId));
			
			//修改属性
			nextQuestion.setNextQId(nowQuestion.getNextQId());
			
			//清空默认的上一题
			nextQuestion.setBeforeQId(0L);
			
			questionService.updateByPrimaryKeySelective(nextQuestion);
			
		}
		
	}

	@Override
	public SurveyOptionRefNextQ selectNextQ(Long qId, String optionStr) {
		return nextMapper.selectNextQ(qId, optionStr);
	}	
}
