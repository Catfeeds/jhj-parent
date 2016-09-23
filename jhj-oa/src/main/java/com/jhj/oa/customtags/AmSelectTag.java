package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.vo.staff.StaffSearchVo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月7日下午4:57:12
 * @Description: 
 *					
 *      助理人员选择下拉表单
 *
 */
public class AmSelectTag extends SimpleTagSupport{
	private String selectId = "0";

    // 是否包含全部，  0 = 不包含  1= 包含
    private String hasAll =  "1";
    
	private OrgStaffsService orgStaffService;

	public AmSelectTag() {

	}

    @Override
    public void doTag() throws JspException, IOException {
        try {

    			WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(((PageContext) getJspContext()).getServletContext());
    			orgStaffService = springContext.getBean(OrgStaffsService.class);
    	        
    			StaffSearchVo searchVo = new StaffSearchVo();
    			searchVo.setStaffType((short) 1);
    			searchVo.setStatus(1);
    			List<OrgStaffs> amList = orgStaffService.selectBySearchVo(searchVo);
    			
//    			orgStaffService.selectAmByOrgId(orgId)
    			
    			
            StringBuffer orgStaffSelect = new StringBuffer();
            orgStaffSelect.append("<select id = \"amId\" name=\"amId\" class=\"form-control\">" );

            if (hasAll.equals("1")) {
            	orgStaffSelect.append("<option value='0' >==请选择助理==</option>");
            }

            OrgStaffs item = null;
            String selected = "";
            for(int i = 0;  i< amList.size();  i++) {
                item = amList.get(i);
                selected = "";
                if (selectId != null && item.getStaffId().toString().equals(selectId)) {
                		selected = "selected=\"selected\"";
                }
                orgStaffSelect.append("<option value='" + item.getStaffId() + "' " + selected + ">" + item.getName() + "</option>");
            }

            orgStaffSelect.append("</select>");

            getJspContext().getOut().write(orgStaffSelect.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public String getHasAll() {
		return hasAll;
	}

	public void setHasAll(String hasAll) {
		this.hasAll = hasAll;
	}

	public String getSelectId() {
		return selectId;
	}

	public void setSelectId(String selectId) {
		this.selectId = selectId;
	}

}
