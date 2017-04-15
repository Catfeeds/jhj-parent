package com.jhj.service.period;

import java.util.List;

import com.jhj.po.model.period.PeriodOrderAddons;

public interface PeriodOrderAddonsService {
    int deleteByPrimaryKey(Integer id);

    int insert(PeriodOrderAddons record);

    int insertSelective(PeriodOrderAddons record);

    PeriodOrderAddons selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PeriodOrderAddons record);

    int updateByPrimaryKey(PeriodOrderAddons record);
    
    int insertBatch(List<PeriodOrderAddons> periodOrdeAddonsList);
    
    PeriodOrderAddons init();
}