myApp.onPageBeforeInit('order-am-result-page', function (page) {

	var orderNo =  page.query.order_no;
	//查看订单
	$$("#order-am-result").on("click", function() {
	
		localStorage.setItem('order_no', orderNo);
		//调整订单跳转
		mainView.router.loadPage("order/order-view-2.html?order_no="+orderNo);

	})
	
});

