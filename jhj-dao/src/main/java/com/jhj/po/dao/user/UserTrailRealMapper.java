package com.jhj.po.dao.user;

import java.util.List;

import com.jhj.po.model.user.UserTrailReal;
import com.jhj.vo.staff.StaffSearchVo;
import com.jhj.vo.user.UserTrailSearchVo;

public interface UserTrailRealMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserTrailReal record);

    int insertSelective(UserTrailReal record);

    UserTrailReal selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserTrailReal record);

    int updateByPrimaryKey(UserTrailReal record);
	
	List<UserTrailReal> selectBySearchVo(UserTrailSearchVo searchVo);

	List<UserTrailReal> selectByListPage(UserTrailSearchVo searchVo);
	
	List<UserTrailReal> selectByStaffListPage(StaffSearchVo searchVo);
}