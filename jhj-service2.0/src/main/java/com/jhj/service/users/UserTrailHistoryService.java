package com.jhj.service.users;

import com.jhj.po.model.user.UserTrailHistory;

public interface UserTrailHistoryService {
	
	int deleteByPrimaryKey(Long id);

    int insert(UserTrailHistory record);

    int insertSelective(UserTrailHistory record);

    UserTrailHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserTrailHistory record);

    int updateByPrimaryKey(UserTrailHistory record);

    UserTrailHistory initUserTrailHistory();

}
