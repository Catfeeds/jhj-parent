package com.jhj.action.app;

import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.meijia.utils.vo.AppResultData;
import com.meijia.wx.utils.Sha1Util;
import com.meijia.wx.utils.WxUtil;

@Controller
@RequestMapping("/app")
public class WxShare extends BaseController{
	
//	@Autowired
//	private OrderShareService orderShareService;
	
	@RequestMapping(value="/wxShare.json", method=RequestMethod.POST)
	public AppResultData<Object> wxShare(String url,HttpServletRequest request) throws Exception{
		
		SortedMap<String, String> map = new TreeMap<String, String>();
		
		String timestamp = String.valueOf(System.currentTimeMillis()/1000);
		String nonceStr = Sha1Util.getNonceStr();
		String jsapiTicket = WxUtil.getTicket(WxUtil.getAccess_token());
		
		map.put("timestamp", timestamp);
		map.put("noncestr", nonceStr);
		map.put("jsapi_ticket", jsapiTicket);
		map.put("url", url);
		
		String string1 = "jsapi_ticket=" + jsapiTicket +
                 "&noncestr=" + nonceStr +
                 "&timestamp=" + timestamp +
                 "&url=" + url;
		
		map.put("signature", Sha1Util.getSha1(string1));
		
		map.put("appId", "wx1da3f16a433d8bd8");
		return new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG,map);
	}
	
	/*@RequestMapping(value="/saveOrderShare.json", method=RequestMethod.POST)
	public AppResultData<Object> saveOrderShare(
			@RequestParam("user_id") Integer userId,
			@RequestParam("mobile") String mobile){
		
		OrderShare orderShare = new OrderShare();
		orderShare.setUserId(userId);
		orderShare.setSendCouponsId(0);
		orderShare.setMobile(mobile);
		orderShare.setAddTime(DateUtil.getNowOfDate());
		
		orderShareService.insert(orderShare);
		return new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG,"");
	}*/
	
}
