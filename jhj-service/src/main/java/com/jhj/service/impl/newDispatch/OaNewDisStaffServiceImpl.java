package com.jhj.service.impl.newDispatch;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserPushBind;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.newDispatch.NewDispatchStaffService;
import com.jhj.service.newDispatch.OaNewDisStaffService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserPushBindService;
import com.meijia.utils.GsonUtil;
import com.meijia.utils.PushUtil;

/**
 *
 * @author :hulj
 * @Date : 2016年3月23日下午2:52:26
 * @Description: 
 *
 */
@Service
public class OaNewDisStaffServiceImpl implements OaNewDisStaffService {
	
	@Autowired
	private NewDispatchStaffService newDisService;
	
	@Autowired
	private OrderDispatchsService disService;
	
	@Autowired
	private UserAddrsService userAddrService;
	
	@Autowired
	private OrdersService orderSevice;
	
	@Autowired
	private OrgStaffsService staffService;
	
	@Autowired	
	private UserPushBindService bindService;
	
	
	@Override
	public void manuDisAmOrder(Long orderId, String fromLat, String fromLng, Long properStaffId) {
		
		/*
		 * 助理类订单，需要 存储 该用户的 新的 选择地址
		 * 	
		 *  插入 userAddr 表
		 * 	
		 *  在  order 表中,更新  addrId、订单状态
		 */
		
		if(properStaffId == 0L){
			
			//此时是  助理类 订单 的  '第一次 ' 手动派工 操作
			
			// 1. 更新地址
			UserAddrs userAddrs = userAddrService.initUserAddrs();
			
			userAddrs.setLatitude(fromLat);
			userAddrs.setLongitude(fromLng);
			
			Orders order = orderSevice.selectbyOrderId(orderId);
			
			Long userId = order.getUserId();
			String mobile = order.getMobile();
			
			userAddrs.setUserId(userId);
			userAddrs.setMobile(mobile);
			
			userAddrService.insertSelective(userAddrs);
			
			Long addrId = userAddrs.getId();
			
			//更新派工表 
			OrderDispatchs dispatchs = disService.selectByOrderId(orderId);
			
			dispatchs.setStaffId(properStaffId);
			
			OrgStaffs staffs = staffService.selectByPrimaryKey(properStaffId);
			
			dispatchs.setStaffMobile(staffs.getMobile());
			dispatchs.setStaffName(staffs.getName());
			
			int latestDistance = newDisService.getLatestDistance(fromLng, fromLng, properStaffId);
			
			dispatchs.setUserAddrDistance(latestDistance);
			
			dispatchs.setDispatchStatus((short)1);
			
			disService.updateByPrimaryKeySelective(dispatchs);
			
			/*
			 * 更新 订单 表. 订单状态
			 * 
			 */
			Orders orders = orderSevice.selectbyOrderId(orderId);
			
			if(orders.getOrderStatus() == Constants.ORDER_AM_STATUS_1){
				
				orders.setOrderStatus(Constants.ORDER_AM_STATUS_2);
				
				orderSevice.updateByPrimaryKeySelective(orders);
				
				
			}
			
			
		}else{
			
			/*
			 * 调整 派工
			 */
			OrderDispatchs orderDispatchs = disService.selectByOrderId(orderId);
			
			
			
			
			
			
			
			
		}
		
		/*
		 * 推送 通知消息
		 */
		UserPushBind userPushBind = bindService.selectByUserId(properStaffId);
		
		//助理的 cid
		String clientId = userPushBind.getClientId();
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cid", clientId);
		
		HashMap<String, String> tranParams = new HashMap<String, String>();
		
		tranParams.put("is_show", "true");		
		tranParams.put("action", "msg");		
		tranParams.put("remind_title", "助理预约单");
		tranParams.put("remind_content", "您好，你的助理预约单，已经完成派工");
		
		String jsonParams = GsonUtil.GsonString(tranParams);
		
		params.put("transmissionContent", jsonParams);
		try {
			boolean flag = PushUtil.AndroidPushToSingle(params);
		} catch (Exception e) {
			System.out.println("助理预约单支付完成时,推送给助理的消息,出现异常:"+e.getMessage());
			e.printStackTrace();
		}
		
		
	}

}
