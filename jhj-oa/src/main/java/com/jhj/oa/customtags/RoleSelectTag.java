package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jhj.po.model.admin.AdminRole;
import com.jhj.service.admin.AdminRoleService;

/**
 *
 * @author :hulj
 * @Date : 2016年4月7日上午10:57:26
 * @Description: 
 *		
 *		 用户 角色  选择
 */
public class RoleSelectTag extends SimpleTagSupport {
	
	private String selectId = "0";

	public RoleSelectTag() {
		
	}
	
	private AdminRoleService roleService;
	
	
	@Override
	public void doTag() throws JspException, IOException {
		try {
			
			WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(((PageContext) getJspContext()).getServletContext());
			roleService = springContext.getBean(AdminRoleService.class);
			
			List<AdminRole> list = roleService.selectAll();
			
			StringBuffer roleSelect = new StringBuffer();
			
			roleSelect.append("<select id = \"roleId\" name=\"roleId\" class=\"form-control\">");

			roleSelect.append("<option value='0' >请选择所属角色</option>");
			
			AdminRole role = new AdminRole();
			
			String selected = "";
			
			for (int i = 0; i < list.size(); i++) {
				role = list.get(i);
				selected = "";
				if (selectId != null && role.getId().equals(Long.valueOf(selectId))) {
					selected = "selected=\"selected\"";
				}
				roleSelect.append("<option value='" + role.getId() + "' "
						+ selected + ">" + role.getName() + "</option>");
			}

			roleSelect.append("</select>");

			getJspContext().getOut().write(roleSelect.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getSelectId() {
		return selectId;
	}

	public void setSelectId(String selectId) {
		this.selectId = selectId;
	}
	
}
