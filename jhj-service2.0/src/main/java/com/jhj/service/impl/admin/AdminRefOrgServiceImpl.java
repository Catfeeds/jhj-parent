package com.jhj.service.impl.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.admin.AdminRefOrgMapper;
import com.jhj.po.model.admin.AdminRefOrg;
import com.jhj.service.admin.AdminRefOrgService;

@Service
public class AdminRefOrgServiceImpl implements AdminRefOrgService {

	@Autowired
	private AdminRefOrgMapper adMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return adMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(AdminRefOrg record) {
		return adMapper.insert(record);
	}

	@Override
	public int insertSelective(AdminRefOrg record) {
		return adMapper.insertSelective(record);
	}

	@Override
	public AdminRefOrg selectByPrimaryKey(Long id) {
		return adMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(AdminRefOrg record) {
		return adMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(AdminRefOrg record) {
		return adMapper.updateByPrimaryKey(record);
	}

	@Override
	public AdminRefOrg selectByAdminIdAndOrgId(Long adminId, Long orgId) {
		return adMapper.selectByAdminIdAndOrgId(adminId, orgId);
	}

	@Override
	public AdminRefOrg selectByAdminId(Long adminId) {
		return adMapper.selectByAdminId(adminId);
	}

}
