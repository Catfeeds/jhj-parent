package com.jhj.service.impl.bs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.bs.OrgStaffSkillMapper;
import com.jhj.po.model.bs.OrgStaffSkill;
import com.jhj.service.bs.OrgStaffSkillService;
import com.jhj.vo.staff.OrgStaffSkillSearchVo;

/**
 *
 * @author :hulj
 * @Date : 2016年3月11日下午2:00:49
 * @Description: 
 *		
 *	技能service
 */
@Service
public class OrgStaffSkillImpl implements OrgStaffSkillService {

	@Autowired
	private OrgStaffSkillMapper skillMapper;	
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return skillMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrgStaffSkill record) {
		return skillMapper.insert(record);
	}

	@Override
	public int insertSelective(OrgStaffSkill record) {
		return skillMapper.insertSelective(record);
	}

	@Override
	public OrgStaffSkill selectByPrimaryKey(Long id) {
		return skillMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OrgStaffSkill record) {
		return skillMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrgStaffSkill record) {
		return skillMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<OrgStaffSkill> selectBySearchVo(OrgStaffSkillSearchVo searchVo) {
		return skillMapper.selectBySearchVo(searchVo);
	}
	
	@Override
	public void delByStaffId(Long staffId) {
		skillMapper.delByStaffId(staffId);
	}
}
