package com.jhj.vo.order;

import java.util.List;

import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.user.UserAddrs;

/**
 *
 * @author :hulj
 * @Date : 2015年8月8日上午11:36:55
 * @Description: 
 * 		用户版--钟点工 -- 订单提交页面。加载时  VO
 * 
 * 			用户地址列表，钟点工附加服务价格
 *
 */
public class OrderHourAddVo {
	
	private List<UserAddrs> userAddrList;	//用户地址列表
	
	private List<DictServiceAddons> dictServiceAddonList;	//附加服务价格 列表

	public List<UserAddrs> getUserAddrList() {
		return userAddrList;
	}

	public void setUserAddrList(List<UserAddrs> userAddrList) {
		this.userAddrList = userAddrList;
	}

	public List<DictServiceAddons> getDictServiceAddonList() {
		return dictServiceAddonList;
	}

	public void setDictServiceAddonList(List<DictServiceAddons> dictServiceAddonList) {
		this.dictServiceAddonList = dictServiceAddonList;
	}
	
	
	
}
