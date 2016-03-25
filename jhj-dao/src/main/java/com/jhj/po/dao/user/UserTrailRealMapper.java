package com.jhj.po.dao.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jhj.po.model.user.UserTrailReal;

public interface UserTrailRealMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserTrailReal record);

    int insertSelective(UserTrailReal record);

    UserTrailReal selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserTrailReal record);

    int updateByPrimaryKey(UserTrailReal record);

	UserTrailReal selectByUserIdAndType(Long userId, short userType);
	
	//得到  参数集合中，每个用户 最新时间的 位置 
	List<UserTrailReal> selectLatestPositionForUser(@Param("userIdList")List<Long> userIdList);
}