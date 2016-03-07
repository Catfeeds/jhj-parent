package com.jhj.service.admin;


import com.jhj.po.model.admin.AdminRefOrg;

public interface AdminRefOrgService {
	
	  	int deleteByPrimaryKey(Long id);

	    int insert(AdminRefOrg record);

	    int insertSelective(AdminRefOrg record);

	    AdminRefOrg selectByPrimaryKey(Long id);

	    int updateByPrimaryKeySelective(AdminRefOrg record);

	    int updateByPrimaryKey(AdminRefOrg record);
	    
	    AdminRefOrg selectByAdminId(Long adminId);
	    
	    AdminRefOrg selectByAdminIdAndOrgId(Long adminId, Long orgId);
}
