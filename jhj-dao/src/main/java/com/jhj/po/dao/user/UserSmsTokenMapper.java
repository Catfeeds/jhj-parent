package com.jhj.po.dao.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jhj.po.model.user.UserSmsToken;
import com.jhj.vo.user.UsersSmsTokenVo;


public interface UserSmsTokenMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserSmsToken record);

    int insertSelective(UserSmsToken record);

    UserSmsToken selectByPrimaryKey(Long id);

    UserSmsToken selectByMobileAndTypes(String mobile);
    
    UserSmsToken selectByMobile(String mobile);

    int updateByPrimaryKeySelective(UserSmsToken record);

    int updateByPrimaryKey(UserSmsToken record);

	UserSmsToken selectByMobileAndType(@Param("mobile")String mobile, @Param("userType")int userType);

	List<UserSmsToken> selectUserSmsTokenByMobile(UsersSmsTokenVo usersSmsTokenVo);

	List<UserSmsToken> selectAll();
}