package com.jhj.service.period;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jhj.po.model.period.PeriodOrderAddons;
import com.jhj.vo.period.PeriodOrderAddonsVo;

public interface PeriodOrderAddonsService {
    int deleteByPrimaryKey(Integer id);

    int insert(PeriodOrderAddons record);

    int insertSelective(PeriodOrderAddons record);

    PeriodOrderAddons selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PeriodOrderAddons record);

    int updateByPrimaryKey(PeriodOrderAddons record);
    
    int insertBatch(@Param("periodOrdeAddonsList") List<PeriodOrderAddons> periodOrdeAddonsList);
    
    PeriodOrderAddons init();
    
    List<PeriodOrderAddons> selectByPeriodOrderId(Integer periodOrderId);
    
    PeriodOrderAddonsVo transVo(PeriodOrderAddons periodOrderAddons);
}