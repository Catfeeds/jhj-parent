package com.jhj.service.users;

import com.jhj.po.model.user.UserRefOrg;


public interface UserRefOrgService {

	int insertSelective(UserRefOrg record);

	int insert(UserRefOrg record);
	
	UserRefOrg selectByUserId(Long userId);

	UserRefOrg selectByPrimaryKey(Long userId);

	int updateByPrimaryKey(UserRefOrg record);

	int updateByPrimaryKeySelective(UserRefOrg record);

	UserRefOrg initUserRefOrg();
		
}
