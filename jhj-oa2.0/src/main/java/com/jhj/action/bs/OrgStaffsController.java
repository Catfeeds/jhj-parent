package com.jhj.action.bs;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffAuth;
import com.jhj.po.model.bs.OrgStaffTags;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.bs.Tags;
import com.jhj.service.bs.OrgStaffAuthService;
import com.jhj.service.bs.OrgStaffTagsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.bs.TagsService;
import com.jhj.vo.StaffSearchVo;
import com.jhj.vo.bs.OrgStaffVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年7月6日下午3:26:00
 * @Description: 服务人员管理（阿姨）
 *
 */
@Controller
@RequestMapping(value = "/bs")
public class OrgStaffsController extends BaseController {
	
	@Autowired
	private OrgStaffsService orgStaffsService;
	@Autowired
	private OrgStaffTagsService orgStaTagService;
	@Autowired
	private TagsService tagService;
	@Autowired
	private OrgsService orgService;
	
	@Autowired
	private OrgStaffAuthService authService;
	
	
	@AuthPassport
	@RequestMapping(value = "/staff-list", method = RequestMethod.GET)
	public String orgStaffList(Model model, HttpServletRequest request, StaffSearchVo staffSearchVo) throws UnsupportedEncodingException{
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		if (staffSearchVo == null) {
			staffSearchVo = new StaffSearchVo();
		}
		//staffType
		staffSearchVo.setStaffType((short)0);
		/*
		 * 设置中文 参数 编码，解决 input 乱码 
		 */
		staffSearchVo.setName(new String(staffSearchVo.getName().getBytes("iso-8859-1"),"utf-8"));
		
		
		// 门店Id,  店长搜索条件
		//得到 当前登录 的 门店id，并作为搜索条件
		String org = AuthHelper.getSessionLoginOrg(request);
		
		if(!StringUtil.isEmpty(org)){
			staffSearchVo.setOrgId(Long.parseLong(org));
		}
		
		List<OrgStaffs> list = orgStaffsService.selectByListPage(staffSearchVo, pageNo, pageSize);
		//tagnames
		OrgStaffs orgStaff = null;
		for (int i = 0; i < list.size(); i++) {
			orgStaff = list.get(i);
			//tagNames
			OrgStaffVo orgStaffVo = orgStaffsService.genOrgStaffVo(orgStaff);
			
			list.set(i,  orgStaffVo);
		}
		
		PageInfo result = new PageInfo(list);
		
		model.addAttribute("orgStaffVoModel", result);
		model.addAttribute("orgStaffSearchVoModel", staffSearchVo);
		return "bs/orgStaffList";
	}
		
	/*
	 *页面跳转
	 */
//	@AuthPassport
	@RequestMapping(value = "/orgStaffForm", method = RequestMethod.GET)
	public String toOrgStaffForm(Model model,HttpServletRequest request,
			@RequestParam("orgStaffId") Long staffId) {
		
		
		OrgStaffVo orgStaffVo = orgStaffsService.initOrgStaffVo();
		
		 //门店列表
		List<Orgs> orgList = orgService.selectAll();
		
		//0表示阿姨
		Short tagType = 0;
		//标签列表 
		List<Tags> tagList = tagService.selectAll(tagType);

		OrgStaffs orgStaffs = orgStaffsService.initOrgStaffs();
		
		String id = request.getParameter("orgStaffId");
		
		
		if (Long.valueOf(id) > 0L) {
			orgStaffs = orgStaffsService.selectByPrimaryKey(Long.valueOf(id));
			
		}
		orgStaffVo = orgStaffsService.genOrgStaffVo(orgStaffs);
		
		//动态展示 门店。助理 下拉列表
		String org = AuthHelper.getSessionLoginOrg(request);
				
		if(!StringUtil.isEmpty(org)){
			//如果登录的是 店长
			List<OrgStaffs> amList = orgStaffsService.selectAmByOrgId(Long.valueOf(org));
			
			orgStaffVo.setNowOrgAmList(amList);
		}
		
		model.addAttribute("loginOrgId", org);
		model.addAttribute("tagList", tagList);
		model.addAttribute("orgStaffVoFormModel", orgStaffVo);
		
		return "bs/orgStaffForm";
	}
	
	/*
	 *添加或修改
	 */
//	@AuthPassport
	@RequestMapping(value = "/doOrgStaffForm", method = { RequestMethod.POST })
	public String doOrgStaffForm(HttpServletRequest request, Model model,
			@ModelAttribute("orgStaffVoFormModel") OrgStaffVo orgStaffVo, BindingResult result) throws IOException {
		
		Long id = Long.valueOf(request.getParameter("staffId"));
		OrgStaffs orgStaffs = orgStaffsService.initOrgStaffs();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffVo, orgStaffs);
		String birth = request.getParameter("birth");
		orgStaffs.setBirth(DateUtil.parse(birth));
		
		if (id > 0L) {
			orgStaffs.setUpdateTime(TimeStampUtil.getNow()/1000);
			orgStaffsService.updateByPrimaryKeySelective(orgStaffs);
		} else {
			orgStaffsService.insertSelective(orgStaffs);
		}
		
		//存储标签
		String tagListStr = request.getParameter("tagIds");
		
		if (!StringUtil.isEmpty(tagListStr)) {
			orgStaTagService.deleteByStaffId(id);
			String[] tagList = StringUtil.convertStrToArray(tagListStr);
			
			for (int i = 0; i < tagList.length; i++) {
				if (StringUtil.isEmpty(tagList[i])) continue;
				
				OrgStaffTags record = new OrgStaffTags();
				record.setId(0L);
				record.setStaffId(orgStaffs.getStaffId());
				record.setTagId(Long.valueOf(tagList[i]));
				record.setAddTime(TimeStampUtil.getNowSecond());
				orgStaTagService.insert(record);
			}
		}
		
		
		/*
		 * 2016年1月22日19:14:09   录入认证信息
		 */
		
		String authIds = request.getParameter("authIds");
		
		if (!StringUtil.isEmpty(authIds)) {
			
			authService.deleteByStaffId(id);
			
			String[] tagList = StringUtil.convertStrToArray(authIds);
			
			for (int i = 0; i < tagList.length; i++) {
				if (StringUtil.isEmpty(tagList[i])) continue;
				
				OrgStaffAuth staffAuth = authService.initOrgStaffAuth();
				
				staffAuth.setStaffId(id);
				staffAuth.setServiceTypeId(Long.valueOf(tagList[i]));
				staffAuth.setAutStatus(Constants.STAFF_AUTH_STATUS_SUCCESS);
				staffAuth.setUpdateTime(TimeStampUtil.getNowSecond());
				
				authService.insertSelective(staffAuth);
			}
		}
		
		
		return "redirect:/bs/staff-list";
	}
	
	/*
	 * 校验手机号是否重复
	 */
	@RequestMapping(value = "/validMobile", method = RequestMethod.GET)
	public void validMobile(PrintWriter out,String name) throws UnsupportedEncodingException{
		
		String names = URLDecoder.decode(name,"utf-8");
		OrgStaffs orgStaffs = orgStaffsService.selectByMobile(names);
		
		if(orgStaffs !=null){
			//如果输入的名称能查出来记录，则返回 名称已存在 标识
			out.write("no");
		}else{
			out.write("yes");
		}
	}
	
	/*
	 * 校验身份证是否重复
	 */
	@RequestMapping(value = "/validCard", method = RequestMethod.GET)
	public void validCard(PrintWriter out,String name) throws UnsupportedEncodingException{
		
		String names = URLDecoder.decode(name,"utf-8");
		OrgStaffs orgStaffs = orgStaffsService.selectByCardId(names);
		
		if(orgStaffs !=null){
			//如果输入的名称能查出来记录，则返回 名称已存在 标识
			out.write("no");
		}else{
			out.write("yes");
		}
	}
}
