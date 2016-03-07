package com.jhj.service.impl.university;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.university.StudyStaffPassMapper;
import com.jhj.po.model.university.StudyStaffPass;
import com.jhj.service.university.StudyStaffPassService;
import com.meijia.utils.TimeStampUtil;

@Service
public class StudyStaffPassServiceImpl implements StudyStaffPassService {
	
	@Autowired
	private StudyStaffPassMapper  studyStaffPassMapper;

	@Override
	public int deleteByPrimaryKey(Long id) {
		
		return studyStaffPassMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(StudyStaffPass record) {
		
		return studyStaffPassMapper.insertSelective(record);
	}

	@Override
	public StudyStaffPass selectByPrimaryKey(Long id) {
		
		return studyStaffPassMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(StudyStaffPass record) {
		
		return studyStaffPassMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public StudyStaffPass initStaffPass() {
		
		StudyStaffPass staffPass = new StudyStaffPass();

	    staffPass.setId(0L);
	    staffPass.setStaffId(0L);
	    staffPass.setServiceTypeId(0L);
	    staffPass.setBankId(0L);
	    staffPass.setTotalNeed((short)0L);
	    staffPass.setTotalRight((short)0L);
	    staffPass.setAddTime(TimeStampUtil.getNow()/1000);
	    staffPass.setUpdateTime(TimeStampUtil.getNow()/1000);
	    
		return staffPass;
	}

	@Override
	public List<StudyStaffPass> selectByStaffId(Long staffId) {
		return studyStaffPassMapper.selectByStaffId(staffId);
	}
	
}
