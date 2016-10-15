myApp.onPageBeforeInit('order-list', function(page) {
	
	removeSessionData();
	
	var userId = localStorage['user_id'];
	var loading = false;// 加载flag
	var page = $$("#page").val();
	
	var orderListSuccess = function(data, textStatus, jqXHR) {

		var result = JSON.parse(data.response);
		var orders = result.data;

		var htmlTemplate = $$('#order-list-part').html();

		var html = ''; // 当前订单

		for (var i = 0; i < orders.length; i++) {
			var order = orders[i];
			var htmlPart = htmlTemplate;
			htmlPart = htmlPart.replace(new RegExp('{orderType}', "gm"), order.order_type);
			htmlPart = htmlPart.replace(new RegExp('{orderId}', "gm"), order.id);
			htmlPart = htmlPart.replace(new RegExp('{orderNo}', "gm"), order.order_no);
			htmlPart = htmlPart.replace(new RegExp('{serviceTypeId}', "gm"), order.service_type);
			htmlPart = htmlPart.replace(new RegExp('{orderPay}', "gm"), order.order_pay);
			
			htmlPart = htmlPart.replace(new RegExp('{serviceTypeName}', "gm"), order.service_type_name);
			htmlPart = htmlPart.replace(new RegExp('{orderStatusStr}', "gm"), order.order_status_name);
			htmlPart = htmlPart.replace(new RegExp('{serviceDateStr}', "gm"), order.service_date_str);
			htmlPart = htmlPart.replace(new RegExp('{addrName}', "gm"), order.address);
			htmlPart = htmlPart.replace(new RegExp('{orderPayStr}', "gm"), order.order_pay + "元");
			
			console.log("order.coupon_id = " + order.coupon_id);
			
			htmlPart = htmlPart.replace(new RegExp('{userCouponId}', "gm"), order.coupon_id);
			htmlPart = htmlPart.replace(new RegExp('{userCouponValue}', "gm"), order.coupon_value);
			htmlPart = htmlPart.replace(new RegExp('{orderStatus}', "gm"), order.order_status);
			
			var orderStatus = order.order_status;
			//如果未支付，则显示去支付按钮.
			var doOrderPayStyle = 'none';
			if (orderStatus == 1) {
				doOrderPayStyle = 'block';
			}
			htmlPart = htmlPart.replace(new RegExp('{doOrderPayStyle}', "gm"), doOrderPayStyle);
			
			//必须为已支付的订单，未完成服务的订单可以补差价
			var priceExtendStyle = 'none';
			if (orderStatus > 1 && orderStatus <= 5) {
				priceExtendStyle = 'block';
			}
			htmlPart = htmlPart.replace(new RegExp('{priceExtendStyle}', "gm"), priceExtendStyle);
			
			html+= htmlPart;
		}
		// 当前订单
		if (page == 1) {
			$$("#order-list-div").html(html);
		} else {
			$$("#order-list-div").append(html);
		}

		loading = false;
		
		page = page + 1;
		$$("#page").val(page);
		console.log("page = " + page);
		console.log("len = " + orders.length);
		if (orders.length >= 10) {
			$$('#order-list-more').css("display", "block");
		}
	};
	
	
	function loadOrderList(userId, page) {
		console.log("page = " + page);
		var postdata = {};
		
		var apiUrl = "order/user_order_list.json";
		postdata.user_id = userId;
		postdata.page = page;

		$$.ajax({
			type : "GET",
			url : siteAPIPath + apiUrl,
			dataType : "json",
			cache : true,
			data : postdata,
			statusCode : {
				200 : orderListSuccess,
				400 : ajaxError,
				500 : ajaxError
			},

		});
	}	
		
	// 注册'infinite'事件处理函数
	$$('#order-list-more').on('click', function() {
		var cpage = ++page;
		loadOrderList(userId, cpage);
	});
	
	
	
	loadOrderList(userId, page);
});

function orderView(orderType, orderNo) {
	var url = "";

	if (orderType == 0) url = "order/order-view-0.html";
	if (orderType == 1) url = "order/order-view-1.html";
	
	sessionStorage.setItem("order_type", orderType);
	sessionStorage.setItem("order_no", orderNo);
	
	mainView.router.loadPage(url);
}

function doOrderPay(obj) {
	
	var orderStatus = obj.find('input[name=orderStatus]').val();
	
	if (orderStatus != 1) return false;
	
	var serviceTypeId = obj.find('input[name=serviceTypeId]').val();
	var orderId = obj.find('input[name=orderId]').val();
	var orderNo = obj.find('input[name=orderNo]').val();
	var orderPay = obj.find('input[name=orderPay]').val();
	var userCouponId = obj.find('input[name=userCouponId]').val();
	var userCouponValue = obj.find('input[name=userCouponValue]').val();
	
	sessionStorage.setItem("service_type_id", serviceTypeId);
	sessionStorage.setItem("order_id", orderId);
	sessionStorage.setItem("order_no", orderNo);
	sessionStorage.setItem("order_pay", orderPay);
	sessionStorage.setItem("user_coupon_id", userCouponId);
	sessionStorage.setItem("user_coupon_value", userCouponValue);
	
	mainView.router.loadPage("order/order-pay.html");
	
}



