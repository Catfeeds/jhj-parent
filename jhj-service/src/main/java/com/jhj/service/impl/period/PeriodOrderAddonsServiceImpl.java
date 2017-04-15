package com.jhj.service.impl.period;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.period.PeriodOrderAddonsMapper;
import com.jhj.po.model.period.PeriodOrderAddons;
import com.jhj.service.period.PeriodOrderAddonsService;
import com.meijia.utils.DateUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class PeriodOrderAddonsServiceImpl implements PeriodOrderAddonsService{
	
	@Autowired
	private PeriodOrderAddonsMapper periodOrderAddonsMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return periodOrderAddonsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(PeriodOrderAddons record) {
		return periodOrderAddonsMapper.insert(record);
	}

	@Override
	public int insertSelective(PeriodOrderAddons record) {
		return periodOrderAddonsMapper.insertSelective(record);
	}

	@Override
	public PeriodOrderAddons selectByPrimaryKey(Integer id) {
		return periodOrderAddonsMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(PeriodOrderAddons record) {
		return periodOrderAddonsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(PeriodOrderAddons record) {
		return periodOrderAddonsMapper.updateByPrimaryKey(record);
	}

	@Override
	public int insertBatch(List<PeriodOrderAddons> periodOrdeAddonsList) {
		return periodOrderAddonsMapper.insertBatch(periodOrdeAddonsList);
	}

	@Override
	public PeriodOrderAddons init() {
		PeriodOrderAddons poa = new PeriodOrderAddons();
		poa.setNum(0);
		poa.setPeriodOrderId(0);
		poa.setPeriodOrderNo("");
		poa.setPrice(new BigDecimal(0));
		poa.setServiceAddonId(0);
		poa.setServiceTypeId(0);
		poa.setUserId(0);
		poa.setAddTime(DateUtil.getNowOfDate());
		poa.setVipPrice(new BigDecimal(0));
		return poa;
	}
   
}