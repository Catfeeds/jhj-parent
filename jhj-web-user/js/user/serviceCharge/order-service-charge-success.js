myApp.onPageInit('order-service-charge-success-page', function(page) {

	var orderNo = page.query.order_no;
	
	myApp.alert(orderNo);
	myApp.alert("充值成功,运营商短信稍后送达");
//	
//	/*
//	 *  该页面的功能：
//	 *  	在微信支付成功后,调用  话费充值 API, 完成真正意义的  话费充值
//	 */
//	
//	
//	$$.ajax({
//		type: "post",
//		 url: siteAPIPath + "order/recharge_real.json",
//		data: postdata,
//		statusCode: {
//         	200: rechargeRealSuccess,
// 	    	400: ajaxError,
// 	    	500: ajaxError
// 	    }
//	});		
//	
//	var rechargeRealSuccess = function(data, textStatus, jqXHR){
//		
//		var result = JSON.parse(data.response);
//		
//		if (result.status == "999") {
//			return;
//		}
//		
//	}
	
});


