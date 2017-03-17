package com.jhj.service.users;

import java.util.List;
import java.util.Map;

import com.jhj.po.model.user.CountUserRest;

public interface CountUserRestService {
    int deleteByPrimaryKey(Integer id);

    int insert(CountUserRest record);

    int insertSelective(CountUserRest record);

    CountUserRest selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CountUserRest record);

    int updateByPrimaryKey(CountUserRest record);
    
    int insertList(List<Map<String,Object>> list);
}