package com.jhj.service.impl.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.service.admin.AdminOrganizationService;
import com.jhj.vo.admin.AdminOrganiztionVo;
import com.jhj.po.dao.admin.AdminOrganizationMapper;
import com.jhj.po.model.admin.AdminOrganization;

@Service
public class AdminOrganizationServiceImpl implements AdminOrganizationService {

	@Autowired
	private AdminOrganizationMapper adminOrganizationMapper;

	@Override
	public AdminOrganization selectByPrimaryKey(Long id) {
		return adminOrganizationMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(AdminOrganization record) {
		return adminOrganizationMapper.updateByPrimaryKey(record);
	}

	@Override
	public AdminOrganization selectByName(String name) {
		return adminOrganizationMapper.selectByName(name);
    }

	@Override
	public int deleteByPrimaryKey(Long id) {
		return adminOrganizationMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(AdminOrganization record) {
		return adminOrganizationMapper.insert(record);
	}

	@Override
	public int insertSelective(AdminOrganization record) {
		return adminOrganizationMapper.insertSelective(record);
	}

	@Override
	public int updateByPrimaryKeySelective(AdminOrganization record) {
		return adminOrganizationMapper.updateByPrimaryKeySelective(record);
	}


	@Override
	public List<AdminOrganiztionVo> listChain() {

		List<AdminOrganiztionVo> listVo = new ArrayList<AdminOrganiztionVo>();
		//根据parentId=0 查询出所用的父节点
		List<AdminOrganization> list = adminOrganizationMapper.selectByParentId(0L);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			AdminOrganization adminOrganiztion = (AdminOrganization) iterator.next();
			//获得每个对象的Id,调用递归返回树形结构的对象
			AdminOrganiztionVo vo = toTree(adminOrganiztion.getId());
			listVo.add(vo);

		}
		return listVo;
	}

	@Override
	public AdminOrganiztionVo toTree(Long partnerId) {
		AdminOrganiztionVo adminOrganiztionVo = new AdminOrganiztionVo();
		//根据id查出某对象
		AdminOrganization adminOrganization = adminOrganizationMapper.selectByPrimaryKey(partnerId);
		try {
			//赋值给树形结构的vo
			BeanUtils.copyProperties(adminOrganiztionVo, adminOrganization);
		}  catch (Exception e1) {
			e1.printStackTrace();
		}

		//已id作为parentId查询出所用的子节点
		List<AdminOrganization>  child = adminOrganizationMapper.selectByParentId(partnerId);

		 for (AdminOrganization adminOrganization2 : child) {
			 AdminOrganiztionVo vo = toTree(adminOrganization2.getId().longValue());
			 adminOrganiztionVo.getChildren().add(vo);
		}
		return adminOrganiztionVo;
	}

}
