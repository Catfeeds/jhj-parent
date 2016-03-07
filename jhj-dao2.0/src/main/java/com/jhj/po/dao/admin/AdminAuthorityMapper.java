package com.jhj.po.dao.admin;

import java.util.List;

import com.jhj.po.model.admin.AdminAuthority;

public interface AdminAuthorityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdminAuthority record);

    int insertSelective(AdminAuthority record);

    AdminAuthority selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdminAuthority record);

    int updateByPrimaryKey(AdminAuthority record);

	List<AdminAuthority> selectByIds(List<Long> ids);

	List<AdminAuthority> selectByParentId(Long id);

	int deleteByPartnerId(Long partnerId);

	int selectMaxId();
	
	List<AdminAuthority> selectChildAdminAuthorities();

}