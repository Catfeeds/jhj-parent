package com.jhj.service.users;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jhj.po.model.user.UserRefAm;
import com.jhj.vo.user.UsersListsVo;


public interface UserRefAmService {

	int insertSelective(UserRefAm record);
	
	UserRefAm selectByAmId(Long userId);

	List<HashMap> totalByAmIds(List<Long> amIds);

	int insert(UserRefAm record);

	List<UserRefAm> selectUserListByAmId(Long staffId, int page, int pAGE_MAX_NUMBER);

	UsersListsVo getUserList(UserRefAm userRefAm);

	List<UserRefAm> selectAllUserByAmId(Long amId);
	
	UserRefAm selectByUserId(Long userId);
	
	
	void delectByStaffId(Map<String, Object> map);
	
	List<UserRefAm> selectByUserIdAndAmId(Map<String, Object> map);
}
