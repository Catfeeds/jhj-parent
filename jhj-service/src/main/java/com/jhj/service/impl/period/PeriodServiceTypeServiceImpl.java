package com.jhj.service.impl.period;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.po.dao.period.PeriodServiceTypeMapper;
import com.jhj.po.model.period.PeriodServiceType;
import com.jhj.service.period.PeriodServiceTypeService;
import com.meijia.utils.TimeStampUtil;

@Service
public class PeriodServiceTypeServiceImpl implements PeriodServiceTypeService{
	
	@Autowired
	private PeriodServiceTypeMapper periodServiceTypeMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return periodServiceTypeMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(PeriodServiceType record) {
		return periodServiceTypeMapper.insert(record);
	}

	@Override
	public int insertSelective(PeriodServiceType record) {
		return periodServiceTypeMapper.insertSelective(record);
	}

	@Override
	public PeriodServiceType selectByPrimaryKey(Integer id) {
		return periodServiceTypeMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(PeriodServiceType record) {
		return periodServiceTypeMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(PeriodServiceType record) {
		return periodServiceTypeMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<PeriodServiceType> getList(PeriodServiceType periodServiceType) {
		
		List<PeriodServiceType> list = periodServiceTypeMapper.getListPage(periodServiceType);
		return list;
	}

	@Override
	public PeriodServiceType init() {
		PeriodServiceType pt = new PeriodServiceType();
		pt.setName("");
		pt.setNum(0);
		pt.setPrice(new BigDecimal(0));
		pt.setPunit("");
		pt.setRemarks("");
		pt.setServiceAddonId(0);
		pt.setServiceTypeId(0);
		pt.setTotal("");
		pt.setVipPrice(new BigDecimal(0));
		pt.setEnbale(0);
		pt.setAddTime(TimeStampUtil.getNowSecond());
		return pt;
	}
    
}