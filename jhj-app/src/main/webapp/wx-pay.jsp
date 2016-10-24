<%@page import="java.net.URLEncoder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title></title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script src="/jhj-app/js/jquery.js"></script>
<script src="/jhj-app/js/lazyloadv3.js"></script>
<style>
body {
	margin: 0;
	padding: 0;
	background: #eae9e6;
}

body, p, table, td, th {
	font-size: 14px;
	font-family: helvetica, Arial, Tahoma;
}

h1 {
	font-family: Baskerville, HelveticaNeue-Bold, helvetica, Arial, Tahoma;
}

a {
	text-decoration: none;
	color: #385487;
}

.container {

}

.title {

}

#content {
	padding: 30px 20px 20px;
	color: #111;
	box-shadow: 0 1px 4px #ccc;
	background: #f7f2ed;
}

.seeAlso {
	padding: 15px 20px 30px;
}

.headpic div {
	margin: 20px 0 0;
}

.headpic img {
	display: block;
}

.title h1 {
	font-size: 22px;
	font-weight: bold;
	padding: 0;
	margin: 0;
	line-height: 1.2;
	color: #1f1f1f;
}

.title p {
	color: #aaa;
	font-size: 12px;
	margin: 5px 0 0;
	padding: 0;
	font-weight: bold;
}

.pic {
	margin: 20px 0;
}

.articlecontent img {
	display: block;
	clear: both;
	box-shadow: 0px 1px 3px #999;
	margin: 5px auto;
}

.articlecontent p {
	text-indent: 2em;
	font-family: Georgia, helvetica, Arial, Tahoma;
	line-height: 1.4;
	font-size: 16px;
	margin: 20px 0;
}

.seeAlso h3 {
	font-size: 16px;
	color: #a5a5a5;
}

.seeAlso ul {
	margin: 0;
	padding: 0;
}

.seeAlso li {
	font-size: 16px;
	list-style-type: none;
	border-top: 1px solid #ccc;
	padding: 2px 0;
}

.seeAlso li a {
	border-bottom: none;
	display: block;
	line-height: 1.1;
	padding: 13px 0;
}

.clr {
	clear: both;
	height: 1px;
	overflow: hidden;
}

.fontSize1 .title h1 {
	font-size: 20px;
}

.fontSize1 .articlecontent p {
	font-size: 14px;
}

.fontSize1 .weibo .nickname, .fontSize1 .weibo .comment {
	font-size: 11px;
}

.fontSize1 .moreOperator {
	font-size: 14px;
}

.fontSize2 .title h1 {
	font-size: 22px;
}

.fontSize2 .articlecontent p {
	font-size: 16px;
}

.fontSize2 .weibo .nickname, .fontSize2 .weibo .comment {
	font-size: 13px;
}

.fontSize2 .moreOperator {
	font-size: 16px;
}

.fontSize3 .title h1 {
	font-size: 24px;
}

.fontSize3 .articlecontent p {
	font-size: 18px;
}

.fontSize3 .weibo .nickname, .fontSize3 .weibo .comment {
	font-size: 15px;
}

.fontSize3 .moreOperator {
	font-size: 18px;
}

.fontSize4 .title h1 {
	font-size: 26px;
}

.fontSize4 .articlecontent p {
	font-size: 20px;
}

.fontSize4 .weibo .nickname, .fontSize4 .weibo .comment {
	font-size: 16px;
}

.fontSize4 .moreOperator {
	font-size: 20px;
}

.jumptoorg {
	display: block;
	margin: 16px 0 16px;
}

.jumptoorg a {

}

.moreOperator a {
	color: #385487;
}

.moreOperator .share {
	border-top: 1px solid #ddd;
}

.moreOperator .share a {
	display: block;
	border: 1px solid #ccc;
	border-radius: 4px;
	margin: 20px 0;
	border-bottom-style: solid;
	background: #f8f7f1;
	color: #000;
}

.moreOperator .share a span {
	display: block;
	padding: 10px 10px;
	border-radius: 4px;
	text-align: center;
	border-top: 1px solid #eee;
	border-bottom: 1px solid #eae9e3;
	font-weight: bold;
}

.moreOperator .share a:hover, .moreOperator .share a:active {
	background: #efedea;
}

@media only screen and (-webkit-min-device-pixel-ratio: 2) {
}
</style>
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




	 // 当微信内置浏览器完成内部初始化后会触发WeixinJSBridgeReady事件。
	 document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
		  //公众号支付
		  jQuery('a#getBrandWCPayRequest').click(function(e){
	          WeixinJSBridge.invoke('getBrandWCPayRequest',{
	                                "appId" : "<%= request.getParameter("appId")%>", //公众号名称，由商户传入
	                                "timeStamp" : "<%= request.getParameter("timeStamp") %>", //时间戳
	                                "nonceStr" : "<%= request.getParameter("nonceStr") %>", //随机串
	                                "package" : "prepay_id=<%= request.getParameter("package") %>",//扩展包
	                                "signType" : "<%= request.getParameter("signType") %>", //微信签名方式:1.sha1
	                                "paySign" : "<%= request.getParameter("paySign") %>" //微信签名
	                                },function(res){
									
	                                var wxAppUrl = "http://www.jia-he-jia.com/u/#!/";
	                                var payOrderType = <%=request.getParameter("payOrderType")%>;
	                                var orderType = <%=request.getParameter("orderType")%>;
	                                var orderNo = <%=request.getParameter("out_trade_no")%>;
	                                var orderId = <%=request.getParameter("orderId")%>;
	                                
	                                
	                                if(res.err_msg == "get_brand_wcpay_request:ok" ) {
	                                	
	                                	// 微信支付成功后,跳转到对应的 成功页面
	                                	if (payOrderType == 0) {
	                                		
		                                	successUrl = wxAppUrl + "order/order-pay-success.html";
		                                	successUrl+= "?order_no="+orderNo;
		                                	successUrl+= "&order_type="+orderType;
	                                	}
										
	                                	if (payOrderType == 1 ) {
		                                	successUrl = wxAppUrl + "user/charge/mine-charge-pay-success.html";
		                                	successUrl+= "?order_no="+orderNo;
		                                	successUrl+= "&orderType="+orderType;	 	                                		
	                                	}
	                                	
	                                	
	                                	// 话费充值类订单
                                		if(payOrderType == 2){
                                			successUrl = wxAppUrl + "user/serviceCharge/order-service-charge-success.html";
                                			successUrl+= "?order_no="+orderNo;
		                                	successUrl+= "&order_type="+orderType;	
                                		}
	                                	
	                                	window.location.href = successUrl;
	                                		
	                                } else if(res.err_msg=="get_brand_wcpay_request:cancel") {
										
	                                	var fromUrl = "";
	                                	alert("payOrderType = " + payOrderType);
	                                	if (payOrderType == 0 || payOrderType == 3) {
	                                		fromUrl = "order/order-pay.html";
	                                		fromUrl+= "?order_no="+orderNo;
	                                		fromUrl+= "&order_type="+orderType;
	                                	}
	                                	
	                                	if (payOrderType == 1) {
	                                		fromUrl = "user/charge/mine-charge-list.html";
	                                	}
	                                	
	                                	if(payOrderType == 2){
	                                		fromUrl = "user/serviceCharge/order-service-charge.html";
	                                	}
	                                	alert(fromUrl);
	                                	window.location.href = wxAppUrl + fromUrl;
	                                }else{
	                                	var fromUrl = "";
	                                	if (payOrderType == 0 || payOrderType == 3) {
	                                		fromUrl = wxAppUrl + "order/order-pay.html";
	                                		fromUrl+= "?order_no="+orderNo;
	                                		fromUrl+= "&order_type="+orderType;
	                                	}
	                                	
	                                	if (payOrderType == 1) {
	                                		fromUrl = "user/charge/mine-charge-list.html";
	                                	}
	                                	
	                                	if(payOrderType == 2){
	                                		fromUrl = "user/serviceCharge/order-service-charge.html";
	                                	}
	                                	
                                		window.location.href = wxAppUrl + fromUrl;
	                                }
	                                // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
	                                //因此微信团队建议，当收到ok返回时，向商户后台询问是否收到交易成功的通知，若收到通知，前端展示交易成功的界面；若此时未收到通知，商户后台主动调用查询订单接口，查询订单的当前状态，并反馈给前端展示相应的界面。
	         						});

	    		});
      WeixinJSBridge.log('yo~ ready.');
      $("#getBrandWCPayRequest").trigger("click");
       }, false)

if(jQuery){
    jQuery(function(){

       var width = jQuery('body').width() * 0.87;
       jQuery('img').error(function(){
                           var self = jQuery(this);
                           var org = self.attr('data-original1');
                           self.attr("src", org);
                           self.error(function(){
                                      auto_remove(this);
                                      });
                           });
       jQuery('img').each(function(){
                          var self = jQuery(this);
                          var w = self.css('width');
                          var h = self.css('height');
                          w = w.replace('px', '');
                          h = h.replace('px', '');
                          if(w <= width){
                          return;
                          }
                          var new_w = width;
                          var new_h = Math.round(h * width / w);
                          self.css({'width' : new_w + 'px', 'height' : new_h + 'px'});
                          self.parents('div.pic').css({'width' : new_w + 'px', 'height' : new_h + 'px'});
                          });
       });
}
</script>
</head>
<body>
	<form name="form1" target="_blank"></form>
	<div class="WCPay">
		<a id="getBrandWCPayRequest" href="javascript:void(0);"><h1
				class="title"></h1></a>
	</div>
</body>
</html>
