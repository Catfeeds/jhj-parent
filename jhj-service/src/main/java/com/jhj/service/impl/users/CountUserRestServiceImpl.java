package com.jhj.service.impl.users;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.user.CountUserRestMapper;
import com.jhj.po.model.user.CountUserRest;
import com.jhj.service.users.CountUserRestService;

@Service
public class CountUserRestServiceImpl implements CountUserRestService{
	
	@Autowired
	private CountUserRestMapper countUserRestMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		countUserRestMapper.deleteByPrimaryKey(id);
		return 0;
	}

	@Override
	public int insert(CountUserRest record) {
		countUserRestMapper.insert(record);
		return 0;
	}

	@Override
	public int insertSelective(CountUserRest record) {
		countUserRestMapper.insertSelective(record);
		return 0;
	}

	@Override
	public CountUserRest selectByPrimaryKey(Integer id) {
		countUserRestMapper.selectByPrimaryKey(id);
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(CountUserRest record) {
		countUserRestMapper.updateByPrimaryKeySelective(record);
		return 0;
	}

	@Override
	public int updateByPrimaryKey(CountUserRest record) {
		countUserRestMapper.updateByPrimaryKey(record);
		return 0;
	}

	@Override
	public int insertList(List<Map<String, Object>> list) {
		countUserRestMapper.insertList(list);
		return 0;
	}

}
