package com.jhj.action.app;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.JDOMException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.meijia.utils.AppSubmit;
import com.meijia.wx.utils.XMLUtil;

@Controller
@RequestMapping("/app")
public class WxNotifyPeriodOrderController extends BaseController{

	
	@RequestMapping(value = "/wxpay-notify-periodOrder.do", method = RequestMethod.POST)
	public void wxNotifyPeriodOrder(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		// 获取请求数据
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		System.out.println("~~~~~~~~~~~~~~~~付款成功~~~~~~~~~");
		outSteam.close();
		inStream.close();
		String result = new String(outSteam.toByteArray(), "utf-8");
		Map<Object, Object> map = new HashMap<Object, Object>();
		try {
			map = XMLUtil.doXMLParse(result);
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		
		try {
			Map<String,String> params = new HashMap<String,String>();
			//用户手机号
			//通知ID
			String notify_id = new String(map.get("nonce_str").toString().getBytes("ISO-8859-1"),"UTF-8");
	
			params.put("notify_id", notify_id);
	
			//通知时间
			String notify_time = new String(map.get("time_end").toString().getBytes("ISO-8859-1"),"UTF-8");
	
			params.put("notify_time", notify_time);
	
			//商户订单号
			String out_trade_no = new String(map.get("out_trade_no").toString().getBytes("ISO-8859-1"),"UTF-8");
	
			params.put("order_no", out_trade_no);
	
			//买家账号
			String open_id  = new String(map.get("openid").toString().getBytes("ISO-8859-1"),"UTF-8");
			params.put("pay_account", open_id );
	
			//支付宝交易号
			String trade_no = new String(map.get("transaction_id").toString().getBytes("ISO-8859-1"),"UTF-8");
	
			params.put("trade_no", trade_no);
	
			//交易状态
			String trade_status = new String(map.get("result_code").toString().getBytes("ISO-8859-1"),"UTF-8");
	
			params.put("trade_status", trade_status);
	
			//支付方式 0 = 余额支付 1 = 支付宝 2 = 微信支付 3 = 智慧支付 4 = 上门刷卡（保留，站位）
			params.put("pay_type", "2");
	
			//通知参数
			//params.put("notify_params", notify_params.toString());
			System.out.println(params.toString());
			String url = "http://localhost/jhj-app/app/user/online_pay_period_notify.json";
			
			System.out.println(url);
			AppSubmit.appAliay(url, params);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getOutputStream().write("success".getBytes());
		
	}
	
	
	
}
