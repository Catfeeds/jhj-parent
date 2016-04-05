package com.jhj.po.dao.user;

import com.jhj.po.model.user.UserRefOrg;

public interface UserRefOrgMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserRefOrg record);

    int insertSelective(UserRefOrg record);

    UserRefOrg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserRefOrg record);

    int updateByPrimaryKey(UserRefOrg record);
    
    UserRefOrg selectByUserId(Long userId);
}