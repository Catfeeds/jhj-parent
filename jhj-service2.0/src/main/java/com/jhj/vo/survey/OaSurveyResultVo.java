package com.jhj.vo.survey;

import java.math.BigDecimal;
import java.util.List;

import com.jhj.po.model.survey.SurveyUserRefRecommend;

/*
 * 运营平台, 调查结果展示 VO
 */
public class OaSurveyResultVo extends SurveyUserRefRecommend {
	
	//调查时填写的用户信息。在 survey_user表中
	private String userName;			 		
	private String userMobile;	
	
	//服务内容
	private  List<OaSurveyContentVo>	contentList;
	
	//当前服务按年 算的 总价
	private BigDecimal sumPriceByYear;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public List<OaSurveyContentVo> getContentList() {
		return contentList;
	}

	public void setContentList(List<OaSurveyContentVo> contentList) {
		this.contentList = contentList;
	}

	public BigDecimal getSumPriceByYear() {
		return sumPriceByYear;
	}

	public void setSumPriceByYear(BigDecimal sumPriceByYear) {
		this.sumPriceByYear = sumPriceByYear;
	}
}
