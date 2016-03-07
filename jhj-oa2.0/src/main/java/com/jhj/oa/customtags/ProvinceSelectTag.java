package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.jhj.service.dict.DictUtil;
import com.jhj.po.model.dict.DictProvince;

public class ProvinceSelectTag extends SimpleTagSupport {

    private String selectId = "0";

    // 是否包含全部，  0 = 不包含  1= 包含
    private String hasAll =  "1";

    public ProvinceSelectTag() {
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {


        	List<DictProvince> optionList = DictUtil.getProvinces();

            StringBuffer serviceTypeSelect = new StringBuffer();
            serviceTypeSelect.append("<select id = \"provinceId\" name=\"provinceId\" class=\"form-control\">" );

            if (hasAll.equals("1")) {
            		serviceTypeSelect.append("<option value='0' >请选择</option>");
            }

            DictProvince item = null;
            String selected = "";
            for(int i = 0;  i<optionList.size();  i++) {
                item = optionList.get(i);
                selected = "";
                if (selectId != null && item.getProvinceId().toString().equals(selectId)) {
                		selected = "selected=\"selected\"";
                }
                serviceTypeSelect.append("<option value='" + item.getProvinceId() + "' " + selected + ">" + item.getName() + "</option>");
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
