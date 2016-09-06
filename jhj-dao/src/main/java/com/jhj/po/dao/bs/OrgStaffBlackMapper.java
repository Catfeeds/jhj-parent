package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffBlack;
import com.jhj.vo.OrgStaffDetailPaySearchVo;

public interface OrgStaffBlackMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrgStaffBlack record);

    int insertSelective(OrgStaffBlack record);

    OrgStaffBlack selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffBlack record);

    int updateByPrimaryKey(OrgStaffBlack record);

	OrgStaffBlack selectByStaffId(Long userId);
	
	List<OrgStaffBlack> selectAll();

	List<OrgStaffBlack> selectVoByListPage(OrgStaffDetailPaySearchVo searchVo);

	List<OrgStaffBlack> selectByStaffIdAndType(Long staffId);
	
	List<OrgStaffBlack> selectAllBadRateStaff();
}