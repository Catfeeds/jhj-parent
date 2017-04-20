package com.jhj.service.period;

import java.util.List;

import com.jhj.po.model.period.PeriodServiceType;

public interface PeriodServiceTypeService {
    int deleteByPrimaryKey(Integer id);

    int insert(PeriodServiceType record);

    int insertSelective(PeriodServiceType record);

    PeriodServiceType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PeriodServiceType record);

    int updateByPrimaryKey(PeriodServiceType record);
    
    List<PeriodServiceType> getList(PeriodServiceType periodServiceType);
    
    PeriodServiceType init();
}