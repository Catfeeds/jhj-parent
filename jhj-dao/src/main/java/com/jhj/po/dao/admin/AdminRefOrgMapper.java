package com.jhj.po.dao.admin;

import org.apache.ibatis.annotations.Param;

import com.jhj.po.model.admin.AdminRefOrg;

public interface AdminRefOrgMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdminRefOrg record);

    int insertSelective(AdminRefOrg record);

    AdminRefOrg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdminRefOrg record);

    int updateByPrimaryKey(AdminRefOrg record);
    
    AdminRefOrg selectByAdminId(Long adminId);
    
    AdminRefOrg selectByAdminIdAndOrgId(@Param("adminId") Long adminId, @Param("orgId")Long orgId);
}