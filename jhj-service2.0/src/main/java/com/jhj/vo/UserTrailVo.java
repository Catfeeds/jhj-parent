package com.jhj.vo;

import java.util.List;

import com.jhj.po.model.user.UserTrailReal;
import com.meijia.utils.baidu.BaiduPoiVo;

public class UserTrailVo {
	
	private List<UserTrailReal> userTrailReals;
	
	private List<BaiduPoiVo> baiduPoiVos;

	public List<UserTrailReal> getUserTrailReals() {
		return userTrailReals;
	}

	public void setUserTrailReals(List<UserTrailReal> userTrailReals) {
		this.userTrailReals = userTrailReals;
	}

	public List<BaiduPoiVo> getBaiduPoiVos() {
		return baiduPoiVos;
	}

	public void setBaiduPoiVos(List<BaiduPoiVo> baiduPoiVos) {
		this.baiduPoiVos = baiduPoiVos;
	}
	
	
	

}
