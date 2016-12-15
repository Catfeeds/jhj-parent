package com.jhj.service.async;

import java.util.concurrent.Future;

import com.jhj.po.model.user.Users;

public interface SendMarketSmsService {
	
	Future<Boolean> allotSms(Users user,Integer marketSmsId,String SmsTempID,String[] content);
	
	public int successNum(); 
	
	public int failNum();

}
