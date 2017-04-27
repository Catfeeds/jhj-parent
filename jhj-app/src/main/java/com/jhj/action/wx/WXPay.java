package com.jhj.action.wx;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.jhj.common.Constants;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.period.PeriodOrder;
import com.jhj.service.order.OrderPriceExtService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.period.PeriodOrderService;
import com.meijia.utils.HttpClientUtil;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.wx.utils.MD5Util;
import com.meijia.wx.utils.ServletUtil;
import com.meijia.wx.utils.WxUtil;
import com.meijia.wx.utils.XMLUtil;

/**
 * 微信支付订单
 *
 * @author jing
 *
 */
public class WXPay extends HttpServlet {

	@Autowired
	private OrdersService orderService;

	@Autowired
	private OrderPricesService orderPricesService;
	
	@Autowired
	private OrderPriceExtService orderPriceExtService;
	
	@Autowired
	private PeriodOrderService periodOrderService;
	
	private String successUrl = "wx-pay-pre.jsp";

	private String errorUrl = "wx-pay-fail.jsp";

	private String tipsUrl = "wx-pay-tips.jsp";

	private String finishUrl = "wx-pay-finish.jsp";
	
	private String tradeName = "叮当到家";
	
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


		// 微信认证code
		String code = request.getParameter("code");

		// 商务订单号
		String orderIdStr = request.getParameter("orderId");
		if (orderIdStr == null) {
			request.setAttribute("tips", "无效的订单");
			ServletUtil.forward(request, response, errorUrl);
		}
		
		Long orderId = Long.valueOf(orderIdStr);
		
		String orderNo = request.getParameter("orderNo");
		Long userId = 0L;
		// 订单类型 0 = 订单支付 1= 充值卡充值  2=手机话费充值 7 = 订单补差价
		String payOrderType = request.getParameter("pay_order_type");
		
		if (payOrderType == null) payOrderType = "0";

		String wxPay = "0";
		// 处理订单支付的情况
		if (payOrderType.equals("0")) {
			// 先做必要的验证
			Orders orders = orderService.selectByPrimaryKey(orderId);
			// 订单找不到的情况.
			if (orders == null || orders.getId().equals(0)) {
				request.setAttribute("tips", "无效的订单");
				ServletUtil.forward(request, response, errorUrl);
			}
			// 订单已经支付过，不需要重复支付
			if (orders.getOrderStatus().equals(Constants.ORDER_STATUS_4)) {
				request.setAttribute("tips", "订单已经支付过，请不要重复支付!");
				ServletUtil.forward(request, response, tipsUrl);
			}
			orderNo = orders.getOrderNo();
			userId = orders.getUserId();

			BigDecimal orderPayNow  = orderPricesService.getPayByOrder(orderId, 0L);
			wxPay = orderPayNow.toString();
		}
		
		//处理充值卡充值的情况
		if (payOrderType.equals("1")) {
			
		}
		
		//处理 手机话费 充值的 情况
		
		if(payOrderType.equals("2")){
			// 先做必要的验证
			Orders orders = orderService.selectByPrimaryKey(orderId);
			// 订单找不到的情况.
			if (orders == null || orders.getId().equals(0)) {
				request.setAttribute("tips", "无效的订单");
				ServletUtil.forward(request, response, errorUrl);
			}
			// 订单已经支付过，不需要重复支付
			if (orders.getOrderStatus().equals(Constants.ORDER_STATUS_14)) {
				request.setAttribute("tips", "订单已经支付过，请不要重复支付!");
				ServletUtil.forward(request, response, tipsUrl);
			}
			orderNo = orders.getOrderNo();
			userId = orders.getUserId();

			BigDecimal orderPayNow  = orderPricesService.getPayByOrder(orderId, 0L);
			wxPay = orderPayNow.toString();
		}
		
		// payOrderType 订单支付类型 0 = 订单支付 1= 充值支付 2 = 手机话费类充值 3 = 订单补差价
		if(payOrderType.equals(Constants.PAY_ORDER_TYPE_3)) {
			OrderPriceExt orderPriceExt = orderPriceExtService.selectByOrderNoExt(orderNo);
			
			// 订单找不到的情况.
			if (orderPriceExt == null || orderPriceExt.getId().equals(0)) {
				request.setAttribute("tips", "无效的订单");
				ServletUtil.forward(request, response, errorUrl);
			}
			
			// 订单已经支付过，不需要重复支付
			if (orderPriceExt.getOrderStatus() == 2) {
				request.setAttribute("tips", "订单已经支付过，请不要重复支付!");
				ServletUtil.forward(request, response, tipsUrl);
			}
			
			// 实际支付金额
			
			BigDecimal p1 = new BigDecimal(100);
			BigDecimal p2 = MathBigDecimalUtil.mul(orderPriceExt.getOrderPay(), p1);
			BigDecimal orderPayNow = MathBigDecimalUtil.round(p2, 0);
			wxPay = orderPayNow.toString();
		}
		
		if(payOrderType.equals("4")) {
			PeriodOrder periodOrder = periodOrderService.selectByOrderNo(orderNo);
			
			// 订单找不到的情况.
			if (periodOrder == null || periodOrder.getId().equals(0)) {
				request.setAttribute("tips", "无效的订单");
				ServletUtil.forward(request, response, errorUrl);
			}
			
			// 订单已经支付过，不需要重复支付
			if (periodOrder.getOrderStatus() == 2) {
				request.setAttribute("tips", "订单已经支付过，请不要重复支付!");
				ServletUtil.forward(request, response, tipsUrl);
			}
			
			// 实际支付金额
			BigDecimal p1 = new BigDecimal(100);
			BigDecimal p2 = MathBigDecimalUtil.mul(periodOrder.getOrderPrice(), p1);
			BigDecimal orderPayNow = MathBigDecimalUtil.round(p2, 0);
			wxPay = orderPayNow.toString();
		}
		
		
		//测试
//		wxPay = "1";

		// 临时订单号
		String tradeno = System.currentTimeMillis() + "" + (int) (Math.random() * 1000000);
		String timeStamp = TimeStampUtil.getNow().toString();
		String nonceStr = WxUtil.getNonceStr();

		Map<String, Object> access = WxUtil.getAccessToken(code);
		String refreshToken = access.get("refresh_token").toString();

		Map<String, Object> refresh = WxUtil.getRefreshToken(refreshToken);
		String openid = "";
		openid = refresh.get("openid").toString();
		String accessToken = refresh.get("access_token").toString();

		if (openid == null || openid.equals("")) {
			request.setAttribute("tips", "微信验证失败,请重新支付!");
			ServletUtil.forward(request, response, tipsUrl); 	
		}

		if (!WxUtil.checkAccessToken(accessToken, openid)) {
			request.setAttribute("tips", "微信验证失败,请重新支付!");
			ServletUtil.forward(request, response, tipsUrl);
		}
			
		
		String appId = WxUtil.appId;
		
		//TODO 手机充值,单独的 通知
		String notifyUrl = WxUtil.getNotifyUrl(Short.valueOf(payOrderType));

		// 签名参数
		String[] s = new String[11];
		s[0] = "appid=" + appId;
		s[1] = "nonce_str=" + nonceStr;
		s[2] = "body=" + userId.toString();
		s[3] = "out_trade_no=" + orderNo;
		s[4] = "total_fee=" + wxPay;
		s[5] = "spbill_create_ip=" + request.getRemoteAddr();
		s[6] = "notify_url=" + notifyUrl;
		s[7] = "trade_type=JSAPI";
		s[8] = "mch_id=" + WxUtil.mchId;
		s[9] = "openid=" + openid;
		s[10] = "attach=" + userId.toString();
		Arrays.sort(s);
		String sign = "";
		for (String string : s) {
			sign += string + "&";
		}
		sign = MD5Util.MD5Encode(sign + "key=" + WxUtil.wxKey, "GBK")
				.toUpperCase();

		String xml = "";
		xml += "<xml>";
		xml += "<appid>" + appId + "</appid>";
		xml += "<mch_id>" + WxUtil.mchId + "</mch_id>";
		xml += "<nonce_str>" + nonceStr + "</nonce_str>";
		xml += "<sign>" + sign + "</sign>";
		xml += "<body><![CDATA[" + userId.toString() + "]]></body>";
		xml += "<out_trade_no>" + orderNo + "</out_trade_no>";
		xml += "<total_fee>" + wxPay + "</total_fee>";
		xml += "<spbill_create_ip>" + request.getRemoteAddr()+ "</spbill_create_ip>";
		xml += "<notify_url>" + notifyUrl+ "</notify_url>";
		xml += "<trade_type>JSAPI</trade_type>";
		xml += "<openid>" + openid + "</openid>";
		xml += "<attach><![CDATA[" + userId.toString() + "]]></attach>";
		xml += "</xml>";
		
		
		String result = HttpClientUtil.post_xml(WxUtil.payUrl, xml);
		
		if (result.indexOf("该订单已支付") >=0) {
			request.setAttribute("tips", "订单已经支付过!");
			ServletUtil.forward(request, response, tipsUrl);			
		}

		String prepayId = "";
		String nonceStr1 = "";
		try {
			Map resultMap = XMLUtil.doXMLParse(result);
			sign = resultMap.get("sign").toString();
			prepayId = resultMap.get("prepay_id").toString();
			nonceStr1 = resultMap.get("nonce_str").toString();
		

			String paySign = "";
			String b[] = new String[5];
			b[0] = "appId=" + appId;
			b[1] = "timeStamp=" + timeStamp;
			b[2] = "nonceStr=" + nonceStr1;
			b[3] = "package=prepay_id=" + prepayId;
			b[4] = "signType=MD5";
			Arrays.sort(b);
			for (String string : b) {
				paySign += string + "&";
			}
			paySign = MD5Util.MD5Encode(paySign + "key=" + WxUtil.wxKey,
					"UTF-8").toUpperCase();

			request.setAttribute("appId", WxUtil.appId);
			request.setAttribute("package", prepayId);
			request.setAttribute("timeStamp", timeStamp);
			request.setAttribute("nonceStr", nonceStr1);
			request.setAttribute("tradeno", tradeno);
			request.setAttribute("out_trade_no", orderNo);
			request.setAttribute("paySign", paySign);
			request.setAttribute("signType", "MD5");
			request.setAttribute("userId", userId);
			//下面为支付成功后的参数
			ServletUtil.forward(request, response, successUrl);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("tips", "微信验证失败,请重新支付!");
			ServletUtil.forward(request, response, errorUrl);
		}
	}
}
