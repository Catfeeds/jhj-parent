/*package com.jhj.action.user;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jhj.action.BaseController;
import com.jhj.oa.auth.AuthPassport;

@Controller
@RequestMapping(value = "/chart")
public class ChartController extends BaseController {


	@AuthPassport
	@RequestMapping(value = "/chartUser", method = { RequestMethod.GET })
	public String chartUser(HttpServletRequest request, Model model) {
			
		return "chart/chartUser";
	}
	@AuthPassport
	@RequestMapping(value = "/chartOrder", method = { RequestMethod.GET })
	public String chartOrder(HttpServletRequest request, Model model) {
			
		return "chart/chartOrder";
	}
	@AuthPassport
	@RequestMapping(value = "/chartIncome", method = { RequestMethod.GET })
	public String chartIncome(HttpServletRequest request, Model model) {
			
		return "chart/chartIncome";
	}
	@AuthPassport
	@RequestMapping(value = "/chartType", method = { RequestMethod.GET })
	public String chartType(HttpServletRequest request, Model model) {
			
		return "chart/chartType";
	}
	@AuthPassport
	@RequestMapping(value = "/chartServiceType", method = { RequestMethod.GET })
	public String chartServiceType(HttpServletRequest request, Model model) {
		
		return "chart/chartServiceType";
	}
	@AuthPassport
	@RequestMapping(value = "/chartSaleCard", method = { RequestMethod.GET })
	public String chartSaleCard(HttpServletRequest request, Model model) {
		
		return "chart/chartSaleCard";
	}
	@AuthPassport
	@RequestMapping(value = "/chartMoreCard", method = { RequestMethod.GET })
	public String chartMoreCard(HttpServletRequest request, Model model) {
		
		return "chart/chartMoreCard";
	}
	
}*/