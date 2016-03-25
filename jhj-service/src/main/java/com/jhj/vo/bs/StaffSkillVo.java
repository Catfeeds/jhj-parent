package com.jhj.vo.bs;

import com.jhj.po.model.bs.OrgStaffSkill;

/**
 *
 * @author :hulj
 * @Date : 2016年3月10日下午6:09:15
 * @Description: 
 *	
 *		服务人员技能Vo
 */
public class StaffSkillVo extends OrgStaffSkill {
	
	private String skillName;

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	
}
