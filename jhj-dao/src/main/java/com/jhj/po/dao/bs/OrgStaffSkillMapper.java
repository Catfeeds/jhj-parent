package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffSkill;

public interface OrgStaffSkillMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrgStaffSkill record);

    int insertSelective(OrgStaffSkill record);

    OrgStaffSkill selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffSkill record);

    int updateByPrimaryKey(OrgStaffSkill record);
    
    //2016年3月11日11:31:52  选择用户的 技能
    List<OrgStaffSkill> selectByStaffId(Long staffId);
    
    void delByStaffId(Long staffId);
    
}