package com.jhj.po.dao.user;

import com.jhj.po.model.user.UserTrailReal;

public interface UserTrailRealMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserTrailReal record);

    int insertSelective(UserTrailReal record);

    UserTrailReal selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserTrailReal record);

    int updateByPrimaryKey(UserTrailReal record);

	UserTrailReal selectByUserIdAndType(Long userId, short userType);
}