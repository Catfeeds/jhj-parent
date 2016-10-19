myApp.onPageBeforeInit('order-pay-ext', function(page) {
	
	var userId = localStorage['user_id'];
	
	var staffNames = sessionStorage.getItem("staff_names");
	
	$$("#staffNames").html(staffNames);
	
	// 点击支付的处理
	$$("#orderPriceExtSumbit").click(function(){
		
		var orderPayExt = $$("#orderPayExt").val();
		
		if (orderPayExt == "" || orderPayExt == 0 || orderPayExt == undefined) {
			myApp.alert("请输入金额.");
			return false;
		}
		
		// payOrderType 订单支付类型 0 = 订单支付 1= 充值支付 2 = 手机话费类充值 3 = 订单补差价
		sessionStorage.setItem("pay_order_type", 3);
		sessionStorage.setItem("order_pay", orderPayExt);
		mainView.router.loadPage("order/order-pay.html");
	});
});

