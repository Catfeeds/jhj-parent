package com.jhj.po.dao.user;

import java.util.List;

import com.jhj.po.model.user.UserTrailHistory;
import com.jhj.vo.user.UserTrailSearchVo;

public interface UserTrailHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserTrailHistory record);

    int insertSelective(UserTrailHistory record);

    UserTrailHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserTrailHistory record);

    int updateByPrimaryKey(UserTrailHistory record);

	List<UserTrailHistory> selectBySearchVo(UserTrailSearchVo searchVo);

	List<UserTrailHistory> selectByListPage(UserTrailSearchVo searchVo);
}