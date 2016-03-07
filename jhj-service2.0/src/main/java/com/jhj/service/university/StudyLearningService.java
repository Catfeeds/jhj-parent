package com.jhj.service.university;

import java.util.List;

import com.jhj.po.model.university.StudyLearning;
import com.jhj.vo.university.StudyVo;

/**
 *
 * @author :hulj
 * @Date : 2015年12月3日下午2:50:08
 * @Description: 
 *
 */
public interface StudyLearningService {
	
	int deleteByPrimaryKey(Long id);

    int insertSelective(StudyLearning record);

    StudyLearning selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StudyLearning record);
    
    List<StudyLearning>  selectByListPage();
    
    StudyVo completeVo(StudyLearning learning);
    
    StudyVo initStudyVo();
    
    StudyLearning initStudyLearning();
    
    List<StudyLearning> selectByServiceTypeId(Long serviceTypeId);
    
    List<StudyLearning> selectAllLearning();
}
