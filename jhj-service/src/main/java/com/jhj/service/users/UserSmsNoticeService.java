package com.jhj.service.users;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.user.UserSmsNotice;
import com.jhj.vo.user.UserSmsNoticeSearchVo;

public interface UserSmsNoticeService {
	 
	int deleteByPrimaryKey(Long id);

    int insert(UserSmsNotice record);

    int insertSelective(UserSmsNotice record);

    int updateByPrimaryKeySelective(UserSmsNotice record);

    int updateByPrimaryKey(UserSmsNotice record);
    
    UserSmsNotice initPo();

    UserSmsNotice selectByPrimaryKey(Long id);

	PageInfo selectByListPage(UserSmsNoticeSearchVo searchVo, int pageNo, int pageSize);

	List<UserSmsNotice> selectBySearchVo(UserSmsNoticeSearchVo searchVo);

	Integer totalBySearchVo(UserSmsNoticeSearchVo searchVo);
}
