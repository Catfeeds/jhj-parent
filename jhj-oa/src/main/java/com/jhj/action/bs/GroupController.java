package com.jhj.action.bs;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.common.ConstantOa;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.po.model.bs.Orgs;
import com.jhj.service.bs.OrgsService;
import com.jhj.vo.org.OrgSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2016年3月7日下午2:04:53
 * @Description: 
 *	
 *		运营平台--小组管理
 *		
 *		ps: 小组和 门店 共用  orgs 表，属性完全一致
 *
 *			通过 字段 org_type区分二者
 *			
 *			0=门店  1=云店
 *
 */
@Controller
@RequestMapping(value = "/group")
public class GroupController {
	
	@Autowired
	private OrgsService orgsService;
	
	/**
	 * 
	 *  @Title: groupList
	  * @Description: 	
	  * 		云店列表
	  * @param model
	  * @param request
	  * @param searchVo	
	  * 			列表页搜索条件
	  * @param orgId	
	  * 			从门店管理 查看 云店
	  * @return String    返回类型
	  * @throws
	 */
	@RequestMapping(value = "/group_list", method = {RequestMethod.GET})
	public String groupList(Model model, HttpServletRequest request, 
			@ModelAttribute("groupSearchVoModel") OrgSearchVo searchVo,
			@RequestParam(value = "orgId",required = false,defaultValue = "0") Long orgId){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		
		PageHelper.startPage(pageNo, pageSize);
		
		if(orgId != 0L){
			searchVo.setParentId(orgId);
		}
		
		//得到 当前登录 的 门店id，并作为搜索条件
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);
		
		if(sessionOrgId > 0L){
			searchVo.setParentId(sessionOrgId);
		}
		
		if (searchVo == null) searchVo = new OrgSearchVo();
		searchVo.setIsCloud((short) 1);
		
		PageInfo result = orgsService.selectByListPage(searchVo, pageNo, pageSize);

		model.addAttribute("orgsModel", result);
		
		model.addAttribute("groupSearchVoModel", searchVo);
		
		return "bs/groupList";
	}
	
	/*
	 * 通过判断页面隐藏域  orgId，确定 页面功能是 添加还是修改
	 */
	@RequestMapping(value = "/groupForm", method = { RequestMethod.GET })
	public String adForm(Model model,
			@RequestParam(value = "orgId") Long id,
			HttpServletRequest request) {
		if (id == null) {
			id = 0L; 
		}
		
		Orgs orgs = orgsService.initOrgs();
		
		if(id !=null && id > 0){
			orgs = orgsService.selectByPrimaryKey(id);
		}
		
		model.addAttribute("orgsModel", orgs);
		
		return "bs/groupForm";
	}
	
	@RequestMapping(value = "/doGroupForm", method = { RequestMethod.POST })
	public String doAdForm(HttpServletRequest request, Model model,
			@ModelAttribute("orgsModel") Orgs orgs){
		
		Long id = Long.valueOf(request.getParameter("orgId"));
		
		Orgs initOrgs = orgsService.initOrgs();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(orgs, initOrgs);
		
		//更新或修改
		if(id!=null && id>0){
			orgs.setUpdateTime(TimeStampUtil.getNow() / 1000);
			orgsService.updateByPrimaryKeySelective(initOrgs);
		}else {
			initOrgs.setPoiUid(UUID.randomUUID().toString());
		
			orgsService.insertSelective(initOrgs);
		}
		
		return "redirect:/group/group_list";
	}

	/*
	 * 校验门店名称是否重复
	 */
	@RequestMapping(value = "/validOrg",method = {RequestMethod.GET})
	public void validName(PrintWriter printWriter, String name ) throws UnsupportedEncodingException{
		
		String names = URLDecoder.decode(name,"utf-8");
		
		OrgSearchVo searchVo = new OrgSearchVo();
		searchVo.setOrgName(names);
		
		List<Orgs> list = orgsService.selectBySearchVo(searchVo);
		if(list.size()>0 && list !=null){
			//如果输入的名称能查出来记录，则返回 名称已存在 标识
			printWriter.write("no");
		}else{
			printWriter.write("yes");
		}
		
	}
	
}
