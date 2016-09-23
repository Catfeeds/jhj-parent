package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffSkill;
import com.jhj.vo.staff.OrgStaffSkillSearchVo;

public interface OrgStaffSkillMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrgStaffSkill record);

    int insertSelective(OrgStaffSkill record);

    int updateByPrimaryKeySelective(OrgStaffSkill record);

    int updateByPrimaryKey(OrgStaffSkill record);
    
    OrgStaffSkill selectByPrimaryKey(Long id);
    
    //2016年3月11日11:31:52  选择用户的 技能
    List<OrgStaffSkill> selectBySearchVo(OrgStaffSkillSearchVo searchVo);
    
    void delByStaffId(Long staffId);
    
}