myApp.onPageBeforeInit('order-view-page', function(page) {

	var userId = page.query.user_id;
	
	var orderNo = page.query.order_no;
	
	var orderId = 0;
	
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	
	var secId = localStorage['sec_id'];
		
	var orderInfoSuccess = function(data, textStatus, jqXHR) {
		
		// We have received response and can hide activity indicator
		myApp.hideIndicator();
		var result = JSON.parse(data.response);
		var order = result.data;
//		console.log(order);
		
		var timestamp = moment.unix(order.add_time);
		var addTimeStr = timestamp.format('YYYY-MM-DD HH:mm:ss');		
		$$("#add_time").text(addTimeStr);
		$$("#order_status").text(order.order_status_name);
		if (order.order_pay_type == 0) {
			$$("#order_pay_type").text("无需支付");
						
		} else {
			$$("#order_pay_type").text("在线支付");
		}
		
		$$("#order_id").val(order.id);
		$$("#order_no").text(order.order_no);
		$$("#name-span").text(order.name);
		$$("#mobile").text(order.mobile);
		$$("#service_type").text(order.service_type_name);
		$$("#service_content").text(order.service_content);

		
		$$("#order_money").text(order.order_money);
		
		if (order.order_status == 1 || order.order_status == 3) {
			$$("#order-modify").css('display','block'); 
			$$("#order-push").css('display','block'); 
		}

	};
	
	var postdata = {};

	postdata.user_id = userId;
	postdata.order_no = orderNo;
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "order/get_detail.json",
		dataType : "json",
		cache : true,
		data : postdata,
		statusCode : {
			200 : orderInfoSuccess,
			400 : ajaxError,
			500 : ajaxError
		},
		success : function() {

		}
	});		

	$$('#order-modify').on('click', function() {
		mainView.router.loadPage("order/order-form.html?user_id="+userId+"&order_no="+ orderNo);
	});
	
	$$('#order-push').on('click', function() {
		
		var orderPushSuccess = function(data, textStatus, jqXHR) {
			// We have received response and can hide activity indicator
			myApp.hideIndicator();
//			console.log("push success");
			var result = JSON.parse(data.response);

//			console.log(result);
			if (result.status == "999") {
				myApp.alert(result.msg);
				return;
			}
			
			if (result.status == "0") {
				myApp.alert("订单推送已完成");
				
			}

		};				
		
		var postdata = {};
		var orderId = $$("#order_id").val();
		postdata.user_id = userId;
		postdata.order_id = orderId;
		
		$$.ajax({
			cache : true,
			type : "POST",
			url : siteAPIPath + "order/push_order.json",
			data : postdata,
			statusCode : {
				200 : orderPushSuccess,
				400 : ajaxError,
				500 : ajaxError
			},
		});		
	});	

});