package com.jhj.po.dao.admin;

import java.util.HashMap;
import java.util.List;

import com.jhj.po.model.admin.AdminAccount;

public interface AdminAccountMapper {
    int deleteByPrimaryKey(Long id);

    Long insert(AdminAccount record);

    int insertSelective(AdminAccount record);

    AdminAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdminAccount record);

    int updateByPrimaryKey(AdminAccount record);

    List<AdminAccount> selectByListPage(HashMap conditions);

    AdminAccount selectByUsername(String username);

    AdminAccount selectByUsernameAndPassword(HashMap conditions);

    List<AdminAccount> selectByRoleId(Long roleId);

    List<AdminAccount> selectByIds(List<Long> ids);

	List<AdminAccount> selectByAll();
}