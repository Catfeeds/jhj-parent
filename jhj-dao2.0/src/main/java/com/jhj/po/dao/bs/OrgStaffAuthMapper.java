package com.jhj.po.dao.bs;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jhj.po.model.bs.OrgStaffAuth;
import com.jhj.po.model.order.Orders;

public interface OrgStaffAuthMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrgStaffAuth record);

    int insertSelective(OrgStaffAuth record);

    OrgStaffAuth selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffAuth record);

    int updateByPrimaryKey(OrgStaffAuth record);

	OrgStaffAuth selectByStaffIdAndServiceTypeId(Long staffId);
	
	List<OrgStaffAuth> selectByStaffId(Long staffId);
	
	void deleteByStaffId(Long staffId);
	
	List<OrgStaffAuth> selectHourStaffByServiceTypeIdAndAuthStatus();

	List<OrgStaffAuth> selectAmStaffByServiceTypeIdAndAuthStatus();

}