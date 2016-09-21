package com.jhj.service.impl.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.order.OrderDispatchsMapper;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.order.OaOrderDisService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.OaOrderDisSearchVo;
import com.jhj.vo.order.OaOrderDisVo;
import com.meijia.utils.BeanUtilsExp;

/**
 *
 * @author :hulj
 * @Date : 2015年8月14日下午8:51:03
 * @Description:
 *
 */
@Service
public class OaOrderDisServiceImpl implements OaOrderDisService {
	
	
	@Autowired
	private OrderDispatchsMapper orderDisMapper;
	@Autowired
	private OrderDispatchsService disService;
	@Autowired
	private UsersService userService;
	@Autowired
	private UserAddrsService userAddrService;
	@Autowired
	private OrdersService orderService;
	@Autowired
	private OrgStaffsService orgStaService;
	@Autowired
	private OrgsService orgService;
	@Override
	public List<OrderDispatchs> selectOrderDisByListPage(OaOrderDisSearchVo oaOrderDisSearchVo, int pageNo, int pageSize) {
		
		PageHelper.startPage(pageNo, pageSize);
		
		List<OrderDispatchs> disList = orderDisMapper.selectAbledOrderDis(oaOrderDisSearchVo);
		
		return disList;
	}
	
	@Override
	public OaOrderDisVo compleVo(OrderDispatchs dispatchs) {
		
		OaOrderDisVo orderDisVo = initVo();

		BeanUtilsExp.copyPropertiesIgnoreNull(dispatchs, orderDisVo);
		//用户名
		Long userId = dispatchs.getUserId();
		Users user = userService.selectByUsersId(userId);
		orderDisVo.setUserName(user.getName());
		
		//服务地址
		/*
		 * bug: 对于 已支付，即 在 派工表有记录的 订单， 如果发生 取消订单操作，
		 */
		
		Orders orders = orderService.selectByOrderNo(dispatchs.getOrderNo());
		Long addrId = orders.getAddrId();
		UserAddrs userAddrs = userAddrService.selectByPrimaryKey(addrId);
		if(userAddrs != null){
			orderDisVo.setAddrName(userAddrs.getName()+" "+userAddrs.getAddr());
		}
		
		Long staffId = dispatchs.getStaffId();
		
		OrgStaffs orgStaffs = orgStaService.selectByPrimaryKey(staffId);
		
		//云店名称
		Long orgId = orgStaffs.getOrgId();
		Orgs orgs = orgService.selectByPrimaryKey(orgId);
		orderDisVo.setOrgName(orgs.getOrgName());
		
		Short orderType = orders.getOrderType();
		
		orderDisVo.setOrderType(orderType);
		orderDisVo.setOrderStatus(orders.getOrderStatus());
		
		return orderDisVo;
	}
	
	@Override
	public OaOrderDisVo initVo(){
		OrderDispatchs orderDispatchs = disService.initOrderDisp();
		
		OaOrderDisVo orderDisVo = new OaOrderDisVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(orderDispatchs, orderDisVo);
		
		orderDisVo.setUserName("");
		orderDisVo.setAddrName("");
		orderDisVo.setOrgName("");
		orderDisVo.setAmName("");
		orderDisVo.setAmMobile("");
		
		return orderDisVo;
	}

	@Override
	public List<OaOrderDisVo> getDisEveryDay(String day) {
		
		List<OrderDispatchs> list = orderDisMapper.selectDisEveryDay(day);
		
		List<OaOrderDisVo> voList = new ArrayList<OaOrderDisVo>();
		for (OrderDispatchs orderDispatchs : list) {
			OaOrderDisVo oaOrderDisVo = compleVo(orderDispatchs);
			voList.add(oaOrderDisVo);
		}
		
		return voList;
	}
}
