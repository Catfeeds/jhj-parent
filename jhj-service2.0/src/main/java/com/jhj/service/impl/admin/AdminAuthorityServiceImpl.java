package com.jhj.service.impl.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.service.admin.AdminAuthorityService;
import com.jhj.vo.admin.AdminAuthorityVo;
import com.jhj.po.dao.admin.AdminAuthorityMapper;
import com.jhj.po.dao.admin.AdminRoleAuthorityMapper;
import com.jhj.po.model.admin.AdminAuthority;
import com.jhj.po.model.admin.AdminRoleAuthority;


@Service
public class AdminAuthorityServiceImpl implements AdminAuthorityService {

	@Autowired
	private AdminAuthorityMapper adminAuthorityMapper;

	@Autowired
	private AdminRoleAuthorityMapper adminRoleAuthorityMapper;

	@Override
	public AdminAuthority selectByPrimaryKey(Long id) {
		return adminAuthorityMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(AdminAuthority record) {
		return adminAuthorityMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<AdminAuthority> selectByIds (List<Long> ids) {
		return adminAuthorityMapper.selectByIds(ids);
	}

	@Override
	public List<AdminAuthority> selectByRole (Long roleId) {
		List<AdminRoleAuthority> list = adminRoleAuthorityMapper.selectByRoleId(roleId);

		List<Long> ids = new ArrayList<Long>();
		for (int i =0; i < list.size(); i++) {
			ids.add(list.get(i).getAuthorityId());
		}
		List<AdminAuthority> result = new ArrayList<AdminAuthority>();
		if (ids.size() > 0) {
			result = adminAuthorityMapper.selectByIds(ids);
		}
		return result;
	}

	@Override
	public List<AdminAuthorityVo> listChain()  {

		List<AdminAuthorityVo> listVo = new ArrayList<AdminAuthorityVo>();
		//根据parentId=0 查询出所用的父节点
		List<AdminAuthority> list = adminAuthorityMapper.selectByParentId(0L);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			AdminAuthority adminAuthority = (AdminAuthority) iterator.next();
			//获得每个对象的Id,调用递归返回树形结构的对象
//			System.out.println(adminAuthority.getId());
			AdminAuthorityVo vo = ToTree(adminAuthority.getId());
			listVo.add(vo);

		}
		return listVo;
	}

	@Override
	public AdminAuthorityVo ToTree(Long id)  {

		AdminAuthorityVo adminAuthorityVo = new AdminAuthorityVo();
		//根据id查出某对象
		AdminAuthority adminAuthority = adminAuthorityMapper.selectByPrimaryKey(id);
		try {
			//赋值给树形结构的vo
			BeanUtils.copyProperties(adminAuthorityVo, adminAuthority);
		}  catch (Exception e1) {
			e1.printStackTrace();
		}

		//已id作为parentId查询出所用的子节点
		List<AdminAuthority>  child = adminAuthorityMapper.selectByParentId(id);

		 for (AdminAuthority adminAuthority2 : child) {
			 if (adminAuthority2 != null && adminAuthority2.getId() != null) {
				 AdminAuthorityVo vo = ToTree(adminAuthority2.getId().longValue());
				 adminAuthorityVo.getChildren().add(vo);
			 }
		}
		return adminAuthorityVo;
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		return adminAuthorityMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(AdminAuthority record) {
		return adminAuthorityMapper.insert(record);
	}

	@Override
	public int insertSelective(AdminAuthority record) {
		return adminAuthorityMapper.insertSelective(record);
	}

	@Override
	public int updateByPrimaryKeySelective(AdminAuthority record) {
		return adminAuthorityMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<AdminAuthority> selectByParentId(Long id) {
		return adminAuthorityMapper.selectByParentId(id);
	}

	@Override
	public int deleteByPartnerId(Long partnerId) {
		return adminAuthorityMapper.deleteByPartnerId(partnerId);
	}

	@Override
	public AdminAuthority initAdminAuthority(AdminAuthorityVo adminAuthorityVo) {
		AdminAuthority adminAuthority = new AdminAuthority();
		adminAuthority.setName(adminAuthorityVo.getName());
		adminAuthority.setPosition(adminAuthorityVo.getPosition());
		adminAuthority.setTheValue(adminAuthorityVo.getTheValue());
		adminAuthority.setUrl(adminAuthorityVo.getUrl());
		adminAuthority.setMatchUrl(adminAuthorityVo.getMatchUrl());
		adminAuthority.setItemIcon(adminAuthorityVo.getItemIcon());
		adminAuthority.setVersion(1L);
		adminAuthority.setEnable((short) 1);
		return adminAuthority;
	}

	@Override
	public int deleteAuthorityByRecurision(AdminAuthority adminAuthority) {
		int result = 0;
		//根据获得的对象，判断是否拥有子节点
		List<AdminAuthority> list = adminAuthorityMapper.selectByParentId(adminAuthority.getId());
		//如果含有子节点，再进行递归调用
		if(list !=null && list.size()>0){
			for (AdminAuthority adminAuthority2 : list) {
				result = deleteAuthorityByRecurision(adminAuthority2);
			}
		}else{//如果没有子节点则直接删除自己
			result = adminAuthorityMapper.deleteByPrimaryKey(adminAuthority.getId());
		}
		return result ;
	}

	@Override
	public int selectMaxId() {
		return adminAuthorityMapper.selectMaxId();
	}

	@Override
	public Map<Long, String> getSelectSource() {
		Map<Long,String> adminAuthorityMap = new HashMap<Long, String>();
		List<AdminAuthority> adminAuthorityList = adminAuthorityMapper.selectChildAdminAuthorities();
		for (AdminAuthority adminAuthority : adminAuthorityList) {
			adminAuthorityMap.put(adminAuthority.getId(),adminAuthority.getName());
		}
		return adminAuthorityMap;
	}
}
