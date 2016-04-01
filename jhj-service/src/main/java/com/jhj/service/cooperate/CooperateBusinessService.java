package com.jhj.service.cooperate;

import com.jhj.po.model.cooperate.CooperativeBusiness;

/**
 *
 * @author :hulj
 * @Date : 2016年4月1日下午3:18:57
 * @Description: 
 *
 */
public interface CooperateBusinessService {
	
	int deleteByPrimaryKey(Long id);

    int insert(CooperativeBusiness record);

    int insertSelective(CooperativeBusiness record);

    CooperativeBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CooperativeBusiness record);

    int updateByPrimaryKey(CooperativeBusiness record);
	
}
