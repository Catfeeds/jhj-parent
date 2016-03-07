package com.jhj.service.university;

import java.util.List;

import com.jhj.po.model.university.StudyStaffPass;

/**
 *
 * @author :hulj
 * @Date : 2015年12月4日上午11:30:04
 * @Description: 
 *
 */
public interface StudyStaffPassService {
		
	int deleteByPrimaryKey(Long id);

    int insertSelective(StudyStaffPass record);

    StudyStaffPass selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StudyStaffPass record);

    StudyStaffPass initStaffPass();
    
    //根据 服务人员id, 得到 服务人员 的 考核情况
    List<StudyStaffPass> selectByStaffId(Long staffId);
}
