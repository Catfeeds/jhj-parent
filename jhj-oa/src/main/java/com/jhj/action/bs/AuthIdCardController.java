package com.jhj.action.bs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.jhj.action.admin.AdminController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.AuthIdcard;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.service.bs.AuthIdCardService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.vo.AuthIdCardSearchVo;
import com.meijia.utils.IDCardAuth;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2016年3月9日上午11:39:07
 * @Description: 
 *		
 *		jhj2.1   服务人员管理 ： 不再区分助理和服务人员, 统一管理为服务人员	
 */
@Controller
@RequestMapping(value = "/newbs")
public class AuthIdCardController extends AdminController {
	
	@Autowired
	private OrgStaffsService staffService;
	
	@Autowired
	private AuthIdCardService authIdCardService;
	
	@RequestMapping(value = "auth-idcard-view", method = RequestMethod.GET)
	public String authIdCardView(Model model,HttpServletRequest request,
			@RequestParam("staffId") Long staffId) {
		
		OrgStaffs orgStaff = staffService.selectByPrimaryKey(staffId);
		
		model.addAttribute("orgStaff", orgStaff);	//当前登录的 id,动态显示搜索 条件

		//认证情况

		String name = orgStaff.getName();
		String idCard = orgStaff.getCardId();
		
		AuthIdCardSearchVo searchVo = new AuthIdCardSearchVo();
		searchVo.setName(name);
		searchVo.setIdCard(idCard);
		
		List<AuthIdcard> auths = authIdCardService.selectBySearchVo(searchVo);
		
		Map<String, String> authData = new HashMap<String, String>();
		int isAuthIdCard = 0;
		if (!auths.isEmpty()) {
			AuthIdcard authIdCard = auths.get(0);
			String content = authIdCard.getContent();
			authData = IDCardAuth.getResultMap(content);
			
			Long updateTime = authIdCard.getUpdateTime();
			String authTime = TimeStampUtil.timeStampToDateStr(updateTime * 1000);
			authData.put("authTime", authTime);
			
			String code = authData.get("code").toString();
			
			if (code.equals("0")) {
				isAuthIdCard = 1;
			} else {
				isAuthIdCard = 2;
			}
		}	
		
		model.addAttribute("isAuthIdCard", isAuthIdCard);
		model.addAttribute("authData", authData);
		
		return "bs/authIdCardView";
	}	
	
	
	@RequestMapping(value = "do-auth-id-card", method = RequestMethod.POST)
	public AppResultData<Object> doAuthIdCard(HttpServletRequest request, 
			@RequestParam("staffId") Long staffId) {
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		OrgStaffs orgStaff = staffService.selectByPrimaryKey(staffId);
		
		String name = orgStaff.getName();
		String cardId = orgStaff.getCardId();
		
		if (StringUtil.isEmpty(name) || StringUtil.isEmpty(cardId) ) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg("姓名或身份证号为空");
			return result;
		}
		
		String httpUrl = "http://v.apix.cn/apixcredit/idcheck/idcard";
		String httpArg = "type=idcard_photo&name="+name+"&cardno="+cardId;

		String jsonResult = IDCardAuth.request(httpUrl, httpArg);
		System.out.println(jsonResult);
//		String jsonResult = "{\"msg\":\"库中不存在该身份证号\",\"code\":101,\"data\":\"\"}";
//		String jsonResult = "{\"msg\":\"姓名和身份证号不一致\",\"code\":102,\"data\":{\"cardno\":\"330727199102104720\",\"birthday\":\"1991-02-10\",\"sex\":\"F\",\"name\":\"张翔\",\"address\":\"浙江省金华市磐安县\"}}";
		AuthIdCardSearchVo searchVo = new AuthIdCardSearchVo();
		searchVo.setName(name);
		searchVo.setIdCard(cardId);
		
		List<AuthIdcard> auths = authIdCardService.selectBySearchVo(searchVo);
		
		AuthIdcard authIdCard = authIdCardService.initAuthIdcard();
		
		if (!auths.isEmpty()) authIdCard = auths.get(0);
		
		authIdCard.setName(name);
		authIdCard.setIdCard(cardId);
		authIdCard.setContent(jsonResult);
		
		if (authIdCard.getId() > 0L) {
			authIdCard.setUpdateTime(TimeStampUtil.getNowSecond());
			authIdCardService.updateByPrimaryKey(authIdCard);
		} else {
			authIdCardService.insert(authIdCard);
		}
		
		Map<String, String> authData = new HashMap<String, String>();
		int isAuthIdCard = 0;
		
		String content = authIdCard.getContent();
		authData = IDCardAuth.getResultMap(content);
		
		Long updateTime = authIdCard.getUpdateTime();
		String authTime = TimeStampUtil.timeStampToDateStr(updateTime * 1000);
		authData.put("authTime", authTime);
		
		String code = authData.get("code").toString();
		
		if (code.equals("0")) {
			isAuthIdCard = 1;
		} else {
			isAuthIdCard = 2;
		}
		
		Map<String, Object> resultData = new HashMap<String, Object>();
		resultData.put("orgStaff", orgStaff);
		resultData.put("authData", authData);
		resultData.put("isAuthIdCard", isAuthIdCard);
		result.setData(resultData);
		return result;
	}
}
