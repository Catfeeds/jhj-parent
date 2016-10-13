myApp.onPageInit('order-pay', function(page) {
	
	var userId = localStorage['user_id'];
	var serviceTypeId = sessionStorage.getItem('service_type_id');
	var orderNo = sessionStorage.getItem('order_no');
	var orderId = sessionStorage.getItem('order_id');
	var orderPay = sessionStorage.getItem('order_pay');
	var userCouponId = sessionStorage.getItem('user_coupon_id');
	var userCouponValue = sessionStorage.getItem('user_coupon_value');
	if (userCouponValue == undefined || userCouponValue == "" || userCouponValue == null) {
		userCouponValue = 0;
	}
	
	
	$$("#userId").val(userId);
	$$("#orderNo").val(orderNo);
	$$("#orderId").val(orderId);
	$$("#orderPay").val(orderPay);
	$$("#orderMoneyStrLi").html("￥"+orderPay+"元");
	$$("#orderPayStrLi").html("￥"+orderPay+"元");
	$$("#userCouponId").val(userCouponId);
	$$("#userCouponValue").val(userCouponValue);
	$$("#userCouponValueStr").html(userCouponValue + "元");
		
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
	} else  {
		$$("#select-wxpay").css("display", "none");
		$$("#select-alipay").css("display", "block");
	}
	
	var postOrderPaySuccess =function(data, textStatus, jqXHR) {
		$$("#orderPaySubmit").removeAttr('disabled');  
	 	var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}

		orderPayType = result.data.pay_type;
		orderType = result.data.order_type;
		serviceTypeId = result.data.service_type;
		console.log("orderPayType = " + orderPayType);
		
		//如果为余额支付或者 现金支付，则直接跳到完成页面
		if (orderPayType == 0 || orderPayType == 6) {
			//支付成功之后，把优惠劵的信息清空.
			sessionStorage.removeItem("user_coupon_id");
			sessionStorage.removeItem("user_coupon_value");
			sessionStorage.removeItem("user_coupon_name");
			mainView.router.loadPage("order/order-pay-success.html?service_type_id="+serviceTypeId);
		}
		
		
		//如果为支付宝支付，则跳转到支付宝手机网页支付页面
		if (orderPayType == 1) {
			var orderPay = result.data.order_pay;
			var alipayUrl = localUrl + "/" + appName + "/pay/alipay_order_api.jsp";
			alipayUrl +="?orderNo="+orderNo;
			alipayUrl +="&orderPay="+orderPay;
			alipayUrl +="&orderType="+orderType;
			alipayUrl +="&serviceTypeId="+serviceTypeId;
			location.href = alipayUrl;
		}
		
		//如果为微信支付，则需要跳转到微信支付页面.
		if (orderPayType == 2) {
			 var userCouponId = $$("#userCouponId").val();
			 if (userCouponId == undefined) userCouponId = 0;
			 var wxPayUrl = localUrl + "/" + appName + "/wx-pay-pre.jsp";
			 wxPayUrl +="?orderId="+orderId;
			 wxPayUrl +="&userCouponId="+userCouponId;
			 wxPayUrl +="&orderType=0";
			 wxPayUrl +="&payOrderType=0";
			 wxPayUrl +="&serviceTypeId="+serviceTypeId;
			 location.href = wxPayUrl;
		}
	};
	
	//点击支付的处理
	$$("#orderPaySubmit").click(function(){
		$$("#orderPaySubmit").attr("disabled", true);
		var params = {};
		params.user_id = userId;
		params.order_no = orderNo;
		var userCouponId = $$("#userCouponId").val();
		if (userCouponId == undefined) userCouponId = 0;
		params.user_coupon_id = userCouponId;
		params.order_pay_type = $$("#orderPayType").val();
		
		console.log(params);

		
		$$.ajax({
			type: "post",
			 url: siteAPIPath + "order/post_pay.json",
			data: params,
			statusCode: {
	         	200: postOrderPaySuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    }
		});		
		 
	});
	
});

function changePayType(imgPayType, orderPayType) {
	
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
}