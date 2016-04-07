package com.jhj.service.impl.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.service.admin.AdminRoleService;
import com.jhj.vo.admin.AdminRoleSearchVo;
import com.jhj.vo.admin.AdminRoleVo;
import com.jhj.po.dao.admin.AdminAuthorityMapper;
import com.jhj.po.dao.admin.AdminRoleAuthorityMapper;
import com.jhj.po.dao.admin.AdminRoleMapper;
import com.jhj.po.model.admin.AdminAuthority;
import com.jhj.po.model.admin.AdminRole;
import com.jhj.po.model.admin.AdminRoleAuthority;

@Service
public class AdminRoleServiceImpl implements AdminRoleService {

	@Autowired
	private AdminRoleMapper adminRoleMapper;

	@Autowired
	private AdminRoleAuthorityMapper adminRoleAuthorityMapper;

	@Autowired
	private AdminAuthorityMapper adminAuthorityMapper;
	

	@Override
	public AdminRole selectByPrimaryKey(Long id) {
		return adminRoleMapper.selectByPrimaryKey(id);
	}

	@Override
	public AdminRole selectByName(String name) {
		return adminRoleMapper.selectByName(name);
	}

	@Override
	public AdminRole initAdminRole() {
		AdminRole adminRole = new AdminRole();
		adminRole.setId((long)0);
		adminRole.setEnable((short)1);
		adminRole.setName("");
		adminRole.setVersion((long)1);
		return adminRole;
	}

	@Override
	public PageInfo searchVoListPage(AdminRoleSearchVo searchVo, int pageNo,
			int pageSize) {

		HashMap<String,Object> conditions = new HashMap<String,Object>();
		 String name = searchVo.getName();

		if(name !=null && !name.isEmpty() ){
			conditions.put("name", name.trim());
		}

		PageHelper.startPage(pageNo, pageSize);
		List<AdminRole> list = adminRoleMapper.selectByListPage(conditions);
        PageInfo result = new PageInfo(list);
		return result;
	}

	@Override
	public int insert(AdminRole record) {
		return adminRoleMapper.insert(record);
	}

	@Override
	public Long insertSelective(AdminRole record) {
		return adminRoleMapper.insertSelective(record);
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		return adminRoleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(AdminRole record) {
		return adminRoleMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(AdminRole record) {
		return adminRoleMapper.updateByPrimaryKey(record);
	}

	@Override
	public AdminRole selectAdminRoleVoByPrimaryKey(Long id) {

		//查找出主键为id的角色
		
		AdminRole adminRole  = initAdminRole();

		if (id != null && id > 0L) {
			adminRole = adminRoleMapper.selectByPrimaryKey(id);
		}	
		AdminRoleVo adminRoleVo = new AdminRoleVo();
		try {
			BeanUtils.copyProperties(adminRoleVo, adminRole);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<AdminAuthority> list = adminRoleVo.getChildList();
		//在角色和权限表中查找出RoleId= id的所有权限
		List<AdminRoleAuthority> amdinAdminRoleAuthorities = adminRoleAuthorityMapper.selectByRoleId(id);
		for (Iterator iterator  = amdinAdminRoleAuthorities.iterator(); iterator.hasNext();) {
			AdminRoleAuthority adminRoleAuthority = (AdminRoleAuthority) iterator.next();
			if(adminRoleAuthority!=null){
				list.add(adminAuthorityMapper.selectByPrimaryKey(adminRoleAuthority.getAuthorityId()));
			}
		}
		return adminRoleVo;
	}

	@Override
	public void saveRoleToAuthorize(Long roleId, Long[] authorityIds) {


		int result = adminRoleAuthorityMapper.deleteByRoleId(roleId);
		if(result >=0){
			List<AdminAuthority> authorities=new ArrayList<AdminAuthority>();
			if(authorityIds.length>0){
				for(Long authorityId : authorityIds){
					AdminRoleAuthority adminRoleAuthority  = new AdminRoleAuthority();
					adminRoleAuthority.setRoleId(roleId);
					adminRoleAuthority.setAuthorityId(authorityId);
					adminRoleAuthorityMapper.insertSelective(adminRoleAuthority);
				}
			}
		}
	}

	@Override
	public Map<Long, String> getSelectSource() {
		Map<Long,String> roleMap = new HashMap<Long, String>();
		List<AdminRole> roleList = adminRoleMapper.selectAll();
		for (AdminRole adminRole : roleList) {
			roleMap.put(adminRole.getId(),adminRole.getName());
		}
		return roleMap;
	}
	
	@Override
	public List<AdminRole> selectAll() {
		return adminRoleMapper.selectAll();
	}
}
