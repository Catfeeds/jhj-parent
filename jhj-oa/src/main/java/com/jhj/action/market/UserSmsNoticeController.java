package com.jhj.action.market;

import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.service.users.UserSmsNoticeService;
import com.jhj.vo.user.UserSmsNoticeSearchVo;

@Controller
@RequestMapping(value = "/market")
public class UserSmsNoticeController extends BaseController {

	@Autowired
	private UserSmsNoticeService userSmsNoticeService;

	@AuthPassport
	@RequestMapping(value = "/sms-notice-list", method = { RequestMethod.GET })
	public String userList(HttpServletRequest request, Model model, @ModelAttribute("searchModel") UserSmsNoticeSearchVo searchVo) throws ParseException {

		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);

		
		PageInfo result = userSmsNoticeService.selectByListPage(searchVo, pageNo, pageSize);
		model.addAttribute("contentModel", result);
		model.addAttribute("searchModel", searchVo);

		return "market/userSmsNoticeList";
	}
}
