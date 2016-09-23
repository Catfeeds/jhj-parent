package com.jhj.po.dao.bs;

import java.util.List;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.vo.staff.StaffSearchVo;

public interface OrgStaffsMapper {
	int deleteByPrimaryKey(Long staffId);

	int insert(OrgStaffs record);

	int insertSelective(OrgStaffs record);

	int updateByPrimaryKeySelective(OrgStaffs record);

	int updateByPrimaryKey(OrgStaffs record);
	
	OrgStaffs selectByPrimaryKey(Long staffId);
	
	List<OrgStaffs> selectBySearchVo(StaffSearchVo staffSearchVo);

	List<OrgStaffs> selectByListPage(StaffSearchVo staffSearchVo);

}