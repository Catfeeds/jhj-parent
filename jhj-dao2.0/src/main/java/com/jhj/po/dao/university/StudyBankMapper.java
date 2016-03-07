package com.jhj.po.dao.university;

import java.util.List;

import com.jhj.po.model.university.StudyBank;

public interface StudyBankMapper {
    int deleteByPrimaryKey(Long bankId);

    int insert(StudyBank record);

    int insertSelective(StudyBank record);

    StudyBank selectByPrimaryKey(Long bankId);

    int updateByPrimaryKeySelective(StudyBank record);

    int updateByPrimaryKey(StudyBank record);
    
    List<StudyBank> selectByListPage();
    
    List<StudyBank> selectBankByServiceType(Long serviceTypeId);
}