package com.jhj.service.users;

import java.util.List;

import com.jhj.po.model.user.UserPushBind;

public interface UserPushBindService {
	
	int deleteByPrimaryKey(Long id);

    int insert(UserPushBind record);

    int insertSelective(UserPushBind record);

    UserPushBind selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserPushBind record);

    int updateByPrimaryKey(UserPushBind record);

    UserPushBind initUserPushBind();

	UserPushBind selectByUserId(Long userId);

	List<UserPushBind> selectByClientId(String clientId);
	
	List<UserPushBind> selectByUserType(Short userType);
}
