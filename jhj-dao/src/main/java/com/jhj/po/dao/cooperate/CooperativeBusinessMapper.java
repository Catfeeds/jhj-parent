package com.jhj.po.dao.cooperate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jhj.po.model.cooperate.CooperativeBusiness;
import com.jhj.vo.dict.CooperativeBusinessSearchVo;

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
	
	List<CooperativeBusiness> selectCooperativeBusinessVo(CooperativeBusinessSearchVo vo);
	
	//统计各个负责人的平台数量
	List<Map<String,String>> selectByGroupBybroker();
	
	CooperativeBusiness selectByBusinessName(String businessName);
}