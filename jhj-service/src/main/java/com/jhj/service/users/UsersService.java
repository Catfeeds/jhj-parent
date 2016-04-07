package com.jhj.service.users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.dict.DictCardType;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.Users;
import com.jhj.vo.UserSearchVo;
import com.jhj.vo.user.UserAppVo;
import com.jhj.vo.user.UserEditViewVo;

public interface UsersService {
	
	int deleteByPrimaryKey(Long id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByUsersId(Long id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);
    
	PageInfo searchVoListPage(UserSearchVo searchVo,int pageNo,int pageSize);

	Users getUserById(Long id);
	
//	List<Long>  getBadOrgStaff(Long userId);

	List<Users> selectByUserIds(List<Long> ids);

	Users getUserByMobile(String mobile);
	
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

	List<Users> selectByListPage(List<Long> userIdList, int pageNo, int pageSize);

	List<Users> selectByAll();
	
	/*
	 *   2016年4月7日16:14:15 
 	 *   不同来源的 用户,主要 针对 注册 自 不同合作商户的 用户
	 */
	List<Users> selectUserByAddFrom(Long addFrom);
	
}
