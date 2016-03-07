package com.jhj.po.dao.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jhj.po.model.user.UserRefAm;
import com.jhj.vo.AmSearchListVo;

public interface UserRefAmMapper {
    int insert(UserRefAm record);

    int insertSelective(UserRefAm record);
    
    UserRefAm selectByUserId(Long userId);

	List<HashMap> totalByAmIds(List<Long> amIds);

	List<UserRefAm> selectUserListByAmId(AmSearchListVo amSearchListVo);

	List<UserRefAm> selectAllUserByAmId(Long amId);
    
	List<UserRefAm> selectByUserIdAndAmId(Map<String, Object> map);
	
	void delectByStaffId(Map<String, Object> map);
}