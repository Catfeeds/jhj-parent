myApp.onPageInit('order-pay-success-page', function(page) {

	var orderNo = page.query.order_no;
	
	var orderType = page.query.order_type;
	
	var orderPayType = page.query.order_pay_type;
	
	if (orderType == 0) {
		$$("#blackContent").text("订单支付成功!")
		$$("#grayContent").text("我们正在为您安排服务人员");
	}
	
	if(orderType == 1){
		$$("#blackContent").text("订单支付成功!")
		$$("#grayContent").text("服务人员即将为您服务");
	}
	
	if(orderType == 2){
		
		if(orderPayType == 6){
			//如果是 现金支付
			$$("#blackContent").text("现金支付成功!")
			$$("#grayContent").text("您的助理会按约定时间为您服务");
		}else{
			$$("#blackContent").text("订单支付成功!")
			$$("#grayContent").text("您的助理会按约定时间为您服务");
		}
	}
	
	$$('#order-pay-success-btn').click(function(){

		var fromUrl = "";
    	if (orderType == 0) {
    		fromUrl =  "order/order-view-0.html";
    	}
    	
    	if (orderType == 1) {
    		fromUrl = "order/order-view-1.html";
    	}
    	
    	if (orderType == 2) {
    		fromUrl = "order/order-view-2.html";                                		
    	}
    	
		
		localStorage.setItem('order_no', orderNo);
		
		fromUrl+= "?order_no="+orderNo;
		
		mainView.router.loadPage(fromUrl);
	});
	
});


