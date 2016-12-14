package com.jhj.service.async;

import java.util.Set;
import java.util.concurrent.Future;

import com.jhj.po.model.user.Users;

public interface SendMarketSmsService {
	
	Future<Boolean> allotSms(Set<Users> set,int marketSmsId,int SmsTempID);

}
