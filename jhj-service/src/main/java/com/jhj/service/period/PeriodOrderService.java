package com.jhj.service.period;

import java.util.List;

import com.jhj.po.model.period.PeriodOrder;
import com.jhj.vo.period.PeriodOrderDetailVo;
import com.jhj.vo.period.PeriodOrderSearchVo;
import com.jhj.vo.period.PeriodOrderVo;

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
    
	List<PeriodOrder> selectByListPage(PeriodOrderSearchVo searchVo, int pageNum, int pageSize);

	List<PeriodOrder> selectBySearchVo(PeriodOrderSearchVo searchVo);

	PeriodOrderVo getVos(PeriodOrder item);

	PeriodOrderDetailVo getDetailVo(PeriodOrder item);

	String getOrderStatusName(Integer orderStatus);
	
	int selectPeriodOrderCount(Integer userId,Integer orderStatus);
}