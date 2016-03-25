package com.jhj.service.impl.users;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.user.UserTrailRealMapper;
import com.jhj.po.model.user.UserTrailReal;
import com.jhj.service.users.UserTrailRealService;
import com.meijia.utils.TimeStampUtil;

@Service
public class UserTrailRealServiceImpl implements UserTrailRealService {
	@Autowired
	private UserTrailRealMapper userTrailRealMapper;
	

	@Override
	public UserTrailReal initUserTrailReal() {
		UserTrailReal record = new UserTrailReal();

	    record.setId(0L);
	    record.setUserId(0L);
	    record.setUserType((short)0L);
	    record.setLat("");
	    record.setLng("");
	    record.setPoiName("");
	    record.setAddTime(TimeStampUtil.getNow() / 1000);
		return record;
	}


	@Override
	public int deleteByPrimaryKey(Long id) {
		
		return userTrailRealMapper.deleteByPrimaryKey(id);
	}


	@Override
	public int insert(UserTrailReal record) {
		
		return userTrailRealMapper.insert(record);
	}


	@Override
	public int insertSelective(UserTrailReal record) {

		return userTrailRealMapper.insertSelective(record);
	}


	@Override
	public UserTrailReal selectByPrimaryKey(Long id) {
		
		return userTrailRealMapper.selectByPrimaryKey(id);
	}


	@Override
	public int updateByPrimaryKeySelective(UserTrailReal record) {
		
		return userTrailRealMapper.updateByPrimaryKeySelective(record);
	}


	@Override
	public int updateByPrimaryKey(UserTrailReal record) {
	
		return userTrailRealMapper.updateByPrimaryKey(record);
	}


	@Override
	public UserTrailReal selectByUserIdAndType(Long userId, short userType) {

		return userTrailRealMapper.selectByUserIdAndType(userId,userType);
	}

	@Override
	public List<UserTrailReal> selectLatestPositionForUser(List<Long> userIdList) {
		
		return userTrailRealMapper.selectLatestPositionForUser(userIdList);
	}
}
	

