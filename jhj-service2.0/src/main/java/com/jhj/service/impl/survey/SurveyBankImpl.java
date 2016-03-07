package com.jhj.service.impl.survey;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.survey.SurveyBankMapper;
import com.jhj.po.model.survey.SurveyBank;
import com.jhj.service.survey.SurveyBankService;
import com.meijia.utils.TimeStampUtil;

@Service
public class SurveyBankImpl implements SurveyBankService{
	
	@Autowired
	private SurveyBankMapper bankMapper;
	
	@Override
	public int deleteByPrimaryKey(Long bankId) {
		return bankMapper.deleteByPrimaryKey(bankId);
	}

	@Override
	public int insertSelective(SurveyBank record) {
		return bankMapper.insertSelective(record);
	}

	@Override
	public SurveyBank selectByPrimaryKey(Long bankId) {
		return bankMapper.selectByPrimaryKey(bankId);
	}

	@Override
	public int updateByPrimaryKeySelective(SurveyBank record) {
		return bankMapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public List<SurveyBank> selectByListPage() {
		return bankMapper.selectByListPage();
	}

	@Override
	public SurveyBank initBank() {
		SurveyBank bank = new SurveyBank();
		
		bank.setBankId(0L);
		bank.setName("");
		bank.setAddTime(TimeStampUtil.getNowSecond());
		
		
		return bank;
	}
}
