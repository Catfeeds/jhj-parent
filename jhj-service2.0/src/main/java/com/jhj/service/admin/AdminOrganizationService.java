package com.jhj.service.admin;

import java.util.List;

import com.jhj.vo.admin.AdminOrganiztionVo;
import com.jhj.po.model.admin.AdminOrganization;


public interface AdminOrganizationService {

	 int deleteByPrimaryKey(Long id);

     int insert(AdminOrganization record);

     int insertSelective(AdminOrganization record);

	 int updateByPrimaryKeySelective(AdminOrganization record);

	AdminOrganization selectByPrimaryKey(Long id);

	int updateByPrimaryKey(AdminOrganization record);

	AdminOrganization selectByName(String name);

	List<AdminOrganiztionVo> listChain();

	AdminOrganiztionVo toTree(Long PartnerId);



}