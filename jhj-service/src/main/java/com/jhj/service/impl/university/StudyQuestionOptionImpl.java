package com.jhj.service.impl.university;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.university.StudyQuestionOptionMapper;
import com.jhj.po.model.university.StudyQuestionOption;
import com.jhj.service.university.StudyQuestionOptionService;

/**
 *
 * @author :hulj
 * @Date : 2015年12月8日上午11:58:42
 * @Description: 
 *
 */
@Service
public class StudyQuestionOptionImpl implements StudyQuestionOptionService{

	@Autowired
	private StudyQuestionOptionMapper optionMapper;

	@Override
	public int deleteByPrimaryKey(Long id) {
		return optionMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(StudyQuestionOption record) {
		return optionMapper.insertSelective(record);
	}

	@Override
	public StudyQuestionOption selectByPrimaryKey(Long id) {
		return optionMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(StudyQuestionOption record) {
		return optionMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public StudyQuestionOption initQuestionOption() {
		
		StudyQuestionOption option = new StudyQuestionOption();
		
		option.setId(0L);
		option.setqId(0L);
		option.setBankId(0L);
		option.setServiceTypeId(0L);
		option.setServiceTypeId(0L);
		option.setTitle("");
		
		return option;
	}

	@Override
	public List<StudyQuestionOption> selectByQId(Long qId) {
		return optionMapper.selectByQId(qId);
	}

	@Override
	public void deleteByIdList(List<Long> optionIdList) {
		optionMapper.deleteByIdList(optionIdList);
	}
	

}
