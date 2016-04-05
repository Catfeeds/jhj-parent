package com.jhj.service.impl.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.user.UserRefOrgMapper;
import com.jhj.po.model.user.UserRefOrg;
import com.jhj.service.users.UserRefOrgService;
import com.jhj.service.users.UsersService;
import com.meijia.utils.TimeStampUtil;

@Service
public class UserRefOrgServiceImpl implements UserRefOrgService {

	@Autowired
	private UserRefOrgMapper userRefOrgMapper;

	@Autowired
	private UsersService usersService;
	
	@Override
	public UserRefOrg initUserRefOrg() {
		UserRefOrg record = new UserRefOrg();
		record.setId(0L);
		record.setUserId(0L);
		record.setOrgId(0L);
		record.setParentOrgId(0L);
		record.setAddTime(TimeStampUtil.getNowSecond());
		return record;
	}
	
	@Override
	public int insert(UserRefOrg record) {
		return userRefOrgMapper.insert(record);
	}
	
	@Override
	public int insertSelective(UserRefOrg record) {
		return userRefOrgMapper.insertSelective(record);
	}
	
	@Override
	public int updateByPrimaryKey(UserRefOrg record) {
		return userRefOrgMapper.updateByPrimaryKey(record);
	}
	
	@Override
	public int updateByPrimaryKeySelective(UserRefOrg record) {
		return userRefOrgMapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public UserRefOrg selectByPrimaryKey(Long userId) {
		return userRefOrgMapper.selectByPrimaryKey(userId);
	}
	
	@Override
	public UserRefOrg selectByUserId(Long userId) {
		return userRefOrgMapper.selectByUserId(userId);
	}

}
