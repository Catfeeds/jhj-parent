package com.jhj.service.users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.dict.DictCardType;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.Users;
import com.jhj.vo.user.UserAppVo;
import com.jhj.vo.user.UserEditViewVo;
import com.jhj.vo.user.UserSearchVo;

public interface UsersService {
	
	int deleteByPrimaryKey(Long id);

    int insert(Users record);

    int insertSelective(Users record);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);
    
    int insertBatch(List<Users> list);
    
	Users genUser(String mobile, Short addFrom);
	
	Users initUsers(String mobile, Short addFrom);

	List<HashMap> totalByUserIds(List<Long> userIds);

	UserAppVo changeToUserAppVo(Long userId);

	UserEditViewVo getUserDetail(String orderNo,Long userId);

	UserEditViewVo getUserAddrDetail(List<UserAddrs> userAddrsList, Long userId);

	UserEditViewVo getUserEditViewDetail(String orderNo, Long userId);

	UserEditViewVo getUserAddrEditDetail(List<UserAddrs> userAddrsList,
			Long userId);
	
	Map<Long,String> selectDictCardDataSource();
	
	Map<String,String> getChargeWayDataSource();
	
	DictCardType selectDictCardTypeById(Long id);

	PageInfo selectByListPage(UserSearchVo searchVo, int pageNo, int pageSize);

	List<Users>	selectBySearchVo(UserSearchVo searchVo);

	Users selectByPrimaryKey(Long id);
	
	Users selectByMobile(String mobile);
	
	List<Users> selectUsersByOrderMobile();
	
	List<Users> listUserRestMoneyGtZero();
	
	//统计所有用户的余额
	Double countUserRestMoney();
}
