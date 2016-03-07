package com.jhj.service.impl.survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.survey.SurveyUserRefQuestionMapper;
import com.jhj.po.model.survey.SurveyUserRefQuestion;
import com.jhj.service.survey.SurveyUserRefQuestionService;
import com.meijia.utils.OrderNoUtil;
import com.meijia.utils.TimeStampUtil;

/**
 * 
 *
 * @author :hulj
 * @Date : 2015年12月24日上午11:28:41
 * @Description: 
 *		
 *		被调查用户  与 答题结果的对应关系
 */
@Service
public class SurveyUserRefQeustionImpl implements SurveyUserRefQuestionService {
	@Autowired
	private SurveyUserRefQuestionMapper refQuestionMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return refQuestionMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(SurveyUserRefQuestion record) {
		return refQuestionMapper.insertSelective(record);
	}

	@Override
	public SurveyUserRefQuestion selectByPrimaryKey(Long id) {
		return refQuestionMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SurveyUserRefQuestion record) {
		return refQuestionMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public SurveyUserRefQuestion initUserRefQ() {
		
		SurveyUserRefQuestion question = new SurveyUserRefQuestion();
		
		
		String resultNo = String.valueOf(OrderNoUtil.genOrderNo());
		
		question.setId(0L);
		question.setqId(0L);
		question.setOptionId(0L);
		
		
		question.setResultNo(resultNo);	//标识。。用来确定 和 推荐套餐是一次完整的操作
		question.setContentId(0L);
		question.setUserId(0L);
		question.setAddTime(TimeStampUtil.getNowSecond());
		
		question.setTimes(0L);   //用户填写或勾选的服务次数
		
		question.setContentChildId(0L);	//如果是子服务,则录入子服务id
		
		return question;
	}

}
