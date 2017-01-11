package com.jhj.action.bs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.github.pagehelper.PageInfo;
import com.jhj.action.admin.AdminController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.models.TreeModel;
import com.jhj.models.extention.TreeModelExtension;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.AuthIdcard;
import com.jhj.po.model.bs.OrgStaffAuth;
import com.jhj.po.model.bs.OrgStaffSkill;
import com.jhj.po.model.bs.OrgStaffTags;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserTrailReal;
import com.jhj.service.bs.AuthIdCardService;
import com.jhj.service.bs.OrgStaffAuthService;
import com.jhj.service.bs.OrgStaffSkillService;
import com.jhj.service.bs.OrgStaffTagsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.bs.TagsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.service.users.UserTrailRealService;
import com.jhj.vo.AuthIdCardSearchVo;
import com.jhj.vo.bs.NewStaffFormVo;
import com.jhj.vo.bs.NewStaffListVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.staff.OrgStaffPoiVo;
import com.jhj.vo.staff.StaffSearchVo;
import com.jhj.vo.user.UserTrailSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.IDCardAuth;
import com.meijia.utils.ImgServerUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.common.extension.ArrayHelper;
import com.meijia.utils.common.extension.StringHelper;
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
}
