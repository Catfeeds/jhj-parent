package com.jhj.action.cooperate;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.oa.auth.AccountAuth;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.po.model.admin.AdminAccount;
import com.jhj.po.model.cooperate.CooperativeBusiness;
import com.jhj.po.model.user.Users;
import com.jhj.service.admin.AdminAccountService;
import com.jhj.service.cooperate.CooperateBusinessService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.chart.CoopUserOrderVo;
import com.jhj.vo.cooperate.CooperateVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.StringUtil;

/**
 *
 * @author :hulj
 * @Date : 2016年4月1日下午3:52:04
 * @Description: 
 *
 */
@Controller
@RequestMapping(value = "/cooperate")
public class CooperatBusinessController extends BaseController {
	
	@Autowired
	private CooperateBusinessService bussService;
	
	@Autowired
	private AdminAccountService adminAccountService;
	
	@Autowired
	private UsersService userService;
	
	@Autowired
	private OrdersService orderService;
	/*
	 * 合作商户列表
	 */
	@RequestMapping(value = "coo_business_list",method = RequestMethod.GET)
	public String cooBusinessList(Model model, HttpServletRequest request){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		// 分页
		PageHelper.startPage(pageNo, pageSize);
		
		List<CooperativeBusiness> list = bussService.selectByListPage();
		
		PageInfo result = new PageInfo(list);
		
		model.addAttribute("cooBusinessModel", result);

		return "cooperate/coopBusList";
	}	
	
	/*
	 *  跳转form页
	 */
	@RequestMapping(value = "coo_business_form", method = RequestMethod.GET)
	public String bussinessForm(HttpServletRequest request,Model model) {
		
		Long businessId = 0L;
		
		String id = request.getParameter("id");
		
		if(id != null){
			businessId = Long.valueOf(id);
		}
		
		CooperativeBusiness bus = bussService.initCooBus();
		
		if(businessId != 0L && businessId != null){
			
			bus = bussService.selectByPrimaryKey(businessId);
		}
		
		CooperateVo formVo = bussService.transToFormVo(bus);
		
		model.addAttribute("cooBusinessModel", formVo);
		
		return "cooperate/coopBusForm";
	}
	
	
	/*
	 * 提交商户 form
	 */
	@RequestMapping(value = "coo_business_form", method = RequestMethod.POST)
	public String submitBussniessForm(Model model,HttpServletRequest request,
			@Valid@ModelAttribute("cooBusinessModel") CooperateVo businessVo,
			BindingResult result,
			@RequestParam("id")Long id) throws NoSuchAlgorithmException {
		
		CooperativeBusiness bus = bussService.initCooBus();
		
		/*
		 * 校验 用户名  是否 重复
		 */
		String loginName = businessVo.getBusinessLoginName();
		
		CooperativeBusiness cooperativeBusiness = bussService.selectByLogInName(loginName);
		
		/*
		 *  合作商户 的 登录名 还需要保证不能 和  家和家 登录角色 重名！！
		 */
		AdminAccount adminAccount = adminAccountService.selectByUsername(loginName);
		
		if(cooperativeBusiness != null 
				&& cooperativeBusiness.getId()>0
				&& cooperativeBusiness.getId() != businessVo.getId()
				
				||( adminAccount != null && adminAccount.getId() > 0 )) {
			
			result.addError(new FieldError("cooBusinessModel", "businessLoginName",
					"该登录名已存在"));
			
			model.addAllAttributes(result.getModel());
			
//			result.rejectValue("businessLoginName", "", "该登录名已存在");
			return bussinessForm(request, model);
		}
		
		String confirmPassWord = businessVo.getConfirmPassWord();
		
		String passWord = businessVo.getBusinessPassWord();
		
		if (!passWord.equals(confirmPassWord)) {
			result.addError(new FieldError("cooBusinessModel", "confirmPassWord",
					"确认密码与密码输入不一致。"));
			
//			result.rejectValue("confirmPassWord", "", "确认密码与密码输入不一致");
			
			return bussinessForm(request, model);
		}
		
		
		/*
		 * md5 加密登录密码
		 */
		String passwordMd5 = StringUtil.md5(businessVo.getBusinessPassWord().trim());
		
		if(id == 0L){
			BeanUtilsExp.copyPropertiesIgnoreNull(businessVo, bus);
			
			bus.setBusinessPassWord(passwordMd5);
			bussService.insertSelective(bus);
		}
		
		if(id > 0L){
			bus = bussService.selectByPrimaryKey(id);
			
			BeanUtilsExp.copyPropertiesIgnoreNull(businessVo, bus);
			
			bus.setBusinessPassWord(passwordMd5);
			bussService.updateByPrimaryKey(bus);
		}
		
		return "redirect:coo_business_list";
	}
	
	
	
	/*
	 *  合作商户-- 用户统计    
	 */
	
	@RequestMapping(value = "cooperate_user_order",method = RequestMethod.GET)
	public String chartCooperateUser(HttpServletRequest request,Model model){
		
		/*
		 *  获得 当前登录    合作商户的 id，， 已设置 从 99 开始自增。。规避了 公司登录角色
		 */
		AccountAuth auth = AuthHelper.getSessionAccountAuth(request);
		
		Long id = auth.getId();
		
		
		List<Users> list = userService.selectUserByAddFrom(id);
		
		List<Long> userIds = new ArrayList<Long>();
		
		for (Users users : list) {
			userIds.add(users.getId());
		}
		
		//总用户数
		model.addAttribute("sumUser",userIds.size());
		
		if(userIds.size() == 0){
			//使 id 集合不为空。。mybatis in 空 list会报错。
			userIds.add(0L);
		}
		
		//总订单数
		Long sumOrder = orderService.totalOrderInUserIds(userIds);
		
		model.addAttribute("sumOrder",sumOrder);
		
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		PageHelper.startPage(pageNo, pageSize);
		
		List<CoopUserOrderVo> userOrderList = orderService.totalUserAndOrder(userIds);
		
		System.out.println(userOrderList.size());
		
		PageInfo result = new PageInfo(userOrderList);
		
		model.addAttribute("userOrderModel", result);
		
		return "cooperate/coopUserOrderList";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
