myApp.onPageInit('order-remind-tixing-success-page',function(page){
	
	var order_no = page.query.order_no;
	
	$$("#order-remind-success").click(function(){
		
		localStorage.setItem('remind_order_no_param',order_no);
		mainView.router.loadPage("order/remind/order-remind-detail.html?order_no="+order_no);
	});
	
	
});