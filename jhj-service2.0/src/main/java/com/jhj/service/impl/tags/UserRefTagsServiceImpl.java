package com.jhj.service.impl.tags;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.tags.UserRefTagsMapper;
import com.jhj.po.model.tags.UserRefTags;
import com.jhj.service.tags.UserRefTagsService;

@Service
public class UserRefTagsServiceImpl implements UserRefTagsService{

	@Autowired
	private UserRefTagsMapper userRefTagsMapper;
	@Override
	public List<UserRefTags> selectListByUserId(Long userId) {
		
		return userRefTagsMapper.selectListByUserId(userId);
	}
	@Override
	public List<UserRefTags> selectList() {
		
		return userRefTagsMapper.selectList();
	}
	@Override
	public int insertByUserRefTags(UserRefTags userRefTags) {
		
		return userRefTagsMapper.insertSelective(userRefTags);
	}
	@Override
	public int deleByUserId(Long userId) {
		
		return userRefTagsMapper.deleteByUserId(userId);
	}

}
