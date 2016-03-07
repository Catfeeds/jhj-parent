package com.jhj.service.tags;

import java.util.List;

import com.jhj.po.model.tags.UserRefTags;

public interface UserRefTagsService {

	List<UserRefTags> selectListByUserId(Long userId);

	List<UserRefTags> selectList();

	int insertByUserRefTags(UserRefTags userRefTags);

	int deleByUserId(Long userId);

}
