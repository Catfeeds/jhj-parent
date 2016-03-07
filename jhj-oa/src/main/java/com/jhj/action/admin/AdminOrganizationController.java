package com.jhj.action.admin;
/*package com.zrj.action.admin;

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

import com.jhj.action.BaseController;
import com.jhj.models.OrganizationEditModel;
import com.jhj.models.TreeModel;
import com.jhj.models.extention.TreeModelExtension;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.oa.vo.account.AdminOrganiztionVo;
import com.jhj.po.model.admin.AdminOrganization;
import com.jhj.service.admin.AdminOrganizationService;
import com.meijia.utils.common.extension.StringHelper;

@Controller
@RequestMapping(value = "/organization")
public class AdminOrganizationController extends BaseController {

	@Autowired
	private AdminOrganizationService adminOrganizationService;

	@AuthPassport
	@RequestMapping(value = "/chain", method = {RequestMethod.GET})
	public String chain(HttpServletRequest request, Model model){
		if(!model.containsAttribute("contentModel")){
			String expanded = ServletRequestUtils.getStringParameter(request, "expanded", null);
			List<TreeModel> children=TreeModelExtension.ToTreeModels(adminOrganizationService.listChain(), null, null, StringHelper.toIntegerList( expanded, ","));
			List<TreeModel> treeModels=new ArrayList<TreeModel>(Arrays.asList(new TreeModel("0","0","根节点",false,false,false,children)));

			String jsonString  = JSONArray .fromObject(treeModels, new JsonConfig()).toString();
			model.addAttribute("contentModel", jsonString);
		}

		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());

        return "organization/chain";
	}

	@AuthPassport
	@RequestMapping(value = "/add/{id}", method = {RequestMethod.GET})
	public String add(HttpServletRequest request, Model model, @PathVariable(value="id") String  id){
		Integer ids = Integer.valueOf(id);
		if(!model.containsAttribute("contentModel")){
			OrganizationEditModel organizationEditModel=new OrganizationEditModel();
			organizationEditModel.setParentId(ids);
			model.addAttribute("contentModel", organizationEditModel);
		}

		List<TreeModel> treeModels;
		String expanded = ServletRequestUtils.getStringParameter(request, "expanded", null);
		if(id!=null && ids>0){
			List<TreeModel> children=TreeModelExtension.ToTreeModels(adminOrganizationService.listChain(), ids, null, StringHelper.toIntegerList( expanded, ","));
			treeModels=new ArrayList<TreeModel>(Arrays.asList(new TreeModel("0","0","根节点",false,false,false,children)));
		}
		else{
			List<TreeModel> children=TreeModelExtension.ToTreeModels(adminOrganizationService.listChain(), null, null, StringHelper.toIntegerList( expanded, ","));
			treeModels=new ArrayList<TreeModel>(Arrays.asList(new TreeModel("0","0","根节点",false,true,false,children)));
		}
		model.addAttribute("treeDataSource", JSONArray .fromObject(treeModels, new JsonConfig()).toString());
        return "organization/edit";
	}

	@AuthPassport
	@RequestMapping(value = "/add/{id}", method = {RequestMethod.POST})
    public String add(HttpServletRequest request, Model model, @Valid @ModelAttribute("contentModel") AdminOrganiztionVo adminOrganiztionVo ,
    		@PathVariable(value="id") String id, BindingResult result) {
        if(result.hasErrors())
            return add(request, model, id);

        String returnUrl = ServletRequestUtils.getStringParameter(request, "returnUrl", null);
        //Organization organization=OrganizationModelExtension.toOrganization(editModel);
        AdminOrganization adminOrganization = new AdminOrganization();
        organization.setVersion(1L);
        organization.setEnable((short)1);
        organization.setLevelCode("1");

        if(organization.getParentId()!=null  && organization.getParentId()>0){

        	organization.setParentId(organization.getParentId());
		}

        //adminOrganizationService.insertSelective(organization);
        if(returnUrl==null)
        	returnUrl="organization/chain";
    	return "redirect:"+returnUrl;
    }

	@AuthPassport
	@RequestMapping(value = "/edit/{id}", method = {RequestMethod.GET})
	public String edit(HttpServletRequest request, Model model, @PathVariable(value="id") Integer id) throws ValidatException{
		if(!model.containsAttribute("contentModel")){
			OrganizationEditModel organizationEditModel=OrganizationModelExtension.toOrganizationEditModel(organizationService.get(id));
			model.addAttribute("contentModel", organizationEditModel);
		}

		List<TreeModel> treeModels;
		OrganizationEditModel editModel=(OrganizationEditModel)model.asMap().get("contentModel");
		String expanded = ServletRequestUtils.getStringParameter(request, "expanded", null);
		if(editModel.getParentId()!=null && editModel.getParentId()>0){
			List<TreeModel> children=TreeModelExtension.ToTreeModels(organizationService.listChain(), editModel.getParentId(), null, StringHelper.toIntegerList( expanded, ","));
			treeModels=new ArrayList<TreeModel>(Arrays.asList(new TreeModel("0","0","根节点",false,false,false,children)));
		}
		else{
			List<TreeModel> children=TreeModelExtension.ToTreeModels(organizationService.listChain(), null, null, StringHelper.toIntegerList( expanded, ","));
			treeModels=new ArrayList<TreeModel>(Arrays.asList(new TreeModel("0","0","根节点",false,true,false,children)));
		}
		model.addAttribute(treeDataSourceName, JSONArray .fromObject(treeModels, new JsonConfig()).toString());

        return "organization/edit";
	}

	@AuthPassport
	@RequestMapping(value = "/edit/{id}", method = {RequestMethod.POST})
    public String edit(HttpServletRequest request, Model model, @Valid @ModelAttribute("contentModel") OrganizationEditModel editModel, @PathVariable(value="id") Integer id, BindingResult result) throws EntityOperateException, ValidatException {
        if(result.hasErrors())
            return edit(request, model, id);

        String returnUrl = ServletRequestUtils.getStringParameter(request, "returnUrl", null);

        Organization organization=OrganizationModelExtension.toOrganization(editModel);
        organization.setId(id);
        organizationService.update(organization);
        if(returnUrl==null)
        	returnUrl="organization/chain";
    	return "redirect:"+returnUrl;
    }

	@AuthPassport
	@RequestMapping(value = "/delete/{id}", method = {RequestMethod.GET})
	public String delete(HttpServletRequest request, Model model, @PathVariable(value="id") Integer id) throws ValidatException, EntityOperateException{
		organizationService.delete(id);
		String returnUrl = ServletRequestUtils.getStringParameter(request, "returnUrl", null);
		if(returnUrl==null)
        	returnUrl="authority/chain";
        return "redirect:"+returnUrl;
	}

}
*/