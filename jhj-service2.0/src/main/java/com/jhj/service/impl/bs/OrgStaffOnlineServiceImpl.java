package com.jhj.service.impl.bs;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.bs.OrgStaffOnlineMapper;
import com.jhj.po.model.bs.OrgStaffOnline;
import com.jhj.service.bs.OrgStaffOnlineService;
import com.meijia.utils.TimeStampUtil;



/**
 *
 * @author :hulj
 * @Date : 2015年7月6日下午2:44:25
 * @Description: 
 *
 */
@Service
public class OrgStaffOnlineServiceImpl implements OrgStaffOnlineService {

	@Autowired
	private OrgStaffOnlineMapper orgStaffOnlineMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		
		return orgStaffOnlineMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrgStaffOnline record) {
		
		return orgStaffOnlineMapper.insert(record);
	}

	@Override
	public int insertSelective(OrgStaffOnline record) {
		
		return orgStaffOnlineMapper.insertSelective(record);
	}

	@Override
	public OrgStaffOnline selectByPrimaryKey(Long id) {
		
		return orgStaffOnlineMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OrgStaffOnline record) {
		
		return orgStaffOnlineMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrgStaffOnline record) {
		
		return orgStaffOnlineMapper.updateByPrimaryKey(record);
	}

	@Override
	public OrgStaffOnline initOnline() {
		
		OrgStaffOnline online = new OrgStaffOnline();
		
		online.setId(0L);
		online.setStaffId(0L);
		online.setIsWork((short)0L);
		online.setLat("");
		online.setLng("");
		online.setAddTime(TimeStampUtil.getNowSecond());
		
		return online;
	}

	@Override
	public OrgStaffOnline selectByStaffId(Long userId) {
		
		return orgStaffOnlineMapper.selectByStaffId(userId);
	}

	@Override
	public List<OrgStaffOnline> selectByStaffIdLimitTwo(Long staffId) {
		
		return orgStaffOnlineMapper.selectByStaffIdLimitTwo(staffId);
	}

	
}
