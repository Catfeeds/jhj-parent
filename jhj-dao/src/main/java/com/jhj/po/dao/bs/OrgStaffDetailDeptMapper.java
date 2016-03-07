package com.jhj.po.dao.bs;

import com.jhj.po.model.bs.OrgStaffDetailDept;

public interface OrgStaffDetailDeptMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrgStaffDetailDept record);

    int insertSelective(OrgStaffDetailDept record);

    OrgStaffDetailDept selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffDetailDept record);

    int updateByPrimaryKey(OrgStaffDetailDept record);
}