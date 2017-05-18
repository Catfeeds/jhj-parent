package com.jhj.service.users;

import java.util.List;

import com.jhj.po.model.user.UserLogined;
import com.jhj.po.model.user.UserSmsToken;



public interface UserLoginedService {
    int deleteByPrimaryKey(Long id);

    int insert(UserLogined record);

    int insertSelective(UserLogined record);

    UserLogined selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserLogined record);

    int updateByPrimaryKey(UserLogined record);
    
    UserLogined initUserLogined(UserSmsToken smsToken, int login_from, long ip);
    
    List<String>  selectDistinctAll();
    
    int countUserLogined(String mobile);
}