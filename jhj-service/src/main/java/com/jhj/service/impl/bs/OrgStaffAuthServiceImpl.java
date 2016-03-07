package com.jhj.service.impl.bs;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.bs.OrgStaffAuthMapper;
import com.jhj.po.model.bs.OrgStaffAuth;
import com.jhj.service.bs.OrgStaffAuthService;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年7月6日下午2:44:25
 * @Description: 
 *
 */
@Service
public class OrgStaffAuthServiceImpl implements OrgStaffAuthService {

	@Autowired
	private OrgStaffAuthMapper orgStaffAuthMapper;

	@Override
	public int deleteByPrimaryKey(Long id) {
		
		return orgStaffAuthMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrgStaffAuth record) {
		
		return orgStaffAuthMapper.insert(record);
	}

	@Override
	public int insertSelective(OrgStaffAuth record) {
	
		return orgStaffAuthMapper.insertSelective(record);
	}

	@Override
	public OrgStaffAuth selectByPrimaryKey(Long orderId) {
		
		return orgStaffAuthMapper.selectByPrimaryKey(orderId);
	}

	@Override
	public int updateByPrimaryKeySelective(OrgStaffAuth record) {
		
		return orgStaffAuthMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrgStaffAuth record) {
		
		return orgStaffAuthMapper.updateByPrimaryKey(record);
	}

	@Override
	public OrgStaffAuth initOrgStaffAuth() {
		
		OrgStaffAuth record = new OrgStaffAuth();
		
		record.setId(0L);
		record.setStaffId(0L);
		record.setServiceTypeId(0L);
		record.setAutStatus((short)0L);
		record.setAddTime(TimeStampUtil.getNowSecond());
		record.setUpdateTime(TimeStampUtil.getNowSecond());
		
		return record; 
	}

	@Override
	public OrgStaffAuth selectByStaffIdAndServiceTypeId(Long staffId) {
		
		return orgStaffAuthMapper.selectByStaffIdAndServiceTypeId(staffId);
	}

	@Override
	public List<OrgStaffAuth> selectByStaffId(Long staffId) {
		return orgStaffAuthMapper.selectByStaffId(staffId);
	}

	@Override
	public void deleteByStaffId(Long staffId) {
		orgStaffAuthMapper.deleteByStaffId(staffId);
	}
	
}
