package com.jhj.service.impl.survey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.survey.SurveyQuestionOptionMapper;
import com.jhj.po.model.survey.SurveyQuestionOption;
import com.jhj.service.survey.SurveyQuestionOptionService;
import com.jhj.vo.survey.SurveyOptionRefVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.TimeStampUtil;

@Service
public class SurveyQuestionOptionImpl implements SurveyQuestionOptionService {
	
	
	@Autowired
	private SurveyQuestionOptionMapper optionMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return optionMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(SurveyQuestionOption record) {
		return optionMapper.insertSelective(record);
	}

	@Override
	public SurveyQuestionOption selectByPrimaryKey(Long id) {
		return optionMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SurveyQuestionOption record) {
		return optionMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<SurveyQuestionOption> selectByQId(Long qId) {
		return optionMapper.selectByQId(qId);
	}

	@Override
	public void deleteByIdList(List<Long> optionIdList) {
		optionMapper.deleteByIdList(optionIdList);
	}

	@Override
	public SurveyQuestionOption initOption() {
		
		SurveyQuestionOption option  = new SurveyQuestionOption();
		
		option.setId(0L);
		option.setqId(0L);
		option.setBankId(0L);
		option.setTitle("");
		option.setNo("");		//选项序号，A B C
		option.setRealtedQId(0L);
		option.setRelatedContentId("");
		option.setAddTime(TimeStampUtil.getNowSecond());
		option.setUpdateTime(TimeStampUtil.getNowSecond());
		
		return option;
	}

	@Override
	public SurveyOptionRefVo initOptionRefVo() {
		
		SurveyOptionRefVo optionRefVo = new SurveyOptionRefVo();
		
		SurveyQuestionOption option = initOption();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(option, optionRefVo);
		
		
//		optionRefVo.setContentId(0L);
		optionRefVo.setOptionNoList(new ArrayList<String>());
		
		optionRefVo.setFlagId(0L);  	//页面标识符， 0 新增页，  1修改页
		optionRefVo.setOptionMap(new HashMap<Long, List<String>>());
		
		optionRefVo.setNextQId(0L);
		
		return optionRefVo;
	}

	@Override
	public List<SurveyQuestionOption> selectByQIdAndNo(Map<String, Object> map) {
		return optionMapper.selectByQIdAndNo(map);
	}

	@Override
	public SurveyQuestionOption selectOneByQIdAndNo(Long qId, String optionNo) {
		return optionMapper.selectOneByQIdAndNo(qId, optionNo);
	}

}
