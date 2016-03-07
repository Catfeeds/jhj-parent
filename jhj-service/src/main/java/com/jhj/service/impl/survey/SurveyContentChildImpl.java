package com.jhj.service.impl.survey;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.survey.SurveyContentChildMapper;
import com.jhj.po.model.survey.SurveyContentChild;
import com.jhj.service.survey.SurveyContentChildService;
import com.meijia.utils.TimeStampUtil;

@Service
public class SurveyContentChildImpl implements SurveyContentChildService{
	
	@Autowired
	private SurveyContentChildMapper contentChildMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return contentChildMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(SurveyContentChild record) {
		return contentChildMapper.insertSelective(record);
	}

	@Override
	public SurveyContentChild selectByPrimaryKey(Long id) {
		return contentChildMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SurveyContentChild record) {
		return contentChildMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public SurveyContentChild initContentChild() {
		
		SurveyContentChild contentChild = new SurveyContentChild();
		
		contentChild.setId(0L);
		contentChild.setContentId(0L);
		contentChild.setOptionStr("");
		contentChild.setOptionNo("");
		contentChild.setAddTime(TimeStampUtil.getNowSecond());
		
		return contentChild;
	}

	@Override
	public List<SurveyContentChild> selectByContentId(Long contentId) {
		return contentChildMapper.selectByContentId(contentId);
	}

	@Override
	public void deleteByContentId(Long contentId) {
		contentChildMapper.deleteByContentId(contentId);
	}
	
}
	
