package com.jhj.po.dao.user;

import java.util.List;
import java.util.Map;

import com.jhj.po.model.user.UserDetailPay;
import com.jhj.vo.UserDetailSearchVo;

public interface UserDetailPayMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserDetailPay record);

    int insertSelective(UserDetailPay record);

    UserDetailPay selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserDetailPay record);

    int updateByPrimaryKey(UserDetailPay record);
    
	List<UserDetailPay> selectByListPage(Map<String,Object> conditions);
	
	List<UserDetailPay> selectByListPages(UserDetailSearchVo userDetailSearchVo);

	UserDetailPay selectByTradeNo(String tradeNo);

}