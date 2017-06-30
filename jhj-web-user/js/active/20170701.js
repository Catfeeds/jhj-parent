myApp.onPageInit('20170701', function (page) {
	
	sessionStorage.setItem("order_op_from", "0");
	
	$$("tr").on("click",'.classifi05',function(serviceTypeId){
		var url = "order/order-custom-choose.html?service_type_id=84";
		mainView.router.loadPage(url);
	});
});



