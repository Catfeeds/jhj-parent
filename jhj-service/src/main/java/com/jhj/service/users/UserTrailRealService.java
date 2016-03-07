package com.jhj.service.users;

import com.jhj.po.model.user.UserTrailReal;

public interface UserTrailRealService {
	
	int deleteByPrimaryKey(Long id);

    int insert(UserTrailReal record);

    int insertSelective(UserTrailReal record);

    UserTrailReal selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserTrailReal record);

    int updateByPrimaryKey(UserTrailReal record);

    UserTrailReal initUserTrailReal();

	UserTrailReal selectByUserIdAndType(Long userId, short userType);

}
