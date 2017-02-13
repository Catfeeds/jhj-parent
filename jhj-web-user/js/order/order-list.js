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
			htmlPart = htmlPart.replace(new RegExp('{orderOriginPay}', "gm"), order.order_origin_pay);

			htmlPart = htmlPart.replace(new RegExp('{serviceTypeName}', "gm"), order.service_type_name);
			htmlPart = htmlPart.replace(new RegExp('{orderStatusStr}', "gm"), order.order_status_name);
			htmlPart = htmlPart.replace(new RegExp('{serviceDateStr}', "gm"), order.service_date_str);
			htmlPart = htmlPart.replace(new RegExp('{addrName}', "gm"), order.address);
			htmlPart = htmlPart.replace(new RegExp('{orderPayStr}', "gm"), order.order_pay + "元");
			
			console.log("order.coupon_id = " + order.coupon_id);
			
			htmlPart = htmlPart.replace(new RegExp('{userCouponId}', "gm"), order.coupon_id);
			htmlPart = htmlPart.replace(new RegExp('{userCouponValue}', "gm"), order.coupon_value);
			htmlPart = htmlPart.replace(new RegExp('{orderStatus}', "gm"), order.order_status);
			
			htmlPart = htmlPart.replace(new RegExp('{staffNames}', "gm"), order.staff_names);
			htmlPart = htmlPart.replace(new RegExp('{staffMobile}', "gm"), order.staff_mobile);
			
			var orderStatus = order.order_status;
			console.log("orderStatus = " + orderStatus);
			//如果未支付，则显示去支付按钮.
			var doOrderPayStyle = 'none';
			if (orderStatus == 1) {
				doOrderPayStyle = 'block';
			}
			htmlPart = htmlPart.replace(new RegExp('{doOrderPayStyle}', "gm"), doOrderPayStyle);
			
			//1.必须为已支付的订单，未完成服务的订单可以补差价
			//2.现金支付不能有补差价功能.
			var payType = order.pay_type;
			var priceExtendStyle = 'none';
			if (orderStatus >= 3 && orderStatus < 7 && payType != 6) {
				priceExtendStyle = 'block';
			}

			htmlPart = htmlPart.replace(new RegExp('{priceExtendStyle}', "gm"), priceExtendStyle);
			
			var orderRateStyle = "none"
			if (orderStatus >= 3 && orderStatus <= 8) {
				orderRateStyle = "block";
			}
			var orderRateStr='';
			if(orderStatus>=3 && orderStatus<7) orderRateStr = "联系服务员";
			if (orderStatus == 7) orderRateStr = "立即评价";
			if (orderStatus == 8) orderRateStr = "已评价";
			htmlPart = htmlPart.replace(new RegExp('{orderRateStr}', "gm"), orderRateStr);
			htmlPart = htmlPart.replace(new RegExp('{orderRateStyle}', "gm"), orderRateStyle);
			
			html+= htmlPart;
		}
		// 当前订单
		if (page == 1) {
			$$("#order-list-div").html(html);
		} else {
			$$("#order-list-div").append(html);
		}

		loading = false;
		
		$$("#page").val(page);
		console.log("page = " + page);
		console.log("len = " + orders.length);
		if (orders.length >= 10) {
			console.log("order-list-more block");
			$$('#order-list-more').css("display", "block");
		} else {
			$$('#order-list-more').css("display", "none");
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
	var orderOriginPay = obj.find('input[name=orderOriginPay]').val();
	var userCouponId = obj.find('input[name=userCouponId]').val();
	var userCouponValue = obj.find('input[name=userCouponValue]').val();
	
	sessionStorage.setItem("service_type_id", serviceTypeId);
	sessionStorage.setItem("order_id", orderId);
	sessionStorage.setItem("order_no", orderNo);
	sessionStorage.setItem("order_pay", orderPay);
	sessionStorage.setItem("order_origin_pay", orderOriginPay);
	sessionStorage.setItem("user_coupon_id", userCouponId);
	sessionStorage.setItem("user_coupon_value", userCouponValue);
	
	// 订单类型 0 = 钟点工 1 = 深度保洁 2 = 助理订单 6= 话费充值类订单 7 = 订单补差价
	sessionStorage.setItem("pay_order_type", 0);
	
	mainView.router.loadPage("order/order-pay.html");
	
}

function doOrderPayExt(obj) {
	
	var orderStatus = obj.find('input[name=orderStatus]').val();
	
	if (orderStatus >= 3 && orderStatus < 7) {
	
		var serviceTypeId = obj.find('input[name=serviceTypeId]').val();
		var orderId = obj.find('input[name=orderId]').val();
		var orderNo = obj.find('input[name=orderNo]').val();
		var orderPay = obj.find('input[name=orderPay]').val();
		
		var staffNames = obj.find('input[name=staffNames]').val();
		
		
		sessionStorage.setItem("service_type_id", serviceTypeId);
		sessionStorage.setItem("order_id", orderId);
		sessionStorage.setItem("order_no", orderNo);
		sessionStorage.setItem("order_pay", orderPay);
		
		sessionStorage.setItem("staff_names", staffNames);
		
		console.log("staffNames = " + sessionStorage.getItem("staff_names"));
		
		mainView.router.loadPage("order/order-pay-ext.html");
	}
	
}


function linkOrderRate(obj) {
	
	var orderStatus = obj.find('input[name=orderStatus]').val();
	
	if (orderStatus <= 2) return false;
	
	
	var orderId = obj.find('input[name=orderId]').val();
	sessionStorage.setItem("order_id", orderId);
	
	var orderNo = obj.find('input[name=orderNo]').val();
	sessionStorage.setItem("order_no", orderNo);
	
	var orderType = obj.find('input[name=orderType]').val();
	sessionStorage.setItem("order_type", orderType);
		
	var staffNames = obj.find('input[name=staffNames]').val();
	sessionStorage.setItem("staff_names", staffNames);
	
	var orderRateUrl = "order/order-rate.html";
	if (orderStatus == 8) {
		orderRateUrl = "order/order-user-rate.html";
	}
	
	mainView.router.loadPage(orderRateUrl);
}

function choseLink(obj){
	var orderStatus = $$(obj).parent().parent().find('input[name=orderStatus]').val();;
	if(orderStatus>=3 && orderStatus<7){
		var html = $$(obj).text();
		var staffMobile = $$("#staffMobile").val();
		$$(obj).parent().attr("href","tel:"+staffMobile);
		
	}
	if(orderStatus==7 || orderStatus==8){
		linkOrderRate($$(obj).parent().parent());
	}
};


