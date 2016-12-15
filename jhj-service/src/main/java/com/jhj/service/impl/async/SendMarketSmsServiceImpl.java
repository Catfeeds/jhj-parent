package com.jhj.service.impl.async;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.market.MarketSmsFailMapper;
import com.jhj.po.model.market.MarketSmsFail;
import com.jhj.po.model.user.Users;
import com.jhj.service.async.SendMarketSmsService;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class SendMarketSmsServiceImpl implements SendMarketSmsService{
	
	@Autowired
	private MarketSmsFailMapper marketSmsFailMapper;

	private Set<String> successSet = Collections.synchronizedSet(new HashSet<String>());
	private Set<String> failSet = Collections.synchronizedSet(new HashSet<String>());
	
	@Async
	public Future<Boolean> allotSms(Users user,Integer marketSmsId,String SmsTempID,String[] content) {
		
		HashMap<String, String> result = SmsUtil.SendSms(user.getMobile(), SmsTempID,content);
		if(result.get("statusCode").equals("000000")){
			successSet.add(user.getMobile());
		}
		if(!result.get("statusCode").equals("000000")){
			failSet.add(user.getMobile());
			MarketSmsFail marketSmsFail=new MarketSmsFail();
			marketSmsFail.setMarketSmsId(marketSmsId);
			marketSmsFail.setUserId(user.getId());
			marketSmsFail.setMobile(user.getMobile());
			marketSmsFail.setSmsResult(result.get("statusCode"));
			marketSmsFail.setSmsMsg(result.get("msg"));
			marketSmsFail.setAddTime(TimeStampUtil.getNowSecond());
			marketSmsFailMapper.insert(marketSmsFail);
		}
		System.out.println(result.get("statusCode")+"--"+result.get("msg")+"--"+user.getMobile()+"--"+SmsTempID);
		
		return  new AsyncResult<Boolean>(true);
	}
	
	public int successNum() {
		return successSet.size();
	}
	
	public int failNum(){
		return failSet.size();
	}

}
