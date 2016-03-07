package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffOnline;

public interface OrgStaffOnlineMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrgStaffOnline record);

    int insertSelective(OrgStaffOnline record);

    OrgStaffOnline selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffOnline record);

    int updateByPrimaryKey(OrgStaffOnline record);

	OrgStaffOnline selectByStaffId(Long staffId);

	List<OrgStaffOnline> selectByStaffIdLimitTwo(Long staffId);
	// <!--查找开工 or 不开工的集合-->
	List<OrgStaffOnline> selectByIsWork(Short isWork);

}