package com.jhj.service.impl.users;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.user.UserTrailHistoryMapper;
import com.jhj.po.model.user.UserTrailHistory;
import com.jhj.po.model.user.UserTrailReal;
import com.jhj.service.users.UserTrailHistoryService;
import com.jhj.vo.user.UserTrailSearchVo;
import com.meijia.utils.TimeStampUtil;

@Service
public class UserTrailHistoryServiceImpl implements UserTrailHistoryService {
	@Autowired
	private UserTrailHistoryMapper userTrailHistoryMapper;
	

	@Override
	public UserTrailHistory initUserTrailHistory() {
		UserTrailHistory record = new UserTrailHistory();

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
		
		return userTrailHistoryMapper.deleteByPrimaryKey(id);
	}


	@Override
	public int insert(UserTrailHistory record) {
		
		return userTrailHistoryMapper.insert(record);
	}


	@Override
	public int insertSelective(UserTrailHistory record) {

		return userTrailHistoryMapper.insertSelective(record);
	}


	@Override
	public UserTrailHistory selectByPrimaryKey(Long id) {
		
		return userTrailHistoryMapper.selectByPrimaryKey(id);
	}


	@Override
	public int updateByPrimaryKeySelective(UserTrailHistory record) {
		
		return userTrailHistoryMapper.updateByPrimaryKeySelective(record);
	}


	@Override
	public int updateByPrimaryKey(UserTrailHistory record) {
	
		return userTrailHistoryMapper.updateByPrimaryKey(record);
	}
	
	@Override
	public List<UserTrailHistory> selectBySearchVo(UserTrailSearchVo searchVo) {

		return userTrailHistoryMapper.selectBySearchVo(searchVo);
	}

	
}
	

