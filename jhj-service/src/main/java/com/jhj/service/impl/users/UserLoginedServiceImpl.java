package com.jhj.service.impl.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.user.UserLoginedMapper;
import com.jhj.po.model.user.UserLogined;
import com.jhj.po.model.user.UserSmsToken;
import com.jhj.service.users.UserLoginedService;
import com.meijia.utils.TimeStampUtil;

@Service
public class UserLoginedServiceImpl implements UserLoginedService{

	@Autowired
	private UserLoginedMapper userLoginedMapper;

	@Override
	public int deleteByPrimaryKey(Long id) {
		return userLoginedMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(UserLogined record) {
		return userLoginedMapper.insert(record);
	}

	@Override
	public int insertSelective(UserLogined record) {
		return userLoginedMapper.insertSelective(record);
	}

	@Override
	public UserLogined selectByPrimaryKey(Long id) {
		return userLoginedMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(UserLogined record) {
		return userLoginedMapper.updateByPrimaryKey(record);
	}

	@Override
	public int updateByPrimaryKeySelective(UserLogined record) {
		return userLoginedMapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public UserLogined initUserLogined(UserSmsToken smsToken, int login_from, long ip) {
		UserLogined record = new UserLogined();
		record.setAddTime(TimeStampUtil.getNow()/1000);
		record.setLoginFrom((short)login_from);//0 = APP 1 = 微网站 2 = 管理后台
		record.setMobile(smsToken.getMobile());
		record.setUserId(smsToken.getUserId());
		record.setLoginIp(ip);
		return record;
	}

	@Override
	public List<String> selectDistinctAll() {
		
		List<String> list = userLoginedMapper.selectDistinctAll();
		
		return list;
	}

	@Override
	public int countUserLogined(String mobile) {
		return userLoginedMapper.countUserLogined(mobile);
	}

}