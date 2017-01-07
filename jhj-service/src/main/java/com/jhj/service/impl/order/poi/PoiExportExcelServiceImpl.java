package com.jhj.service.impl.order.poi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jhj.service.order.poi.PoiExportExcelService;
import com.jhj.vo.order.OaOrderListVo;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2016年5月30日上午10:09:28
 * @Description:
 *
 */
@Service
public class PoiExportExcelServiceImpl implements PoiExportExcelService {

	@Override
	public List<Map<String, Object>> createExcelRecord(List<OaOrderListVo> voList) {

		List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sheetName", "sheet1");
		listmap.add(map);

		OaOrderListVo vo = null;

		for (int j = 0; j < voList.size(); j++) {

			vo = voList.get(j);

			Map<String, Object> mapValue = new HashMap<String, Object>();

			mapValue.put("orgName", vo.getOrgName());
			mapValue.put("cloudOrgName", vo.getCloudOrgName());
			mapValue.put("staffName", vo.getStaffName());
			mapValue.put("addTime", TimeStampUtil.timeStampToDateStr(vo.getAddTime() * 1000, "yyyy-MM-dd HH:mm"));

			mapValue.put("orderTypeName", vo.getOrderTypeName());
			mapValue.put("serviceDate", TimeStampUtil.timeStampToDateStr(vo.getServiceDate() * 1000, "yyyy-MM-dd HH:mm"));
			mapValue.put("mobile", vo.getMobile());
			mapValue.put("orderAddress", vo.getOrderAddress());
			
			String orderStatusName = OneCareUtil.getJhjOrderStausNameFromOrderType(vo.getOrderType(),vo.getOrderStatus());
			
			mapValue.put("orderStatusName", orderStatusName);
			mapValue.put("payTypeName", vo.getPayTypeName());
			mapValue.put("applyStatus", vo.getApplyStatus());
			mapValue.put("orderOpFromName", vo.getOrderOpFromName());
//			mapValue.put("orderMoney", vo.getOrderMoney());
			mapValue.put("orderPay", vo.getOrderPay());
			BigDecimal spreadMoeny = vo.getSpreadMoeny();
			BigDecimal comp=new BigDecimal(0);
			if(spreadMoeny.compareTo(comp)>0){
				StringBuffer sb=new StringBuffer();
				short payTypeExt = vo.getPayTypeExt();
				String payTypeName = OneCareUtil.getPayTypeName(payTypeExt);
				
				if(vo.getOrderExtType()==0){
					sb.append("补差价：").append(payTypeName);
					mapValue.put("spreadMoeny", vo.getSpreadMoeny());
				}
				if(vo.getOrderExtType()==1){
					sb.append("补时：").append(payTypeName);
					mapValue.put("spreadMoeny", vo.getSpreadMoeny());
				}
				mapValue.put("orderExtType", sb.toString());
			}

			listmap.add(mapValue);
		}
		return listmap;
	}

}
