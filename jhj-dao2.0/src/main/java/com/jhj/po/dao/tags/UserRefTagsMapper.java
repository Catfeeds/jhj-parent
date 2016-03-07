package com.jhj.po.dao.tags;

import java.util.List;

import com.jhj.po.model.tags.UserRefTags;

public interface UserRefTagsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserRefTags record);

    int insertSelective(UserRefTags record);

    UserRefTags selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserRefTags record);

    int updateByPrimaryKey(UserRefTags record);

	List<UserRefTags> selectListByUserId(Long userId);

	List<UserRefTags> selectList();

	int deleteByUserId(Long userId);
}