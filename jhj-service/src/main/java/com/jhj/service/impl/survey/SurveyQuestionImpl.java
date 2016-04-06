package com.jhj.service.impl.survey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.dao.survey.SurveyContentChildMapper;
import com.jhj.po.dao.survey.SurveyContentMapper;
import com.jhj.po.dao.survey.SurveyOptionRefContentMapper;
import com.jhj.po.dao.survey.SurveyOptionRefNextQMapper;
import com.jhj.po.dao.survey.SurveyQuestionMapper;
import com.jhj.po.dao.survey.SurveyQuestionOptionMapper;
import com.jhj.po.model.survey.SurveyBank;
import com.jhj.po.model.survey.SurveyContent;
import com.jhj.po.model.survey.SurveyContentChild;
import com.jhj.po.model.survey.SurveyOptionRefContent;
import com.jhj.po.model.survey.SurveyOptionRefNextQ;
import com.jhj.po.model.survey.SurveyQuestion;
import com.jhj.po.model.survey.SurveyQuestionOption;
import com.jhj.service.survey.SurveyBankService;
import com.jhj.service.survey.SurveyContentService;
import com.jhj.service.survey.SurveyQuestionServcie;
import com.jhj.vo.survey.SurveyContentVo;
import com.jhj.vo.survey.SurveyQuestionVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class SurveyQuestionImpl implements SurveyQuestionServcie {
	
	@Autowired
	private SurveyQuestionMapper questionMapper;
	
	@Autowired
	private SurveyBankService bankService;
	
	@Autowired
	private SurveyQuestionOptionMapper optionMapper;
	
	@Autowired
	private SurveyContentMapper contentMapper;
	
	@Autowired
	private SurveyOptionRefNextQMapper nextMapper;
	
	@Autowired
	private SurveyOptionRefContentMapper refContentMapper;
	
	@Autowired
	private SurveyContentChildMapper contentChildMapper;
	
	@Autowired
	private SurveyContentService contentService;
	
	@Override
	public int deleteByPrimaryKey(Long qId) {
		return questionMapper.deleteByPrimaryKey(qId);
	}

	@Override
	public int insertSelective(SurveyQuestion record) {
		return questionMapper.insertSelective(record);
	}

	@Override
	public SurveyQuestion selectByPrimaryKey(Long qId) {
		return questionMapper.selectByPrimaryKey(qId);
	}

	@Override
	public int updateByPrimaryKeySelective(SurveyQuestion record) {
		return questionMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public SurveyQuestion initQuestion() {
		
		SurveyQuestion question = new SurveyQuestion();
		
		question.setqId(0L);
		question.setBankId(0L);
		question.setTitle("");
		question.setIsMulti((short)0);	//0 单选  1=多选
		question.setAddTime(TimeStampUtil.getNowSecond());
		
		
		question.setBeforeQId(0L);
		question.setNextQId(0L);
		question.setIsFirst((short)1);	// 0 第一题  1位于中间  2最后一题
		
		
		return question;
	}
	
	
	@Override
	public List<SurveyQuestion> selectByListPage() {
		return questionMapper.selectByListPage();
	}
	
	@Override
	public SurveyQuestionVo initVo() {
		
		SurveyQuestion question = initQuestion();
		
		SurveyQuestionVo questionVo = new SurveyQuestionVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(question, questionVo);
		
		questionVo.setBankName("");
		questionVo.setOptionList(new ArrayList<SurveyQuestionOption>());
		questionVo.setQuestionTypeName("");	//单选、多选
		questionVo.setOptionReContent("");	//选项关联的附加服务内容
		questionVo.setOptionReQName("");	//选项关联的 下一题
		
		return questionVo;
	}
	
	
	@Override
	public SurveyQuestionVo completeVo(SurveyQuestion question) {
		
		SurveyQuestionVo questionVo = initVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(question, questionVo);
		
		//题目id
		Long qId = question.getqId();
		
		if(qId != null && qId !=0L){
			
			//题库名称
			SurveyBank surveyBank = bankService.selectByPrimaryKey(question.getBankId());
			questionVo.setBankName(surveyBank.getName());
			
			//题型名称   单选、多选
			if(questionVo.getIsMulti() == (short)0){
				questionVo.setQuestionTypeName("单选题");
			}else{
				questionVo.setQuestionTypeName("多选题");
			}
			
			//附加服务内容
			List<SurveyQuestionOption> optionList = optionMapper.selectByQId(question.getqId());
			
			questionVo.setOptionList(optionList);
			
			
			String contentStr = "";
			
			for (SurveyQuestionOption option : optionList) {
				
				String contentId = option.getRelatedContentId();
				
				List<SurveyContent> list = getContentByOption(contentId);
				
				for (SurveyContent surveyServiceContent : list) {
					
					contentStr += surveyServiceContent.getName();
				}
				
				//关联的服务内容名称
				questionVo.setOptionReContent(contentStr);
				
				//关联的下一题的 id, 这里 是 一对一关系
				SurveyQuestion question2 = questionMapper.selectByPrimaryKey(option.getRealtedQId());
				
				if(question2 !=null){
					questionVo.setOptionReQName(question2.getTitle());
				}
			}
		}
		return questionVo;
	}
	
	/*
	 *  通过选项Id,得到 该选项关联的 所有 服务内容
	 */
	public List<SurveyContent> getContentByOption(String contentId){
		
		String[] contentArray = contentId.split(",");
		
		//此处传 string[], 虽然能查询出正确的结果,但是 与数据库 字段类型不一致，会有隐患
		
		/**
		 * 此处用了  Long 的引用类型
		 */
		
		Long[] cotentIdArray = (Long[]) ConvertUtils.convert(contentArray, Long.class);
		
		List<SurveyContent> contentList = contentMapper.selectByIds(cotentIdArray);
		
		return contentList;
		
	}

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
		
//		System.out.println(map);
		
		return map;
		
		
	}
	
	
	@Override
	public SurveyQuestion selectFirstQuestion() {
		return questionMapper.selectFirstQuestion();
	}

	@Override
	public SurveyQuestionVo selectNextQuestion(Long qId, String optionStr) {
		
		SurveyQuestion surveyQuestion = initQuestion();
		
		//第一次加载页面
		if(qId == 0L){
			  surveyQuestion = selectFirstQuestion();
		}
		
		/**
		 * 加载 第二题及之后的题目
		 * 	
		 *  通过当前题目 id 和选择的 选项，确定下一题
		 *  
		 *  2016年1月13日15:43:49  要求 对应关系存在 才能正确加载题目
		 * 
		 */
		if(qId !=null && qId >0L){
			  
			  SurveyOptionRefNextQ selectNextQ = nextMapper.selectNextQ(qId, optionStr);
			  
			  /**
			   * 如何确定下一题
			   * 
			   * 	1.根据当前题目和选择的选项，查看选项是否有关联的下一题
			   * 	  	1>有。返回
			   *    	2>未找到,则根据 当前题目,得到 当前题目对应的 默认的下一题 （录入时的记录）并返回
			   *    		
			   */
			  
			  
			  if(selectNextQ !=null){
				  String relatedQId = selectNextQ.getRelatedQId();
				  
				  if(!StringUtil.isEmpty(relatedQId)){
						 
					  surveyQuestion = selectByPrimaryKey(Long.valueOf(relatedQId));
				  }
				  
			  }else{
				  
				  SurveyQuestion surveyQuestion2 = selectByPrimaryKey(qId);
				  
				  Long nextQId = surveyQuestion2.getNextQId();
				  
				  surveyQuestion = selectByPrimaryKey(nextQId);
			  }
			  
		}
		
		SurveyQuestionVo questionVo = completeVo(surveyQuestion);
		
		return questionVo;
	}
	
	
	
	/**
	 * 解析  答题结果，返回结果页 展示  vo
	 * @throws JSONException 
	 */
	
	@Override
	public Map<Long, List<SurveyContentVo>> completeResultVo(String surveyResult) throws JSONException {
		
		Map<Long, List<SurveyContentVo>> map = new HashMap<Long, List<SurveyContentVo>>();
		
		
		/*
		 * 2016-1-13 15:18:22  存放 由选项确定次数的 content,  < 服务id:次数 >
		 */
		Map<Long, Long> contentRealTime = new HashMap<Long, Long>();
		
		//用来存放  已选择的  content Id
		List<Long>  selectContentIdList = new ArrayList<Long>();
		
		//设置选项和 内容相关
		JSONArray jsonArray = new JSONArray(surveyResult);
		
		for (int i = 0; i < jsonArray.length(); i++) {
			
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			
			//题目 id
			String questionId = jsonObject.getString("questionId");
			
			if(questionId.equals(28) || questionId.equals(57)){
				System.out.println(questionId);
			}
			
			// 选项序号   “A,B。。。”
			String optionStr = jsonObject.getString("optionStr");
			
			String[] optionArray = optionStr.split(",");
			
			for (int j = 0; j < optionArray.length; j++) {
				
				 String optionNo = optionArray[j];
				 
				 SurveyOptionRefContent surveyOptionRefContent = refContentMapper.selectOneByQIdAndNo(Long.valueOf(questionId), optionNo);
				 
				 //由题目和 选项，得到该选项 对象，进而得到 该选项对应的  次数
				 SurveyQuestionOption questionOption = optionMapper.selectOneByQIdAndNo(Long.valueOf(questionId), optionNo);
				 
				 Long defaultTimeOption = questionOption.getDefaultTimeOption();
				 
				 
				 //2016-1-13 15:03:31  对于 保洁类 服务，默认次数 由选项决定
					
					//所有  默认次数 由 选项决定的  服务Id 的集合
					List<Long> list2 = contentService.selectSetDefaultTime();
				 
				 
				 if(surveyOptionRefContent !=null){
					 
					 /**
					  *  因为会有  某个题目 某个选项，对应多个服务的情况，
					  *  	
					  *  
					  */
					 
					 String contentId = surveyOptionRefContent.getContentId();
					 
					 String[] contentIdArray = contentId.split(",");
					 
					 for (int k = 0; k < contentIdArray.length; k++) {
						 selectContentIdList.add(Long.valueOf(contentIdArray[k]));
						 
						 if(list2.contains(Long.valueOf(contentIdArray[k]))){
							 
							 contentRealTime.put(Long.valueOf(contentIdArray[k]),defaultTimeOption);
						 }
					 }
				 }
			}
		}
		
		/**
		 *  处理list,
		 *  
		 *  此处的 selectContentId  极大可能会有重复情况，但是不影响查询结果
		 */
		
		//选择结果包含的内容
		List<SurveyContent> selectContentList = contentMapper.selectByIdList(selectContentIdList);
		
		//所有的内容
		List<SurveyContent> allContentList = contentMapper.selectAllContent();
		
		List<Long> allContentIdList =  new ArrayList<Long>();
		
		for (SurveyContent surveyContent : allContentList) {
			
			allContentIdList.add(surveyContent.getContentId());
		}
		
		
		//免费内容
		List<SurveyContent> freeContentList = contentMapper.selectFreeContent();
		
		//所有免费内容的Id
		List<Long> freeContentIdList = contentMapper.selectFreeContentId();
		
		
		//移除之后 的 allContentList 就是未选择的 内容, 即推荐套餐
		
		/**
		 * 此处的移除,需要使用 移除  id 的方式，用对象，不行
		 */
		
		allContentIdList.removeAll(selectContentIdList);
		
		/*
		 * 2016-1-4 16:14:19  此处 
		 * 
		 * 	 对于 多选 类型的 服务，如果用户选择了 家电清洗A ,则不再显示  家电清洗B 和 家电清洗C
		 * 	
		 */
		List<Long> boxChildIdList = contentMapper.selectByChildType(Constants.SURVEY_CHILD_TYPE_2);
		
		allContentIdList.removeAll(boxChildIdList);
		
		
		//使用移除后的  id,得到 推荐套餐
		
		/*
		 * 移除后可能出现,用户选择了全部服务,移除后 list为空
		 */
		
		if(allContentIdList.size() >0 && allContentIdList !=null){
			
			allContentList = contentMapper.selectByIdList(allContentIdList);
		}
		
		
		/**
		 * 生成Vo,带子服务
		 */
		
		//基础服务
		
		//关联内容中有 免费服务，则不再展示
		selectContentList.removeAll(freeContentIdList);
		
		List<SurveyContentVo> selectContentList2 = getChildVoForContent(selectContentList);
		//处理 选中服务中 ，次数由 选项决定的服务
		List<SurveyContentVo> baseContentList = dealWithBaseContentList(selectContentList2, contentRealTime);
		
		//推荐服务
		List<SurveyContentVo> allContentList2 = getChildVoForContent(allContentList);
		
		//免费服务
		List<SurveyContentVo> freeContentList2 = getChildVoForContent(freeContentList);
		
		map.put(Constants.SURVEY_RESULT_0, baseContentList);	//选择结果的服务
		map.put(Constants.SURVEY_RESULT_1, allContentList2);	//未选择的（推荐服务）
		map.put(Constants.SURVEY_RESULT_2, freeContentList2);	//免费赠送服务
		
		
		return map;
	}
	
	/**
	 * 	为  一个服务的集合  生成  
	 * 			包含子服务的服务集合
	 * 	
	 * 	关于子服务：
	 *    			
	 *      1>若该服务有子服务。子服务 的 展示类型为   单选或者 多选,此时 surveyContent对象的  contentChildType 为 1（单选）2多选
	 *      
 	 * 		2>若无子服务,则 contentChildType = 0
	 * 
	 *   
	 */
	@Override
	public List<SurveyContentVo> getChildVoForContent(List<SurveyContent> contentList){
		
		List<SurveyContentVo> list = new ArrayList<SurveyContentVo>();
		
		List<SurveyContentChild> childList = new ArrayList<SurveyContentChild>();
		
		//过滤后，可能 没有 “推荐服务”,需要校验 size
		if(contentList.size() >0){
			for (SurveyContent surveyContent : contentList) {
				
				SurveyContentVo contentVo = contentService.initContentVo();
				
				//子服务是 单选题形式，如  除尘除螨  口4次	口6次  口8次。。。 
				if(surveyContent.getContentChildType() != (short)0){
					
					Long contentId = surveyContent.getContentId();
					childList  = contentChildMapper.selectByContentId(contentId);
					
					contentVo.setChildList(childList);
				}
				BeanUtilsExp.copyPropertiesIgnoreNull(surveyContent, contentVo);
				
				list.add(contentVo);
			}
		}
		return list;
	}
	
	/*
	 * 2016-1-13 15:28:34
	 * 
	 *  分析：  前提，该服务已经被选中
	 * 		
	 * 	对于   服务次数 由  选项 确定的服务, 单独处理其 默认次数		
	 * 			
	 */
	private List<SurveyContentVo> dealWithBaseContentList(List<SurveyContentVo> list,Map<Long, Long> map){
			
		Set<Entry<Long,Long>> entrySet = map.entrySet();
		
		for (Entry<Long, Long> entry : entrySet) {
			
			Long key = entry.getKey();
			
			for (SurveyContentVo contentVo: list) {
				if (contentVo.getContentId() == key) {
					contentVo.setBaseContentRealTime(entry.getValue());	
				}
			}
		}
		
		return list;
	}
	
}
