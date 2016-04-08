package com.jhj.po.dao.cooperate;

import java.util.HashMap;
import java.util.List;

import com.jhj.po.model.cooperate.CooperativeBusiness;

public interface CooperativeBusinessMapper {
	int deleteByPrimaryKey(Long id);

    int insert(CooperativeBusiness record);

    int insertSelective(CooperativeBusiness record);

    CooperativeBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CooperativeBusiness record);

    int updateByPrimaryKey(CooperativeBusiness record);
    
    List<CooperativeBusiness> selectByListPage();
    
	CooperativeBusiness selectBylogInName(String logInName);
	
	CooperativeBusiness selectByUsernameAndPassword(HashMap conditions);
	
	List<Long> getAllCoopId();
}