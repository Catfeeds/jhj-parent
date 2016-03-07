package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.meijia.utils.OneCareUtil;

public class PayTypeSelectTag extends SimpleTagSupport {

	private String selectId = "0";

	public PayTypeSelectTag() {
	}

	@Override
	public void doTag() throws JspException, IOException {
		try {

			List<String> optionList = OneCareUtil.getPayType();

			StringBuffer serviceTypeSelect = new StringBuffer();
			serviceTypeSelect
					.append("<select id = \"payTypeSelect\" name=\"payType\" class=\"form-control\">");

			String item = null;
			String selected = "";
			for (int i = 0; i < optionList.size(); i++) {
				item = optionList.get(i);
				selected = "";
				if (selectId != null && item.equals(selectId)) {
					selected = "selected=\"selected\"";
				}
				serviceTypeSelect.append("<option value='" + i + "' "
						+ selected + ">" + item + "</option>");
			}

			serviceTypeSelect.append("</select>");

			getJspContext().getOut().write(serviceTypeSelect.toString());
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
