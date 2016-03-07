myApp.onPageBeforeInit('order-remind-list-page', function (page) {
	//这是在 index.js 定义的全局方法，动态获取 当前 已预约的 提醒类订单的 数量
//	myRemindOrderCount();
	
	var postdata = {};
	var page = 1;
	var userId = localStorage['user_id'];
	var loading = false;// 加载flag
	var apiUrl = "remind/remind_list_now.json";
	postdata.user_id = userId;
	postdata.page = page;
	
	var orderListSuccess= function(data, textStatus, jqXHR) {

		var result = JSON.parse(data.response);
		var orders = result.data;
		
		var html = $$('#order-remind-list').html();
		
		var resultHtmlNow = '';	
		
		for(var i = 0; i< orders.length; i++) {
			var order = orders[i];
			
			var htmlPart = html;
			//var img_tag = '<img alt="" src="img/icons/order_type_img_'+ order.order_type +'.png ">';
			//htmlPart = htmlPart.replace(new RegExp('{img_tab}',"gm"), img_tag);
			htmlPart = htmlPart.replace(new RegExp('{order_type_name}',"gm"), order.order_type_name);
			htmlPart = htmlPart.replace(new RegExp('{order_status_name}',"gm"), order.order_status_name);
			
			htmlPart = htmlPart.replace(new RegExp('{service_content}',"gm"), order.service_content);
			htmlPart = htmlPart.replace(new RegExp('{service_date}',"gm"), moment.unix(order.service_date).format("YYYY-MM-DD HH:mm"));
			
			htmlPart = htmlPart.replace(new RegExp('{order_no}',"gm"), order.order_no);
			
			resultHtmlNow += htmlPart;
		}
		//当前订单
		$$("#card-remind-list ul").append(resultHtmlNow);
		
		page = page + 1;
		loading = false;
			
		if (orders.length < 10) {
			
			$$('#order-remind-more').css("display", "none");
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
	$$('#order-remind-more').on('click', function () {
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
			
			apiUrl = "remind/remind_list_now.json";
			$$('#order-remind-more').css("display", "block");
			$$("#card-remind-list ul").html("");
			page = 1;
			var postdata = {};
			postdata.user_id = userId;
			postdata.page = page;
			 $$.ajax({
		         type : "GET",
		         url  : siteAPIPath+"remind/remind_list_now.json",
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
			
			apiUrl = "remind/remind_list_old.json";
			$$('#order-remind-more').css("display", "block");
			$$("#card-remind-list ul").html("");
			page = 1;
			var postdata = {};
			postdata.user_id = userId;
			postdata.page = page;
			 $$.ajax({
		         type : "GET",
		         url  : siteAPIPath+"remind/remind_list_old.json",
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


function hrefToRemindView(order_no) {
	localStorage.setItem('remind_order_no_param', order_no);
	mainView.router.loadPage("order/remind/order-remind-detail.html?order_no="+order_no);
}