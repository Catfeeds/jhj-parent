package com.jhj.service.impl.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.po.dao.dict.DictServiceAddonsMapper;
import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.service.dict.ServiceAddonsService;
import com.jhj.vo.ServiceAddonSearchVo;
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
	public DictServiceAddons selectByPrimaryKey(Long serviceAddonId) {
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
	public List<DictServiceAddons> selectBySearchVo(ServiceAddonSearchVo searchVo) {
		return dictServiceAddonsMapper.selectBySearchVo(searchVo);
	}

	@Override
	public PageInfo selectByListPage(ServiceAddonSearchVo searchVo, int pageNo, int pageSize) {
		
		PageHelper.startPage(pageNo, pageSize);
		List<DictServiceAddons> list =  dictServiceAddonsMapper.selectByListPage(searchVo);
		PageInfo result = new PageInfo(list);
		
		return result;
	}

}
