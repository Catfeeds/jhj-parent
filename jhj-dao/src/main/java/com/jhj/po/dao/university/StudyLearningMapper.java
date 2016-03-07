package com.jhj.po.dao.university;

import java.util.List;

import com.jhj.po.model.university.StudyLearning;

public interface StudyLearningMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StudyLearning record);

    int insertSelective(StudyLearning record);

    StudyLearning selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StudyLearning record);

    int updateByPrimaryKeyWithBLOBs(StudyLearning record);

    int updateByPrimaryKey(StudyLearning record);
    
    
    List<StudyLearning>  selectByListPage();
    
    List<StudyLearning> selectByServiceTypeId(Long serviceTypeId);
    
    List<StudyLearning> selectAllLearning();
    
}