package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.jhj.service.dict.DictUtil;
import com.jhj.po.model.dict.DictCity;

public class CitySelectTag extends SimpleTagSupport {

    private String selectId = "0";

    // 是否包含全部，  0 = 不包含  1= 包含
    private String hasAll =  "1";

    public CitySelectTag() {
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {


        	List<DictCity> optionList = DictUtil.getCitys();

            StringBuffer serviceTypeSelect = new StringBuffer();
            serviceTypeSelect.append("<select id = \"cityId\" name=\"cityId\" class=\"form-control\">" );

            if (hasAll.equals("1")) {
            	serviceTypeSelect.append("<option value='0' >全部</option>");
            }

            DictCity item = null;
            String selected = "";
            for(int i = 0;  i<optionList.size();  i++) {
                item = optionList.get(i);
                selected = "";
                if (selectId != null && item.getCityId().toString().equals(selectId)) {
                	selected = "selected=\"selected\"";
                }
                serviceTypeSelect.append("<option value='" + item.getCityId() + "' " + selected + ">" + item.getName() + "</option>");
            }

            serviceTypeSelect.append("</select>");

            getJspContext().getOut().write(serviceTypeSelect.toString());
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
