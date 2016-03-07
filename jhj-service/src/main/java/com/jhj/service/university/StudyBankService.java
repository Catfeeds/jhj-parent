package com.jhj.service.university;

import java.util.List;

import com.jhj.po.model.university.StudyBank;

/**
 *
 * @author :hulj
 * @Date : 2015年12月4日上午11:30:04
 * @Description: TODO
 *
 */
public interface StudyBankService {
		
	int deleteByPrimaryKey(Long bankId);

    int insertSelective(StudyBank record);

    StudyBank selectByPrimaryKey(Long bankId);

    int updateByPrimaryKeySelective(StudyBank record);

    List<StudyBank> selectByListPage();
    
    StudyBank  initBank();
    
    List<StudyBank> selectBankByServiceType(Long serviceTypeId);
}
