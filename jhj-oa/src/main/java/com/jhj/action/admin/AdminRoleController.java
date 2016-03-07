package com.jhj.action.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;
import com.jhj.common.ConstantOa;
import com.jhj.models.TreeModel;
import com.jhj.models.extention.TreeModelExtension;
import com.jhj.oa.common.ArrayHelper;
import com.jhj.po.model.admin.AdminAuthority;
import com.jhj.po.model.admin.AdminRole;
import com.jhj.service.admin.AdminAuthorityService;
import com.jhj.service.admin.AdminRoleAuthorityService;
import com.jhj.service.admin.AdminRoleService;
import com.jhj.vo.admin.AdminRoleSearchVo;
import com.jhj.vo.admin.AdminRoleVo;
import com.meijia.utils.StringUtil;
import com.meijia.utils.common.extension.StringHelper;


@Controller
@RequestMapping(value = "/role")
public class AdminRoleController extends AdminController {

	@Autowired
	private AdminRoleService adminRoleService;

	@Autowired
	private AdminAuthorityService adminAuthorityService;
	
	@Autowired
	private AdminRoleAuthorityService adminRoleAuthorityService;
	
	//@AuthPassport
	@RequestMapping(value="/list", method = {RequestMethod.GET})
    public String list1(HttpServletRequest request, Model model, AdminRoleSearchVo searchModel){
    	model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());
        model.addAttribute("searchModel", searchModel);

        int pageNo = ServletRequestUtils.getIntParameter(request,ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		PageInfo result = adminRoleService.searchVoListPage(searchModel, pageNo, pageSize);
		model.addAttribute("contentModel",result);
        return "role/roleList";
    }


	//@AuthPassport
	@RequestMapping(value = "/toRoleForm", method = {RequestMethod.GET})
	public String toAddAdminRole(HttpServletRequest request,Model model){
		Long ids = Long.valueOf(request.getParameter("id"));
		if (ids == null) {
			ids = 0L;
		}
		AdminRole adminRole  = adminRoleService.initAdminRole();

		if (ids != null && ids > 0L) {
			adminRole = adminRoleService.selectByPrimaryKey(ids);
		}		
		
		AdminRoleVo adminRoleVo = (AdminRoleVo)adminRoleService.selectAdminRoleVoByPrimaryKey(ids);
		BeanUtils.copyProperties(adminRole,adminRoleVo);
		
		List<Long> checkedAuthorityIds = new ArrayList<Long>();
		List<Integer> checkedAuthorityIntegers = new ArrayList<Integer>();

		if(adminRoleVo.getChildList()!=null){
			List<AdminAuthority> roleAuthorities=adminRoleVo.getChildList();
			for (Iterator iterator  = roleAuthorities.iterator(); iterator.hasNext();) {
				AdminAuthority adminAuthority = (AdminAuthority) iterator.next();
				if(adminAuthority!=null){
					checkedAuthorityIds.add(adminAuthority.getId());
					checkedAuthorityIntegers.add(adminAuthority.getId().intValue());
				}
			}
		}
		if(!model.containsAttribute("adminRole")){
			Long[] checkedAuthorityIdsArray=new Long[checkedAuthorityIds.size()];
			checkedAuthorityIds.toArray(checkedAuthorityIdsArray);
			adminRoleVo.setAuthorityIds(checkedAuthorityIdsArray);
			model.addAttribute("adminRole", adminRoleVo);
		}

		String expanded = ServletRequestUtils.getStringParameter(request, "expanded", null);
		List<TreeModel> children=TreeModelExtension.ToTreeModels(adminAuthorityService.listChain(), null, checkedAuthorityIntegers, StringHelper.toIntegerList( expanded, ","));
		List<TreeModel> treeModels=new ArrayList<TreeModel>(Arrays.asList(new TreeModel(null,null,"根节点",false,false,false,children)));
		model.addAttribute(treeDataSourceName, JSONArray .fromObject(treeModels, new JsonConfig()).toString());
		
		model.addAttribute(selectDataSourceName,
				adminAuthorityService.getSelectSource());
        return "role/roleForm";
	}
	//@AuthPassport
	@RequestMapping(value ="/roleForm", method = {RequestMethod.POST})
    public String addAdminRole(HttpServletRequest request, Model model,
    		@ModelAttribute("adminRole") AdminRoleVo adminRoleVo, BindingResult result)  {
		String id = request.getParameter("id");
		if (StringUtil.isEmpty(id)) {
			id = "0";
		}
		//保存角色
		AdminRole adminRole = adminRoleService.initAdminRole();
		if(id !=null && Long.valueOf(id) >0){
			adminRoleService.updateByPrimaryKeySelective(adminRole);
		}else {
			adminRole.setName(adminRoleVo.getName());
			adminRoleService.insertSelective(adminRole);
			adminRoleVo.setId(adminRole.getId());
			id = adminRole.getId().toString();
		}
		//将角色和选中的权限绑定
		  if(result.hasErrors())
	            return bind(request, model, id);

	        adminRoleService.saveRoleToAuthorize(Long.valueOf(id), ArrayHelper.removeArrayLongItem(adminRoleVo.getAuthorityIds(), new Integer(0)));
	        String returnUrl = ServletRequestUtils.getStringParameter(request, "returnUrl", null);
	        if(returnUrl==null)
	        	returnUrl="role/list";
    	return "redirect:list";
    }

	//@AuthPassport
	@RequestMapping(value ="/delete/{id}", method = {RequestMethod.GET})
	public String deleterAdminRole(Model model,@PathVariable(value="id") String id,HttpServletRequest response)  {
		Long ids = 0L;
		if (id != null && NumberUtils.isNumber(id)) {
			ids = Long.valueOf(id.trim());
		}
		String path = "redirect:/role/list";
		//删除角色
		int result = adminRoleService.deleteByPrimaryKey(ids);
		//删除该角色对应的权限
		int results = adminRoleAuthorityService.deleteByRoleId(ids);
		if(result>0 && results >0){
			return path;
		}else{
			return "error";
		}
	}
	/**
	 * 给对应id的用户绑定角色
	 * @param request
	 * @param model
	 * @param id
	 * @return 跳转到绑定权限的页面
	 */
	//@AuthPassport
	/*@RequestMapping(value="/bind/{id}", method = {RequestMethod.GET})*/
	public String bind(HttpServletRequest request, Model model,  String id) {
		
		Long ids = 0L;
		if (id != null && NumberUtils.isNumber(id)) {
			ids = Long.valueOf(id.trim());
		}
		//AdminRole role=adminRoleService.selectByPrimaryKey(ids);
		AdminRoleVo adminRoleVo = (AdminRoleVo)adminRoleService.selectAdminRoleVoByPrimaryKey(ids);
		List<Long> checkedAuthorityIds = new ArrayList<Long>();
		List<Integer> checkedAuthorityIntegers = new ArrayList<Integer>();

		if(adminRoleVo.getChildList()!=null){
			List<AdminAuthority> roleAuthorities=adminRoleVo.getChildList();
			for (Iterator iterator  = roleAuthorities.iterator(); iterator.hasNext();) {
				AdminAuthority adminAuthority = (AdminAuthority) iterator.next();
				if(adminAuthority!=null){
					checkedAuthorityIds.add(adminAuthority.getId());
					checkedAuthorityIntegers.add(adminAuthority.getId().intValue());
				}
			}
		}
		if(!model.containsAttribute("contentModel")){
			Long[] checkedAuthorityIdsArray=new Long[checkedAuthorityIds.size()];
			checkedAuthorityIds.toArray(checkedAuthorityIdsArray);
			adminRoleVo.setAuthorityIds(checkedAuthorityIdsArray);
			model.addAttribute("contentModel", adminRoleVo);
		}

		String expanded = ServletRequestUtils.getStringParameter(request, "expanded", null);
		List<TreeModel> children=TreeModelExtension.ToTreeModels(adminAuthorityService.listChain(), null, checkedAuthorityIntegers, StringHelper.toIntegerList( expanded, ","));
		List<TreeModel> treeModels=new ArrayList<TreeModel>(Arrays.asList(new TreeModel(null,null,"根节点",false,false,false,children)));
		model.addAttribute(treeDataSourceName, JSONArray .fromObject(treeModels, new JsonConfig()).toString());

		return "role/bind";
	}
}
