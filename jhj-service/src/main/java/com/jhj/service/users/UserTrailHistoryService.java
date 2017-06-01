package com.jhj.service.users;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.user.UserTrailHistory;
import com.jhj.vo.user.UserTrailSearchVo;

public interface UserTrailHistoryService {
	
	int deleteByPrimaryKey(Long id);

    int insert(UserTrailHistory record);

    int insertSelective(UserTrailHistory record);

    UserTrailHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserTrailHistory record);

    int updateByPrimaryKey(UserTrailHistory record);

    UserTrailHistory initUserTrailHistory();

	List<UserTrailHistory> selectBySearchVo(UserTrailSearchVo searchVo);

	PageInfo selectByListPage(UserTrailSearchVo searchVo, int pageNo, int pageSize);

}
