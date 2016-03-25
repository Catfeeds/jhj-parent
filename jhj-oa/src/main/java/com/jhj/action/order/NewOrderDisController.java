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
	
	/**
	 * 
	 *  @Title: toDisStaForOrder
	  * @Description: 
			跳转到订单明细页. 动态展示 派工信息	
	  * @param orderId
	  * @return String    返回类型
	  * @throws
	 */
	
	@RequestMapping(value = "new_dis_for_order",method = RequestMethod.GET)
	public String toDisStaForOrder(Model model,@RequestParam("orderId")Long orderId){
		
		if(orderId == null || orderId <= 0L){
			
			return "";
		}
		
		OrderDispatchs dispatchs = disService.selectByOrderId(orderId);
		
		NewAutoDisStaffVo autoDisStaffVo = new NewAutoDisStaffVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(dispatchs, autoDisStaffVo);
		
		Long staffId = dispatchs.getStaffId();
		
		/*
		 * 基础保洁订单
		 *  1. 如果是 未 完成 派工, 根据 派工逻辑,表示 无任何 可用 派工
		 *  2. 如果是 已经派工, 需要 修改 派工人员
		 * 
		 * 助理类订单
		 *  1. 新订单, 客服 第一次 手动派工
		 *  2. 已派工, 修改 派工人员
		 *  
		 *  分析: 
		 *  	对于 两种订单的第一种情况, 无需返回    可用员工 VO
		 *  	对于 第二种情况,需要 回显 员工VO	
		 */
		
		if(staffId > 0){
			
			Orders order = orderSevice.selectByPrimaryKey(orderId);
			
			UserAddrs userAddrs = userAddrService.selectByPrimaryKey(order.getAddrId());
			
			String fromLat = userAddrs.getLatitude();
			
			String fromLng = userAddrs.getLongitude();
			
			autoDisStaffVo = newDisService.displayAllProperStaff(orderId, fromLat, fromLng);
		}
		
		model.addAttribute("disStaffVoModel", autoDisStaffVo);
		
		return "newDisOrder/orderDispatch";
	}
	
	
	/*
	 *  提交  手动派工 结果
	 */
	
	@RequestMapping(value = "new_dis_for_order",method = RequestMethod.POST)
	public String manuDisStaForOrder(Model model,
			@RequestParam("orderId")Long orderId,
			@RequestParam(value = "fromLat",required = false,defaultValue = "")String fromLat,
			@RequestParam(value = "fromLng",required = false,defaultValue = "")String fromLng,
			@RequestParam("properStaffId")Long properStaffId){
		
		
		
		return "";
	}
	
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
					|| orderStatus == Constants.ORDER_HOUR_STATUS_3
				){
			
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
		
		orderSevice.updateByPrimaryKeySelective(order);
		
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
		
		disService.updateByPrimaryKeySelective(dispatchs);
		
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
		
		List<OrgStaffsNewVo> list = newDisService.getTheNearestStaff(fromLat, fromLng, staIdList);
		
		Short orderStatus = orders.getOrderStatus();
		
		//对于 助理 类 订单，只有在  '已预约','已派工' 两种状态 可以 修改 派工人员
		if(orderStatus  == Constants.ORDER_AM_STATUS_1
					|| orderStatus == Constants.ORDER_AM_STATUS_2){
			
			list = newDisService.getTheNearestStaff(fromLat, fromLng, staIdList);
		}
		
		return list;
	}
}
