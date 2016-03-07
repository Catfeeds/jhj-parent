package com.jhj.service.impl.survey;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.survey.SurveyServiceMapper;
import com.jhj.po.model.survey.SurveyService;
import com.jhj.service.survey.SurveyServiceService;
import com.meijia.utils.TimeStampUtil;

@Service
public class SurveyServiceImpl implements SurveyServiceService {

	@Autowired
	private SurveyServiceMapper serviceMapper;
	
	@Override
	public int deleteByPrimaryKey(Long serviceId) {
		return serviceMapper.deleteByPrimaryKey(serviceId);
	}

	@Override
	public int insertSelective(SurveyService record) {
		return serviceMapper.insertSelective(record);
	}

	@Override
	public SurveyService selectByPrimaryKey(Long serviceId) {
		return serviceMapper.selectByPrimaryKey(serviceId);
	}

	@Override
	public int updateByPrimaryKeySelective(SurveyService record) {
		return serviceMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<SurveyService> selectByListPage() {
		return serviceMapper.selectByListPage();
	}

	@Override
	public SurveyService initService() {
		
		SurveyService service  = new SurveyService();
		
		service.setServiceId(0L);
		service.setName("");
		service.setEnable((short)1); // 0= 不可用  1= 可用
		service.setAddTime(TimeStampUtil.getNowSecond());
		
		return service;
	}

}
