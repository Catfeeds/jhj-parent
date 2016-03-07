package com.jhj.po.dao.user;
import java.util.List;

import com.jhj.po.model.user.UserPushBind;



public interface UserPushBindMapper {
   
	int updateByPrimaryKeySelective(UserPushBind record);

	int updateByPrimaryKey(UserPushBind record);

	int deleteByPrimaryKey(Long id);

    int insert(UserPushBind record);

    int insertSelective(UserPushBind record);

    UserPushBind selectByPrimaryKey(Long id);

	UserPushBind selectByUserId(Long userId);

	List<UserPushBind> selectByClientId(String clientId);
	
	List<UserPushBind> selectByUserType(Short userType);
}