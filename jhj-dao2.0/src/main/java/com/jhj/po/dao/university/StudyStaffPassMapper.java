package com.jhj.po.dao.university;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jhj.po.model.university.StudyStaffPass;

public interface StudyStaffPassMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StudyStaffPass record);

    int insertSelective(StudyStaffPass record);

    StudyStaffPass selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StudyStaffPass record);

    int updateByPrimaryKey(StudyStaffPass record);
    
    
    //根据 服务人员id, 得到 服务人员 的 考核情况
    List<StudyStaffPass> selectByStaffId(Long staffId);
    
    //服务人员 某个 服务类别的 考核情况, 只有 考试通过才录入，因而这条sql 只会返回一个结果
    StudyStaffPass selectByStaffIdAndServiceTypeId(@Param("staffId")Long staffId,
    								@Param("serviceTypeId")Long serviceTypeId);
}