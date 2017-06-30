myApp.onPageInit('20170701', function (page) {
	
	var orderOpFrom = page.query.order_op_from;
	sessionStorage.setItem("order_op_from", orderOpFrom);
	
	$$("tr").on("click",'.classifi05',function(serviceTypeId){
		var url = "order/order-custom-choose.html?service_type_id=84";
		mainView.router.loadPage(url);
	});
});



