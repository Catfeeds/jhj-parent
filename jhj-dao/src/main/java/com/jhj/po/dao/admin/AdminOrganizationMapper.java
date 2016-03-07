package com.jhj.po.dao.admin;

import java.util.List;

import com.jhj.po.model.admin.AdminOrganization;

public interface AdminOrganizationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdminOrganization record);

    int insertSelective(AdminOrganization record);

    AdminOrganization selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdminOrganization record);

    int updateByPrimaryKey(AdminOrganization record);

    AdminOrganization selectByName(String name);

    List<AdminOrganization> listChain();

    List<AdminOrganization> selectByParentId(Long PartnerId);
}