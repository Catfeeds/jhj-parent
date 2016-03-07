package com.jhj.po.dao.university;

import java.util.List;

import com.jhj.po.model.university.StudyStaffTest;
import com.jhj.vo.university.DaoStaffTestMapVo;

public interface StudyStaffTestMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StudyStaffTest record);

    int insertSelective(StudyStaffTest record);

    StudyStaffTest selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StudyStaffTest record);

    int updateByPrimaryKey(StudyStaffTest record);
    
    List<DaoStaffTestMapVo> selectLatestTimeByStaffId(Long staffId);
}