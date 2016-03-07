package com.jhj.service.order;

import java.util.List;
import java.util.Map;

public interface OrderStatService {
	
	List<Map<String, Object>> selectOrdersCountByYearAndMonth(Long orgStaffId,String start,String end);


}
