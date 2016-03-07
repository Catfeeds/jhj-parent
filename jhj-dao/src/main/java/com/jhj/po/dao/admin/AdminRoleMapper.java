package com.jhj.po.dao.admin;

import java.util.List;
import java.util.Map;

import com.jhj.po.model.admin.AdminRole;

public interface AdminRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdminRole record);

    Long insertSelective(AdminRole record);

    AdminRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdminRole record);

    int updateByPrimaryKey(AdminRole record);

	AdminRole selectByName(String name);

	List<AdminRole> selectAll();

	List<AdminRole> selectByListPage(Map<String,Object> conditions);

	List<AdminRole> selectByRoleIds(List<Long> ids);
}