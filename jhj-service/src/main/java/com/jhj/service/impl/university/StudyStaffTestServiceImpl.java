package com.jhj.service.impl.university;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.university.StudyStaffTestMapper;
import com.jhj.po.model.university.StudyStaffTest;
import com.jhj.service.university.StudyStaffTestService;
import com.jhj.vo.university.DaoStaffTestMapVo;
import com.meijia.utils.TimeStampUtil;

@Service
public class StudyStaffTestServiceImpl implements StudyStaffTestService {
	
	@Autowired
	private StudyStaffTestMapper  studyStaffTestMapper;

	@Override
	public int deleteByPrimaryKey(Long id) {
		
		return studyStaffTestMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(StudyStaffTest record) {
		
		return studyStaffTestMapper.insertSelective(record);
	}

	@Override
	public StudyStaffTest selectByPrimaryKey(Long id) {
		
		return studyStaffTestMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(StudyStaffTest record) {
		
		return studyStaffTestMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public StudyStaffTest initStaffTest() {
		
		StudyStaffTest staffTest = new StudyStaffTest();
	    
	    staffTest.setId(0L);
	    staffTest.setStaffId(0L);
	    staffTest.setBankId(0L);
	    staffTest.setqId(0L);
	    staffTest.setTestAnswer("");
	    staffTest.setIsRight((short)0L);
	    staffTest.setAddTime(TimeStampUtil.getNowSecond());
		
		return staffTest;
	}

	@Override
	public List<DaoStaffTestMapVo> selectLatestTimeByStaffId(Long staffId) {
		return studyStaffTestMapper.selectLatestTimeByStaffId(staffId);
	}


}
