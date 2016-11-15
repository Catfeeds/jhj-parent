myApp.onPageBeforeInit('order-user-rate', function(page) {
	
	removeSessionData();
	
	var userId = localStorage['user_id'];
	var loading = false;// 加载flag
	var page = $$("#page").val();
	
	var orderListSuccess = function(data, textStatus, jqXHR) {

		var result = JSON.parse(data.response);
		var orderRates = result.data;

		var htmlTemplate = $$('#order-user-rate-temp').html();

		var html = ''; // 当前订单

		for (var i = 0; i < orderRates.length; i++) {
			var or = orderRates[i];
			var htmlPart = htmlTemplate;
			if(or.staff_list!=null){
				var staffhtml="";
				var headImgHtml="";
				var staffs=or.staff_list;
				var staffLen=staffs.length;
				for(var i=0;i<staffLen;i++){
					staffhtml ="<a href='order/order-rate-success.html'><span class='special-color2'>"+staffs[i].name+"</span></a>"
					if(i<2){
						headImgHtml="<img src='"+staffs[i].head_img+"' alt='' />";
					}
				}
				htmlPart = htmlPart.replace(new RegExp('{staff_name}', "gm"), staffhtml);
				htmlPart = htmlPart.replace(new RegExp('{head_img}', "gm"), headImgHtml);
			}else{
				htmlPart = htmlPart.replace(new RegExp('{staff_name}', "gm"), "");
			}
			
			htmlPart = htmlPart.replace(new RegExp('{service_type_name}', "gm"), or.service_type_name);
			htmlPart = htmlPart.replace(new RegExp('{service_date_str}', "gm"), or.service_date_str);
			if(or.rate_content!='' && or.rate_content!=null){
				htmlPart = htmlPart.replace(new RegExp('{rate_content}', "gm"), or.rate_content);
			}else{
				htmlPart = htmlPart.replace(new RegExp('{rate_content}', "gm"), "");
			}
			
			var rateArrival="";
			if(or.rate_arrival==0){
				rateArrival="<span class='waiter10-3-2'>准时</span><span class='waiter10-3-1'>迟到</span></li>";
			}else if(or.rate_arrival==1){
				rateArrival="<span class='waiter10-3-1'>准时</span><span class='waiter10-3-2'>迟到</span></li>"
			}
			htmlPart = htmlPart.replace(new RegExp('{rate_arrival}', "gm"), rateArrival);
			
			//服务态度
			var rateAttitude = "";
			if (or.rate_attitude > 0) {
				for (var i = 1 ; i <= 5; i++) {
					if (i <= or.rate_attitude) {
						rateAttitude+='<img src="img/yudingayi/xx.png" alt="">';
					} else {
						rateAttitude+='<img src="img/yudingayi/xx1.png" alt="">';
					}
					
				}
			}
			htmlPart = htmlPart.replace(new RegExp('{rate_attitude}', "gm"), rateAttitude);
			
			//技能
			
			var rateSkill = "";
			if (or.rate_attitude > 0) {
				for (var i = 1 ; i <= 5; i++) {
					if (i <= or.rate_attitude) {
						rateSkill+='<img src="img/yudingayi/xx.png" alt="">';
					} else {
						rateSkill+='<img src="img/yudingayi/xx1.png" alt="">';
					}
					
				}
			}
			htmlPart = htmlPart.replace(new RegExp('{rate_skill}', "gm"), rateSkill);
			
			var rateUrl="";
			if(or.order_rate_url!=null && rateUrlLen>0){
				var rateUrlLen=or.order_rate_url.length;
				for(var i=0;i<rateUrlLen;i++){
					rateUrl = "<div class='waiter10-4-1'><img src='"+or.order_rate_url[i]+"' alt=''></div>";
				}
				htmlPart = htmlPart.replace(new RegExp('{rate_url}', "gm"), rateUrl);
			}else{
				htmlPart = htmlPart.replace(new RegExp('{rate_url}', "gm"), "");
			}
			
			html+= htmlPart;
		}
		// 当前订单
		if (page == 1) {
			$$("#order-user-rate").html(html);
		} else {
			$$("#order-user-rate").append(html);
		}

		loading = false;
		
		$$("#page").val(page);
//		console.log("page = " + page);
//		console.log("len = " + orders.length);
		if (orderRates.length >= 10) {
			console.log("order-list-more block");
			$$('#order-list-more').css("display", "block");
		} else {
			$$('#order-list-more').css("display", "none");
		}
	};
	
	
	function loadOrderList(userId, page) {
		console.log("page = " + page);
		var postdata = {};
		
		var apiUrl = "order/get_user_rates.json";
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

//function orderView(orderType, orderNo) {
//	var url = "";
//
//	if (orderType == 0) url = "order/order-view-0.html";
//	if (orderType == 1) url = "order/order-view-1.html";
//	
//	sessionStorage.setItem("order_type", orderType);
//	sessionStorage.setItem("order_no", orderNo);
//	
//	mainView.router.loadPage(url);
//}

//function doOrderPay(obj) {
//	
//	var orderStatus = obj.find('input[name=orderStatus]').val();
//	
//	if (orderStatus != 1) return false;
//	
//	var serviceTypeId = obj.find('input[name=serviceTypeId]').val();
//	var orderId = obj.find('input[name=orderId]').val();
//	var orderNo = obj.find('input[name=orderNo]').val();
//	var orderPay = obj.find('input[name=orderPay]').val();
//	var userCouponId = obj.find('input[name=userCouponId]').val();
//	var userCouponValue = obj.find('input[name=userCouponValue]').val();
//	
//	sessionStorage.setItem("service_type_id", serviceTypeId);
//	sessionStorage.setItem("order_id", orderId);
//	sessionStorage.setItem("order_no", orderNo);
//	sessionStorage.setItem("order_pay", orderPay);
//	sessionStorage.setItem("user_coupon_id", userCouponId);
//	sessionStorage.setItem("user_coupon_value", userCouponValue);
//	
//	// 订单类型 0 = 钟点工 1 = 深度保洁 2 = 助理订单 6= 话费充值类订单 7 = 订单补差价
//	sessionStorage.setItem("pay_order_type", 0);
//	
//	mainView.router.loadPage("order/order-pay.html");
//	
//}
//
//function doOrderPayExt(obj) {
//	
//	var orderStatus = obj.find('input[name=orderStatus]').val();
//	
//	if (orderStatus >= 3 && orderStatus < 7) {
//	
//		var serviceTypeId = obj.find('input[name=serviceTypeId]').val();
//		var orderId = obj.find('input[name=orderId]').val();
//		var orderNo = obj.find('input[name=orderNo]').val();
//		var orderPay = obj.find('input[name=orderPay]').val();
//		var staffNames = obj.find('input[name=staffNames]').val();
//		
//		sessionStorage.setItem("service_type_id", serviceTypeId);
//		sessionStorage.setItem("order_id", orderId);
//		sessionStorage.setItem("order_no", orderNo);
//		sessionStorage.setItem("order_pay", orderPay);
//		sessionStorage.setItem("staff_names", staffNames);
//		
//		console.log("staffNames = " + sessionStorage.getItem("staff_names"));
//		
//		mainView.router.loadPage("order/order-pay-ext.html");
//	}
//	
//}
//
//
//function linkOrderRate(obj) {
//	
//	var orderStatus = obj.find('input[name=orderStatus]').val();
//	
//	if (orderStatus <= 2) return false;
//	
//	
//	var orderId = obj.find('input[name=orderId]').val();
//	sessionStorage.setItem("order_id", orderId);
//	
//	var staffNames = obj.find('input[name=staffNames]').val();
//	sessionStorage.setItem("staff_names", staffNames);
//	
//	var orderRateUrl = "order/order-rate.html";
//	if (orderStatus == 8) {
//		orderRateUrl = "order/order-rate-success.html";
//	}
//	
//	mainView.router.loadPage(orderRateUrl);
//}


