package com.jhj.po.dao.order;

import java.util.List;

import com.jhj.po.model.order.OrderCards;
import com.jhj.vo.chart.ChartMapVo;
import com.jhj.vo.chart.ChartSearchVo;

public interface OrderCardsMapper {
    int deleteByPrimaryKey(Long id);

    Long insert(OrderCards record);

    int insertSelective(OrderCards record);

    OrderCards selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderCards record);

    int updateByPrimaryKey(OrderCards record);

	OrderCards selectByOrderCardsNo(String cardOrderNo);
	
	List<OrderCards> selectNoPayByUserId(Long userId);

	
	/**
	 * 充值卡销售图表
	 * @param chartSearchVo
	 * @return
	 */
	List<ChartMapVo> saleCardByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> saleCardByMonth(ChartSearchVo chartSearchVo);

	List<ChartMapVo> saleCardByQuarter(ChartSearchVo chartSearchVo);
}