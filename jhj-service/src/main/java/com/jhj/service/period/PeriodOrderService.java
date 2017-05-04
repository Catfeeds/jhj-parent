package com.jhj.service.period;

import java.util.List;

import com.jhj.po.model.period.PeriodOrder;

public interface PeriodOrderService {
    int deleteByPrimaryKey(Integer id);

    int insert(PeriodOrder record);

    int insertSelective(PeriodOrder record);

    PeriodOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PeriodOrder record);

    int updateByPrimaryKey(PeriodOrder record);
    
    PeriodOrder init();
    
    PeriodOrder selectByOrderNo(String orderNo);
    
    int insertBatch(List<PeriodOrder> periodOrderList);
    
    List<PeriodOrder> periodOrderListPage(PeriodOrder periodOrder,int pageNum, int pageSize);
    
    List<PeriodOrder> selectByPeriodOrder(PeriodOrder periodOrder);
}