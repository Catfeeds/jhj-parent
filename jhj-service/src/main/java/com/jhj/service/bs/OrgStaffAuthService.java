package com.jhj.service.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffAuth;
import com.jhj.vo.staff.StaffAuthSearchVo;


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
	
	void deleteByStaffId(Long staffId);

	List<OrgStaffAuth> selectBySearchVo(StaffAuthSearchVo searchVo);
}
