package com.jhj.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.po.model.cooperate.CooperativeBusiness;
import com.jhj.service.cooperate.CooperateBusinessService;
import com.meijia.utils.StringUtil;


@Controller
@RequestMapping(value = "/app/")
public class OController extends BaseController {
	
	@Autowired
	private CooperateBusinessService cooperateBusinessService;
	
	//跳转到对应的h5页面
	/**
	 * @param f    
	 *        f = qrcode  扫描二维码
	 * @param s = 来源标识 
	 *        123 = 微网站社区一
	 *        124 = 微网站社区二
	 *        125 = 微网站社区三
	 *        126 = 微网站社区四
	 * 
	 * @return
	 */
	@RequestMapping(value = "o", method = RequestMethod.GET)
	public String redirectUrl(HttpServletRequest request,
			@RequestParam(value = "f", required = false, defaultValue = "") String f,
			@RequestParam(value = "s", required = false, defaultValue = "") String s
			
		) {
		
		if (StringUtil.isEmpty(f) || StringUtil.isEmpty(s)) {
			return "";
		}
		
		if (f.equals("qrcode") && !StringUtil.isEmpty(s)) {
			//检测来源ID是否存在
			CooperativeBusiness record = cooperateBusinessService.selectByPrimaryKey(Long.valueOf(s));
			if (record == null) return "";
		}
		
		String reqHost = request.getRemoteHost();
		
		String url = reqHost + "/u/";
		
		logger.info(url);
		return "";
//		return "redirect:"+url;
		
	}
	

}
