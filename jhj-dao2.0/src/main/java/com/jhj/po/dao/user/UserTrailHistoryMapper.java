package com.jhj.po.dao.user;

import com.jhj.po.model.user.UserTrailHistory;

public interface UserTrailHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserTrailHistory record);

    int insertSelective(UserTrailHistory record);

    UserTrailHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserTrailHistory record);

    int updateByPrimaryKey(UserTrailHistory record);
}