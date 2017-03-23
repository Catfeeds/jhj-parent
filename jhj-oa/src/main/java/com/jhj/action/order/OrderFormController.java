package com.jhj.action.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jhj.action.BaseController;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AccountAuth;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.cooperate.CooperativeBusiness;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.cooperate.CooperateBusinessService;
import com.jhj.service.order.DispatchStaffFromOrderService;
import com.jhj.service.order.OaOrderService;
import com.jhj.service.order.OrderCancelService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderHourAddService;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPayService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.PartnerServiceTypeVo;
import com.jhj.vo.dict.CooperativeBusinessSearchVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :
 * @Date :
 * @Description:后台订单下单及预派工功能
 */
@Controller
@RequestMapping(value = "/order")
public class OrderFormController extends BaseController {

	@Autowired
	private OrdersService orderService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private OrderPricesService OrderPricesService;

	@Autowired
	private PartnerServiceTypeService partnerServiceTypeService;

	@Autowired
	private OrgsService orgService;

	@Autowired
	private OrderDispatchsService orderDispatchsService;

	@Autowired
	private OrderQueryService orderQueryService;

	@Autowired
	private CooperateBusinessService cooperateBusinessService;

	@Autowired
	private OrderLogService orderLogService;

	@Autowired
	private OrderPricesService orderPriceService;

	@AuthPassport
	@RequestMapping(value = "/order-add", method = RequestMethod.GET)
	public String oaOrderHourAdd(Model model, HttpServletRequest request) {

		// 订单的来源
		CooperativeBusinessSearchVo vo = new CooperativeBusinessSearchVo();
		vo.setEnable((short) 1);
		List<CooperativeBusiness> CooperativeBusinessList = cooperateBusinessService.selectCooperativeBusinessVo(vo);
		if (CooperativeBusinessList != null) {
			model.addAttribute("cooperativeBusiness", CooperativeBusinessList);
		}

		AccountAuth accountAuth = AuthHelper.getSessionAccountAuth(request);
		model.addAttribute("accountAuth", accountAuth);

		return "order/orderAdd";
	}
}
