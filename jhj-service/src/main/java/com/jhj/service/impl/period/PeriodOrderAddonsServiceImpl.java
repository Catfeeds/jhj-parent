package com.jhj.service.impl.period;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.dict.DictServiceAddonsMapper;
import com.jhj.po.dao.period.PeriodOrderAddonsMapper;
import com.jhj.po.dao.university.PartnerServiceTypeMapper;
import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.period.PeriodOrder;
import com.jhj.po.model.period.PeriodOrderAddons;
import com.jhj.po.model.period.PeriodServiceType;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.period.PeriodOrderAddonsService;
import com.jhj.service.period.PeriodOrderService;
import com.jhj.service.period.PeriodServiceTypeService;
import com.jhj.vo.period.PeriodOrderAddonsVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;

@Service
public class PeriodOrderAddonsServiceImpl implements PeriodOrderAddonsService{
	
	@Autowired
	private PeriodOrderService periodOrderService;
	
	@Autowired
	private PeriodOrderAddonsMapper periodOrderAddonsMapper;
	
	@Autowired
	private DictServiceAddonsMapper dictServiceAddonsMapper;
	
	@Autowired
	private PartnerServiceTypeMapper partnerServiceTypeMapper;
	
	@Autowired
	private PeriodServiceTypeService periodServiceTypeService;

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
		poa.setPeriodServiceAddonId(0);
		poa.setServiceTypeId(0);
		poa.setUserId(0);
		poa.setAddTime(DateUtil.getNowOfDate());
		poa.setVipPrice(new BigDecimal(0));
		poa.setPackageType(0);
		return poa;
	}

	@Override
	public List<PeriodOrderAddons> selectByPeriodOrderId(Integer periodOrderId) {
		
		return periodOrderAddonsMapper.selectByPeriodOrderId(periodOrderId);
	}

	@Override
	public PeriodOrderAddonsVo transVo(PeriodOrderAddons periodOrderAddons) {
		
		PeriodOrderAddonsVo vo = new PeriodOrderAddonsVo();
		
		if(periodOrderAddons==null) return vo;
		
		BeanUtilsExp.copyPropertiesIgnoreNull(periodOrderAddons, vo);
		
		Integer periodServiceAddonId = vo.getPeriodServiceAddonId();
		PeriodServiceType periodServiceType = periodServiceTypeService.selectByPrimaryKey(periodServiceAddonId);
		
		vo.setPunit("");
		if (periodServiceType != null) vo.setPunit(periodServiceType.getPunit());
		
		
		if(periodOrderAddons.getServiceAddonId()>0){
			DictServiceAddons serviceAddons = dictServiceAddonsMapper.selectByPrimaryKey(periodOrderAddons.getServiceAddonId().longValue());
			vo.setServiceAddonsName(serviceAddons.getName());
		}
		
		if(periodOrderAddons.getServiceAddonId()==0){
			PartnerServiceType serviceType = partnerServiceTypeMapper.selectByPrimaryKey(periodOrderAddons.getServiceTypeId().longValue());
			vo.setServiceAddonsName(serviceType.getName());
		}
		
		return vo;
	}
   
}