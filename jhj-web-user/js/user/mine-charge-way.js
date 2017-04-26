myApp.onPageBeforeInit('mine-charge-way', function(page) {

	var userId = localStorage.getItem("user_id");
	var cardId = page.query.card_id;
//	var cardPay = page.query.card_pay;
	
	localStorage.removeItem("pay_card_id");
//	localStorage.setItem("pay_card_id",cardId);
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}

	$$("#mobile").text(localStorage.getItem("user_mobile"));

//	var html = compiledTemplate(cardPay);

//	if (cardPay == undefined || cardPay == '' || cardPay == 0) {
//		var cardInfoSuccess = function(data, textStatus, jqXHR) {
//			myApp.hideIndicator();
//			var result = JSON.parse(data.response);
//			if (result.status == "999") {
//				myApp.alert(data.msg);
//				return;
//			}
//			var cardInfo = result.data;
//			$$("#card_pay_view").text(cardInfo.card_value);
//		};

//		$$.ajax({
//			type:"GET",
//			url:siteAPIPath+"dict/get_card_detail.json?card_id="+cardId,
//			dataType:"json",
//			cache:false,
//			statusCode : {
//				200 : cardInfoSuccess,
//				400 : ajaxError,
//				500 : ajaxError
//			}
//		});
//	}
	
	//m默认支付方式
	var orderPayType = 2;
	var isWx = isWeiXin();
	if(isWx){
		$$("#zhifubao-pay").css("display","none");
	}else{
		$$("#wexin-pay").css("display","none");
		orderPayType = 1;
	}

	var postCardBuySuccess =function(data, textStatus, jqXHR) {

		var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}
		var orderId = result.data.id;
		var orderNo = result.data.card_order_no;
		var orderPay = result.data.card_pay;
		var orderStatus = result.data.order_status;
		
		//测试情况下，直接下单则会直接付款成功
		if (orderStatus == 1) {
			mainView.router.loadPage("user/charge/mine-charge-pay-success.html");
			return false;
		}
		
		
		//如果为支付宝支付，则跳转到支付宝手机网页支付页面
		if (orderPayType == 1) {
			var alipayUrl = localUrl + "/" + appName + "/pay/alipay_card_order.jsp";
			alipayUrl +="?orderNo="+orderNo;
			alipayUrl +="&orderPay="+orderPay;
			alipayUrl +="&orderType=0";
			alipayUrl +="&payOrderType=1";
			location.href = alipayUrl;
		}
		
		//如果为微信支付，则需要跳转到微信支付页面.
		if (orderPayType == 2) {
			var wxPayUrl = localUrl + "/" + appName + "/wx-pay-pre.jsp";
			wxPayUrl +="?orderId="+orderId;
			wxPayUrl +="&userCouponId=0";
			wxPayUrl +="&orderType=4";
			wxPayUrl +="&payOrderType=1";
			location.href = wxPayUrl;
		}
	};

	//点击支付的处理
	$$("#chongzhi-submit").click(function(){
		var postdata = {};
		postdata.user_id = userId;
		postdata.card_type = cardId;
		postdata.pay_type = orderPayType;
		postdata.staff_code=$$("#staffCode").val();

		$$.ajax({
			type: "post",
			url: siteAPIPath + "user/card_buy.json",
			data: postdata,
			statusCode: {
				200: postCardBuySuccess,
				400: ajaxError,
				500: ajaxError
			}
		});

	});
});

myApp.template7Data['page:mine-charge-way'] = function(){
	var result
	var cardId = sessionStorage.getItem("card_id");
	$$.ajax({
		type:"GET",
		url:siteAPIPath+"dict/get_card_detail.json?card_id="+cardId,
		dataType:"json",
		cache:true,
		async:false,
		success :function(data) {
			result=data.data;
		}
	});
//	sessionStorage.removeItem("card_id");
	return result;
}