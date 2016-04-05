package com.jhj.po.dao.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jhj.po.model.user.UserAddrs;


public interface UserAddrsMapper {
	    int deleteByPrimaryKey(Long id);
	
	    Long insert(UserAddrs record);
	
	    Long insertSelective(UserAddrs record);
	
	    int updateByPrimaryKeySelective(UserAddrs record);
	
	    int updateByPrimaryKey(UserAddrs record);

	    UserAddrs selectByPrimaryKey(Long id);

	    int updateDefaultByMobile(String mobile);
	    
	    int updateDefaultByUserId(Long userId);
	    
	    int updateDefaultById(Long addrId);

	    int updateByMobile(String mobile);

		List<UserAddrs> selectByMobile(String mobile);
		
		List<UserAddrs> selectByUserId(Long userId);

		List<UserAddrs> selectAll();

		List<UserAddrs> selectByIds(List<Long> ids);
		
		UserAddrs selectByUserIdAndAddrId(@Param("addrId")Long addrId,@Param("userId")Long userId);

		UserAddrs selectUserId(Long userId);

		List<UserAddrs> selectListByUserId(Long userId);
		
		UserAddrs selectByNameAndAddr(@Param("userId") Long userId, @Param("name") String name,@Param("addr") String addr);

		UserAddrs selectByDefaultAddr(Long userId);
		
		//根据唯一标识和 用户,得到该记录
		UserAddrs selectByUidAndUserId(@Param("uid") String uid, @Param("userId") Long userId);
	
}