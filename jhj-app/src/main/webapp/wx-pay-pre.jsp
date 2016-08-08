<%@page import="com.meijia.wx.utils.WxUtil"%>
<%@page import="java.net.URLEncoder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String orderId = request.getParameter("orderId");
String userCouponId = request.getParameter("userCouponId");
String orderType = request.getParameter("orderType");
String payOrderType = request.getParameter("payOrderType");
String fromUrl = request.getParameter("fromUrl");
String successUrl = request.getParameter("successUrl");

String redirectUrl = "http://www.jia-he-jia.com/jhj-app/app/wxpay";
redirectUrl += "?orderId=" + orderId;
redirectUrl += "&userCouponId=" + userCouponId;
redirectUrl += "&orderType=" + orderType;
redirectUrl += "&payOrderType=" + payOrderType;
redirectUrl = URLEncoder.encode(redirectUrl,"utf-8");

String wxAccessUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+WxUtil.appId+"&redirect_uri="+redirectUrl+"&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
System.out.println("wx-pay-pre.jsp ==" + wxAccessUrl);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>微信站-在线支付</title>
<meta content="1 days" name="revisit-after" />
<meta content="" name="keywords" />
<meta content="" name="description" />
<meta content="width=device-width,height=device-height,target-densitydpi=284,maximum-scale=1.0, user-scalable=no" name="viewport" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<meta content="telephone=no" name="format-detection" />
<meta content="false" id="twcClient" name="twcClient" />
<link rel="stylesheet" type="text/css" href="/css/css.css"/>
<script language="javascript" src="http://res.mail.qq.com/mmr/static/lib/js/jquery.js"></script>
<script language="javascript" src="http://res.mail.qq.com/mmr/static/lib/js/lazyloadv3.js"></script>
        <script language="javascript">
            function auto_remove(img){
                div=img.parentNode.parentNode;div.parentNode.removeChild(div);
                img.onerror="";
                return true;
            }

            function changefont(fontsize){
                if(fontsize < 1 || fontsize > 4)return;
                $('#content').removeClass().addClass('fontSize' + fontsize);
            }
			$(function(){
				location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=<%=WxUtil.appId%>&redirect_uri=<%=redirectUrl%>&code=CODE&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
			});
</script>
</head>
<body>

</body>
</html>
