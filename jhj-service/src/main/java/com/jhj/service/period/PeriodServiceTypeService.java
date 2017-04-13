package com.jhj.service.period;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.period.PeriodServiceType;

public interface PeriodServiceTypeService {
    int deleteByPrimaryKey(Integer id);

    int insert(PeriodServiceType record);

    int insertSelective(PeriodServiceType record);

    PeriodServiceType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PeriodServiceType record);

    int updateByPrimaryKey(PeriodServiceType record);
    
    PageInfo<PeriodServiceType> getListPage(PeriodServiceType record, int pageNo, int pageSize);
    
    PeriodServiceType init();
}