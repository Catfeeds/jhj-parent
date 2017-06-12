myApp.onPageInit('period-order-pay', function(page) {
	// payOrderType 订单支付类型 0 = 订单支付 1= 充值支付 2 = 手机话费类充值 3 = 订单补差价 4 = 定制支付
	
	var userId = localStorage['user_id'];
	var orderNo = page.query.order_no;
	
	$$("#userId").val(userId);
	$$("#orderNo").val(orderNo);
	var periodOrderPay = sessionStorage.getItem("periodPayMoney");
	$$("#periodOrderMoneyStrLi").html("￥"+periodOrderPay+"元");
	$$("#periodOrderPayStrLi").html("￥"+periodOrderPay+"元");
		
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "user/get_userinfo.json?user_id="+userId,
		dataType : "json",
		cache : true,
		async : false,
		success : function(data) {
			var restMoney = data.data.rest_money;
			$$("#restMoney").val(restMoney);
			$$("#restMoneyStr").html("余额"+restMoney+"元");
		}
	})
	
	//默认支付类型
	var orderPayType = 0;
	
	var isWx = isWeiXin();
	
	if (isWx) {
		$$("#select-wxpay").css("display", "block");
		$$("#select-alipay").css("display", "none");
		$$('#img-restpay').attr("src","img/dingdan-pay/dingdan-pay2.png");
		$$('#img-alipay').attr("src","img/dingdan-pay/dingdan-pay2.png");
		$$('#img-wxpay').attr("src","img/dingdan-pay/dingdan-pay1.png");
		$$("#orderPayType").val(2);
		changePeriodPayType('img-wxpay', 2);
	} else  {
		$$("#select-wxpay").css("display", "none");
		$$("#select-alipay").css("display", "block");
		$$('#img-restpay').attr("src","img/dingdan-pay/dingdan-pay2.png");
		$$('#img-wxpay').attr("src","img/dingdan-pay/dingdan-pay2.png");
		$$('#img-alipay').attr("src","img/dingdan-pay/dingdan-pay1.png");
		$$("#orderPayType").val(1);
		changePeriodPayType('img-alipay', 1)
	}
	
	var postOrderPaySuccess =function(data, textStatus, jqXHR) {
		$$("#orderPaySubmit").removeAttr('disabled');  
	 	var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}

		orderPayType = result.data.pay_type;
		var orderType = result.data.order_type;
		var periodServiceTypeId = result.data.period_service_type_id;
		var orderNo = result.data.order_no;
		
		//如果为余额支付或者 现金支付，则直接跳到完成页面
		if (orderPayType == 0) {
			//支付成功之后，把优惠劵的信息清空.
			sessionStorage.removeItem("user_coupon_id");
			sessionStorage.removeItem("user_coupon_value");
			sessionStorage.removeItem("user_coupon_name");
			mainView.router.loadPage("order/order-pay-success.html");
		}
		
		//如果为支付宝支付，则跳转到支付宝手机网页支付页面
		if (orderPayType == 1) {
			var orderPrice = result.data.order_price;
			var alipayUrl = localUrl + "/" + appName + "/pay/alipay_period-order.jsp";
			alipayUrl +="?orderNo="+orderNo;
			alipayUrl +="&orderPrice="+orderPrice;
			alipayUrl +="&orderType="+orderType;
			alipayUrl +="&periodServiceTypeId="+periodServiceTypeId;
			alipayUrl +="&payOrderType=4";
			alipayUrl +="&shareUserId="+shareUserId;
			location.href = alipayUrl;
		}
		
		//如果为微信支付，则需要跳转到微信支付页面.
		if (orderPayType == 2) {
			 var wxPayUrl = localUrl + "/" + appName + "/wx-pay-pre.jsp";
			 wxPayUrl +="?orderId="+result.data.id;
			 wxPayUrl +="&orderNo="+orderNo;
			 wxPayUrl +="&userCouponId=0";
			 wxPayUrl +="&orderType=0";
			 wxPayUrl +="&payOrderType=4";
			 wxPayUrl +="&periodServiceTypeId="+periodServiceTypeId;
			 wxPayUrl +="&shareUserId="+shareUserId;
			 location.href = wxPayUrl;
		}
		
	};
	
	//点击支付的处理
	$$("#orderPaySubmit").click(function(){
		$$("#orderPaySubmit").attr("disabled", true);
		
		var params = {};
		params.user_id = userId;
		params.order_no = orderNo;
		params.pay_type = $$("#orderPayType").val();
		params.share_user_id = shareUserId;
		console.log(params);
		
		$$.ajax({
			type: "post",
			url: siteAPIPath + "order/post_pay_period_order.json",
			data: params,
			statusCode: {
	         	200: postOrderPaySuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    }
		});
	});
	
});

function changePeriodPayType(imgPayType, orderPayType) {
	
	$$("#orderPayType").val(orderPayType);
	var imgPayTypes = ['img-restpay', 'img-wxpay', 'img-alipay'];
	
	$$.each(imgPayTypes,function(n,value) {  
		console.log("value = " + value + "=== imgPayType=" + imgPayType);
		if (value == imgPayType) {
			$$('#' + value).attr("src","img/dingdan-pay/dingdan-pay1.png");
		} else {
			$$('#' + value).attr("src","img/dingdan-pay/dingdan-pay2.png");
		}
	});
	
	//更换价格
	var periodOrderPay = sessionStorage.getItem('periodPayMoney');
	var periodOrderOriginPay = sessionStorage.getItem('periodOrderMoney');
	if(periodOrderPay==undefined || periodOrderPay==null || periodOrderPay==''){
		periodOrderPay = 0;
	}
	if(periodOrderOriginPay==undefined || periodOrderOriginPay==null || periodOrderOriginPay==''){
		periodOrderOriginPay = 0;
	}
	
	$$("#periodOrderMoneyStrLi").html("￥"+periodOrderPay+"元");
	$$("#periodOrderPayStrLi").html("￥"+periodOrderPay+"元");
	
}

