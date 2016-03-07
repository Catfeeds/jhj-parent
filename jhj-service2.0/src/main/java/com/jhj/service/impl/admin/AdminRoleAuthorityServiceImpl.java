package com.jhj.service.impl.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.admin.AdminRoleAuthorityMapper;
import com.jhj.po.model.admin.AdminRoleAuthority;
import com.jhj.service.admin.AdminRoleAuthorityService;


@Service
public class AdminRoleAuthorityServiceImpl implements AdminRoleAuthorityService{

	@Autowired
	private AdminRoleAuthorityMapper adminRoleAuthorityMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return adminRoleAuthorityMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int deleteByRoleId(Long id) {
		return adminRoleAuthorityMapper.deleteByRoleId(id);
	}

	@Override
	public int insert(AdminRoleAuthority record) {
		return adminRoleAuthorityMapper.insert(record);
	}

	@Override
	public int insertSelective(AdminRoleAuthority record) {
		return adminRoleAuthorityMapper.insertSelective(record);
	}

	@Override
	public AdminRoleAuthority selectByPrimaryKey(Long id) {
		return adminRoleAuthorityMapper.selectByPrimaryKey(id);
	}

	@Override
	public AdminRoleAuthority selectByRoleAndAuthority(Long roleId,
			Long authorityId) {
		return adminRoleAuthorityMapper.selectByRoleAndAuthority(roleId, authorityId);
	}

	@Override
	public int updateByPrimaryKeySelective(AdminRoleAuthority record) {
		return adminRoleAuthorityMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(AdminRoleAuthority record) {
		return adminRoleAuthorityMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<AdminRoleAuthority> selectByRoleId(Long roleId) {
		return adminRoleAuthorityMapper.selectByRoleId(roleId);
	}

	@Override
	public List<AdminRoleAuthority> selectAll() {
		return adminRoleAuthorityMapper.selectAll();
	}

	

	

}
