package com.jhj.service.admin;

import java.util.List;
import java.util.Map;

import com.jhj.vo.admin.AdminAuthorityVo;
import com.jhj.po.model.admin.AdminAuthority;

public interface AdminAuthorityService {

	int deleteByPrimaryKey(Long id);

	int insert(AdminAuthority record);

	int insertSelective(AdminAuthority record);

	int updateByPrimaryKeySelective(AdminAuthority record);

	List<AdminAuthority> selectByParentId(Long id);

	AdminAuthority selectByPrimaryKey(Long id);

	int updateByPrimaryKey(AdminAuthority record);

	List<AdminAuthority> selectByRole(Long roleId);

	List<AdminAuthority> selectByIds(List<Long> ids);

	List<AdminAuthorityVo> listChain();

	AdminAuthorityVo ToTree(Long parentId);

	int deleteByPartnerId(Long partnerId);

	AdminAuthority initAdminAuthority(AdminAuthorityVo adminAuthorityVo);

	int deleteAuthorityByRecurision(AdminAuthority adminAuthority);

	int selectMaxId();
	
	Map<Long,String> getSelectSource();

}