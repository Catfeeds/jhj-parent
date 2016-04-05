package com.jhj.service.users;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jhj.po.model.user.UserAddrs;

public interface UserAddrsService {
	
	int deleteByPrimaryKey(Long id);

    Long insert(UserAddrs record);

	Long insertSelective(UserAddrs record);

	UserAddrs selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(UserAddrs record);

	int updateByPrimaryKey(UserAddrs record);

	List<UserAddrs> selectByUserId(Long userId);

	int updataDefaultByUserId(Long userId);

	List<UserAddrs> selectByMobile(String mobile);

	int updataDefaultByMobile(String mobile);

	int updateByPrimaryKey(UserAddrs record, boolean updateOther);

	List<UserAddrs> getAll();

	List<UserAddrs> selectByIds(List<Long> addrIds);

	UserAddrs initUserAddrs();

	UserAddrs selectByUserIdAndAddrId(Long addrId,Long userId);

	int updataDefaultById(Long addrId);

	UserAddrs selectUserId(Long userId);
	
	UserAddrs selectByDefaultAddr(Long userId);

	UserAddrs selectByNameAndAddr(Long userId, String name, String addr);

	//根据唯一标识和 用户id,得到该记录
	UserAddrs selectByUidAndUserId(@Param("uid") String uid, @Param("userId") Long userId);


}