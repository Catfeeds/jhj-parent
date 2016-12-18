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

import com.jhj.po.dao.market.MarketSmsLogMapper;
import com.jhj.po.model.market.MarketSms;
import com.jhj.po.model.market.MarketSmsLog;
import com.jhj.po.model.user.Users;
import com.jhj.service.async.SendMarketSmsService;
import com.jhj.service.market.MarketSmsService;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.TimeStampUtil;

@Service
@Async 
public class SendMarketSmsServiceImpl implements SendMarketSmsService{
	
	@Autowired
	private MarketSmsLogMapper marketSmsLogMapper;
	
	@Autowired
	private MarketSmsService marketSmsService;

	private Set<String> successSet = Collections.synchronizedSet(new HashSet<String>());
	private Set<String> failSet = Collections.synchronizedSet(new HashSet<String>());
	
	@Async
	public Future<Boolean> allotSms(Users user,Integer marketSmsId,String SmsTempID,String[] content) {
		
		HashMap<String, String> result = SmsUtil.SendSms(user.getMobile(), SmsTempID,content);
		
		
		successSet.add(user.getMobile());
		MarketSmsLog marketSmsFail=new MarketSmsLog();
		marketSmsFail.setMarketSmsId(marketSmsId);
		marketSmsFail.setUserId(user.getId());
		marketSmsFail.setMobile(user.getMobile());
		marketSmsFail.setSmsResult(result.get("statusCode"));
		marketSmsFail.setSmsMsg(result.get("msg"));
		marketSmsFail.setAddTime(TimeStampUtil.getNowSecond());
		marketSmsLogMapper.insert(marketSmsFail);
		
		MarketSms marketSms = marketSmsService.selectByPrimaryKey(marketSmsId);
		String statusCode = result.get("statusCode");
		

	    Integer totalSended = marketSms.getTotalSended();
	    totalSended = totalSended + 1;
	    
	    Integer totalFail = marketSms.getTotalFail();
	    if (!statusCode.equals("000000")) {
	    	totalFail = totalFail + 1;
	    	marketSms.setTotalFail(totalFail);
	    }
	    marketSms.setTotalSended(totalSended);
	    
	    marketSmsService.updateByPrimaryKeySelective(marketSms);
	
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
