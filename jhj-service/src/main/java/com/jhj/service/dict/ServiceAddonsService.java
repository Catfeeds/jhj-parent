package com.jhj.service.dict;

import java.util.List;

import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.order.OrderServiceAddons;

public interface ServiceAddonsService {
	
	int deleteByPrimaryKey(Long serviceAddonId);

    int insert(DictServiceAddons record);

    int insertSelective(DictServiceAddons record);

    List<DictServiceAddons> selectByPrimaryKey(Long serviceAddonId);

    int updateByPrimaryKeySelective(DictServiceAddons record);

    int updateByPrimaryKey(DictServiceAddons record);

	DictServiceAddons selectByAddId(Long serviceAddonId);
	
	OrderServiceAddons selectByAddonId(Long serviceAddonId);
	
	List<DictServiceAddons> getServiceAddonsTypes();

	List<DictServiceAddons> selectAllHourAddons();

	List<DictServiceAddons> selectByServiceAddonIds(List<Long> serviceAddonIds);
}
