package com.jhj.po.dao.dict;

import java.util.List;

import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.order.OrderServiceAddons;

public interface DictServiceAddonsMapper {
    int deleteByPrimaryKey(Long serviceAddonId);

    int insert(DictServiceAddons record);

    int insertSelective(DictServiceAddons record);

    List<DictServiceAddons> selectByPrimaryKey(Long serviceAddonId);

    int updateByPrimaryKeySelective(DictServiceAddons record);

    int updateByPrimaryKey(DictServiceAddons record);

	DictServiceAddons selectByAddId(Long serviceAddonId);
		
	OrderServiceAddons selectByAddonId(Long serviceAddonId);
	
	List<DictServiceAddons> selectAll();
	
	List<DictServiceAddons> selectAllHourAddons();

	List<DictServiceAddons> selectByServiceAddonIds(List<Long> serviceAddonIds);

}