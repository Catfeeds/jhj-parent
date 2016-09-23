package com.jhj.service.impl.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.bs.OrgStaffsMapper;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderAmHourService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderHourDetailService;
import com.jhj.vo.order.OrderAmHourViewVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderHourViewVo;
import com.meijia.utils.BeanUtilsExp;

/**
 *
 * @author :hulj
 * @Date : 2015年8月7日下午2:11:46
 * @Description: 
 * 	
 * 		助理端--保洁类 订单  service
 *
 */
@Service
public class OrderAmHourServiceImpl implements OrderAmHourService {
	
	
	@Autowired
	private OrderHourDetailService orderHourDetailService;
	@Autowired
	private OrderDispatchsService orderDispatchsService;
	@Autowired
	private OrgStaffsService orgStaffService;
	@Autowired
	private OrgStaffsMapper orgStaMapper;
	
	@Override
	public OrderAmHourViewVo getOrderAmHourView(String orderNo,Long amId) {
		
		//助理端--订单详情VO
		OrderAmHourViewVo orderAmHourViewVo = initOAHVO();
		
		//用户端--订单详情 VO
		OrderHourViewVo orderHourViewVo = orderHourDetailService.getOrderHourDetail(orderNo);
		
		// orderNo 能 唯一确定 一条 订单，--> 两端  展示 内容相同,而且 是继承 关系
		BeanUtilsExp.copyPropertiesIgnoreNull(orderHourViewVo, orderAmHourViewVo);
		
		//阿姨, 如果有派工表记录，则显示阿姨名字，否则 ，为默认值 ''，前端展示，暂未分配
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderNo(orderNo);
		searchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(searchVo);

		OrderDispatchs orderDispatch = null;
		if (!orderDispatchs.isEmpty()) {
			orderDispatch = orderDispatchs.get(0);
		}

		
		if(orderDispatch != null){
			String staffName = orderDispatch.getStaffName();
			orderAmHourViewVo.setStaffName(staffName);
		}
		
		
		return orderAmHourViewVo;
	}

	@Override
	public OrderAmHourViewVo initOAHVO() {
		OrderHourViewVo hourViewVo = orderHourDetailService.initOHVO();
		
		OrderAmHourViewVo amHourViewVo  = new OrderAmHourViewVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(hourViewVo, amHourViewVo);
		
		amHourViewVo.setStaffName("");
		amHourViewVo.setStaffMobile("");
		amHourViewVo.setStaffList(new ArrayList<OrgStaffs>());
		
		return amHourViewVo;
	}
}
