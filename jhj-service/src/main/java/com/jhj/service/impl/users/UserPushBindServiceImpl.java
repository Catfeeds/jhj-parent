package com.jhj.service.impl.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.user.UserPushBindMapper;
import com.jhj.po.model.user.UserPushBind;
import com.jhj.service.users.UserPushBindService;
import com.jhj.vo.user.UserPushBindSearchVo;
import com.meijia.utils.TimeStampUtil;

@Service
public class UserPushBindServiceImpl implements UserPushBindService {
	@Autowired
	private UserPushBindMapper userPushBindMapper;

	@Override
	public UserPushBind initUserPushBind() {
		UserPushBind record = new UserPushBind();

		record.setId(0L);
		record.setUserId(0L);
		record.setUserType((short) 0L);
		record.setDeviceType("");
		record.setClientId("");
		record.setAddTime(TimeStampUtil.getNow() / 1000);
		return record;
	}

	@Override
	public int deleteByPrimaryKey(Long id) {

		return userPushBindMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(UserPushBind record) {

		return userPushBindMapper.insert(record);
	}

	@Override
	public int insertSelective(UserPushBind record) {

		return userPushBindMapper.insertSelective(record);
	}

	@Override
	public UserPushBind selectByPrimaryKey(Long id) {

		return userPushBindMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(UserPushBind record) {

		return userPushBindMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(UserPushBind record) {

		return userPushBindMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<UserPushBind> selectBySearchVo(UserPushBindSearchVo searchVo) {
		return userPushBindMapper.selectBySearchVo(searchVo);
	}

}
