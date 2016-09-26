package com.jhj.service.dict;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.vo.ServiceAddonSearchVo;

public interface ServiceAddonsService {
	
	int deleteByPrimaryKey(Long serviceAddonId);

    int insert(DictServiceAddons record);

    int insertSelective(DictServiceAddons record);

    DictServiceAddons selectByPrimaryKey(Long serviceAddonId);

    int updateByPrimaryKeySelective(DictServiceAddons record);

    int updateByPrimaryKey(DictServiceAddons record);
	
	List<DictServiceAddons> selectBySearchVo(ServiceAddonSearchVo searchVo);

	PageInfo selectByListPage(ServiceAddonSearchVo searchVo, int pageNo, int pageSize);

	DictServiceAddons initDictServiceAddons();

	int deleteByServiceType(Long serviceType);

	boolean updateByRequest(HttpServletRequest request, Long serviceTypeId);
	
}
