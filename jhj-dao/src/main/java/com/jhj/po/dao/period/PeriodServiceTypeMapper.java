package com.jhj.po.dao.period;

import java.util.List;

import com.jhj.po.model.period.PeriodServiceType;

public interface PeriodServiceTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PeriodServiceType record);

    int insertSelective(PeriodServiceType record);

    PeriodServiceType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PeriodServiceType record);

    int updateByPrimaryKey(PeriodServiceType record);
    
    List<PeriodServiceType> getListPage(PeriodServiceType record);
}