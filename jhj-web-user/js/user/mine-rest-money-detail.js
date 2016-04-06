myApp.onPageBeforeInit('mine-rest-money-detail-page', function (page) {
	
	var postdata = {};
	var page = 1;
	var userId = localStorage['user_id'];
	var loading = false;// 加载flag
	var apiUrl = "user/pay_detail_list.json";
	postdata.user_id = userId;
	postdata.page = page;
	
	var orderListSuccess= function(data, textStatus, jqXHR) {

		var result = JSON.parse(data.response);
		var orders = result.data;
		
		var html = $$('#order-hour-list').html();
		
		var resultHtmlNow = '';	//当前订单
		
		for(var i = 0; i< orders.length; i++) {
			var order = orders[i];
			
			var htmlPart = html;
			
			
			if(order.order_flag == 0){
				//支付
				htmlPart = htmlPart.replace(new RegExp('{img}',"gm"), "img/userRestMoney/iconfont-jianhao.png");
//				htmlPart = htmlPart.replace(new RegExp('{orderMoney}',"gm"), "支付金额:"+order.order_money);
			}else{
				//充值	
				htmlPart = htmlPart.replace(new RegExp('{img}',"gm"), "img/userRestMoney/iconfont-jiahao.png");
			}
			htmlPart = htmlPart.replace(new RegExp('{orderMoney}',"gm"), "金额:"+order.order_money);
			
			htmlPart = htmlPart.replace(new RegExp('{orderTypeName}',"gm"), order.order_type_name);
			
			htmlPart = htmlPart.replace(new RegExp('{addTime}',"gm"), moment.unix(order.add_time).format("YYYY-MM-DD HH:mm"));
			
			resultHtmlNow += htmlPart;
		}
		//当前订单
		$$("#card-hour-now-list ul").append(resultHtmlNow);
		
		page = page + 1;
		loading = false;
			
		if (orders.length < 10) {
			
			$$('#pay-detail-list-more').css("display", "none");
			return;			
		}

	};	
	
	postdata.user_id = userId;
	postdata.page = page;
	 $$.ajax({
         type : "GET",
         url  : siteAPIPath + apiUrl,
         dataType: "json",
         cache : true,
         data : postdata,
 		statusCode : {
 			200 : orderListSuccess,
 			400 : ajaxError,
 			500 : ajaxError
 		},
 		
	 });
	// 注册'infinite'事件处理函数
	$$('#pay-detail-list-more').on('click', function () {
		if (loading) return;//如果正在加载，则退出
		loading = true;// 设置flag
		postdata.user_id = userId;
		postdata.page = page;
		
		$$.ajax({
			type : "GET",
			url  : siteAPIPath + apiUrl,
			dataType : "json",
			cache : true,
			data : postdata,
			timeout: 10000, //超时时间：10秒
			statusCode : {
				200 : orderListSuccess,
				400 : ajaxError,
				500 : ajaxError
			},
		});
	}); 
	
	$$('#btn-order-now').on('click', function() {

		if ($$(this).attr("class") != "dangqian-box active-state") {
			$$(this).attr("class", "dangqian-box");
			$$('#btn-order-history').attr("class", "history-box");		
			
			apiUrl = "order/orderHourNowList.json";
			$$('#pay-detail-list-more').css("display", "block");
			$$("#card-hour-now-list ul").html("");
			page = 1;
			var postdata = {};
			postdata.user_id = userId;
			postdata.page = page;
			 $$.ajax({
		         type : "GET",
		         url  : siteAPIPath+"user/pay_detail_list.json",
		         dataType: "json",
		         cache : true,
		         data : postdata,
		 		statusCode : {
		 			200 : orderListSuccess,
		 			400 : ajaxError,
		 			500 : ajaxError
		 		},
		 		
			 });			
			
		}
	});
	
	$$('#btn-order-history').on('click', function() {

		if ($$(this).attr("class") != "history-box1 active-state") {
			$$(this).attr("class", "history-box1");
			$$('#btn-order-now').attr("class", "dangqian-box1");
			
			apiUrl = "order/orderHourOldList.json";
			$$('#pay-detail-list-more').css("display", "block");
			$$("#card-hour-now-list ul").html("");
			page = 1;
			var postdata = {};
			postdata.user_id = userId;
			postdata.page = page;
			 $$.ajax({
		         type : "GET",
		         url  : siteAPIPath+"user/pay_detail_list.json.json",
		         dataType: "json",
		         cache : true,
		         data : postdata,
		 		statusCode : {
		 			200 : orderListSuccess,
		 			400 : ajaxError,
		 			500 : ajaxError
		 		},
		 		
			 });			
			
		}
	});	
	
	
});

