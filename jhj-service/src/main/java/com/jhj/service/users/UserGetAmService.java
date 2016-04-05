package com.jhj.service.users;

import com.jhj.vo.user.UserGetAmVo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月11日下午5:24:40
 * @Description: 
 * 
 * 	 公用service
 * 		1、 用户版--用户查看助理页签
 *      2、 助理版 -- 我的
 */
public interface UserGetAmService {
	
	UserGetAmVo  getAmVoByUserId(Long userId);
	
	UserGetAmVo getAmVoByAmId(Long amId);
	
	UserGetAmVo initUserGetAmVo();

	UserGetAmVo getStaffByUserId(Long userId, Long amId);
}
