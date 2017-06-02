package com.jhj.service.users;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.user.UserTrailReal;
import com.jhj.vo.staff.OrgStaffPoiListVo;
import com.jhj.vo.staff.StaffSearchVo;
import com.jhj.vo.user.UserTrailSearchVo;

public interface UserTrailRealService {
	
	int deleteByPrimaryKey(Long id);

    int insert(UserTrailReal record);

    int insertSelective(UserTrailReal record);

    UserTrailReal selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserTrailReal record);

    int updateByPrimaryKey(UserTrailReal record);

    UserTrailReal initUserTrailReal();

	List<UserTrailReal> selectBySearchVo(UserTrailSearchVo searchVo);
	
	PageInfo selectByListPage(UserTrailSearchVo searchVo, int pageNo, int pageSize);

	PageInfo selectByStaffListPage(StaffSearchVo searchVo, int pageNo, int pageSize);

	OrgStaffPoiListVo getOrgStaffPoiListVo(UserTrailReal item);
}
