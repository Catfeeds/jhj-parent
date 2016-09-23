package com.jhj.po.dao.user;
import java.util.List;

import com.jhj.po.model.user.UserPushBind;
import com.jhj.vo.user.UserPushBindSearchVo;



public interface UserPushBindMapper {
   
	int updateByPrimaryKeySelective(UserPushBind record);

	int updateByPrimaryKey(UserPushBind record);

	int deleteByPrimaryKey(Long id);

    int insert(UserPushBind record);

    int insertSelective(UserPushBind record);

    UserPushBind selectByPrimaryKey(Long id);

	List<UserPushBind> selectBySearchVo(UserPushBindSearchVo searchVo);
}