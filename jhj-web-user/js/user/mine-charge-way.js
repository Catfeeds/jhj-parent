myApp.onPageBeforeInit('mine-charge-way', function(page) {
	
	var userId = localStorage.getItem("user_id");

	var cardId = page.query.card_id;
	var cardPay = page.query.card_pay;
	var orderPayType = 2;
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	
	$$("#mobile").text(localStorage.getItem("user_mobile"));
	
    $$("#card_pay_view").text(cardPay);
	if (cardPay == undefined || cardPay == '' || cardPay == 0) {
		var cardInfoSuccess = function(data, textStatus, jqXHR) {
		  	myApp.hideIndicator();
			var result = JSON.parse(data.response);
			if (result.status == "999") {
				myApp.alert(data.msg);
				return;
			}
			var cardInfo = result.data; 
			$$("#card_pay_view").text(cardInfo.card_value);
		};
	

		$$.ajax({
	        type:"GET",
	        url:siteAPIPath+"dict/get_card_detail.json?card_id="+cardId,
	        dataType:"json",
	        cache:false,
	        statusCode : {
				200 : cardInfoSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
		}); 
	}
	
	
	var postCardBuySuccess =function(data, textStatus, jqXHR) {

	 	var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}
		var orderId = result.data.id;
		//如果为微信支付，则需要跳转到微信支付页面.
//		if (orderPayType == 2) {
			 var wxPayUrl = localUrl + "/" + appName + "/wx-pay-pre.jsp";
			 wxPayUrl +="?orderId="+orderId;
			 wxPayUrl +="&userCouponId=0";
			 wxPayUrl +="&orderType=4";
			 wxPayUrl +="&payOrderType=1";
			 location.href = wxPayUrl;
//		}
	};		
	
	//点击支付的处理
	$$(".chongzhi-submit").click(function(){

		
		var postdata = {};
		postdata.user_id = userId;
		postdata.card_type = cardId;
		postdata.pay_type = 2;
						
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