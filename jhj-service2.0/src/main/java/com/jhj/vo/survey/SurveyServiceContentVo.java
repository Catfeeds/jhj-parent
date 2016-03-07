package com.jhj.vo.survey;

import com.jhj.po.model.survey.SurveyContent;


public class SurveyServiceContentVo extends SurveyContent {

	private String serviceName;		//服务大类名称

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
}
