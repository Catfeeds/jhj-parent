package com.jhj.service.impl.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.dict.DictServiceAddonsMapper;
import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.service.dict.ServiceAddonsService;
/**
 * @description：
 * @author： kerryg
 * @date:2015年7月31日 
 */
@Service
public class ServiceAddonsServiceImpl implements ServiceAddonsService {

	@Autowired
	private DictServiceAddonsMapper dictServiceAddonsMapper;
	
	@Override
	public int deleteByPrimaryKey(Long serviceAddonId) {
		return dictServiceAddonsMapper.deleteByPrimaryKey(serviceAddonId);
	}

	@Override
	public int insert(DictServiceAddons record) {
		return dictServiceAddonsMapper.insert(record);
	}

	@Override
	public int insertSelective(DictServiceAddons record) {
		return dictServiceAddonsMapper.insertSelective(record);
	}

	@Override
	public List<DictServiceAddons> selectByPrimaryKey(Long serviceAddonId) {
		return dictServiceAddonsMapper.selectByPrimaryKey(serviceAddonId);
	}

	@Override
	public int updateByPrimaryKeySelective(DictServiceAddons record) {
		return dictServiceAddonsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(DictServiceAddons record) {
		return dictServiceAddonsMapper.updateByPrimaryKey(record);
	}

	@Override
	public DictServiceAddons selectByAddId(Long serviceAddonId) {
		return dictServiceAddonsMapper.selectByAddId(serviceAddonId);
	}

	@Override
	public List<DictServiceAddons> selectByServiceAddonIds(List<Long> serviceAddonIds) {
		return dictServiceAddonsMapper.selectByServiceAddonIds(serviceAddonIds);
	}

	@Override
	public OrderServiceAddons selectByAddonId(Long serviceAddonId) {
		return dictServiceAddonsMapper.selectByAddonId(serviceAddonId);
	}

	@Override
	public List<DictServiceAddons> getServiceAddonsTypes() {
		return dictServiceAddonsMapper.selectAll();
	}

	@Override
	public List<DictServiceAddons> selectAllHourAddons() {
		return dictServiceAddonsMapper.selectAllHourAddons();
	}

}
