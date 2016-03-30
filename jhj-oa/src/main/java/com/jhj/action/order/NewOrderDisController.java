package com.jhj.action.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jhj.action.BaseController;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserPushBind;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.newDispatch.NewDispatchStaffService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPayService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserPushBindService;
import com.jhj.service.users.UserTrailRealService;
import com.jhj.vo.order.OrgStaffsNewVo;
import com.jhj.vo.order.newDispatch.NewAutoDisStaffVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.GsonUtil;
import com.meijia.utils.PushUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2016年3月21日下午2:48:09
 * @Description: 
 *		
 *		jhj2.1 -- 运营平台，手动派工 
 */
@Controller
@RequestMapping(value = "/new_dispatch")
public class NewOrderDisController extends BaseController {
	
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
	
	@Autowired
	private OrderPayService orderPayService;
	
	/**
	 * 
	 *  @Title: loadProperStaffListForBase
	  * @Description: 
	  * 	钟点工订单调整时, 根据  改变后的  服务时间, 加载 可用派工人员列表
	  * 		
	  * @param @param model
	  * @param @param orderId
	  * @param @param newServiceDate
	  * @param @return    设定文件
	  * @return String    返回类型
	  * @throws
	 */
	@RequestMapping(value = "load_staff_by_change_service_date.json",method = RequestMethod.GET)
	public List<OrgStaffsNewVo> loadProperStaffListForBase(Model model,
			@RequestParam("orderId")Long orderId,
			@RequestParam("newServiceDate")Long newServiceDate){
		
		Orders orders = orderSevice.selectbyOrderId(orderId);
		
		Short orderStatus = orders.getOrderStatus();
		
		List<OrgStaffsNewVo> list = new ArrayList<OrgStaffsNewVo>();
		
		//对于  钟点工订单, 只有订单状态为    "已支付" 或  "已派工",可以进行 调整派工
		if(orderStatus  == Constants.ORDER_HOUR_STATUS_2
					|| orderStatus == Constants.ORDER_HOUR_STATUS_3){
			
			 list = newDisService.getAbleStaffList(orderId, newServiceDate);
		}
		
		return list;
	}
	
	/*
	 * 钟点工-- 提交修改结果
	 */
	@RequestMapping(value = "submit_manu_base_order.json",method = RequestMethod.POST)
	public AppResultData<Object> submitManuBaseOrder(Model model,
			@RequestParam("orderId")Long orderId,
			@RequestParam("selectStaffId")Long selectStaffId,
			@RequestParam("newServiceDate")Long newServiceDate,
			@RequestParam("distanceValue")int distanceValue){
		
		AppResultData<Object> resultData = new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		Orders order = orderSevice.selectbyOrderId(orderId);
		
		order.setServiceDate(newServiceDate);
		order.setUpdateTime(TimeStampUtil.getNowSecond());
		//更新为 已派工
		order.setOrderStatus(Constants.ORDER_HOUR_STATUS_3);
		
		
		//更新派工表.发送通知
		/*
		 *  2016年3月24日18:02:07 
		 *  		
		 *  历史遗留问题: 在 jhj1.0  ， 由于存在 手动派工, 所以 在 order_dispathes表中，
		 *  		
		 *  			一条 order_id 对应 多条 派工记录, 但是 有效派工 只有一条
		 * 
		 *    所以 ,更新时, 只更新有效的 那一条记录即可。
		 *    
		 *   此后， 每条 订单 对应的都只有 一条 有效 派工记录 ,无效记录不再存储
		 */
		
		//查询 出  某个 order_No 对应的 有效的 派工记录。。根据修改值，更新该条记录
		List<OrderDispatchs> list = disService.selectByNoAndDisStatus(order.getOrderNo(), Constants.ORDER_DIS_ENABLE);
		
		OrderDispatchs dispatchs = list.get(0);		
		
		dispatchs.setServiceDate(newServiceDate);
		dispatchs.setServiceDatePre(newServiceDate - 3600);
		dispatchs.setUpdateTime(TimeStampUtil.getNowSecond());
		
		if(selectStaffId !=0L){
			
			OrgStaffs staffs = staffService.selectByPrimaryKey(selectStaffId);
			
			dispatchs.setStaffId(selectStaffId);
			dispatchs.setStaffName(staffs.getName());
			dispatchs.setStaffMobile(staffs.getMobile());
			dispatchs.setUserAddrDistance(distanceValue);
		}
		
		Short orderStatus = order.getOrderStatus();
		
		
		//对于  钟点工订单, 只有订单状态为    "已支付" 或  "已派工",可以进行 调整派工
		if(orderStatus  == Constants.ORDER_HOUR_STATUS_2
				|| orderStatus == Constants.ORDER_HOUR_STATUS_3){
		
			//更新 派工表
			disService.updateByPrimaryKeySelective(dispatchs);
			//更新订单表
			orderSevice.updateByPrimaryKeySelective(order);
		}
		
		//发推送 消息 TODO
		
		return resultData;
	}
	
	/**
	 * 
	 *  @Title: loadProperStaffListForAm
	  * @Description: 
	  * 	 	助理类订单, 手动派工时,根据条件 加载 可用派工人员
	  * 
	  *  对于 此类订单, 服务时间 不可变。。但是 地址和 服务人员可以改变
	  * 
		@param rderId
	  * @param fromLat
	  * @param fromLng
	  * @return List<OrgStaffsNewVo>    返回类型
	  * @throws
	 */
	@RequestMapping(value = "load_staff_for_am_order.json",method = RequestMethod.POST)
	public List<OrgStaffsNewVo> loadProperStaffListForAm(Model model,
			@RequestParam("orderId")Long orderId,
			@RequestParam("fromLat")String fromLat,
			@RequestParam("fromLng")String fromLng){
		 
		Orders orders = orderSevice.selectbyOrderId(orderId);
		
		List<Long> staIdList = newDisService.autoDispatchForAmOrder(fromLat, fromLng, orders.getServiceType());
		
		List<OrgStaffsNewVo> list = new ArrayList<OrgStaffsNewVo>();
		
		Short orderStatus = orders.getOrderStatus();
		
		//对于 助理 类 订单，只有在  '已预约','已派工' 两种状态 可以 修改 派工人员
		if(orderStatus  == Constants.ORDER_AM_STATUS_1
					|| orderStatus == Constants.ORDER_AM_STATUS_2){
			list = newDisService.getTheNearestStaff(fromLat, fromLng, staIdList);
		}
		return list;
	}
	
	/**
	 * 
	 *  @Title: submitManuAmOrderResult
	  * @Description: 
	  * 	  提交 助理 订单 手动派工结果	
	  * @param orderId	
	  *
	  * @param fromLat	 用户的地址 经纬度
	  * @param fromLng	 
	  * 
	  * @param staffId   被选中的服务人员
	  * @param distance  被选中服务人员,距离用户地址的 距离 数字,(更新派工表字段使用)
	  * 
	  * @param userAddrName  与用户沟通后确定的服务地址
	  * @throws
	 */
	@RequestMapping(value = "/submit_manu_am_order_result.json",method = RequestMethod.POST)
	public AppResultData<Object> submitManuAmOrderResult(
			@RequestParam("orderId")Long orderId,
			@RequestParam("fromLat")String fromLat,
			@RequestParam("fromLng")String fromLng,
			@RequestParam("selectStaffId")Long staffId,
			@RequestParam("userAddrName")String userAddrName,
			@RequestParam("distance")int distance){
		
		AppResultData<Object> resultData = new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		Orders order = orderSevice.selectbyOrderId(orderId);
		
		//修改的 只是 有效派工的订单
		List<OrderDispatchs> list = disService.selectByNoAndDisStatus(order.getOrderNo(), Constants.ORDER_DIS_ENABLE);
		
		
		OrderDispatchs dispatchs = disService.initOrderDisp();
		
		//如果可以查出记录。表示未 修改 派工, 否则是 手动新增派工
		if(list.size() <= 0){
			
			// 如果没有派工记录。。表示是 新增派工
			
			dispatchs.setUserId(order.getUserId());
			dispatchs.setMobile(order.getMobile());
			dispatchs.setOrderId(orderId);
			dispatchs.setOrderNo(order.getOrderNo());
			dispatchs.setServiceDate(order.getServiceDate());
			dispatchs.setServiceDatePre(order.getServiceDate()-3600);
			dispatchs.setRemarks(order.getRemarks());
			//默认3小时服务时长
			dispatchs.setServiceHours((short)3); 
			
		}else{
			
			//有派工记录。表示 是 修改 派工
			dispatchs = list.get(0);	
		}
		
		dispatchs.setPickAddrName(userAddrName);
		dispatchs.setPickAddrLat(fromLat);
		dispatchs.setPickAddrLng(fromLng);
		
		//助理派工表。。。这里两个距离 :   距用户/取货地址  距离 设置 为同一个值了、、
		dispatchs.setPickDistance(distance);
		dispatchs.setUserAddrDistance(distance);
		
		OrgStaffs staffs = staffService.selectByPrimaryKey(staffId);
		
		dispatchs.setOrgId(staffs.getOrgId());
		dispatchs.setStaffId(staffId);
		dispatchs.setStaffMobile(staffs.getMobile());
		dispatchs.setStaffName(staffs.getName());
		dispatchs.setDispatchStatus(Constants.ORDER_DIS_ENABLE);
		
		Short orderStatus = order.getOrderStatus();
		
		//对于 助理 类 订单，只有在  '已预约','已派工' 两种状态 可以 修改 派工人员
		
		if(orderStatus  == Constants.ORDER_AM_STATUS_1){
			
			//如果订单状态是 已预约。 手动增加派工
			
			//插入 派工表
			disService.insertSelective(dispatchs);
			
			//更新 order_status
			order.setOrderStatus(Constants.ORDER_AM_STATUS_2);
			order.setUpdateTime(TimeStampUtil.getNowSecond());
			
			orderSevice.updateByPrimaryKeySelective(order);
			
		}
				
		if(orderStatus == Constants.ORDER_AM_STATUS_2){
			
			// 已派工。 修改派工表
			disService.updateByPrimaryKeySelective(dispatchs);
		}
		
		
		// 发推送消息
		orderPayService.orderPaySuccessToDoForAm(order);
		
		return resultData;
	}
	
}
