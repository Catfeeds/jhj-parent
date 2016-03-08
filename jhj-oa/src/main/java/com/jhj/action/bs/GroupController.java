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
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.Orgs;
import com.jhj.service.bs.OrgsService;
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
 *			0=门店  1=小组
 *
 */
@Controller
@RequestMapping(value = "/group")
public class GroupController {
	
	
	@Autowired
	private OrgsService orgsService;
	/*
	 * 列表查看所有门店信息
	 */
	@RequestMapping(value = "/group_list", method = {RequestMethod.GET})
	public String groupList(Model model, HttpServletRequest request){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		PageHelper.startPage(pageNo, pageSize);
		
		List<Orgs> list = orgsService.selectGroupsByListPage();
		PageInfo result = new PageInfo(list);	
		
		model.addAttribute("orgsModel", result);
		
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
			@ModelAttribute("orgs") Orgs orgs){
		
		Long id = Long.valueOf(request.getParameter("orgId"));
		
		//更新或修改
		if(id!=null && id>0){
			orgs.setUpdateTime(TimeStampUtil.getNow() / 1000);
			orgsService.updateByPrimaryKeySelective(orgs);
		}else {
			orgs.setAddTime(TimeStampUtil.getNow()/1000);
			orgs.setUpdateTime(0L);
			orgs.setPoiName("");
			orgs.setPoiPhone("");
			orgs.setPoiPostCode("");
			orgs.setPoiType((short)0);
			orgs.setPoiUid(UUID.randomUUID().toString());
			
			orgsService.insertSelective(orgs);
		}
		
		return "redirect:/group/group_list";
	}
	
	/*
	 * 删除一条记录
	 */
	@AuthPassport
	@RequestMapping(value = "/delete", method = {RequestMethod.GET})
	public String deleteOrg(@RequestParam(value = "orgId") Long orgId){
		if(orgId !=null && orgId>0){
			orgsService.deleteByPrimaryKey(orgId);
		}
		return "redirect:/bs/org-list";
	}
	
	/*
	 * 校验门店名称是否重复
	 */
	@RequestMapping(value = "/validOrg",method = {RequestMethod.GET})
	public void validName(PrintWriter printWriter, String name ) throws UnsupportedEncodingException{
		
		String names = URLDecoder.decode(name,"utf-8");
		List<Orgs> list = orgsService.selectByOrgName(names);
		
		if(list.size()>0 && list !=null){
			//如果输入的名称能查出来记录，则返回 名称已存在 标识
			printWriter.write("no");
		}else{
			printWriter.write("yes");
		}
		
	}
	
	
	
	
}
