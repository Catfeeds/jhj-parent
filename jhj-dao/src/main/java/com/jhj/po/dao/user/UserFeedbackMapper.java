package com.jhj.po.dao.user;

import com.jhj.po.model.user.UserFeedback;

public interface UserFeedbackMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserFeedback record);

    int insertSelective(UserFeedback record);

    UserFeedback selectByPrimaryKey(Long id);

    UserFeedback selectByUserId(Long userId);

    int updateByPrimaryKeySelective(UserFeedback record);

    int updateByPrimaryKey(UserFeedback record);
}