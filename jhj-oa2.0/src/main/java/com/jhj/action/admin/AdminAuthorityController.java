package com.jhj.action.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jhj.po.model.admin.AdminAuthority;
import com.jhj.service.admin.AdminAuthorityService;
import com.jhj.models.AuthorityEditModel;
import com.jhj.models.TreeModel;
import com.jhj.models.extention.TreeModelExtension;
import com.meijia.utils.common.extension.StringHelper;
import com.jhj.vo.admin.AdminAuthorityVo;

@Controller
@RequestMapping(value = "/authority")
public class AdminAuthorityController extends AdminController {

	@Autowired
	private AdminAuthorityService adminAuthorityService;

	/**
	 * 树形展示权限列表
	 * @param request
	 * @param model
	 * @return
	 */
	//@AuthPassport
	@RequestMapping(value = "/chain", method = { RequestMethod.GET })
	public String chain(HttpServletRequest request, Model model) {
		if (!model.containsAttribute("contentModel")) {
			String expanded = ServletRequestUtils.getStringParameter(request,"expanded", null);
			List<TreeModel> children = TreeModelExtension.ToTreeModels(adminAuthorityService.listChain(), null, null,
					StringHelper.toIntegerList(expanded, ","));
			List<TreeModel> treeModels = new ArrayList<TreeModel>(Arrays.asList(new TreeModel("0", "0", "根节点", false, false,
							false, children)));
			String jsonString = JSONArray.fromObject(treeModels,new JsonConfig()).toString();
			model.addAttribute("contentModel", jsonString);
		}
		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());

		return "authority/authorityList";
	}
	/**
	 * 根据id添加同级或者子级节点
	 * @param request
	 * @param model
	 * @param id
	 * @return 编辑页面
	 */
	//@AuthPassport
	@RequestMapping(value = "/add/{id}", method = { RequestMethod.GET })
	public String add(HttpServletRequest request, Model model,
			@PathVariable(value = "id") Integer id) {
		if (!model.containsAttribute("contentModel")) {
			AuthorityEditModel authorityEditModel = new AuthorityEditModel();
			authorityEditModel.setParentId(id);
			model.addAttribute("contentModel", authorityEditModel);
		}
		List<TreeModel> treeModels;
		String expanded = ServletRequestUtils.getStringParameter(request,"expanded", null);
		if (id != null && id > 0) {
			List<TreeModel> children = TreeModelExtension.ToTreeModels(	adminAuthorityService.listChain(), id, null,
					StringHelper.toIntegerList(expanded, ","));
			treeModels = new ArrayList<TreeModel>(Arrays.asList(new TreeModel("0", "0", "根节点", false, false, false, children)));
		} else {
			List<TreeModel> children = TreeModelExtension.ToTreeModels(
					adminAuthorityService.listChain(), null, null,
					StringHelper.toIntegerList(expanded, ","));
			treeModels = new ArrayList<TreeModel>(Arrays.asList(new TreeModel(
					"0", "0", "根节点", false, true, false, children)));
		}
		model.addAttribute(treeDataSourceName,JSONArray.fromObject(treeModels, new JsonConfig()).toString());
		return "authority/authorityForm";
	}
	/**
	 * 根据页面选择的id,增加新节点
	 * @param request
	 * @param model
	 * @param adminAuthorityVo
	 * @param id
	 * @param result
	 * @return 权限的树形展示页面
	 */
	//@AuthPassport
	@RequestMapping(value = "/add/{id}", method = { RequestMethod.POST })
	public String add(HttpServletRequest request,Model model,
			@Valid @ModelAttribute("contentModel") AdminAuthorityVo adminAuthorityVo,
			@PathVariable(value = "id") String id, BindingResult result) {
		if (result.hasErrors()) return add(request, model, Integer.valueOf(id));
		String returnUrl = ServletRequestUtils.getStringParameter(request,
				"returnUrl", null);

		AdminAuthority adminAuthority = adminAuthorityService.initAdminAuthority(adminAuthorityVo);
		String levelCode = "";
		int count = adminAuthorityService.selectMaxId()+1;
		if (adminAuthorityVo.getParentId() != null 	&& adminAuthorityVo.getParentId() > 0) {
			adminAuthority.setParentId(adminAuthorityVo.getParentId());
			String parentLevelCode = adminAuthorityService.selectByPrimaryKey(adminAuthorityVo.getParentId()).getLevelCode();
			levelCode = count+ "," + parentLevelCode;

		}else{
			levelCode = count + levelCode  ;
		}
		adminAuthority.setLevelCode(levelCode);
		adminAuthorityService.insertSelective(adminAuthority);
		adminAuthority.setLevelCode(adminAuthority.getLevelCode()+adminAuthority.getId());
		if (returnUrl == null)	returnUrl = "authority/authorityList";
		return "redirect:" + returnUrl;
	}
	/**
	 * 根据id编辑对应的权限
	 * @param request
	 * @param model
	 * @param id
	 * @return 跳转到编辑页面
	 */
	//@AuthPassport
	@RequestMapping(value = "/edit/{id}", method = { RequestMethod.GET })
	public String edit(HttpServletRequest request, Model model,
			@PathVariable(value = "id") Long id) {
		//Long ids = Long.valueOf(id.trim());
		if (!model.containsAttribute("contentModel")) {
			AdminAuthority adminAuthority = adminAuthorityService.selectByPrimaryKey(id);
			model.addAttribute("contentModel", adminAuthority);
		}
		List<TreeModel> treeModels;
		AdminAuthority editModel = (AdminAuthority) model.asMap().get("contentModel");
		String expanded = ServletRequestUtils.getStringParameter(request,"expanded", null);
		if (editModel.getParentId() != null && editModel.getParentId() > 0) {
			List<TreeModel> children = TreeModelExtension.ToTreeModels(
					adminAuthorityService.listChain(), editModel.getParentId()
							.intValue(), null, StringHelper.toIntegerList(
							expanded, ","));
			treeModels = new ArrayList<TreeModel>(Arrays.asList(new TreeModel(
					"0", "0", "根节点", false, false, false, children)));
		} else {
			List<TreeModel> children = TreeModelExtension.ToTreeModels(
					adminAuthorityService.listChain(), null, null,
					StringHelper.toIntegerList(expanded, ","));
			treeModels = new ArrayList<TreeModel>(Arrays.asList(new TreeModel(
					"0", "0", "根节点", false, true, false, children)));
		}
		model.addAttribute("treeDataSource",
				JSONArray.fromObject(treeModels, new JsonConfig()).toString());

		return "authority/authorityForm";
	}

	/**
	 *根据id更新权限
	 * @param request
	 * @param model
	 * @param adminAuthority
	 * @param id
	 * @param result
	 * @return 跳转到权限的树形列表
	 */
	//@AuthPassport
	@RequestMapping(value = "/edit/{id}", method = { RequestMethod.POST })
	public String edit(	HttpServletRequest request,	Model model,
			@Valid @ModelAttribute("contentModel") AdminAuthority adminAuthority,
			@PathVariable(value = "id") Long id, BindingResult result) {
		if (result.hasErrors()) return edit(request, model, id);
		String returnUrl = ServletRequestUtils.getStringParameter(request,"returnUrl", null);
		if(adminAuthority!=null){
			adminAuthority.setId(Long.valueOf(id));
			adminAuthorityService.updateByPrimaryKeySelective(adminAuthority);
		}
		if (returnUrl == null) returnUrl = "authority/chain";
		return "redirect:" + returnUrl;
	}
	/**
	 * 根据id删除权限
	 * @param request
	 * @param model
	 * @param id
	 * @return 跳转到权限树形展示
	 */
	//@AuthPassport
	@RequestMapping(value = "/delete/{id}", method = { RequestMethod.GET })
	public String delete(HttpServletRequest request, Model model,@PathVariable(value = "id") String id) {
		Long ids = Long.valueOf(id.trim());
		//根据id查找出对应的该权限对象
		//int result = adminAuthorityService.deleteAuthorityByRecurision(adminAuthority);
		adminAuthorityService.deleteByPrimaryKey(ids);
		String returnUrl = ServletRequestUtils.getStringParameter(request,
				"returnUrl", null);
		if (returnUrl == null)
			returnUrl = "authority/chain";
		return "redirect:" + returnUrl;
	}

}
