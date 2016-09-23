package com.jhj.service.users;

import java.util.HashMap;
import java.util.List;

import com.jhj.po.model.user.UserSmsToken;
import com.jhj.vo.user.UsersSmsTokenVo;



public interface UserSmsTokenService {
	 
	int deleteByPrimaryKey(Long id);

    int insert(UserSmsToken record);

    int insertSelective(UserSmsToken record);

    UserSmsToken selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserSmsToken record);

    int updateByPrimaryKey(UserSmsToken record);
    
	UserSmsToken initUserSmsToken(String mobile, int sms_type, String code,
			HashMap<String, String> sendSmsResult);

	UserSmsToken selectByMobileAndType(String mobile, int userType);

	List<UserSmsToken> selectByListPage(UsersSmsTokenVo usersSmsTokenVo,
			int pageNo, int pageSize);

	List<UserSmsToken> selectAll();

}
