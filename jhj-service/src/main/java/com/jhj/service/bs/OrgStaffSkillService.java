package com.jhj.service.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffSkill;
import com.jhj.vo.staff.OrgStaffSkillSearchVo;

/**
 *
 * @author :hulj
 * @Date : 2016年3月11日下午1:59:25
 * @Description: 
 *		技能service
 */
public interface OrgStaffSkillService {
	
	int deleteByPrimaryKey(Long id);

    int insert(OrgStaffSkill record);

    int insertSelective(OrgStaffSkill record);

    OrgStaffSkill selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffSkill record);

    int updateByPrimaryKey(OrgStaffSkill record);
    
    void delByStaffId(Long staffId);

	List<OrgStaffSkill> selectBySearchVo(OrgStaffSkillSearchVo searchVo);
	
}
