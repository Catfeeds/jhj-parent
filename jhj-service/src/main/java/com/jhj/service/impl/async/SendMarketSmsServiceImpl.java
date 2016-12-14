package com.jhj.service.impl.async;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
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

	@Async("allotSms")
	public Future<Boolean> allotSms(Set<Users> set,int marketSmsId,int SmsTempID) {
		
		if(set == null) return null;
		
		Gson gson=new Gson();
		String tempID = gson.toJson(SmsTempID);
		
		Iterator<Users> iterator = set.iterator();
	
		String[] content = new String[]{""};
		while(iterator.hasNext()){
			Users u = iterator.next();
			String mobile = gson.toJson(u.getMobile());
			HashMap<String, String> result = SmsUtil.SendSms(mobile, tempID,content);
			if(!result.get("statusCode").equals("000000")){
				MarketSmsFail marketSmsFail=new MarketSmsFail();
				marketSmsFail.setMarketSmsId(marketSmsId);
				marketSmsFail.setUserId(u.getId());
				marketSmsFail.setMobile(u.getMobile());
				marketSmsFail.setSmsResult(result.get("statusCode"));
				marketSmsFail.setSmsMsg(result.get("msg"));
				marketSmsFail.setAddTime(TimeStampUtil.getNowSecond());
				marketSmsFailMapper.insert(marketSmsFail);
			}
			System.out.println(result.get("statusCode")+"-----"+result.get("msg"));
		}
		
		return  new AsyncResult<Boolean>(true);
	}

}
