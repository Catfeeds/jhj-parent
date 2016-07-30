myApp.onPageBeforeInit('order-hour-now-list-page', function (page) {
	
	var postdata = {};
	var page = 1;
	var userId = localStorage['user_id'];
	var loading = false;// 加载flag
	var apiUrl = "order/orderHourNowList.json";
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
			//var img_tag = '<img alt="" src="img/icons/order_type_img_'+ order.order_type +'.png ">';
			//htmlPart = htmlPart.replace(new RegExp('{img_tab}',"gm"), img_tag);
			htmlPart = htmlPart.replace(new RegExp('{order_type}',"gm"), order.order_type);
			htmlPart = htmlPart.replace(new RegExp('{order_no}',"gm"), order.order_no);
			htmlPart = htmlPart.replace(new RegExp('{order_hour_type_name}',"gm"), order.order_hour_type_name);
			htmlPart = htmlPart.replace(new RegExp('{order_hour_status_name}',"gm"), order.order_hour_status_name);
			
			if(order.order_type == 2){
				htmlPart = htmlPart.replace(new RegExp('{service_date}',"gm"), moment.unix(order.add_time).format("YYYY-MM-DD HH:mm"));
			}else{
				htmlPart = htmlPart.replace(new RegExp('{service_date}',"gm"), moment.unix(order.service_date).format("YYYY-MM-DD HH:mm"));
			}
			
			htmlPart = htmlPart.replace(new RegExp('{address}',"gm"), order.address);
			
			resultHtmlNow += htmlPart;
		}
		//当前订单
		$$("#card-hour-now-list ul").append(resultHtmlNow);
		
		page = page + 1;
		loading = false;
			
		if (orders.length < 10) {
			
			$$('#order-list-more').css("display", "none");
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
	$$('#order-list-more').on('click', function () {
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
			$$('#order-list-more').css("display", "block");
			$$("#card-hour-now-list ul").html("");
			page = 1;
			var postdata = {};
			postdata.user_id = userId;
			postdata.page = page;
			 $$.ajax({
		         type : "GET",
		         url  : siteAPIPath+"order/orderHourNowList.json",
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
			$$('#order-list-more').css("display", "block");
			$$("#card-hour-now-list ul").html("");
			page = 1;
			var postdata = {};
			postdata.user_id = userId;
			postdata.page = page;
			 $$.ajax({
		         type : "GET",
		         url  : siteAPIPath+"order/orderHourOldList.json",
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


function hrefToUorderView(order_type, order_no) {
	localStorage.setItem('order_no', order_no);
	mainView.router.loadPage("order/order-view-"+order_type+".html?order_no="+order_no);
}