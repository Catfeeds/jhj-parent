package com.jhj.service.impl.university;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.university.StudyBankMapper;
import com.jhj.po.model.university.StudyBank;
import com.jhj.service.university.StudyBankService;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年12月4日上午11:31:48
 * @Description: 
 * 
 * 		题库 service
 *
 */
@Service
public class StudyBankImpl implements StudyBankService {
	
	@Autowired
	private StudyBankMapper bankMapper;
	
	@Override
	public int deleteByPrimaryKey(Long bankId) {
		return bankMapper.deleteByPrimaryKey(bankId);
	}

	@Override
	public int insertSelective(StudyBank record) {
		return bankMapper.insertSelective(record);
	}

	@Override
	public StudyBank selectByPrimaryKey(Long bankId) {
		return bankMapper.selectByPrimaryKey(bankId);
	}

	@Override
	public int updateByPrimaryKeySelective(StudyBank record) {
		return bankMapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public List<StudyBank> selectByListPage() {
		return bankMapper.selectByListPage();
	}

	@Override
	public StudyBank initBank() {
		StudyBank bank = new StudyBank();
		
		bank.setBankId(0L);
		bank.setServiceTypeId(0L);
		bank.setName("");
		bank.setDescription("");
		bank.setAddTime(TimeStampUtil.getNowSecond());
		
//		bank.setTotalNeed((short)1);  //此处不设置默认值，以显示 input框的提示文字
//		bank.setRandomQNum();		  
		return bank;
	}

	@Override
	public List<StudyBank> selectBankByServiceType(Long serviceTypeId) {
		return bankMapper.selectBankByServiceType(serviceTypeId);
	}

}
