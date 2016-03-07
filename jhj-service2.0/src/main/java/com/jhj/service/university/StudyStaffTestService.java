package com.jhj.service.university;

import java.util.List;

import com.jhj.po.model.university.StudyStaffTest;
import com.jhj.vo.university.DaoStaffTestMapVo;

/**
 *
 * @author :hulj
 * @Date : 2015年12月4日上午11:30:04
 * @Description: TODO
 *
 */
public interface StudyStaffTestService {
		
	int deleteByPrimaryKey(Long id);

    int insertSelective(StudyStaffTest record);

    StudyStaffTest selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StudyStaffTest record);

    StudyStaffTest initStaffTest();

    List<DaoStaffTestMapVo> selectLatestTimeByStaffId(Long staffId);
}
