package com.jhj.service.impl.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.bs.OrgStaffsMapper;
import com.jhj.po.dao.order.OrderDispatchsMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.dao.user.UserAddrsMapper;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.service.order.OrderStatService;
import com.meijia.utils.TimeStampUtil;
@Service
public class OrderStatServiceImpl implements OrderStatService {

	@Autowired
	private OrderDispatchsMapper orderDispatchsMapper;
	
	@Autowired
	private OrgStaffsMapper orgStaffsMapper;

	@Autowired
	private OrdersMapper ordersMapper;
	
	@Autowired UserAddrsMapper userAddrsMapper;
	
	@Override
	public List<Map<String, Object>> selectOrdersCountByYearAndMonth(Long orgStaffId, String start, String end) {
		Long startLong = TimeStampUtil.getMillisOfDay(start)/1000;
		Long endLong = TimeStampUtil.getMillisOfDay(end)/1000;
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();

		Map<String,Object> conditions = new HashMap<String, Object>();
		conditions.put("startTime", startLong);
		conditions.put("endTime",endLong);
		conditions.put("orgStaffId",orgStaffId);
		List<Map<String, Object>> list1 = orderDispatchsMapper.selectOrdersCountByYearAndMonth(conditions);
		int total = 0;
		boolean flag = true;
		for (Iterator iterator = list1.iterator(); iterator.hasNext();) {
			total = list1.size();
			Map<String, Object> map = (Map<String, Object>) iterator.next();
			Map<String,Object> map1 = new HashMap<String, Object>();
			String serviceDate = (String)map.get("startTime");
			String orderNo = (String)map.get("order_no");
			Long staffId = (Long)map.get("staff_id");
			OrgStaffs  orgStaffs = orgStaffsMapper.selectByPrimaryKey(staffId);
			Orders orders= ordersMapper.selectByOrderNo(orderNo);
			UserAddrs userAddrs = userAddrsMapper.selectByPrimaryKey(orders.getAddrId());
			String detailAddrs = userAddrs.getName()+userAddrs.getAddr();
			map1.put("id",map.get("id"));
			map1.put("start",map.get("serviceTime"));
			map1.put("end",map.get("service_end"));
			map1.put("title",orgStaffs.getName()+"有1个派工,"+"  服务地址："+detailAddrs);
			map1.put("color","blue");
			/*if(flag){
				map1.put("color","blue");
				flag = false;
			}else {
				map1.put("color","orange");
				flag = true;
			}*/
			map1.put("url","/jhj-oa/order/cal-list?name='agendaDay'&serviceDate="+serviceDate+"&staffId="+orgStaffId);
			listMap.add(map1);
		}
		return listMap;
	}

}
