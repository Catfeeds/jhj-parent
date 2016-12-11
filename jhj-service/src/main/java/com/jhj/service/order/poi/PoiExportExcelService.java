package com.jhj.service.order.poi;

import java.util.List;
import java.util.Map;

import com.jhj.vo.order.OaOrderListVo;

/**
 *
 * @author :hulj
 * @Date : 2016年5月27日下午5:17:25
 * @Description: 
 *
 */
public interface PoiExportExcelService {
	
	List<Map<String,Object>>  createExcelRecord(List<OaOrderListVo> voList);
}
