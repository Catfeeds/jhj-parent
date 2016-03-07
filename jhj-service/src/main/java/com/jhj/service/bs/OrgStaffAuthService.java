package com.jhj.service.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffAuth;


/**
 *
 * @author :hulj
 * @Date : 2015年7月1日上午11:16:44
 * @Description: TODO
 *
 */
public interface OrgStaffAuthService {
	int deleteByPrimaryKey(Long id);

    int insert(OrgStaffAuth record);

    int insertSelective(OrgStaffAuth record);

    OrgStaffAuth selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffAuth record);

    int updateByPrimaryKey(OrgStaffAuth record);
    
    OrgStaffAuth initOrgStaffAuth();

	OrgStaffAuth selectByStaffIdAndServiceTypeId(Long staffId);

	List<OrgStaffAuth> selectByStaffId(Long staffId);
	
	void deleteByStaffId(Long staffId);
}
