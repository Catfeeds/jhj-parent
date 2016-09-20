package com.jhj.action.bs;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
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

import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.dict.DictCity;
import com.jhj.service.bs.OrgsService;
import com.jhj.vo.OrgSearchVo;
import com.jhj.vo.org.GroupSearchVo;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年7月1日上午11:34:05
 * @Description: 门店管理）
 *
 */
@Controller
@RequestMapping(value = "/bs")
public class OrgsController extends BaseController{
	
	@Autowired
	private OrgsService orgsService;
	/*
	 * 列表查看所有门店信息
	 */
	@AuthPassport
	@RequestMapping(value = "/org-list", method = {RequestMethod.GET})
	public String orgsList(Model model, HttpServletRequest request,OrgSearchVo orgSearchVo){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
				
		if(orgSearchVo == null){
			orgSearchVo = new OrgSearchVo();
		}
		
		//得到 当前登录 的 门店id，并作为搜索条件
		String org = AuthHelper.getSessionLoginOrg(request);
		if(!org.equals("0") && !StringUtil.isEmpty(org)){
			//未选择 门店， 且 当前 登录 用户 为 店长 （  session中的  orgId 不为 0）,设置搜索条件为  店长的门店
			orgSearchVo.setSearchOrgId(Long.valueOf(org));
		}
		
		//默认将员工级别字段  设置  为 阿姨
		PageInfo pageInfo = orgsService.selectByListPage(orgSearchVo,pageNo, pageSize);
		model.addAttribute("orgsModel", pageInfo);
		
		model.addAttribute("nowOrgId", org);
		
		return "bs/orgList";
	}
	
	/*
	 * 通过判断页面隐藏域  orgId，确定 页面功能是 添加还是修改
	 */
	@AuthPassport
	@RequestMapping(value = "/orgForm", method = { RequestMethod.GET })
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
		
		return "bs/orgForm";
	}
//	@AuthPassport
	@RequestMapping(value = "/doOrgForm", method = { RequestMethod.POST })
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
		
		return "redirect:/bs/org-list";
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
	
	/**
	 * 根据门店查询云点
	 * @return
	 */
	@RequestMapping(value = "get-cloud-by-orgid.json", method = RequestMethod.GET)
	public AppResultData<Object> getCitys(@RequestParam(value = "orgId", required = true, defaultValue = "0") Long orgId) {

		List<Orgs> list = new ArrayList<Orgs>();

		if (orgId > 0) {
			GroupSearchVo searchVo = new GroupSearchVo();
			searchVo.setParentId(orgId);
			searchVo.setOrgStatus((short) 1);
			list = orgsService.selectCloudOrgByParentOrg(searchVo);
		}

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, list);

		return result;
	}
}
