package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffInvite;
import com.jhj.vo.OrgStaffFinanceSearchVo;

public interface OrgStaffInviteMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrgStaffInvite record);

    int insertSelective(OrgStaffInvite record);

    OrgStaffInvite selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffInvite record);

    int updateByPrimaryKey(OrgStaffInvite record);

	OrgStaffInvite selectByMobile(String mobile);

	List<OrgStaffInvite> selectByInviteStaffIdAndStatus();

	List<OrgStaffInvite> selectByListPage(OrgStaffFinanceSearchVo searvhVo);
}