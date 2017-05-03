myApp.onPageInit('20170504', function (page) {
	
	var orderOpFrom = page.query.order_op_from;
	sessionStorage.setItem("order_op_from", orderOpFrom);
});

function active_link_20170504(serviceTypeId, orderType) {
	var url = "order/order-custom-choose.html?service_type_id="+serviceTypeId;
	mainView.router.loadPage(url);
}

