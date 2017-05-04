package com.jhj.po.dao.period;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jhj.po.model.period.PeriodOrder;

public interface PeriodOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PeriodOrder record);

    int insertSelective(PeriodOrder record);

    PeriodOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PeriodOrder record);

    int updateByPrimaryKey(PeriodOrder record);
    
    PeriodOrder selectByOrderNo(String orderNo);
    
    int insertBatch(@Param("periodOrderList") List<PeriodOrder> periodOrderList);
    
    List<PeriodOrder> periodOrderListPage(PeriodOrder periodOrder);
    
    List<PeriodOrder> selectByPeriodOrder(PeriodOrder periodOrder);
}