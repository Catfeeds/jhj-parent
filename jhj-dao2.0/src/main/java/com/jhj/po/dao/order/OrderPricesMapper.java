package com.jhj.po.dao.order;
import java.math.BigDecimal;
import java.util.List;



import com.jhj.po.model.order.OrderPrices;
import com.jhj.vo.chart.ChartMapVo;
import com.jhj.vo.chart.ChartSearchVo;

public interface OrderPricesMapper {
	int deleteByPrimaryKey(Long id);

    int insert(OrderPrices record);

    int insertSelective(OrderPrices record);

    OrderPrices selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderPrices record);

    int updateByPrimaryKey(OrderPrices record);

	OrderPrices selectByOrderId(Long id);
	
	List<OrderPrices> selectByOrderIds(List<Long> orderIdList);
	
	
	OrderPrices selectByOrderNo(String orderNo);
	
	BigDecimal selectByOrderNoList(List<String> orderNoList);	//根据订单号，得到流水
	
	
	
}