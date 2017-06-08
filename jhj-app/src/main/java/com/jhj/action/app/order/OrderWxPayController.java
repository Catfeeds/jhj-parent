package com.jhj.action.app.order;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.Constants;
import com.jhj.po.model.dict.DictCardType;
import com.jhj.po.model.order.OrderCards;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.period.PeriodOrder;
import com.jhj.po.model.share.OrderShare;
import com.jhj.service.dict.CardTypeService;
import com.jhj.service.order.OrderCardsService;
import com.jhj.service.order.OrderPriceExtService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.period.PeriodOrderService;
import com.jhj.service.share.OrderShareService;
import com.jhj.service.users.UserCouponsService;
import com.meijia.utils.ConfigUtil;
import com.meijia.utils.HttpClientUtil;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.wx.utils.MD5Util;
import com.meijia.wx.utils.ServletUtil;
import com.meijia.wx.utils.WxUtil;
import com.meijia.wx.utils.XMLUtil;

/**
 * 微信公众号支付订单
 *
 * @author jing
 *
 */
@Controller
@RequestMapping(value = "/app/")
public class OrderWxPayController extends BaseController {

	@Autowired
	private OrdersService orderService;

	@Autowired
	private OrderPricesService orderPricesService;

	private String tradeName = "叮当到家家庭服务";

	@Autowired
	private OrderCardsService orderCardsService;

	@Autowired
	private CardTypeService cardTypeService;
	
	@Autowired
	private OrderPriceExtService orderPriceExtService;
	
	@Autowired
	private PeriodOrderService periodOrderService;
	
	@Autowired
	private UserCouponsService userCouponsService;
	
	@Autowired 
	private OrderShareService orderShareService;

	@RequestMapping(value = "wxpay", method = RequestMethod.GET)
	public void jsPay(HttpServletRequest request, HttpServletResponse response,
	// 微信认证code
			@RequestParam(value = "code", required = false, defaultValue = "") String code,

			// 商务订单号
			@RequestParam("orderId") Long orderId,

			// 优惠劵ID
			@RequestParam(value = "userCouponId", required = false, defaultValue = "0") Long userCouponId,

			// payOrderType 订单支付类型 0 = 订单支付 1= 充值支付 2 = 手机话费类充值 3 = 订单补差价
			@RequestParam("payOrderType") Short payOrderType,

			// 订单类型 0 = 钟点工 1 = 深度保洁 2 = 助理订单 6= 话费充值类订单 7 = 订单补差价
			@RequestParam("orderType") Short orderType) throws Exception {

		System.out.println("=====WX pay Start======");
		System.out.println("code = " + code);
		String orderNo = "";
		Long userId = 0L;
		
		String shareUserId = request.getParameter("shareUserId");
		if(shareUserId!=null && !"".equals(shareUserId)){
			List<OrderShare> orderShareList = orderShareService.selectByShareId(Integer.parseInt(shareUserId));
			if(orderShareList!=null && orderShareList.size()>0){
				OrderShare orderShare = orderShareList.get(0);
				userCouponsService.shareSuccessSendCoupons(orderShare);
			}
		}
		
		if (userCouponId == null) userCouponId = 0L;

		String wxPay = "0";
		// 处理订单支付的情况
		if (payOrderType.equals(Constants.PAY_ORDER_TYPE_0)) {
			// 先做必要的验证
			Orders orders = orderService.selectByPrimaryKey(orderId);
			// 订单找不到的情况.
			if (orders == null || orders.getId().equals(0)) {
				// request.setAttribute("tips", "无效的订单");
				// ServletUtil.forward(request, response, errorUrl);
			}
			userId = orders.getUserId();

			orderNo = orders.getOrderNo();
			userId = orders.getUserId();

			BigDecimal orderPayNow = orderPricesService.getPayByOrder(orderId, userCouponId);
			wxPay = orderPayNow.toString();
			System.out.println("支付金额="+wxPay+"-----------------------------");
			tradeName = "叮当到家家庭服务";
		}

		// 处理充值卡充值的情况
		if (payOrderType.equals(Constants.PAY_ORDER_TYPE_1)) {
			OrderCards orderCard = orderCardsService.selectByPrimaryKey(orderId);
			Long cardType = orderCard.getCardType();
			orderNo = orderCard.getCardOrderNo();
			System.out.println("orderNo = " + orderNo);
			userId = orderCard.getUserId();
			DictCardType dictCardType = cardTypeService.selectByPrimaryKey(cardType);
			wxPay = dictCardType.getCardPay().toString();
			BigDecimal cardPay = dictCardType.getCardPay();
			BigDecimal p1 = new BigDecimal(100);
			BigDecimal p2 = MathBigDecimalUtil.mul(cardPay, p1);
			BigDecimal orderPayNow = MathBigDecimalUtil.round(p2, 0);
			wxPay = orderPayNow.toString();
			tradeName = "叮当到家家庭服务";
		}

		// payOrderType 订单支付类型 0 = 订单支付 1= 充值支付 2 = 手机话费类充值 3 = 订单补差价
		if (payOrderType.equals(Constants.PAY_ORDER_TYPE_3)) {
			OrderPriceExt orderPriceExt = orderPriceExtService.selectByPrimaryKey(orderId);

			// 订单找不到的情况.
			if (orderPriceExt == null || orderPriceExt.getId().equals(0)) {

			}
			
			userId = orderPriceExt.getUserId();
			orderNo = orderPriceExt.getOrderNoExt();
			// 实际支付金额

			BigDecimal p1 = new BigDecimal(100);
			BigDecimal p2 = MathBigDecimalUtil.mul(orderPriceExt.getOrderPay(), p1);
			BigDecimal orderPayNow = MathBigDecimalUtil.round(p2, 0);
			wxPay = orderPayNow.toString();
			
			tradeName = "叮当到家家庭服务";
		}
		
		//定制支付
		if (payOrderType.equals(Constants.PAY_PERIOD_ORDER_TYPE_4)) {
			PeriodOrder periodOrder = periodOrderService.selectByPrimaryKey(orderId.intValue());
			
			userId = periodOrder.getUserId().longValue();
			orderNo = periodOrder.getOrderNo();
			// 实际支付金额

			BigDecimal p1 = new BigDecimal(100);
			BigDecimal p2 = MathBigDecimalUtil.mul(periodOrder.getOrderPrice(), p1);
			BigDecimal orderPayNow = MathBigDecimalUtil.round(p2, 0);
			wxPay = orderPayNow.toString();
			
			tradeName = "叮当到家家庭服务";
		}


		// 测试
		// wxPay = "1";

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
			// request.setAttribute("tips", "微信验证失败,请重新支付!");
			// ServletUtil.forward(request, response, tipsUrl);
		}

		if (!WxUtil.checkAccessToken(accessToken, openid)) {
			// request.setAttribute("tips", "微信验证失败,请重新支付!");
			// ServletUtil.forward(request, response, tipsUrl);
		}

		String appId = WxUtil.appId;
		// TODO 手机话费
		String notifyUrl = WxUtil.getNotifyUrl(Short.valueOf(payOrderType));

		// 签名参数
		String[] s = new String[11];
		s[0] = "appid=" + appId;
		s[1] = "nonce_str=" + nonceStr;
		s[2] = "body=" + tradeName;
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
		// sign = MD5Util.MD5Encode(sign + "key=" + WxUtil.wxKey,
		// "GBK").toUpperCase();
		sign = MD5Util.MD5Encode(sign + "key=" + WxUtil.wxKey, "utf-8").toUpperCase();
		String xml = "";
		xml += "<xml>";
		xml += "<appid>" + appId + "</appid>";
		xml += "<mch_id>" + WxUtil.mchId + "</mch_id>";
		xml += "<nonce_str>" + nonceStr + "</nonce_str>";
		xml += "<sign>" + sign + "</sign>";
		xml += "<body>" + tradeName + "</body>";
		xml += "<out_trade_no>" + orderNo + "</out_trade_no>";
		xml += "<total_fee>" + wxPay + "</total_fee>";
		xml += "<spbill_create_ip>" + request.getRemoteAddr() + "</spbill_create_ip>";
		xml += "<notify_url>" + notifyUrl + "</notify_url>";
		xml += "<trade_type>JSAPI</trade_type>";
		xml += "<openid>" + openid + "</openid>";
		xml += "<attach><![CDATA[" + userId.toString() + "]]></attach>";
		xml += "</xml>";

		String result = HttpClientUtil.post_xml(WxUtil.payUrl, xml);

		if (result.indexOf("该订单已支付") >= 0) {
			// request.setAttribute("tips", "订单已经支付过!");
			// ServletUtil.forward(request, response, tipsUrl);
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
			paySign = MD5Util.MD5Encode(paySign + "key=" + WxUtil.wxKey, "UTF-8").toUpperCase();
			// paySign = MD5Util.MD5Encode(f, "utf-8").toUpperCase();

			Map<String, String> jumpParam = new HashMap<String, String>();

			jumpParam.put("appId", WxUtil.appId);
			jumpParam.put("package", prepayId);
			jumpParam.put("timeStamp", timeStamp);
			jumpParam.put("nonceStr", nonceStr1);
			jumpParam.put("tradeno", tradeno);
			jumpParam.put("out_trade_no", orderNo);
			jumpParam.put("paySign", paySign);
			jumpParam.put("signType", "MD5");
			jumpParam.put("userId", userId.toString());
			// 下面为支付成功后的参数
			
			String payCallBackServiceHost = Constants.PAY_CALLBACK_SERVICE_HOST;
			if (ConfigUtil.getInstance().getRb().getString("debug").equals("true")) {
				payCallBackServiceHost = Constants.PAY_CALLBACK_SERVICE_HOST_DEBUG;
			}
			
			String jumpUrl = payCallBackServiceHost + "/jhj-app/wx-pay.jsp?showwxpaytitle=1";
			jumpUrl += "&appId=" + WxUtil.appId;
			jumpUrl += "&package=" + prepayId;
			jumpUrl += "&timeStamp=" + timeStamp;
			jumpUrl += "&nonceStr=" + nonceStr1;
			jumpUrl += "&tradeno=" + tradeno;
			jumpUrl += "&out_trade_no=" + orderNo;
			jumpUrl += "&paySign=" + paySign;
			jumpUrl += "&signType=MD5";
			jumpUrl += "&paySign=" + paySign;
			jumpUrl += "&userId=" + userId;
			jumpUrl += "&orderType=" + orderType;
			jumpUrl += "&payOrderType=" + payOrderType;
			jumpUrl += "&orderId=" + orderId;
			ServletUtil.jump(request, response, jumpUrl);
		} catch (Exception e) {
			e.printStackTrace();
			// request.setAttribute("tips", "微信验证失败,请重新支付!");
			// ServletUtil.forward(request, response, errorUrl);
		}
	}
}
