package com.jhj.po.dao.admin;

import java.util.List;

import com.jhj.po.model.admin.AdminRoleAuthority;

public interface AdminRoleAuthorityMapper {
    int deleteByPrimaryKey(Long id);

    int deleteByRoleId(Long id);

    int insert(AdminRoleAuthority record);

    int insertSelective(AdminRoleAuthority record);

    AdminRoleAuthority selectByPrimaryKey(Long id);

    AdminRoleAuthority selectByRoleAndAuthority(Long roleId,Long authorityId);

    int updateByPrimaryKeySelective(AdminRoleAuthority record);

    int updateByPrimaryKey(AdminRoleAuthority record);

    List<AdminRoleAuthority> selectByRoleId(Long roleId);

    List<AdminRoleAuthority> selectAll();
    
    
    

}