package com.jhj.service.impl.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.dao.user.UsersMapper;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.Users;
import com.jhj.service.order.OaRemindOrderService;
import com.jhj.vo.OaRemindOrderSearchVo;
import com.jhj.vo.order.OaRemindOrderVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.StringUtil;

@Service
public class OaRemindOrderServiceImpl implements OaRemindOrderService {
	
	@Autowired
	private OrdersMapper orderMapper;
	@Autowired
	private UsersMapper userMapper;
	
	@Override
	public List<Orders> getRemindOrderList(int pageNo, int pageSize, OaRemindOrderSearchVo searchVo) {
		
		PageHelper.startPage(pageNo, pageSize);
		
		List<Orders> orderList = orderMapper.selectOaRemindOrderByListPage(searchVo);
		
		return orderList;
	}

	@Override
	public OaRemindOrderVo transVo(Orders order) {
		
		OaRemindOrderVo oaRemindOrderVo = new OaRemindOrderVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(order, oaRemindOrderVo);
		
		Long userId = order.getUserId();
		Users users = userMapper.selectByPrimaryKey(userId);
		
		if(StringUtil.isEmpty(users.getName())){
			oaRemindOrderVo.setUserName("");
		}
		oaRemindOrderVo.setUserName(users.getName());
		
		return oaRemindOrderVo;
	}

}
