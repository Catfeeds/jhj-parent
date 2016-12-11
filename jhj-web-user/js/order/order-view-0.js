myApp.onPageInit('order-view-0', function(page) {


});

myApp.template7Data['page:order-view-0'] = function() {
	var result;
	var postdata = {};
	var orderNo = sessionStorage.getItem("order_no");
	postdata.order_no = orderNo;
	console.log("order_no = " + orderNo);
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "order/order_hour_detail.json",
		dataType : "json",
		data : postdata,
		cache : true,
		async : false, // 不能是异步
		success : function(data) {
			console.log(data);
			result = data.data;
		}
	})

	return result;
}
