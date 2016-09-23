package com.jhj.service.users;

import java.util.List;

import com.jhj.po.model.user.UserPushBind;
import com.jhj.vo.user.UserPushBindSearchVo;

public interface UserPushBindService {
	
	int deleteByPrimaryKey(Long id);

    int insert(UserPushBind record);

    int insertSelective(UserPushBind record);

    UserPushBind selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserPushBind record);

    int updateByPrimaryKey(UserPushBind record);

    UserPushBind initUserPushBind();

	List<UserPushBind> selectBySearchVo(UserPushBindSearchVo searchVo);
}
