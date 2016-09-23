package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffAuth;
import com.jhj.vo.staff.StaffAuthSearchVo;

public interface OrgStaffAuthMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrgStaffAuth record);

    int insertSelective(OrgStaffAuth record);

    OrgStaffAuth selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffAuth record);

    int updateByPrimaryKey(OrgStaffAuth record);

	List<OrgStaffAuth> selectBySearchVo(StaffAuthSearchVo searchVo);
	
	void deleteByStaffId(Long staffId);

}