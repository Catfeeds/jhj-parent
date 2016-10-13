myApp.onPageInit('order-pay-success', function(page) {
	console.log("order-pay-success-page");
	var orderNo = page.query.order_no;
	
	var orderType = page.query.order_type;
	
	var serviceTypeId = page.query.service_type_id;
		
	if (sessionStorage.getItem('service_type_id') != "") {
		serviceTypeId = sessionStorage.getItem('service_type_id');
	}
	console.log("serviceTypeId = " + serviceTypeId);
	var recoList = orderRecomment(serviceTypeId);
	
	console.log(recoList);
	if (recoList != undefined && recoList != "") {
		var htmlStr = "";
		$$.each(recoList, function(i, item) {
			console.log(item.name);
			htmlStr+='<a href="'+item.url+'" class="special-color7"><span>'+item.name+'</span></a>';
		});
		
		$$("#recoList").append(htmlStr);
	}
	
	
	
});


