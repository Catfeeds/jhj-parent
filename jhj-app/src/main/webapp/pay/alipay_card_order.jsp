<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.alipay.config.*"%>
<%@ page import="com.alipay.util.*"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>叮当到家-支付宝支付</title>
	</head>
	<%
		////////////////////////////////////请求参数//////////////////////////////////////

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = new String(request.getParameter("orderNo").getBytes("ISO-8859-1"),"UTF-8");
		
        //订单名称，必填
        String subject = "叮当到家家庭服务";
		
        //付款金额，必填
        String total_fee = new String(request.getParameter("orderPay").getBytes("ISO-8859-1"),"UTF-8");
		//total_fee = "0.01";
        System.out.println("total_fee = " + total_fee);
        
        //订单类型
        String orderType = new String(request.getParameter("orderType").getBytes("ISO-8859-1"),"UTF-8");
		
     	// payOrderType 订单支付类型 0 = 订单支付 1= 充值支付 2 = 手机话费类充值 3 = 订单补差价
        String payOrderType = new String(request.getParameter("payOrderType").getBytes("ISO-8859-1"),"UTF-8");
		
        String host = "http://123.57.209.81";
        
        //收银台页面上，商品展示的超链接，必填
       String show_url = host + "/u/#!/user/mine-charge-way.html";
		
        //商品描述，可空， 传递payOrderType
        
        String body = payOrderType;
        
        String notifyUrl = host + "/jhj-app/pay/notify_alipay_card_order.jsp";
        
        String returnUrl = host + "/u/#!/user/mine-charge-pay-success.html";
        
		//////////////////////////////////////////////////////////////////////////////////
		
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", AlipayConfig.service);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_id", AlipayConfig.seller_id);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", AlipayConfig.payment_type);
		sParaTemp.put("notify_url", notifyUrl);
		sParaTemp.put("return_url", returnUrl);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("show_url", show_url);
		sParaTemp.put("body", body);
		//其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.2Z6TSk&treeId=60&articleId=103693&docType=1
        //如sParaTemp.put("参数名","参数值");

		
		//建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
		out.println(sHtmlText);
	%>
	<body>
	</body>
</html>
