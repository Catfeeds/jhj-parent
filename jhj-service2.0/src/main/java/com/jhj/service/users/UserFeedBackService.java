package com.jhj.service.users;

import com.jhj.po.model.user.UserFeedback;

public interface UserFeedBackService {
	int deleteByPrimaryKey(Long id);

    int insert(UserFeedback record);

    int insertSelective(UserFeedback record);

    UserFeedback selectByPrimaryKey(Long id);

    UserFeedback selectByUserId(Long userId);

    int updateByPrimaryKeySelective(UserFeedback record);

    int updateByPrimaryKey(UserFeedback record);
    
    UserFeedback initUserFeedBack();
}
