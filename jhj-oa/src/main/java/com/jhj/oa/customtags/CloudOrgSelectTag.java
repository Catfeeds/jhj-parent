package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.PageContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jhj.oa.auth.AuthHelper;
import com.jhj.po.model.bs.Orgs;
import com.jhj.service.bs.OrgsService;
import com.jhj.vo.OrgSearchVo;
import com.meijia.utils.StringUtil;


/**
 * 
 *
 * @author :hulj
 * @Date : 2016年3月8日下午7:03:35
 * @Description: 
 * 		
 * 	云店选择
 *
 */
public class CloudOrgSelectTag extends SimpleTagSupport {

    private String selectId = "0";

    // 是否包含全部，  0 = 不包含  1= 包含
    private String hasAll =  "1";
    
    //登录角色的 门店 id
    private Long logInParentOrgId;
    
	private OrgsService orgService;

    public CloudOrgSelectTag() {
    	
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {

			WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(((PageContext) getJspContext()).getServletContext());
			orgService = springContext.getBean(OrgsService.class);
    	    
			/* 	
			 * 	 2016年3月25日17:46:18  	
			 * 
			 *   选择 云店时, 也需要做 权限控制。根据当前  登录角色（比如店长）所在的门店。
			 *   
			 *   	选择该门店下的 云店
			 * 
			 */
			
			
			List<Orgs> orgList = new ArrayList<Orgs>();
			
			if(logInParentOrgId != 0){
				//如果登录的是店长
				
				OrgSearchVo searchVo = new OrgSearchVo();
				searchVo.setParentId(logInParentOrgId);
				searchVo.setOrgStatus((short) 1);
				orgList = orgService.selectBySearchVo(searchVo);
			}else{
				//如果登录的是 运营人员
				OrgSearchVo searchVo = new OrgSearchVo();
				searchVo.setIsCloud((short) 1);
				searchVo.setOrgStatus((short) 1);
				orgList = orgService.selectBySearchVo(searchVo);
			}
			

            StringBuffer orgSelect = new StringBuffer();
            orgSelect.append("<select id = \"orgId\" name=\"orgId\" class=\"form-control\">" );

            if (hasAll.equals("1")) {
            	orgSelect.append("<option value='0' >请选择云店</option>");
            }

            Orgs item = null;
            String selected = "";
            for(int i = 0;  i< orgList.size();  i++) {
                item = orgList.get(i);
                selected = "";
                if (selectId != null && item.getOrgId().toString().equals(selectId)) {
                		selected = "selected=\"selected\"";
                }
                orgSelect.append("<option value='" + item.getOrgId() + "' " + selected + ">" + item.getOrgName() + "</option>");
            }

            orgSelect.append("</select>");

            getJspContext().getOut().write(orgSelect.toString());
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

	public Long getLogInParentOrgId() {
		return logInParentOrgId;
	}

	public void setLogInParentOrgId(Long logInParentOrgId) {
		this.logInParentOrgId = logInParentOrgId;
	}
	
}
