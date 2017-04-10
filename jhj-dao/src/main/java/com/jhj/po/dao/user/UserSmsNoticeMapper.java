package com.jhj.po.dao.user;

import java.util.List;

import com.jhj.po.model.user.UserSmsNotice;
import com.jhj.vo.user.UserSmsNoticeSearchVo;

public interface UserSmsNoticeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserSmsNotice record);

    int insertSelective(UserSmsNotice record);

    UserSmsNotice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserSmsNotice record);

    int updateByPrimaryKey(UserSmsNotice record);
    
    List<UserSmsNotice> selectBySearchVo(UserSmsNoticeSearchVo searchVo);
    
    List<UserSmsNotice> selectByListPage(UserSmsNoticeSearchVo searchVo);
    
    Integer totalBySearchVo(UserSmsNoticeSearchVo searchVo);
}