package com.jhj.action.order;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.BaseController;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderExpCleanService;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPayService;
import com.jhj.service.order.OrderServiceAddonsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserPushBindService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OrgStaffDispatchVo;

/**
 *
 * @author :hulj
 * @Date : 2016年3月21日下午2:48:09
 * @Description:
 *
 *               jhj2.1 -- 运营平台，手动派工
 */
@Controller
@RequestMapping(value = "/new_dispatch")
public class OrderDispatchController extends BaseController {

	@Autowired
	private OrderDispatchsService orderDispatchsService;

	@Autowired
	private UsersService userService;

	@Autowired
	private UserAddrsService userAddrService;

	@Autowired
	private OrdersService orderSevice;

	@Autowired
	private OrgStaffsService orgStaffsService;

	@Autowired
	private UserPushBindService bindService;

	@Autowired
	private OrderPayService orderPayService;

	@Autowired
	private PartnerServiceTypeService partnerService;

	@Autowired
	private OrderServiceAddonsService orderServiceAddonsService;

	@Autowired
	private OrderLogService orderLogService;
	
	@Autowired
	private OrderExpCleanService orderExpCleanService;

	
	/**
	 * 
	 * @Title: loadAutoDispatch
	 * @Description: 自动派工，返回一个可用服务人员ID.
	 *              
	 * 
	 * @param @param model
	 * @param @param orderId
	 * @param @param newServiceDate
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "load_auto_dispatch.json", method = RequestMethod.GET)
	public List<OrgStaffDispatchVo> loadAutoDispatch(
			@RequestParam("addrId") Long addrId,
			@RequestParam("serviceTypeId") Long serviceTypeId,
			@RequestParam("serviceDate") Long serviceDate,
			@RequestParam("serviceHour") double serviceHour,
			@RequestParam("staffNums") int staffNums
			) {
		
		List<OrgStaffDispatchVo> list = new ArrayList<OrgStaffDispatchVo>();
		
		PartnerServiceType serviceType = partnerService.selectByPrimaryKey(serviceTypeId);

		if (serviceType == null)
			return list;

		if (serviceType.getIsAuto().equals((short) 0))
			return list;
		
		
		list = orderDispatchsService.autoDispatch(addrId, serviceTypeId, serviceDate, serviceHour, staffNums, new ArrayList<Long>());

		return list;
	}
	
	
	/**
	 * 
	 * @Title: loadProperStaffListForBase
	 * @Description:
	 *               钟点工订单调整时, 根据 改变后的 服务时间, 加载 可用派工人员列表
	 * 
	 * @param @param model
	 * @param @param orderId
	 * @param @param newServiceDate
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "load_staff_by_change_service_date.json", method = RequestMethod.GET)
	public List<OrgStaffDispatchVo> loadProperStaffListForBase(HttpServletRequest request, 
			Model model, 
			@RequestParam("addrId") Long addrId,
			@RequestParam("serviceTypeId") Long serviceTypeId,
			@RequestParam("orderStatus") Short orderStatus,
			@RequestParam("serviceDate") Long serviceDate,
			@RequestParam("serviceHour") double serviceHour
			) {

		List<OrgStaffDispatchVo> list = new ArrayList<OrgStaffDispatchVo>();

		// 对于 钟点工订单, 只有订单状态为 "已支付" 或 "已派工",可以进行 调整派工
		if (orderStatus != Constants.ORDER_HOUR_STATUS_2 && orderStatus != Constants.ORDER_HOUR_STATUS_3) {
			return list;
		}
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);
		Long orgId = 0L;
		list = orderDispatchsService.manualDispatch(addrId, serviceTypeId, serviceDate, serviceHour, sessionOrgId, orgId);
		return list;
	}



	/**
	 * 
	 * @Title: loadProperStaffListForBaseByCloudOrg
	 * @Description:
	 *               根据 云店 动态加载 该 云店下 可用的 派工
	 * @param orderId
	 * @param cloudOrgId
	 * @throws
	 */
	@RequestMapping(value = "load_staff_by_change_cloud_org.json", method = RequestMethod.GET)
	public List<OrgStaffDispatchVo> loadProperStaffListForBaseByCloudOrg(Model model, 
			@RequestParam("parentId") Long parentId, 
			@RequestParam("orgId") Long orgId, 
			@RequestParam("addrId") Long addrId,
			@RequestParam("serviceTypeId") Long serviceTypeId,
			@RequestParam("orderStatus") Short orderStatus,
			@RequestParam("serviceDate") Long serviceDate,
			@RequestParam("serviceHour") double serviceHour) {

		List<OrgStaffDispatchVo> list = new ArrayList<OrgStaffDispatchVo>();

		// 对于 钟点工订单, 只有订单状态为 "已支付" 或 "已派工",可以进行 调整派工
		if (orderStatus != Constants.ORDER_HOUR_STATUS_2 && orderStatus != Constants.ORDER_HOUR_STATUS_3) {
			return list;
		}
		
		
		list = orderDispatchsService.manualDispatchByOrg(addrId, serviceTypeId, serviceDate, serviceHour, parentId, orgId);
		return list;
	}

}
