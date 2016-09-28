package com.jhj.po.dao.dict;

import java.util.List;

import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.vo.ServiceAddonSearchVo;

public interface DictServiceAddonsMapper {
    int deleteByPrimaryKey(Long serviceAddonId);

    int insert(DictServiceAddons record);

    int insertSelective(DictServiceAddons record);

    DictServiceAddons selectByPrimaryKey(Long serviceAddonId);

    int updateByPrimaryKeySelective(DictServiceAddons record);

    int updateByPrimaryKey(DictServiceAddons record);

    List<DictServiceAddons> selectBySearchVo(ServiceAddonSearchVo searchVo);
			
	List<DictServiceAddons> selectByListPage(ServiceAddonSearchVo searchVo);

	int deleteByServiceType(Long serviceType);

}