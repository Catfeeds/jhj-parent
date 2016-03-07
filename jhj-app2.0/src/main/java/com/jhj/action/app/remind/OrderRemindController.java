package com.jhj.action.app.remind;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserRefAm;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.remind.OrderRemindService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.remind.OrderRemindVo;
import com.meijia.utils.OrderNoUtil;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年9月22日上午11:26:09
 * @Description: 
 *		
 *	 主要 针对 类似于 "提醒"类 的 不用支付，免费 的 业务
 *
 *		流程： 1.用户预约，给定 时间、服务内容	
 *			 2.根据 给定时间，发送短信
 */
@Controller
@RequestMapping(value = "/app/remind")
public class OrderRemindController extends BaseController {
	
	@Autowired
	private OrdersService orderService;
	@Autowired
	private UsersService userService;
	@Autowired
	private UserRefAmService userRefAmService;
	@Autowired
	private OrderPricesService orderPriceService;
	@Autowired
	private OrderLogService orderLogService;
	@Autowired
	private OrderRemindService orderRemindService;
	@Autowired
	private OrgStaffsService orgStaffService;
	@Autowired
	private UserAddrsService userAddrService;
	/*
	 * 发起 "提醒" 预约
	 */
	@RequestMapping(value = "post_remind",method = RequestMethod.POST)
	public AppResultData<Object> submitRemindOrder(
			@RequestParam("userId") Long userId,
			@RequestParam("serviceType") Long serviceType,
			@RequestParam("serviceDate") Long serviceDate,
			@RequestParam("title") String title,
			@RequestParam("remarks") String remarks,
			@RequestParam(value = "orderFrom", required = false, defaultValue = "1") Short orderFrom){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		
		//当前时间 的时间戳
		Long nowTimeStamp =  new Date().getTime()/1000;
		
		if(nowTimeStamp >= serviceDate){
			
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.BEFORE_ONE_HOUR);
			return result;
		}
		
		Users u = userService.getUserById(userId);

		// 判断是否为注册用户，非注册用户返回 999
		if (u == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}
		
		UserRefAm userRefAm = userRefAmService.selectByAmId(userId);
		// 调用公共订单号类，生成唯一订单号
		String orderNo = String.valueOf(OrderNoUtil.genOrderNo());
		
		
		/*   
		 * 	1.  提醒类  免费的 订单，同样 在  order_price 表里 生成记录，各类价格 均设置为 0
		 *  2. 不提供 评价 功能，但提供 列表、添加、查看详情 、取消功能 
		 */
		
		Orders order = orderService.initOrders();
		
		/*
		 *  orders表
		 */
		//保存订单信息 
		order.setOrderNo(orderNo);
		
		order.setMobile(u.getMobile());
		order.setUserId(userId);
		order.setAmId(userRefAm.getStaffId());
		// 服务类型，新增 为  7
		order.setServiceType(serviceType);
		order.setServiceDate(serviceDate);
		
		// 新增提醒 的  订单 状态，定为 10
		order.setOrderStatus(Constants.ORDER_STATUS_10);
		//将 提醒类 业务 的订单类型  定为  5 
		order.setOrderType(Constants.ORDER_TYPE_5);
		
		// title 字段 作为 service_content 存储,	短信提醒需要！
		order.setServiceContent(title);
		// 提醒内容，在  备注 字段 存储
		order.setRemarks(remarks);
		
		
		/*
		 * 预防 bug， 设置 门店 id 、 地址 id
		 */
		
		//通过 助理 确定 门店
		OrgStaffs orgStaffs = orgStaffService.selectByPrimaryKey(userRefAm.getStaffId());
		
		order.setOrgId(orgStaffs.getOrgId());
		
		//用户地址，从 地址集合中，随机取一条即可，无需展示;	 新用户则 为 默认值 0 ，无地址
		
		List<UserAddrs> addrList = userAddrService.selectByUserId(userId);
		
		if(addrList.size() > 0){
			order.setAddrId(addrList.get(0).getId());
		}
		
		orderService.insert(order);
		
		
		
		
		/*
		 * order_price 表, 这里 在价格表中 生成 价格为 0 的记录
		 */
		OrderPrices orderPrice = orderPriceService.initOrderPrices();
		
		orderPrice.setUserId(userId);
		orderPrice.setOrderId(order.getId());
		orderPrice.setMobile(u.getMobile());
		orderPrice.setOrderNo(orderNo);
		
		orderPriceService.insert(orderPrice);
		
		/*
		 * order_logs 表
		 */
		
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLogService.insert(orderLog);
		
		result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, order);
	
		return result;
	}
	
	/*
	 *  提醒类 订单  列表 页
	 *  	我的提醒
	 */
	@RequestMapping(value = "/remind_list_now",method = RequestMethod.GET)
	public AppResultData<Object>  remindListNow(
			@RequestParam("user_id") Long userId, 
			@RequestParam(value = "page", required = false, defaultValue = "1") int page){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		List<Orders> lists = orderRemindService.selectNowRemind(userId,page, Constants.PAGE_MAX_NUMBER);
		
		List<OrderRemindVo> list = orderRemindService.transListToVO(lists);
		
		result.setData(list);
		
		return result;
	}
	
	/*
	 * 	历史提醒
	 */
	@RequestMapping(value = "/remind_list_old",method = RequestMethod.GET)
	public AppResultData<Object> remindListOld(
			@RequestParam("user_id") Long userId, 
			@RequestParam(value = "page", required = false, defaultValue = "1") int page){
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		List<Orders> lists = orderRemindService.selectOldRemind(userId, page, Constants.PAGE_MAX_NUMBER);
		
		List<OrderRemindVo> list = orderRemindService.transListToVO(lists);
		result.setData(list);
		
		return result;
	}
	
	
	/*
	 *  查看  提醒 订单 详情
	 */
	
	@RequestMapping(value = "/remind_detail",method = RequestMethod.GET)
	public AppResultData<Object>  getRemindDetail(
			@RequestParam("order_no") String orderNo){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		OrderRemindVo remindVo = orderRemindService.getRemindDetail(orderNo);
		
		result.setData(remindVo);
		
		return result;
	}
	
	/*
	 *  取消 提醒 订单，暂时 定为  服务开始前 20 分钟 以内 不能 取消订单
	 */
	@RequestMapping(value = "/cancle_remind", method = RequestMethod.GET)
	public AppResultData<Object> cancleRemind(
			@RequestParam("order_no") String orderNo){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		Orders orders = orderService.selectByOrderNo(orderNo);
		
		if (orders == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ORDER_NO_NOT_EXIST_MG);
			return result;
		}
		
		// 防止重复 取消
		if(orders.getOrderStatus() == 0){
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.HAVE_CANCLE);
			return result;
		}
		
		
		if(orders.getOrderStatus() == 10){

			OrderPrices orderPrices = orderPriceService.selectByOrderNo(orderNo);
			if (orderPrices == null) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.ORDER_NO_NOT_EXIST_MG);
				return result;
			}
			
			//获得当前时间时间戳
			Long nowDate = System.currentTimeMillis()/ 1000;
			//服务时间时间戳
			Long serviceDate = orders.getServiceDate();
            //时间差
		    Long minute = (serviceDate - nowDate) / 60;
			
		    // 服务开始前 20 分钟 之内。。不能取消提醒
		    if(minute >= 20){
		    	orders.setOrderStatus(Constants.ORDER_STATUS_12);
		    	
		    	orderService.updateByPrimaryKeySelective(orders);
				
				// 记录订单日志.
				OrderLog orderLog = orderLogService.initOrderLog(orders);
				orderLogService.insert(orderLog);
		    	
		    }else if(minute >= 10){
		    	//预留 2分钟 电话时间
		    	
		    	result.setStatus(Constants.ERROR_999);
		    	result.setMsg("服务即将开始,现在无法取消哦");
		    	return result;
		    }else{
		    	//开始前 10分钟 以内，已经服务过，不能取消 提醒
		    	result.setStatus(Constants.ERROR_999);
		    	result.setMsg("服务已经生效,现在不能取消哦");
		    	
		    	return result;
		    }
			
		}else{
			result.setStatus(Constants.ERROR_999);
			result.setMsg("提醒已取消,请勿重复操作");
			return result;
		}
		
		result.setStatus(Constants.SUCCESS_0);
		result.setMsg("提醒已取消,祝您生活愉快");
		
		return result;
		
	}
	
	/*
	 * 用户版--首页 小红点，获得 当前已预约  提醒 的数量
	 */
	
	@RequestMapping(value = "/get_remind_count_to_do",method = RequestMethod.GET)
	public AppResultData<Object> nowRemindCount(@RequestParam("user_id") Long userId){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		Long remindCount = orderRemindService.getRemindCountToDo(userId);
		
		result.setData(remindCount);
		return result;
	}
	
	
}
