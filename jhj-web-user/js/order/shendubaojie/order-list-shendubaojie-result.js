myApp.onPageBeforeInit('order-list-shendubaojie-result', function (page) {

	//获取深度保洁订单号
	var orderNo = page.query.order_no;
	//to深度保洁详情页面
	$$("#to-shendu-order-detail").on("click", function() {
		mainView.router.loadPage("order/order-view-1.html?order_no="+orderNo);
		localStorage.setItem('u_order_no_param',orderNo);
	});
});
	



